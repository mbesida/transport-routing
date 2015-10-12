package com.mbesida.routing

import scala.util.{Failure, Success, Try}

class Routing(rawRoutes: List[String]) {

  import RoutingHelper._

  private val routesTries: List[Try[Route]] = rawRoutes.map(RoutingHelper.parseRoute)

  private lazy val routes: List[Route] = routesTries.map(_.get)
  private lazy val validationErrors: List[String] = routesTries.collect{case Failure(ex) => ex.getMessage}

  private lazy val stationsToindexMap: Map[String, Int] = buildIndexMap(routes)
  private lazy val indexToStationMap: Map[Int, String] = stationsToindexMap.map(_.swap)
  private lazy val weightMatrix: Matrix = buildWeightMatrix(routes, stationsToindexMap)
  private lazy val (distanceMatrix: Matrix, pathMatrix: Matrix) = calculateAllPaths(weightMatrix)

  def getShortestPath(source: String, dest: String): Try[String] = {
    withValidation {
      val result = for {
        from <- stationsToindexMap.get(source)
        to <- stationsToindexMap.get(dest)
        time <- distanceMatrix(from)(to)
        path = buildPath(pathMatrix, from, to).map(index => indexToStationMap(index))
      } yield s"${path.mkString(PathDelimeter)}: $time"
      Success(result.getOrElse(s"Error: No route from $source to $dest"))
    }
  }

  def findNearbyStations(station: String, travelTime: Int): Try[String] = {
    withValidation {
      stationsToindexMap.get(station) match {
        case Some(index) => {
          val pathsFromStation = distanceMatrix(index)
          val nearbyStaions = pathsFromStation.zipWithIndex.collect{case (Some(a), j) if a <= travelTime => (a,j)}
          val sorted = nearbyStaions.sortWith((cur, next) => cur._1 < next._1)
          if (sorted.nonEmpty)
            Success(sorted.map{case (time, stationIndex) => s"${indexToStationMap(stationIndex)}: $time"}.mkString(NearByDelimeter))
          else Success(s"No path from station $station")
        }
        case None => Success(s"No such station $station")
      }
    }
  }


  private def withValidation(func: => Try[String]): Try[String] = {
    if (validationErrors.isEmpty)  func
    else Failure(new RuntimeException(validationErrors.mkString(NearByDelimeter)))
  }
}

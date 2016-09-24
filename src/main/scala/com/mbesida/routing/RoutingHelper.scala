package com.mbesida.routing

import scala.collection.mutable.ListBuffer
import scala.languageFeature.postfixOps
import scala.util.{Success, Failure, Try}


object RoutingHelper {

  type Matrix = Array[Array[Option[Int]]]

  val RouteRegex = """(\w+)\s*->\s*(\w+)\s*:\s*(\d+)""".r
  val RouteQueryRegex = """route\s*(\w+)\s*->\s*(\w+)\s*""".r
  val NearbyQueryRegex = """nearby\s*(\w+),\s*(\d+)\s*""".r
  val PathDelimeter = " -> "
  val NearByDelimeter = ", "

  case class Route(source: String, destination: String, travelTime: Int)

  def buildIndexMap(routes: List[Route]): Map[String, Int] = {
    val allSources = routes.map(_.source).toSet
    val allDestinations = routes.map(_.destination).toSet
    val union = allSources union allDestinations
    (0 until union.size).zip(union).map { case (i, s) => (s -> i) }.toMap
  }

  def buildWeightMatrix(routes: List[Route], indexMap: Map[String, Int]): Matrix = {
    val matrix: Array[Array[Option[Int]]] = Array.fill(indexMap.size, indexMap.size)(None)
    routes foreach { r =>
      matrix(indexMap(r.source))(indexMap(r.destination)) = Some(r.travelTime)
    }
    matrix
  }

  /**
   * Implementation of Floyd-Warshall algorithm with path reconstruction modifications
   * @return (distance matrix, path matrix)
   */
  def calculateAllPaths(weightMatrix: Matrix): (Matrix, Matrix) = {
    val distances = weightMatrix.map(_.clone())
    val paths = Array.tabulate(weightMatrix.size, weightMatrix.size)((i, j) => distances(i)(j).map(_ => j))
    for {
      k <- 0 until distances.size
      i <- 0 until distances.size
      j <- 0 until distances.size
    } {
      for {
        subPath1 <- distances(i)(k)
        subPath2 <- distances(k)(j)
        if subPath1 + subPath2 < distances(i)(j).getOrElse(Int.MaxValue)
      } {
        distances(i)(j) = Some(subPath1 + subPath2)
        paths(i)(j) = paths(i)(k)
      }
    }
    (distances, paths)
  }

  def buildPath(pathMatrix: Matrix, from: Int, to: Int): List[Int] = {
    if (pathMatrix(from)(to).isEmpty) Nil
    else {
      val path = ListBuffer(from)
      var k: Option[Int] = Some(from)
      while (k.isDefined && k.get != to) {
        k = pathMatrix(k.get)(to)
        path.append(k.get)
      }
      path.toList
    }
  }

  def parseRoute(route: String): Try[Route] = {
    route match {
      case RouteRegex(source, destination, time) => Try(time.toInt).map(t => Route(source, destination, t))
      case _ => Failure(new RuntimeException(s"Invalid route format of route $route"))
    }
  }
}



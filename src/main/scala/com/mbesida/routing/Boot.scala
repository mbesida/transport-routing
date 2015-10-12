package com.mbesida.routing

import scala.annotation.tailrec
import scala.util.{Success, Failure}
import scala.util.control.Breaks._


object Boot extends App {

  println("Enter initial data:")
  val n = Console.readInt()

  @tailrec
  def readMap(index: Int, agg: List[String]): List[String] = {
    if (index == n) {
      println("Map read")
      agg
    }
    else {
      readMap(index + 1, Console.readLine() :: agg)
    }
  }

  val routing = new Routing(readMap(0, Nil))

  println("Enter queries:")
  breakable {
    while(true) {

      val result = Console.readLine() match {
        case RoutingHelper.RouteQueryRegex(source, destination) => routing.getShortestPath(source, destination)
        case RoutingHelper.NearbyQueryRegex(station, time) => routing.findNearbyStations(station, time.toInt)
        case null => Success("END")
        case _ => Failure(new RuntimeException("Incorrect query"))
      }

      result match {
        case Success(a) if a == "END" => break
        case Success(a) => println(a)
        case Failure(e) => println(e.getMessage)
      }
    }
  }


}

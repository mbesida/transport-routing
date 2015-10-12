package com.mbesida.routing

import org.scalatest.{Matchers,FlatSpec}

import scala.util.{Failure, Success}


class RoutingSpec extends FlatSpec with Matchers{
  import RoutingSpec._

  val routing = new Routing(testData)

  "Routing" should "find shortest path from A to B" in {
    val path = routing.getShortestPath("A", "B")
    path.get should be("A -> C -> B: 130")
  }

  it should "find shortest path from E to B" in {
    val path = routing.getShortestPath("E", "B")
    path.get should be("E -> A -> C -> B: 430")
  }

  it should "find shortest path from A to E" in {
    val path = routing.getShortestPath("A", "E")
    path.get should be("A -> C -> E: 310")
  }

  it should "find stations near A" in {
    val nearby = routing.findNearbyStations("A", 130)
    nearby.get should be("C: 70, D: 120, B: 130")
  }

  it should "find stations near B" in {
    val nearby = routing.findNearbyStations("B", 300)
    nearby.get should be("E: 210")
  }

  it should "handle errors in input format" in {
    val path = new Routing(badTestData).getShortestPath("A", "C")
    path match {
      case Success(_) => fail()
      case Failure(e: RuntimeException) => e.getMessage should be(s"Invalid route format of route A to E: 10")
    }
  }

  it should "indicate if path is unavailable" in {
    val path = new Routing(noPathTestData).getShortestPath("C", "D")
    path.get should be(s"Error: No route from C to D")
  }
}

object RoutingSpec {
  val testData = List(
    "A -> B: 240",
    "A -> C: 70",
    "A -> D: 120",
    "C -> B: 60",
    "D -> E: 480",
    "C -> E: 240",
    "B -> E: 210",
    "E -> A: 300"
  )

  val badTestData = List("A -> B: 240",
    "A -> C: 70",
    "A -> D: 120",
    "A to E: 10"
  )

  val noPathTestData = List(
    "A -> B: 240",
    "A -> C: 70",
    "D -> A: 120",
    "D -> B: 60",
    "B -> C: 480",
    "C -> B: 240"
  )
}

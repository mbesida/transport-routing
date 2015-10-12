## transport-routing

* The problem is solved using modification of Floyd–Warshall algorithm. Shortest path calculation is done when the 
  program starts.
* Complexity of the initial step(calculating all shortest paths from each statsion) is O(|stations|^3)
* Worst case complexity of each query is O(|stations|)
* It is assumed that travel time can't be greater than Int.MaxValue

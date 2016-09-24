## notes of implementation

* The problem is solved using modification of Floyd-Warshall algorithm. Shortest path calculation is done when the 
  program starts.
* Complexity of the initial step(calculating all shortest paths from each stations) is *O(|stations|^3)*
* Worst case complexity of each query is *O(|stations|)*
* It is assumed that travel time can't be greater than Int.MaxValue
* To exit the program type **END**


## problem definition
 
 You are asked to develop a new routing solution for public transport system that lets people 
 search for the quickest route between to given stations. Your customer is also asking for the
 posibility to find stations nearby a given station that can be reached within a given time.
 
 The transport network is modelled as a directed graph with edges that represent a connection between 
 the two stations and are labelled with the travel time in seconds. If a transport line can be used in two directions 
 it is modelled as two distinct, opposing edges. The stations are identified
 with alphanumeric strings without special characters or whitespace, such as “A” or “B” or “EXAMPLESTATION”.
 To keep things simple the train schedule and changing times at stations will be ignored.
 
###Input
 
 The routing program starts by reading the transport network graph. The first line of input is the
 number of edges in the graph. The edges are defined on a single line each in the form:
      *\<source\> -\> \<destination\>: \<travel time\>*
      
 Where *\<source\>* and *\<destination\>* are station identifiers and *\<travel time\>* is a decimal
 number representing the travel time between source and destination in seconds.
 After the graph has been defined the routing program is ready to process routing queries. Each query
 is followed by a newline.
 There are two kinds of queries. A routing query which asks for the shortest route between two
 stations takes the following form:
     *route \<source\> -\> \<destination\>*
     
 A nearby query which asks for all the stations that can be reached from a given station within a given
 time looks as follows:
    *nearby \<source\>, \<maximum travel time\>*
 
###Output
 
 The output for each query goes to a separate line.
 The output for a routing query is the *-\>-* separated route followed by a colon and the travel time:
    *\<source\> -\> \<waypoint1\> -\> ... \<destination\> : \<travel time\>*
 
 If no route exists an error message is printed.
    *Error: No route from \<source\> to \<destination\>*
 
 For nearby queries the output is a comma separated list of stations with the travel times ordered by 
 ascending travel times.
    *\<destination1\>:\<travel time\>, \<destinationation2\>:\<travel time\>, ...*
    
###Test Input
    
    8
    A -> B: 240
    A -> C: 70
    A -> D: 120
    C -> B: 60
    D -> E: 480
    C -> E: 240
    B -> E: 210
    E -> A: 300
    route A -> B
    nearby A, 130
    
###Expected Output
    
    A -> C -> B: 130
    C: 70, D: 120, B: 130

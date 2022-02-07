# Hyper Metro
**Description**

Console application which works with json files of metropolitan information. This application allow to navigate on metro-map and find the shortest path between two stations.

**Requirements**
1. Parse JSON files with information about metropolitan
   `"Bakerloo line": [
   {
   "name": "Harrow & Wealdstone",
   "prev": [],
   "next": [
   "Kenton"
   ],
   "transfer": [],
   "time": 5
   },`
2. Output information of lines
3. Add and remove stations
4. Connection two station from different lines
5. Find the shortest route by _Breadth-First search algorithm_
6. Find the fastest route by _Dijkstra's algorithm_
7. Create branching and the possibility of a looped line

**Launch**

`mvn package -PRun `
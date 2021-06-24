
import java.util.*;

public class MetroGraph {
    private List<MetroStation> stations;
    private List<MetroLine> lines;
    private List<List<Integer>> graph;
    public MetroGraph() {
        stations = new ArrayList<>();
        lines = new ArrayList<>();
    }

    public List<MetroLine> getLines() {
        return lines;
    }

    public List<List<Integer>> getGraph() {
        return graph;
    }

    public List<MetroStation> getStations() {
        return stations;
    }

    public void createStation(MetroStation station) {
        stations.add(station);
    }
    public void createLine(MetroLine line) {
        lines.add(line);
    }

    public void createGraph() {
        graph = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++) {
            graph.add(new ArrayList<>());
            Map<String, String> currentAdjacency = stations.get(i).getAdjacency();
            for (int j = 0; j < stations.size(); j++) {
                if (currentAdjacency.get(stations.get(j).getName()) != null && currentAdjacency.get(stations.get(j).getName()).equals(stations.get(j).getLine())) {
                    graph.get(i).add(1);
                } else {
                    graph.get(i).add(0);
                }
            }
        }
    }

    public void modifyGraph(MetroStation station1, MetroStation station2) {
        graph.get(stations.indexOf(station1)).set(stations.indexOf(station2), 1);
        station1.addAdjacency(station2.getName(), station2.getLine());
        graph.get(stations.indexOf(station2)).set(stations.indexOf(station1), 1);
        station2.addAdjacency(station1.getName(), station1.getLine());
    }

    public void pushBackStation(String stationName, String lineName, int time) {
        boolean last = false;
        int last_index = 0;
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getLine().equals(lineName)) {
                last_index = i;
                last = true;

            } else {
                if (last)
                    break;
            }
        }
        System.out.println(stations.get(last_index));
        if (!last) {
            System.out.println("No such line");
            return;
        }
        MetroStation newStation = new MetroStation(stationName, lineName, time, new HashMap<>());
        stations.add(last_index + 1, newStation);
        findLine(lineName).addStation(newStation, last_index + 1);
        graph.add(last_index + 1, new ArrayList<>());
        for (int i = 0; i < stations.size(); i++) {
            graph.get(last_index + 1).add(0);
            if (i != last_index + 1)
                graph.get(i).add(last_index + 1, 0);
        }
        modifyGraph(stations.get(last_index + 1), stations.get(last_index));
    }

    public void pushFrontStation(String stationName, String lineName, int time) {
        int first_index = 0;
        boolean checker = false;
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getLine().equals(lineName)) {
                checker = true;
                first_index = i;
                break;
            }
        }
        if (!checker) {
            System.out.println("No such line");
            return;
        }
        MetroStation newStation = new MetroStation(stationName, lineName, time, new HashMap<>());
        stations.add(first_index, newStation);
        findLine(lineName).addStation(newStation, first_index);
        graph.add(first_index, new ArrayList<>());
        for (int i = 0; i < stations.size(); i++) {
            graph.get(first_index).add(0);
        }
        modifyGraph(stations.get(first_index), stations.get(first_index + 1));
    }

    public void removeStation(String stationName, String lineName) {
        int delete_index = stations.indexOf(findStation(stationName, lineName));
        if (delete_index == -1) {
            System.out.println("No such line");
            return;
        }
        MetroStation deleteStation = findStation(stationName, lineName);
        stations.remove(delete_index);
        findLine(lineName).deleteStation(deleteStation);
        graph.remove(delete_index);
        for (MetroStation station : stations) {
            Map<String, String> adjacency = station.getAdjacency();
            if (adjacency.get(stationName) != null && adjacency.get(stationName).equals(lineName))
                adjacency.remove(stationName);
            station.setAdjacency(adjacency);
        }
        for (int i = 0; i < stations.size(); i++) {
            List<Integer> adjacency = graph.get(i);
            adjacency.remove(delete_index);
            graph.set(i, changeAdjacency(delete_index, adjacency));
        }

    }

    public MetroStation findStation(String stationName, String lineName) {
        for (MetroStation station: stations) {
            if (stationName.equals(station.getName()) && lineName.equals(station.getLine()))
                return station;
        }
        return null;
    }
    public MetroLine findLine(String lineName) {
        for (MetroLine line: lines) {
            if (lineName.equals(line.getName()))
                return line;
        }
        return null;
    }
    public List<Integer> changeAdjacency(int index, List<Integer> adjacency) {
        if (index != stations.size() - 1) {
            adjacency.set(index, 0);
            if (index != 0) {
                adjacency.set(index - 1, 1);
                stations.get(index).addAdjacency(stations.get(index - 1).getName(), stations.get(index - 1).getLine());
                stations.get(index - 1).addAdjacency(stations.get(index).getName(), stations.get(index).getLine());

            }
        }

        return adjacency;
    }
    public void findRoute(String startStation, String startLine, String endStation, String endLine) {
        String currentLine = startLine;
        int startIndex = stations.indexOf(findStation(startStation, startLine));
        int endIndex = stations.indexOf(findStation(endStation, endLine));
        Queue<Integer> queue = new ArrayDeque<>(stations.size());
        queue.add(startIndex);
        boolean[] used = new boolean[stations.size()];
        int[] paths = new int[stations.size()];
        used[startIndex] = true;
        paths[startIndex] = -1;
        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int i = 0; i < stations.size(); i++) {
                if (!used[i] && graph.get(v).get(i) == 1) {
                    used[i] = true;
                    queue.add(i);
                    paths[i] = v;
                }
            }
        }
        if (!used[endIndex])
            System.out.println("No path!");
        else {
            List<Integer> currentPath = new ArrayList<>();
            for (int v = endIndex; v != -1; v = paths[v]) {
                currentPath.add(v);
            }
            for (int i = currentPath.size() - 1; i >= 0; i--) {
                if (!currentLine.equals(stations.get(currentPath.get(i)).getLine())) {
                    currentLine = stations.get(currentPath.get(i)).getLine();
                    System.out.println("Пересадка на линию: " + currentLine);
                }
                System.out.println(stations.get(currentPath.get(i)).getName());
            }
        }
    }
    public void fastestRoute(String startStation, String startLine, String endStation, String endLine) {
        String currentLine = startLine;
        int startIndex = stations.indexOf(findStation(startStation, startLine));
        int endIndex = stations.indexOf(findStation(endStation, endLine));
        Queue<Integer> queue = new ArrayDeque<>(stations.size());
        int[] distance = new int[stations.size()];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[startIndex] = 0;
        int[] paths = new int[stations.size()];
        boolean[] used = new boolean[stations.size()];
        for (int i = 0; i < stations.size(); i++) {
            int v = -1;
            for (int j = 0; j < stations.size(); j++) {
                if (!used[j] && (v == -1 || distance[j] < distance[v])) {
                    v = j;
                }
            }
            if (distance[v] == Integer.MAX_VALUE)
                break;
            used[v] = true;
            for (int j = 0; j < stations.size(); j++) {
                if (graph.get(v).get(j) == 0)
                    continue;
                int pathLength = j == 0 || j < v? stations.get(j).getTime() : stations.get(j - 1).getTime();
                if (!stations.get(v).getLine().equals(stations.get(j).getLine())) {
                    pathLength = 5;
                }
                if (distance[v] + pathLength < distance[j]) {
                    distance[j] = distance[v] + pathLength;
                    paths[j] = v;
                }
            }

        }
        List<Integer> currentPath = new ArrayList<>();
        for (int i = endIndex; i != startIndex; i = paths[i]) {
            currentPath.add(i);
        }
        currentPath.add(startIndex);
        for (int i = currentPath.size() - 1; i >= 0; i--) {
            if (!currentLine.equals(stations.get(currentPath.get(i)).getLine())) {
                currentLine = stations.get(currentPath.get(i)).getLine();
                System.out.println("Transfer to line " + currentLine);
            }
            System.out.println(stations.get(currentPath.get(i)).getName());
        }
        System.out.println("Total: " + distance[endIndex] + " minutes in the way");
    }
}


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class MetroApplicationUtils {
    private final MetroGraph metro;
    private final boolean isFileExist;

    public MetroApplicationUtils(String fileName, MetroGraph metro) throws IOException {
        this.metro = metro;
        isFileExist = parserJSON(fileName);
        metro.createGraph();
    }

    public boolean isFileExist() {
        return isFileExist;
    }

    private String readName(Scanner scanner) {
        String name = scanner.next();
        if (name.startsWith("\"") && name.endsWith("\"")) {
            name = name.replaceAll("\"", "");
        } else if (name.startsWith("\"")) {
            String partName = scanner.next();
            name += " " + partName;
            while (!partName.endsWith("\"")) {
                partName = scanner.next();
                name += " " + partName;
            }
            name = name.replaceAll("\"", "");
        }
        return name;
    }

    public void metroApplication() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String command = scanner.next();
            String lineName;
            String stationName;
            String connectLine;
            String connectStation;
            switch (command) {
                case ("/exit"):
                    return;
                case ("/output"):
                    lineName = readName(scanner);
                    printLineStations(lineName);
                    break;
                case ("/append"):
                    lineName = readName(scanner);
                    stationName = readName(scanner);
                    appendStation(lineName, stationName, 5);
                    break;
                case ("/connect"):
                    lineName = readName(scanner);
                    stationName = readName(scanner);
                    connectLine = readName(scanner);
                    connectStation = readName(scanner);
                    connectLines(lineName, connectLine, stationName, connectStation);
                    break;
                case ("/add-head"):
                    lineName = readName(scanner);
                    stationName = readName(scanner);
                    addHeadStation(lineName, stationName, 5);
                    break;
                case ("/remove"):
                    lineName = readName(scanner);
                    stationName = readName(scanner);
                    removeStation(lineName, stationName);
                    break;
                case ("/route"):
                    lineName = readName(scanner);
                    stationName = readName(scanner);
                    connectLine = readName(scanner);
                    connectStation = readName(scanner);
                    getRoute(lineName, stationName, connectLine, connectStation);
                    break;
                case ("/fastest-route"):
                    lineName = readName(scanner);
                    stationName = readName(scanner);
                    connectLine = readName(scanner);
                    connectStation = readName(scanner);
                    fastRoute(lineName, connectLine, stationName, connectStation);
                    break;
                default:
                    System.out.println("Invalid command");
                    scanner.nextLine();
            }
        }
    }
    private void fastRoute(String lineName1, String lineName2, String stationName1, String stationName2) {
        if (metro.findLine(lineName1) == null || metro.findLine(lineName2) == null) {
            System.out.println("No such lines");
        } else {
            metro.fastestRoute(stationName1, lineName1, stationName2, lineName2);
        }
    }
    private void connectLines(String lineName1, String lineName2, String stationName1, String stationName2) {
        if (metro.findLine(lineName1) == null || metro.findLine(lineName2) == null)
            System.out.println("No such lines");
        else {
            Map<String, String> adjacency = metro.findStation(stationName1, lineName1).getAdjacency();
            if (adjacency.get(stationName2) != null && adjacency.get(stationName2).equals(lineName2)) {
                    System.out.println("Stations have already connected!");
                    return;
                }
            metro.modifyGraph(metro.findStation(stationName1, lineName1), metro.findStation(stationName2, lineName2));
        }
    }
    private void getRoute(String lineName1, String stationName1, String lineName2, String stationName2) {
        if (metro.findLine(lineName1) == null || metro.findLine(lineName2) == null)
            System.out.println("No such line!");
        else
            metro.findRoute(stationName1, lineName1, stationName2, lineName2);
    }
    private void printLineStations(String lineName) {
        List<MetroStation> stations = new ArrayList<>(metro.findLine(lineName).getStations());
        if (stations.isEmpty()) {
            System.out.println("No such line!");
            return;
        }
        if (metro.findLine(lineName).getTransfered()) {
            System.out.println("depot - ");
            System.out.print(stations.get(0).getName());
            if (!stations.get(0).getAdjacency().isEmpty()) {
                Map<String, String> transfers = stations.get(0).getAdjacency();
                for (String transferStation : transfers.keySet()) {
                    if (!transfers.get(transferStation).equals(lineName))
                        System.out.print(" - " + transferStation + " (" + transfers.get(transferStation) + " line)");
                }
            }
            for (int i = 1; i < stations.size(); i++) {
                System.out.println();
                System.out.print(stations.get(i).getName());
                if (!stations.get(i).getAdjacency().isEmpty()) {
                    Map<String, String> transfers = stations.get(i).getAdjacency();
                    for (String transferStation : transfers.keySet()) {
                        if (!transfers.get(transferStation).equals(lineName))
                            System.out.print(" - " + transferStation + " (" + transfers.get(transferStation) + " line)");
                    }
                }
            }
            System.out.println();
        } else {
//            stations.add(0, "depot");
//            stations.add("depot");
//            int counter = 0;
//            for (int i = 0; i < stations.size(); i++) {
//                if (counter == 0) {
//                    System.out.print(stations.get(i));
//                } else {
//                    System.out.print(" - " + stations.get(i));
//                }
//                counter++;
//                if (counter == 3 && i != stations.size() - 1) {
//                    i -= 2;
//                    counter = 0;
//                    System.out.println();
//                }
//            }
//            System.out.println();
            System.out.println("depot");
            for (MetroStation station : stations) {
                System.out.println(station.getName());
            }
        }
        System.out.println("depot");
    }

    private void appendStation(String lineName, String stationName, int time) {
        metro.pushBackStation(stationName, lineName, time);
    }

    private void addHeadStation(String lineName, String stationName, int time) {
        metro.pushFrontStation(stationName, lineName, time);
    }

    private void removeStation(String lineName, String stationName) {
        metro.removeStation(stationName, lineName);
    }

    private boolean parserJSON(String fileName) throws IOException {
        int stationNum = 1;
        Map<Integer, MetroStation> currentStations = null;
        boolean lineTransfered = false;
        try (Scanner reader = new Scanner(new File(fileName))) {
            reader.nextLine();
            int block = 1;
            StringBuilder lineName = new StringBuilder();
            while (block != 0) {
                String curLine = reader.nextLine().trim();
                if (curLine.contains("[") && block == 1) {
                    block++;
                    currentStations = new HashMap<>();
                    lineName = new StringBuilder();
                    for (int i = 1; i < curLine.length(); i++) {
                        if (curLine.charAt(i) == '"') break;
                        lineName.append(curLine.charAt(i));
                    }
                    lineTransfered = false;
                    stationNum = 1;
                } else if (curLine.contains("{") && block == 2) {
                    Map<String, String> adjacency = new HashMap<>();
                    int time = 0;
                    MetroStation currentStation;
                    String stationName;
                    String lineTransfer;
                    String stationTransfer;
                    //read station name
                    curLine = reader.nextLine().trim();
                    if (curLine.endsWith("\",")) stationName = curLine.substring(9, curLine.length() - 2);
                    else stationName = curLine.substring(9, curLine.length() - 3);
                    curLine = reader.nextLine().trim();
                    if (!curLine.contains("[]")) {
                        curLine = reader.nextLine().trim();
                        while (!curLine.replaceAll(" ", "").equals("],")) {
                            if (!curLine.contains(","))
                                curLine = curLine.substring(1, curLine.length() - 1);
                            else
                                curLine = curLine.substring(1, curLine.length() - 3);
                            adjacency.put(curLine, lineName.toString());
                            curLine = reader.nextLine().trim();
                        }
                    }
                    curLine = reader.nextLine().trim();
                    if (!curLine.contains("[]")) {
                        curLine = reader.nextLine().trim();
                        while (!curLine.replaceAll(" ", "").equals("],")) {
                            if (!curLine.contains(","))
                                curLine = curLine.substring(1, curLine.length() - 1);
                            else
                                curLine = curLine.substring(1, curLine.length() - 3);
                            adjacency.put(curLine, lineName.toString());
                            curLine = reader.nextLine().trim();
                        }
                    }
                    //read transfer stations
                    curLine = reader.nextLine().trim();
                    if (!curLine.contains("[]")) {
                        curLine = reader.nextLine().trim();
                        while (!curLine.equals("}]") && !curLine.equals("}")) {
                            //read line transfer
                            if (curLine.equals("},")) curLine = reader.nextLine().trim();
                            if (curLine.equals("{")) curLine = reader.nextLine().trim();
                            if (curLine.endsWith("\",")) lineTransfer = curLine.substring(9, curLine.length() - 2);
                            else lineTransfer = curLine.substring(9, curLine.length() - 3);
                            //read station transfer
                            curLine = reader.nextLine().trim();
                            if (curLine.endsWith(",")) {
                                if (curLine.endsWith("\","))
                                    stationTransfer = curLine.substring(9, curLine.length() - 2);
                                else stationTransfer = curLine.substring(9, curLine.length() - 3);
                            } else {
                                stationTransfer = curLine.substring(12, curLine.length() - 1);
                            }
                            adjacency.put(stationTransfer, lineTransfer);
                            curLine = reader.nextLine().trim();
                        }
                        if (curLine.equals("}")) reader.nextLine();
                        lineTransfered = true;
                    }
                    curLine = reader.nextLine();//}
                    if (curLine.trim().startsWith("\"time\"")) {
                        if (!curLine.trim().substring(8).equals("null"))
                            time = Integer.parseInt(curLine.trim().substring(8));
                        reader.nextLine();
                    }
                    currentStation = new MetroStation(stationName, lineName.toString(), time, adjacency);
                    currentStations.put(stationNum, currentStation);
                    stationNum++;
                } else if (curLine.replaceAll(" ", "").equals("],") || curLine.equals("]") && block == 2) {
                    MetroLine currentLine = new MetroLine(lineName.toString(), lineTransfered);
                    metro.createLine(currentLine);
                    List<MetroStation> stationsLine = new ArrayList<>();
                    for (Integer key: currentStations.keySet()) {
                        metro.createStation(currentStations.get(key));
                        stationsLine.add(currentStations.get(key));
                    }
                    currentLine.setStations(stationsLine);
                    if (curLine.equals("]") && block == 2)
                        break;
                    block--;

                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }
}

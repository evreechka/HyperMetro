import java.util.List;
import java.util.Map;

public class MetroStation {
    private String name;
    private String line;
    private Map<String, String> adjacency;
    private int time;
    public MetroStation(String name, String line, int time, Map<String, String> adjacency) {
        this.time = time;
        this.name = name;
        this.line = line;
        this.adjacency = adjacency;
    }

    public void setAdjacency(Map<String, String> adjacency) {
        this.adjacency = adjacency;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    public int getTime() {
        return time;
    }

    public void addAdjacency(String station, String lineName) {
        adjacency.put(station, lineName);
    }

    public Map<String, String> getAdjacency() {
        return adjacency;
    }
}

package metro;

import java.util.ArrayList;
import java.util.List;

public class MetroLine {
    private String name;
    private Boolean isTransfered;
    private List<MetroStation> stations;
    public MetroLine(String name, Boolean isTransfered) {
        this.name = name;
        this.isTransfered = isTransfered;
        this.stations = new ArrayList<>();
    }
    public void addStation(MetroStation stationName, int index) {
        stations.add(index, stationName);
    }
    public void setStations(List<MetroStation> stations) {
        this.stations = stations;
    }
    public void deleteStation(MetroStation station) {
        stations.remove(station);
    }
    public String getName() {
        return name;
    }

    public List<MetroStation> getStations() {
        return stations;
    }

    public Boolean getTransfered() {
        return isTransfered;
    }
}

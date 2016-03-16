import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamie on 16/03/2016.
 */
public class RouteData {

    private int currentFloor;
    private List<Integer> floorsToVisit = new ArrayList<>();

    public RouteData(int currentFloor, List<Integer> floorsToVisit){
        this.currentFloor = currentFloor;
        this.floorsToVisit = floorsToVisit;
    }

    public List<Integer> getFloorsToVisit() {
        return floorsToVisit;
    }

    public void setFloorsToVisit(ArrayList<Integer> floorsToVisit) {
        this.floorsToVisit = floorsToVisit;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
}

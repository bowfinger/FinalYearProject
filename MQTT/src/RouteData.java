import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamie on 16/03/2016.
 */
public class RouteData {

    private int currentFloor;
    private List<Call> floorsToVisit = new ArrayList<>();

    public RouteData(){

    }

    public RouteData(int currentFloor, List<Call> calls){
        this.currentFloor = currentFloor;
        this.floorsToVisit = floorsToVisit;
    }

    public List<Call> getFloorsToVisit() {
        return floorsToVisit;
    }

    public void setFloorsToVisit(ArrayList<Call> floorsToVisit) {
        this.floorsToVisit = floorsToVisit;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void addFloorToVisit(Call call){
        this.floorsToVisit.add(call);
    }
}

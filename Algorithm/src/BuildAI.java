import java.awt.*;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jamie on 08/12/2015.
 */

public class BuildAI {


    public static final int FLOOR_TRAVEL_TIME = 3000; //milliseconds
    public static final int DOOR_OPEN_CLOSE_TIME = 2000; //milliseconds

    public static final int NO_OF_FLOORS = 8;
    public static int currentFloor;
    public static List<Integer> callStack = new LinkedList<Integer>();
    public static Random rnd = new Random();
    public static List<BitSet> permutations = new LinkedList<BitSet>();
    public static List<List<Integer>> possibleRoutes = new LinkedList<List<Integer>>();

    public static void main(String[] args)
    {

        // Create permutations
        for (int i = 0; i < Math.pow(2,NO_OF_FLOORS); i++) {
            permutations.add(convert(i));
        }

        // Display the results.
        for (BitSet permutation : permutations) {
            for (int i = 0; i < NO_OF_FLOORS; i++) {
                if (permutation.get(i)) {
                    System.out.print("1");
                } else {
                    System.out.print("0");
                }
            }
            System.out.println();
        }

        //populate calls with current floor set
        addDummyCalls(3);

        //check stack for floors
        checkStack();

        //get all possible combinations through recursion
        permute();

        //compute costs
        computeCosts();
    }

    //needs more logic to include the factor of 0/G only travelling up
    //and 8/top floor only travelling down
    private static void computeCosts() {
        int tempCurrentFloor = getCurrentFloor();

        int bestRoute = 0;
        int bestRouteTime = 0;

        for(List<Integer> route : possibleRoutes){
            int routeTotalTime = 0;
            for(int floorToVisit : route){
                //abs diff between current and next
                int floorDiff = Math.abs(getCurrentFloor() - floorToVisit);
                routeTotalTime += (floorDiff * FLOOR_TRAVEL_TIME) +
                        ((DOOR_OPEN_CLOSE_TIME * 2) * floorDiff);

                //set current floor to floorToVisit (now visited)
                setCurrentFloor(floorToVisit);
            }
            //print route
            int routeNum = (possibleRoutes.indexOf(route) + 1);
            System.out.println("Route: " + routeNum);
            System.out.println("Total time (ms): " + routeTotalTime);

            //set best route
            if (bestRouteTime == 0 || routeTotalTime < bestRouteTime){
                bestRoute = routeNum;
                bestRouteTime = routeTotalTime;
            }

            //reset current floor to original
            setCurrentFloor(tempCurrentFloor);
        }

        //best route
        System.out.println("Best route: " + bestRoute + " time: " +bestRouteTime);
    }

    private static void permute(){
        permute(0);
    }

    private static void permute(int index) {
            if (index == callStack.size())
            {
                List<Integer> route = new LinkedList<Integer>();
                for (int i = 0; i < callStack.size(); i++)
                {
                    System.out.print(" [" + callStack.get(i) + "] ");
                    //add to list
                    route.add(callStack.get(i));
                }
                System.out.println();
                possibleRoutes.add(route);
            }
            else
            {
                for (int i = index; i < callStack.size(); i++)
                {
                    int temp = callStack.get(index);
                    callStack.set(index, callStack.get(i));
                    callStack.set(i, temp);

                    permute(index + 1);

                    temp = callStack.get(index);
                    callStack.set(index, callStack.get(i));
                    callStack.set(i,temp);
                }
            }
    }

    private static void checkStack() {
        //print floors in stack
        System.out.println("Floor numbers in stack");
        for(int i : callStack){
            System.out.println(i);
        }
    }

    private static void addDummyCalls(int currentFloor) {
        if (currentFloor < 0 || currentFloor > NO_OF_FLOORS){
            System.out.println("Error - outside of floor range");
            System.exit(-1);
        }

        setCurrentFloor(currentFloor);
        addDummyCalls();
    }

    private static void addDummyCalls() {
        //create 3 random calls excluding current and duplicates
        int floor;
        for (int i = 0; i < 3; i++){
            floor = rnd.nextInt(NO_OF_FLOORS - 1);
            if (floor != currentFloor && !callStack.contains(floor)){
                callStack.add(floor);
            }else{
                i--;
            }
        }
    }

    public static void setCurrentFloor(int currentFloor) {
        BuildAI.currentFloor = currentFloor;
    }

    public static int getCurrentFloor() {
        return BuildAI.currentFloor;
    }

    public static BitSet convert(long value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

}


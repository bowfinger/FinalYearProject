import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Jamie on 29/12/2015.
 */
public class Algorithm implements ICallstackCallback {


    private Messenger messenger;

    private static final String CLIENT_ID = "ALGORITHM";
    private static final int FLOOR_TRAVEL_TIME = 3000; //milliseconds
    private static final int DOOR_OPEN_CLOSE_TIME = 2000; //milliseconds
    private static final int NO_OF_FLOORS = 8;
    private int currentFloor = 0;

    //split this call stack into hall and car
    private CallStackList<Integer> callStack = new CallStackList<Integer>();

    private List<BitSet> permutations = new LinkedList<BitSet>();
    private List<List<Integer>> possibleRoutes = new LinkedList<List<Integer>>();

    private BlockingQueue<FloorData> monitoringData = new LinkedBlockingQueue<FloorData>();

    public void run() throws MqttException {

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

        callStack.setCallback(this);

        //register MQTT
        messenger = new Messenger(CLIENT_ID, buildCallback());
        messenger.connect();

        //subscribe to all
        try {
            messenger.subscribe("Monitor/#");
            messenger.subscribe("Call/Floor/#");
            messenger.subscribe("Call/Hall/#");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
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

    //needs more logic to include the factor of 0/G only travelling up
    //and 8/top floor only travelling down
    private void computeCosts() {

        //clear possible routes
        possibleRoutes.clear();
        //work out routes first with recursion
        permute();

        int tempCurrentFloor = getCurrentFloor();

        int bestRoute = 0;
        int bestRouteTime = 0;
        String bestRouteString = "";

        for(List<Integer> route : possibleRoutes){
            int routeTotalTime = 0;
            String routeString = String.valueOf(getCurrentFloor());
            for(int floorToVisit : route){
                //abs diff between current and next
                int floorDiff = Math.abs(getCurrentFloor() - floorToVisit);
                routeTotalTime += (floorDiff * FLOOR_TRAVEL_TIME) +
                        ((DOOR_OPEN_CLOSE_TIME * 2) * floorDiff);

                //set current floor to floorToVisit (now visited)
                routeString += " => " + String.valueOf(floorToVisit);
                setCurrentFloor(floorToVisit);
            }
            //print route
            int routeNum = (possibleRoutes.indexOf(route) + 1);
            System.out.println("Route: " + routeNum);
            System.out.println("Total time (ms): " + routeTotalTime);

            //set best route
            //change to setters
            if (bestRouteTime == 0 || routeTotalTime < bestRouteTime){
                bestRoute = routeNum;
                bestRouteTime = routeTotalTime;
                bestRouteString = routeString;
            }

            //reset current floor to original
            setCurrentFloor(tempCurrentFloor);
        }

        //best route
        System.out.println(String.format("Best route: %d\nRoute time: %d\nRoute: %s", bestRoute, bestRouteTime, bestRouteString));
    }

    private void permute(){
        permute(0);
    }

    private void permute(int index) {
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

    private MqttCallback buildCallback() {
        return new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("CONNECTION LOST");
            }

            @Override
            public void messageArrived(String topic, MqttMessage msg) throws Exception {
                String jsonData = new String(msg.getPayload());

                //display received message
                System.out.println("Received Topic: " + topic);
                System.out.println("Received Data: " + jsonData);
                System.out.println();

                try {
                    FloorData data = DataInterpreter.read(jsonData);

                    //depending on topic of data do something
                    //finalise topic values
                    switch(topic.substring(0, topic.length() - 1)){
                        case "Monitor/":

                            //store data
                            monitoringData.put(data);

                            //output
                            System.out.println("Storing floor data...");
                            System.out.println(data.toString());
                            System.out.println();


                            break;
                        //test
                        case "Call/Floor/":
                            System.out.println("Floor call received");
                            callStack.add(data.getFloor());
                            break;

                        case "Call/Hall/":
                            System.out.println("Hall call received");
                            callStack.add(data.getFloor());
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        };
    }

    @Override
    public void stackUpdated() {
        if(!callStack.isEmpty()) {
            //output
            System.out.println("CALLBACK FIRED");
            computeCosts();
        }
    }
}

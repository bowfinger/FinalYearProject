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

    private MQTTMessenger mqttMessenger;

    public static final int FLOOR_TRAVEL_TIME = 3000; //milliseconds
    public static final int DOOR_OPEN_CLOSE_TIME = 2000; //milliseconds

    public static final int NO_OF_FLOORS = 8;
    public int currentFloor = 0;

    //split this call stack into hall and car
    public CallStackList<Integer> callStack = new CallStackList<Integer>();
    public List<Integer> tempCallStack = new LinkedList<Integer>();

    public Random rnd = new Random();
    public List<BitSet> permutations = new LinkedList<BitSet>();
    public List<List<Integer>> possibleRoutes = new LinkedList<List<Integer>>();

    public BlockingQueue<FloorData> monitoringData = new LinkedBlockingQueue<FloorData>();

    public void run() throws MqttException {

        callStack.setCallback(this);

        //register MQTT
        mqttMessenger = new MQTTMessenger();
        mqttMessenger.setCallback(buildCallback());
        mqttMessenger.connect();

        try {
            //mqttMessenger.subscribe("Monitoring Data");
            mqttMessenger.subscribe("Floor Call");
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
            //change to setters
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
                    switch(topic){
                        case "Monitoring Data":

                            //store data
                            monitoringData.put(data);

                            //output
                            System.out.println("Storing floor data...");
                            System.out.println(data.toString());
                            System.out.println();


                            break;
                        case "Floor Check":
                            if(data.getCount() == 0){
                                //remove hall call from stack
                                System.out.println("Nobody waiting at Floor " + data.getFloor());
                                System.out.println("Removing hall call from stack based on data...");
                                System.out.println();
                            }else{
                                System.out.println("Passengers still waiting at Floor " + data.getFloor() + ", proceeding as normal");
                                System.out.println();
                            }
                            break;

                        //test
                        case "Floor Call":
                            System.out.println("Floor call received");
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

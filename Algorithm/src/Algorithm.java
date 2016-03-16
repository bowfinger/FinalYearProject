import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Jamie on 29/12/2015.
 */
public class Algorithm {

    private Messenger publisher;
    private Messenger subscriber;
    private DataStorageHelper dsHelper;

    private static final String CLIENT_ID = "ALGORITHM";

    //weightings
    private static final double BASE_WEIGHTING = 1.0;
    private static final double TRAVEL_UP_MULTIPLIER = 1.1;
    private static final double TRAVEL_DOWN_MULTIPLIER = 0.8;
    private static final double FLOOR_DISTANCE_MULTIPLIER = 0.1;
    private static final int NO_OF_FLOORS = 8;
    private static final double[][] COST_MATRIX = new double[8][8];
    private int currentFloor = 0;

    private List<BitSet> permutations = new LinkedList<>();
    private Map<BitSet, HashMap<Integer, List<Integer>>> optimumRouteLookup = new HashMap<>();

    private List<FloorData> monitoringData = new ArrayList<>();

    public void run() throws MqttException {

        // Create permutations
        for (int i = 0; i < Math.pow(2,NO_OF_FLOORS); i++) {
            permutations.add(convert(i));
        }

        // generate optimum map
        for(BitSet permutation : permutations){
            optimumRouteLookup.put(permutation, new HashMap<>());
            for(int i = 0; i < NO_OF_FLOORS; i++){
                optimumRouteLookup.get(permutation).put(i, new ArrayList<>());
            }
        }

        //
        calculateCostMatrix();

        //register MQTT and connect
        publisher = new Messenger(CLIENT_ID + "Pub", publisherCallBack());
        subscriber = new Messenger(CLIENT_ID, subscriberCallBack());
        subscriber.connect();

        //subscribe to all required topics
        try {
            subscriber.subscribe("RouteList");
            subscriber.subscribe("Monitor/#");
            //messenger.subscribe("Call/Floor/#");
            //messenger.subscribe("Call/Hall/#");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private MqttCallback publisherCallBack() {
        return new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("CONNECTION LOST");
                try {
                    subscriber.wait(10000);
                    subscriber.connect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                //not used in publisher
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("Message delivered to broker");
            }
        };
    }

    private void calculateCostMatrix() {
        for(byte row = 0; row < NO_OF_FLOORS; row++){
            for(byte col = 0; col < NO_OF_FLOORS; col++){
                if(row == col){
                    COST_MATRIX[row][col] = 0;
                }else if(row < col){
                    //going up
                    COST_MATRIX[row][col] = BASE_WEIGHTING + ((col - row) * TRAVEL_UP_MULTIPLIER) - (Math.pow((col - row)* FLOOR_DISTANCE_MULTIPLIER,2) );
                }else{
                    //going down
                    COST_MATRIX[row][col] = BASE_WEIGHTING + ((row - col) * TRAVEL_DOWN_MULTIPLIER) - (Math.pow((row - col) * FLOOR_DISTANCE_MULTIPLIER,2));
                }
            }
        }

        //print out - 2dp (precision not shown)
        for(byte row = 0; row < NO_OF_FLOORS; row++){
            for(byte col = 0; col < NO_OF_FLOORS; col++){
                System.out.print(String.format("[%1$,.2f]",COST_MATRIX[row][col]));
            }
            System.out.println();
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

    private MqttCallback subscriberCallBack() {
        return new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("CONNECTION LOST");
                try {
                    subscriber.wait(10000);
                    subscriber.connect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage msg) throws Exception {
                String jsonData = new String(msg.getPayload());

                //display received message
                System.out.println("Received Topic: " + topic);
                System.out.println("Received Data: " + jsonData);
                System.out.println();

                //check topic
                if(topic.contains("RouteList")){
                    RouteData rd = DataInterpreter.readRouteData(jsonData);
                    BitSet b = new BitSet();
                    rd.getFloorsToVisit().stream().forEach(b::set);
                    computeOptimum(rd, b);
                }else if(topic.contains("Monitor")){
                    FloorData data = DataInterpreter.readFloorData(jsonData);
                    if(monitoringData.size() < 1){
                        monitoringData.add(data);
                    }else{
                        dsHelper.store(monitoringData);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                //not used in subscriber
            }
        };
    }

    private void computeOptimum(RouteData rd, BitSet b) {
        //look-up bitset in optimum map
        List<Integer> optimum = optimumRouteLookup.get(b).get(rd.getCurrentFloor());
        if (optimum.size() == 0){
            System.out.println("COMPUTING OPTIMUM");

            List<List<Integer>> permutations = permutationsOf(rd.getFloorsToVisit());

            double bestCost = 0;
            double currentCost;
            List<Integer> bestRoute = new ArrayList<>();

            for(List<Integer> ftv : permutations){
                currentCost = 0;
                setCurrentFloor(rd.getCurrentFloor());

                System.out.print("Route: " + permutations.indexOf(ftv) + " - ");
                for(int f : ftv){

                    //look up current floor
                    currentCost += COST_MATRIX[getCurrentFloor()][f];
                    setCurrentFloor(f);

                    System.out.print(f + " ");
                }

                if(bestCost == 0 || currentCost < bestCost){
                    bestCost = currentCost;
                    bestRoute = ftv;
                }

                System.out.println();
                System.out.println("Cost: " + currentCost);
            }

            System.out.println("Best Cost: " + bestCost);
            System.out.println("Best Route: " + permutations.indexOf(bestRoute));

            //set best in map
            optimumRouteLookup.get(b).put(rd.getCurrentFloor(), bestRoute);
            //set optimum
            optimum = bestRoute;
        }
        System.out.println("RETRIEVED + PUBLISHING OPTIMUM");
        try {
            publisher.connect();
            publisher.publish("OptimisedRouting", optimum);
            publisher.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private List<List<Integer>> permutationsOf(List<Integer> list) {
        return permutationsOf(list, 0, list.size());
    }

    private List<List<Integer>> permutationsOf(List<Integer> list, int start, int end) {
        List<List<Integer>> permutations = new ArrayList<>();
        if (start < end) {
            Integer nextInt = list.get(start);
            if (start == end - 1) {
                permutations.add(Arrays.asList(nextInt));
            } else {
                for (List<Integer> subList : permutationsOf(list, start + 1, end)) {
                    for (int i = 0; i <= subList.size(); i++) {
                        List<Integer> newList = new ArrayList<>(subList);
                        newList.add(i, nextInt);
                        permutations.add(newList);
                    }
                }
            }
        }
        return permutations;
    }
}

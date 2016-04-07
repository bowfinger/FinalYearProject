import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Jamie on 29/12/2015.
 */
public class BasicAlgorithm {

    private Messenger publisher;
    private Messenger subscriber;
    private DataStorageHelper dsHelper;

    private static final String CLIENT_ID = "ALGORITHM";
    private final int NO_OF_FLOORS = 8;
    private ElevatorDirection direction = ElevatorDirection.DOWN;

    private int currentFloor = 0;

    private List<FloorData> monitoringData = new ArrayList<>();

    public BasicAlgorithm(){
    }

    public void run() throws MqttException {
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

    public int getNoOfFloors(){
        return NO_OF_FLOORS;
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

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
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
                    publisher.connect();
                    publisher.publish("OptimisedRouting", computeOptimum(rd));
                    publisher.disconnect();
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

    public List<Integer> computeOptimum(RouteData rd) {
        List<Integer> optimum = new ArrayList<>();
        if(rd.getFloorsToVisit().size() > 0){
            //look-up bitset in optimum map
            optimum = rd.getFloorsToVisit().stream().sorted().collect(Collectors.toList());
            int current = rd.getCurrentFloor();
            if(current <= optimum.get(0)){
                //current route is ok
            }else if (current >= optimum.get(optimum.size()-1)){
                //reverse route
                Collections.reverse(optimum);
            }else if(direction == ElevatorDirection.UP){
                //get all above it
                optimum = rd.getFloorsToVisit().stream().sorted().filter(x -> x >= current).collect(Collectors.toList());
                direction = ElevatorDirection.DOWN;
                optimum.addAll(rd.getFloorsToVisit().stream().sorted().filter(x -> x < current).collect(Collectors.toList()));
            }else{
                //get all below it
                optimum = rd.getFloorsToVisit().stream().sorted().filter(x -> x <= current).collect(Collectors.toList());
                direction = ElevatorDirection.UP;
                optimum.addAll(rd.getFloorsToVisit().stream().sorted().filter(x -> x > current).collect(Collectors.toList()));
            }
        }
        return optimum;
    }
}

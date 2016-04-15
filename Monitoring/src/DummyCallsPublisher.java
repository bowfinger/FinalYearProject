import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.*;

/**
 * Created by Jamie on 10/04/2016.
 */
public class DummyCallsPublisher {
    private static final int NO_OF_FLOORS = 8;
    private static final int MAX_WAITING = 22;
    private List<Integer> floorList = new ArrayList<>();

    private Messenger messenger;
    private Timer timer;
    private Random rnd;

    public DummyCallsPublisher() throws MqttException {
        for(int i = 0; i< NO_OF_FLOORS; i++){
            floorList.add(i);
        }
        messenger = new Messenger("", buildCallback());
        timer = new Timer();
        rnd = new Random();
    }

    private MqttCallback buildCallback() {
        return new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("CONNECTION LOST");
            }

            @Override
            public void messageArrived(String topic, MqttMessage msg) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("Delivery complete");
            }
        };
    }

    public void run(){

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                int count = 0;
                Date timestamp = new Date();
                try {
                    //connect
                    messenger.connect();

                    RouteData rd = new RouteData();

                    rd.setCurrentFloor(rnd.nextInt(NO_OF_FLOORS));
                    int noOfCalls = rnd.nextInt(1 + (NO_OF_FLOORS - 1));
                    Collections.shuffle(floorList);
                    for (int i = 0; i <= noOfCalls; i++){
                        CallType rndCallType = rnd.nextInt(2) == 0 ? CallType.HALL : CallType.CAR;
                        rd.addFloorToVisit(new Call(floorList.get(i), rndCallType));
                    }

                    messenger.publish("RouteList", rd);
                    //disconnect
                    messenger.disconnect();

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 60000);
    }
}

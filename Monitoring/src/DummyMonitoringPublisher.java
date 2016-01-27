import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jamie on 06/01/2016.
 */
public class DummyMonitoringPublisher {

    private static final int NO_OF_FLOORS = 8;
    private static final int PROBABILITY_OF_ZERO = 4; // 3 in 4 (75%)
    private static final int MAX_WAITING = 22;

    private Messenger messenger;
    private Timer timer;
    private Random rnd;

    public DummyMonitoringPublisher() throws MqttException {
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

                    //test call from inside elevator
                    int floor = 3;
                    FloorData d = new FloorData(floor, timestamp, 5);
                    messenger.publish("Call/Floor/" + floor, d);

                    floor = 6;
                    d = new FloorData(floor, timestamp, 5);
                    messenger.publish("Call/Floor/" + floor, d);

                    //test call from floors
                    floor = 1;
                    d = new FloorData(floor, timestamp, 5);
                    messenger.publish("Call/Hall/" + floor, d);

                    floor = 6;
                    d = new FloorData(floor, timestamp, 5);
                    messenger.publish("Call/Hall/" + floor, d);

                    //generate data for each floor and publish
                    //dummy monitoring data from the floors
                    for(int i = 0; i < NO_OF_FLOORS; i++){

                        //probability of people waiting
                        //only publish if waiting?
                        if(rnd.nextInt(PROBABILITY_OF_ZERO) == 0){
                            count = rnd.nextInt(MAX_WAITING);
                            FloorData data = new FloorData(i, timestamp, count);
                            messenger.publish("Monitor/" + i, data);
                        }

                    }

                    //disconnect
                    messenger.disconnect();

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 60000);
    }
}

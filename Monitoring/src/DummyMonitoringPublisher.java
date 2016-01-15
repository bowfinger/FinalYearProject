import org.eclipse.paho.client.mqttv3.MqttException;

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

    private MQTTMessenger messenger;
    private Timer timer;
    private Random rnd;

    public DummyMonitoringPublisher() throws MqttException {
        messenger = new MQTTMessenger();
        timer = new Timer();
        rnd = new Random();
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

                    //test
                    FloorData d = new FloorData(3, timestamp, 5);
                    messenger.publish("Floor Call", d);

                    //generate data for each floor and publish
                    for(int i = 0; i < NO_OF_FLOORS; i++){

                        //probability of people waiting
                        //only publish if waiting?
                        if(rnd.nextInt(PROBABILITY_OF_ZERO) == 0){
                            count = rnd.nextInt(MAX_WAITING);
                            FloorData data = new FloorData(i, timestamp, count);
                            messenger.publish("Monitoring Data", data);
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

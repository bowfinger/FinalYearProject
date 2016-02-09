import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.opencv.core.MatOfPoint;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Jamie on 22/01/2016.
 */
public class PackageData implements Consumer<List<MatOfPoint>> {
    private Messenger messenger;
    private int floorId;
    public PackageData(int floorId) throws MqttException {
        this.messenger = new Messenger(String.format("Floor%d", floorId), new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                try {
                    messenger.wait(10000);
                    messenger.connect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("Messenger - Delivery Complete");
            }
        });
        this.floorId = floorId;
        messenger.connect();
    }

    @Override
    public void accept(List<MatOfPoint> matOfPoints) {
        FloorData data = new FloorData(floorId, new Date(), matOfPoints.size());

        //publish first data then sleep for 10 seconds
        try {
            messenger.publish(String.format("Monitor/%d", floorId), data);
            Thread.sleep(10000);
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

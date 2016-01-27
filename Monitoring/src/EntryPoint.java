import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Jamie on 06/11/2015.
 */
public class EntryPoint {

    //hardcoded floor num
    private static final int FLOOR_NUM = 4;

    public static void main(String[] args) throws InterruptedException, MqttException {

        OpenCVApplication app = new OpenCVApplication(AppMode.DEBUG, FLOOR_NUM);
        app.run();

        //using dummy publisher to generate counts of people waiting at floors every 1 minute
        //DummyMonitoringPublisher dummy = new DummyMonitoringPublisher();
        //dummy.run();
    }

}
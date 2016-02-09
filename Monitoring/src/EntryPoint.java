import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Jamie on 06/11/2015.
 */
public class EntryPoint {

    public static void main(String[] args) throws InterruptedException, MqttException {

        int floorNum = 0;
        if(args.length == 1){
            try {
                floorNum = Integer.parseInt(args[0]);
            }catch (NumberFormatException ex){
                System.exit(-1);
            }
        }

        OpenCVApplication app = new OpenCVApplication(AppMode.DEBUG, floorNum);
        app.run();

        //using dummy publisher to generate counts of people waiting at floors every 1 minute
        //DummyMonitoringPublisher dummy = new DummyMonitoringPublisher();
        //dummy.run();
    }

}
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Jamie on 29/12/2015.
 */
public class EntryPoint {

    public static void main(String[] args) throws MqttException {
        //optimised
        Algorithm a = new Algorithm();
        a.run();

        //basic
//        BasicAlgorithm ba = new BasicAlgorithm();
//        ba.run();
    }
}

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Date;

/**
 * Created by Jamie on 11/12/2015.
 */
public class TEST {

    public static void main(String[] args){
        //MQTTSubscriber sub = new MQTTSubscriber("Server Instance");
        //sub.connect();
        //code afterwards still executes

        //set new floor data
        System.out.println("Creating Floor Data");
        FloorData d = new FloorData(1, new Date(), 12);

        //convert to json
        System.out.println("Converting to JSON");
        String jsonData = DataInterpreter.write(d);

        System.out.println("JSON Data below");
        System.out.println(jsonData);

        //convert back to obj
        System.out.println("Converting to Floor Data");
        FloorData d1 = DataInterpreter.read(jsonData);

        //change data
        System.out.println("Setting count of floor to 0");
        d1.setCount(0);
        System.out.println("Floor Data below");

        System.out.println(d1.toString());


        /*
        Messenger messenger = null;
        try {
            messenger = new Messenger();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        messenger.connect();

        try {
            //set subscribes
            messenger.subscribe("Monitoring Data");
            messenger.subscribe("Floor Check");

            //publish
            messenger.publish("Monitoring Data", d);
            Thread.sleep(1000);
            messenger.publish("Monitoring Data", d);
            Thread.sleep(1000);
            messenger.publish("Floor Check", d1);
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */


    }
}

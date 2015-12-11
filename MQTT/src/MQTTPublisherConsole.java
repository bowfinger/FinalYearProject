import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jamie on 13/11/2015.
 */
public class MQTTPublisherConsole {
    private static final int QOS = 1;


    public static void main(String[] args) throws InterruptedException {

        //set up interface for credentials
        //MQTTInterface i = new MQTTInterface();

        String broker = System.getenv("MQTT_BROKER");
        String username = System.getenv("MQTT_USERNAME");
        String pass = System.getenv("MQTT_PASS");

        String clientId = "Publisher";

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            mqttClient.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage msg)
                        throws Exception {
                    //not receiving any messages
                }

                public void deliveryComplete(IMqttDeliveryToken arg0) {
                    System.out.println("Delivery complete");
                }

                public void connectionLost(Throwable arg0) {
                    // TODO Auto-generated method stub
                }
            });

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setUserName(username);
            connOpts.setPassword(pass.toCharArray());
            mqttClient.connect(connOpts);




            //create 4 test messages
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            System.out.println(sdf.format(d));

            String s = "{\"Floor\":1, \"Timestamp\":\""+ sdf.format(d) +"\", \"Count\":3}";
            MqttMessage message = new MqttMessage(s.getBytes());
            message.setQos(QOS);
            System.out.println("Publish message: " + message);
            mqttClient.publish("Monitoring Data", message);

            Thread.sleep(1000);

            d = new Date();
            s = "{\"Floor\":5, \"Timestamp\":\""+ sdf.format(d) +"\", \"Count\":0}";
            message = new MqttMessage(s.getBytes());
            message.setQos(QOS);
            System.out.println("Publish message: " + message);
            mqttClient.publish("Floor Check", message);

            Thread.sleep(1000);

            d = new Date();
            s = "{\"Floor\":2, \"Timestamp\":\""+ sdf.format(d) +"\", \"Count\":8}";
            message = new MqttMessage(s.getBytes());
            message.setQos(QOS);
            System.out.println("Publish message: " + message);
            mqttClient.publish("Monitoring Data", message);

            Thread.sleep(1000);

            d = new Date();
            s = "{\"Floor\":6, \"Timestamp\":\""+ sdf.format(d) +"\", \"Count\":2}";
            message = new MqttMessage(s.getBytes());
            message.setQos(QOS);
            System.out.println("Publish message: " + message);
            mqttClient.publish("Floor Check", message);






            mqttClient.disconnect();
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}

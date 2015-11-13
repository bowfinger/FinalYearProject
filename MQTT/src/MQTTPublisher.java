import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jamie on 13/11/2015.
 */
public class MQTTPublisher {
    public static void main(String[] args) throws InterruptedException {

        //set up interface for credentials
        MQTTInterface i = new MQTTInterface();

        String clientId = "Publisher";

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient mqttClient = new MqttClient(i.BROKER, clientId, persistence);
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
            connOpts.setUserName(i.USERNAME);
            connOpts.setPassword(i.PASSWORD.toCharArray());
            mqttClient.connect(connOpts);




            //create 4 test messages
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            System.out.println(sdf.format(d));

            String s = "{\"Floor\":1, \"Timestamp\":\""+ sdf.format(d) +"\", \"Count\":3}";
            MqttMessage message = new MqttMessage(s.getBytes());
            message.setQos(i.QOS);
            System.out.println("Publish message: " + message);
            mqttClient.publish(i.MONITORING_TOPIC, message);

            Thread.sleep(1000);

            d = new Date();
            s = "{\"Floor\":5, \"Timestamp\":\""+ sdf.format(d) +"\", \"Count\":0}";
            message = new MqttMessage(s.getBytes());
            message.setQos(i.QOS);
            System.out.println("Publish message: " + message);
            mqttClient.publish(i.FLOOR_TOPIC, message);

            Thread.sleep(1000);

            d = new Date();
            s = "{\"Floor\":2, \"Timestamp\":\""+ sdf.format(d) +"\", \"Count\":8}";
            message = new MqttMessage(s.getBytes());
            message.setQos(i.QOS);
            System.out.println("Publish message: " + message);
            mqttClient.publish(i.MONITORING_TOPIC, message);

            Thread.sleep(1000);

            d = new Date();
            s = "{\"Floor\":6, \"Timestamp\":\""+ sdf.format(d) +"\", \"Count\":2}";
            message = new MqttMessage(s.getBytes());
            message.setQos(i.QOS);
            System.out.println("Publish message: " + message);
            mqttClient.publish(i.FLOOR_TOPIC, message);






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

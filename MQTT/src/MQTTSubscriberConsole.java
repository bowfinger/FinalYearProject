import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Jamie on 13/11/2015.
 */
public class MQTTSubscriberConsole {
    private static final int QOS = 1;

    public static void main(String[] args) throws InterruptedException {

        //set up interface for credentials
        // use environment variables instead here for security
        //MQTTInterface i = new MQTTInterface();

        String broker = System.getenv("MQTT_BROKER");
        String username = System.getenv("MQTT_USERNAME");
        String pass = System.getenv("MQTT_PASS");

        System.out.println(System.getenv());

        //MQTT client id to use for the device. "" will generate a client id automatically
        String clientId = "Server Instance";

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(pass.toCharArray());

            mqttClient.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage msg)
                        throws Exception {

                    String jsonData = new String(msg.getPayload());

                    //display received message
                    System.out.println("Received Topic: " + topic);
                    System.out.println("Received Data: " + jsonData);
                    System.out.println();

                    try{
                        FloorData data = DataInterpreter.read(jsonData);

                        //depending on topic of data do something
                        switch(topic){
                            case "Monitoring Data":
                                //store data
                                System.out.println("Storing floor data...");
                                System.out.println(data.toString());
                                System.out.println();
                                break;
                            case "Floor Check":
                                if(data.getCount() == 0){
                                    //remove hall call from stack
                                    System.out.println("Nobody waiting at Floor " + data.getFloor());
                                    System.out.println("Removing hall call from stack based on data...");
                                    System.out.println();
                                }else{
                                    System.out.println("Passengers still waiting at Floor " + data.getFloor() + ", proceeding as normal");
                                    System.out.println();
                                }
                                break;
                        }

                        System.out.println();
                        System.out.println("Waiting for messages...");
                        System.out.println();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                public void deliveryComplete(IMqttDeliveryToken arg0) {
                    //does not send
                }

                public void connectionLost(Throwable arg0) {

                    System.out.println("Disconnected from CloudMQTT");

                    if(!mqttClient.isConnected()){
                        try{
                            mqttClient.connect(connOpts);
                            Thread.sleep(5000);
                        } catch (MqttSecurityException e) {
                            e.printStackTrace();
                        } catch (MqttException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            mqttClient.connect(connOpts);

            mqttClient.subscribe("Monitoring Data", QOS);
            System.out.println("Subscribed to: " + "Monitoring Data");

            mqttClient.subscribe("Floor Check", QOS);
            System.out.println("Subscribed to: " + "Floor Check");

            System.out.println();
            System.out.println("Waiting for messages...");
            System.out.println();

            //keeps subscriber connection open
            //try {
            //    while (true) {
            //        Thread.sleep(300);
            //    }
            //} catch (InterruptedException ie) {
                //execution closed by used
            //} finally {
            //    mqttClient.disconnect();
            //    System.exit(0);
            //}

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

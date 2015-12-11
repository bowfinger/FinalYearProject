import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Date;

/**
 * Created by Jamie on 11/12/2015.
 */
public class MQTTSubscriber {

    private static final String BROKER;
    private static final String USER;
    private static final String PASS;

    //private String ClientId;
    private MqttClient mqttClient;

    static{
        BROKER = System.getenv("MQTT_BROKER");
        USER = System.getenv("MQTT_USERNAME");
        PASS = System.getenv("MQTT_PASS");
    }

    public MQTTSubscriber(String clientId){
        //this.ClientId = clientId;

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(BROKER, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(USER);
            connOpts.setPassword(PASS.toCharArray());

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

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void connect(){
        try {
            mqttClient.subscribe("Monitoring Data", 1);
            System.out.println("Subscribed to: " + "Monitoring Data");

            mqttClient.subscribe("Floor Check", 1);
            System.out.println("Subscribed to: " + "Floor Check");

            System.out.println("\nWaiting for messages...\n");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

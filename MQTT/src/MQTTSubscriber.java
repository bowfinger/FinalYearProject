import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Jamie on 13/11/2015.
 */
public class MQTTSubscriber {
    public static void main(String[] args) throws InterruptedException {

        //set up interface for credentials
        MQTTInterface i = new MQTTInterface();

        //MQTT client id to use for the device. "" will generate a client id automatically
        String clientId = "Server Instance";

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient mqttClient = new MqttClient(i.BROKER, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(i.USERNAME);
            connOpts.setPassword(i.PASSWORD.toCharArray());

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

            mqttClient.subscribe(i.MONITORING_TOPIC, i.QOS);
            System.out.println("Subscribed to: " + i.MONITORING_TOPIC);

            mqttClient.subscribe(i.FLOOR_TOPIC, i.QOS);
            System.out.println("Subscribed to: " + i.FLOOR_TOPIC);

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

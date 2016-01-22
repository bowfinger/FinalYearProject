import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Jamie on 17/12/2015.
 */
public class Messenger{

    private MqttClient mqttClient;
    //private MemoryPersistence persistence = new MemoryPersistence();
    private MqttConnectOptions connOpts = new MqttConnectOptions();
    private static final String BROKER;
    private static final String USER;
    private static final String PASS;

    //set in constructor if needed
    //private static final String CLIENT_ID = "";

    static{
        BROKER = System.getenv("MQTT_BROKER");
        USER = System.getenv("MQTT_USERNAME");
        PASS = System.getenv("MQTT_PASS");
    }

    public Messenger(String clientId, MqttCallback callback) throws MqttException {
        mqttClient = new MqttClient(BROKER, clientId, new MemoryPersistence());
        mqttClient.setCallback(callback);
        connOpts.setCleanSession(true);
        connOpts.setUserName(USER);
        connOpts.setPassword(PASS.toCharArray());
    }

    public void connect(){
        try {
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

    public void disconnect(){
        try{
            mqttClient.disconnect();
        }catch(MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void subscribe(String topic) throws MqttException {
        mqttClient.subscribe(topic, 1);
    }

    public void publish(String topic, FloorData floorData) throws MqttException {
        String jsonData = DataInterpreter.write(floorData);
        MqttMessage message = new MqttMessage(jsonData.getBytes());
        message.setQos(1);

        System.out.println("Publishing message: " + message);

        mqttClient.publish(topic, message);
    }
}

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Jamie on 17/12/2015.
 */
public class MQTTMessenger extends MqttClient {

    //private MemoryPersistence persistence = new MemoryPersistence();
    private MqttConnectOptions connOpts = new MqttConnectOptions();
    private static final String BROKER;
    private static final String USER;
    private static final String PASS;

    //set in constructor if needed
    private static final String CLIENT_ID = "";

    static{
        BROKER = System.getenv("MQTT_BROKER");
        USER = System.getenv("MQTT_USERNAME");
        PASS = System.getenv("MQTT_PASS");
    }

    public MQTTMessenger() throws MqttException {
        super(BROKER, CLIENT_ID, new MemoryPersistence());

        connOpts.setCleanSession(true);
        connOpts.setUserName(USER);
        connOpts.setPassword(PASS.toCharArray());
    }

    @Override
    public void connect(){
        try {
            super.connect(connOpts);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    @Override
    public void subscribe(String topic) throws MqttException {
        super.subscribe(topic, 1);
    }

    public void publish(String topic, FloorData floorData) throws MqttException {
        String jsonData = DataInterpreter.write(floorData);
        MqttMessage message = new MqttMessage(jsonData.getBytes());
        message.setQos(1);

        System.out.println("Publishing message: " + message);

        super.publish(topic, message);
    }
}

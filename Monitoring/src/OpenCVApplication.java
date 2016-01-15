/**
 * Created by Jamie on 06/11/2015.
 */
import org.eclipse.paho.client.mqttv3.MqttException;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.Date;
import java.util.stream.Stream;

public class OpenCVApplication {

    private ScreenOutput screen;
    private Preprocessor pp;
    private FrameSupplier supplier;
    private Stream<Mat> frameStream;
    private static Mat initialFrame;
    private MQTTMessenger mqttMessenger;

    public OpenCVApplication(AppMode mode){
        //load OpenCV lib
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        //Instantiate variables
        supplier = new FrameSupplier();
        initialFrame = new Mat();

        if(mode == AppMode.DEBUG){
            //build panels and display video streams
            screen = new ScreenOutput();
        }
    }

    public void run() throws MqttException {

        //register MQTT
        mqttMessenger = new MQTTMessenger();
        mqttMessenger.connect();

        FloorData d = new FloorData(1, new Date(), 12);
        try {
            mqttMessenger.publish("Monitoring Data", d);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        //set stream
        frameStream = Stream.generate(supplier);

        //grab initial frame
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        initialFrame = new Preprocessor().apply(supplier.get());

        //show in other window
        //screen.filterPanel.display(initialFrame);

        frameStream.map(new Preprocessor())
                .map(new ComputeContours())
                .forEach(x -> screen.mainPanel.display(x));
    }

    public static Mat getInitialFrame(){
        return initialFrame;
    }
}
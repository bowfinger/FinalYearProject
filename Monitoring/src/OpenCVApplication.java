/**
 * Created by Jamie on 06/11/2015.
 */
import org.eclipse.paho.client.mqttv3.MqttException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import java.util.stream.Stream;

public class OpenCVApplication {

    private FrameSupplier supplier;
    private static Mat initialFrame;
    private int floorId;

    public OpenCVApplication(AppMode mode, int floorId){
        this.floorId = floorId;
        //load OpenCV lib
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        //Instantiate variables
        supplier = new FrameSupplier();
        initialFrame = new Mat();
    }

    public static Mat getInitialFrame(){
        return initialFrame;
    }

    public void run() throws MqttException {

        //set stream pipeline
        Stream<Mat> frameStream = Stream.generate(supplier);

        //grab initial frame
        try {
            Thread.sleep(5000);
            initialFrame = new Preprocessor().apply(supplier.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //functions applied to stream
        frameStream.map(new Preprocessor())
                .map(new ComputeContours())
                .forEach(new PackageData(floorId));
    }
}
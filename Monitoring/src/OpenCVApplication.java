/**
 * Created by Jamie on 06/11/2015.
 */
import org.eclipse.paho.client.mqttv3.MqttException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import java.util.stream.Stream;

public class OpenCVApplication {

    private FrameSupplier supplier;
    private Preprocessor preprocessor;
    private ComputeContours computeContours;
    private PackageData packageData;

    public OpenCVApplication(int floorId){
        //load OpenCV lib
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        //Instantiate variables
        supplier = new FrameSupplier();
        preprocessor = new Preprocessor();
        computeContours = new ComputeContours();
        try {
            packageData = new PackageData(floorId);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void run() throws MqttException {

        //set stream pipeline
        Stream<Mat> frameStream = Stream.generate(supplier);

        //allow init of camera
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //functions applied to stream
        frameStream.map(preprocessor)
                .map(computeContours)
                .forEach(packageData);
    }
}
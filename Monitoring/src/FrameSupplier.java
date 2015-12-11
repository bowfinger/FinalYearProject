/**
 * Created by Jamie on 06/11/2015.
 */
import java.util.function.Supplier;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class FrameSupplier implements Supplier<Mat>{

    private static String VIDEO_SOURCE = "F:\\Cam2_Indoor.avi";

    private Mat bgrFrame;
    private VideoCapture camera;

    public FrameSupplier(){
        bgrFrame = new Mat();
        camera = new VideoCapture(0);
        //camera = new VideoCapture(VIDEO_SOURCE);
    }

    @Override
    public Mat get(){
        if(camera.isOpened()){
            camera.read(bgrFrame);
        }
        return bgrFrame;
    }

}
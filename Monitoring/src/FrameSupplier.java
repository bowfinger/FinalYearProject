/**
 * Created by Jamie on 06/11/2015.
 */
import java.util.function.Supplier;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class FrameSupplier implements Supplier<Mat>{

    private Mat bgrFrame;
    private VideoCapture camera;

    public FrameSupplier(){
        bgrFrame = new Mat();
        camera = new VideoCapture(0);
    }

    @Override
    public Mat get(){
        if(camera.isOpened()){
            camera.read(bgrFrame);
        }
        return bgrFrame;
    }

}
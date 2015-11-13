/**
 * Created by Jamie on 06/11/2015.
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class OpenCVApplication {

    private ScreenOutput screen;
    private Preprocessor pp;
    private FrameSupplier supplier;
    private Stream<Mat> frameStream;
    private Mat frame;

    public OpenCVApplication(AppMode mode){
        //load OpenCV lib
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        //Instantiate variables
        supplier = new FrameSupplier();
        frame = new Mat();

        if(mode == AppMode.DEBUG){
            //build panels and display video streams
            screen = new ScreenOutput();
        }
    }

    public void run(){

        //set stream
        frameStream = Stream.generate(supplier);

    }
}
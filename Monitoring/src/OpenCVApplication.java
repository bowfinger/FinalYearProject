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
    private Mat initialFrame;

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
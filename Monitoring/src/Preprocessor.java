/**
 * Created by Jamie on 06/11/2015.
 */
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.function.Function;

public class Preprocessor implements Function<Mat, Mat> {

    private Mat mirroredFrame;

    public Preprocessor(){
        mirroredFrame = new Mat();
    }

    @Override
    public Mat apply(Mat t) {

        //basic horizontal flip
        //add more pre-processing
        Core.flip(t, mirroredFrame, 1);

        //test
        //System.out.println("Flipped");

        return mirroredFrame;
    }

}

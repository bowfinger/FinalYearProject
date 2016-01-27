/**
 * Created by Jamie on 06/11/2015.
 */
import org.opencv.core.Core;
import org.opencv.core.Mat;
import java.util.function.Function;

public class Preprocessor implements Function<Mat, Mat> {

    @Override
    public Mat apply(Mat t) {

        Mat mirroredFrame = new Mat();

        //basic horizontal flip
        Core.flip(t, mirroredFrame, 1);

        //resize frame for specific region to be monitored?

        return mirroredFrame;
    }
}

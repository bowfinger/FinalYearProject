/**
 * Created by Jamie on 06/11/2015.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

public class ComputeContours implements Function<Mat, List<MatOfPoint>>{
    private List<MatOfPoint> contours;
    private Mat hierarchy;

    public ComputeContours(){
        contours = new ArrayList<MatOfPoint>();
        hierarchy = new Mat();
    }

    @Override
    public List<MatOfPoint> apply(Mat t) {
        Imgproc.findContours(t, contours,  hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

}
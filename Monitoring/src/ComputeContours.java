/**
 * Created by Jamie on 06/11/2015.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class ComputeContours implements Function<Mat, List<MatOfPoint>>{

    private static final double THRESHOLD = 25;
    private static final double MAX_THRESHOLD = 255;
    private static final double BLUR_SIZE = 21;
    private static final double MIN_CONTOUR_AREA = 2000;
    private static final double MAX_CONTOUR_AREA = 4000;

    @Override
    public List<MatOfPoint> apply(Mat t) {

        Mat grey = new Mat();
        Mat hierarchy = new Mat();

        //test
        Mat frameDelta = new Mat();

        Imgproc.cvtColor(t, grey, Imgproc.COLOR_RGBA2GRAY);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>(200);
        //blur
        Imgproc.GaussianBlur(grey, grey, new Size(BLUR_SIZE,BLUR_SIZE), 0);

        //threshold
        Mat greyInitial = new Mat();
        Imgproc.cvtColor(OpenCVApplication.getInitialFrame(), greyInitial, Imgproc.COLOR_RGBA2GRAY);
        Core.absdiff(greyInitial, grey, frameDelta);
        Imgproc.threshold(frameDelta, grey, THRESHOLD, MAX_THRESHOLD, Imgproc.THRESH_BINARY);

        //dilate
        Imgproc.dilate(grey, grey, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));
        Imgproc.dilate(grey, grey, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));

        //erode
        Imgproc.erode(grey, grey, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));

        Mat greyCopy = new Mat();
        grey.copyTo(greyCopy);

        Imgproc.findContours(greyCopy, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        //a list for only selected contours
        List<MatOfPoint> selectedContours = new ArrayList<MatOfPoint>();

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area >= MIN_CONTOUR_AREA && area <= MAX_CONTOUR_AREA) {
                selectedContours.add(contour);
            }
        }

        //contour count
        System.out.println("Contours = " + selectedContours.size());
        return selectedContours;
    }

}

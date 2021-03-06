/**
 * Created by Jamie on 06/11/2015.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class ComputeContours implements Function<Mat, List<MatOfPoint>>{

    private static final double THRESHOLD = 25; //25
    private static final double MAX_THRESHOLD = 255; //255
    private static final double BLUR_SIZE = 5; //21
    private static final double MIN_CONTOUR_AREA = 6000; //3000
    private static final double MAX_CONTOUR_AREA = 36500; //10000
    private static final int DILATE_X = 2;
    private static final int DILATE_Y = 2;
    private static final int ERODE_X = 2;
    private static final int ERODE_Y = 2;
    private static Mat initialFrame;

    @Override
    public List<MatOfPoint> apply(Mat t) {
        if(initialFrame == null){
            initialFrame = t;
        }

        Mat grey = new Mat();
        Mat hierarchy = new Mat();
        Mat frameDelta = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>(200);

        //greyscale
        Imgproc.cvtColor(t, grey, Imgproc.COLOR_RGBA2GRAY);

        //blur
        Imgproc.GaussianBlur(grey, grey, new Size(BLUR_SIZE,BLUR_SIZE), 10); //0

        //threshold
        Mat greyInitial = new Mat();
        Imgproc.cvtColor(initialFrame, greyInitial, Imgproc.COLOR_RGBA2GRAY);
        Core.absdiff(greyInitial, grey, frameDelta);
        Imgproc.threshold(frameDelta, grey, THRESHOLD, MAX_THRESHOLD, Imgproc.THRESH_BINARY);

        //dilate
        Size dilateArea = new Size(DILATE_X, DILATE_Y);
        Imgproc.dilate(grey, grey, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, dilateArea));
        Imgproc.dilate(grey, grey, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, dilateArea));

        //erode
        Size erodeArea = new Size(ERODE_X, ERODE_Y);
        Imgproc.erode(grey, grey, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, erodeArea));

        //find contours
        Imgproc.findContours(grey, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE); //RETR_EXTERNAL

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

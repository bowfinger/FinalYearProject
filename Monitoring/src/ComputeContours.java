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
        List<MatOfPoint> SelectedContours = new ArrayList<MatOfPoint>(200);

        for(int i=0; i < contours.size(); i++)
        {
            if (Imgproc.contourArea(contours.get(i)) > MIN_CONTOUR_AREA){
                SelectedContours.add(contours.get(i));
            }
        }

        //contour count
        System.out.println("Contours = " + SelectedContours.size());

        Imgproc.drawContours(t, SelectedContours, -1, new Scalar(255,255,255,0), 1);
        return t;







        //Imgproc.findContours(t, contours,  hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //return contours;
    }

}
/**
 * Created by Jamie on 06/11/2015.
 */
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;


public class ImageProcessor {

    private double mogLearnRate = 0.1;
    private BackgroundSubtractorMOG2 mog = Video.createBackgroundSubtractorMOG2();

    public ImageProcessor(){

    }

    //mixture of gaussians
    public Mat processFrameMOG(Mat frame){
        Mat foregroundImage = new Mat();

        mog.apply(frame, foregroundImage, mogLearnRate);
        Imgproc.erode(foregroundImage, foregroundImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
        Imgproc.dilate(foregroundImage, foregroundImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
        return foregroundImage;
    }

    //Incorporate into preprocessing
    public Mat mirror(Mat frame){
        Mat mirroredFrame = new Mat();
        Core.flip(frame, mirroredFrame, 1);
        return mirroredFrame;
    }
}
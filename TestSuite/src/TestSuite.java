import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Jamie on 25/02/2016.
 */
public class TestSuite {

    private static int NO_OF_FLOORS = 8;
    private static Random RND = new Random();


    public static void main(String[] args) throws MqttException, InterruptedException {

        //////////////////////
        // MONITORING TESTS //
        //////////////////////

        System.loadLibrary( Core.NATIVE_LIBRARY_NAME ); //required by OpenCV functions

        //frame supplier
        FrameSupplier fs = new FrameSupplier();
        Thread.sleep(1000);//initialise camera
        Mat mat = fs.get();
        System.out.println(mat.channels()); //output no of channels
        System.out.println(mat.total()); //output no of pixels

        //preprocessor
        Mat mat1 = new Mat();
        Mat mat1flipped = new Preprocessor().apply(mat1);
        System.out.println(mat1flipped.equals(mat1)); // matrix comparison should be false

        //compute contours - requires captured frame
        //FrameSupplier fs1 = new FrameSupplier();
        //Thread.sleep(1000);//initialise camera
        //Mat mat3 = fs.get();
        //List<MatOfPoint> points1 = new ComputeContours().apply(mat3);
        //System.out.println(points1); // > 0

        //package data
        Point p = new Point(1,1);
        MatOfPoint matOfPoint = new MatOfPoint(p);
        List<MatOfPoint> points3 = new ArrayList<MatOfPoint>();
        points3.add(matOfPoint);
        PackageData pd = new PackageData(0); //floor 0
        pd.accept(points3);


        /////////////////////////
        // SERIALIZATION TESTS //
        /////////////////////////

        Gson gson = new Gson();
        FloorData d1 = new FloorData(1, new Date(), 10);
        FloorData d2 = new FloorData(2, new Date(), 20);
        String d1Serialized = gson.toJson(d1);
        FloorData d3 = gson.fromJson(d1Serialized, FloorData.class);
        System.out.println(d3.equals(d1)); //true
        System.out.println(d2.equals(d1)); //false





        /////////////////////
        // MESSENGER TESTS //
        /////////////////////

        Messenger messenger = new Messenger("TEST", new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                //do nothing
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                //do nothing
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("Message delivered to broker");
            }
        });

        List<FloorData> floorDataList = new ArrayList<>();

        for(int x = 0; x < 1000; x++){
            for(int i = 0; i < NO_OF_FLOORS; i++){
                floorDataList.add(
                        new FloorData(i, new Date(), RND.nextInt(30))
                );
            }
        }

        messenger.connect();

        messenger.publish(String.format("Monitor/%d", 3), floorDataList.get(3));

        /*
        for(FloorData fd : floorDataList){
            messenger.publish(String.format("Monitor/%d", fd.getFloor()), fd);
            Thread.sleep(1000);
        }
        */


    }
}

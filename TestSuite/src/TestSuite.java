import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.opencv.core.*;
//import org.opencv.core.Algorithm;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.util.*;

/**
 * Created by Jamie on 25/02/2016.
 */
public class TestSuite {

//    private static int NO_OF_FLOORS = 8;
//    private static Random RND = new Random();


    public static void main(String[] args) throws MqttException, InterruptedException {

        //////////////////////
        // MONITORING TESTS //
        //////////////////////

        System.loadLibrary( Core.NATIVE_LIBRARY_NAME ); //required by OpenCV functions

        //frame supplier
//        FrameSupplier fs = new FrameSupplier();
//        Thread.sleep(1000);//initialise camera
//        Mat mat = fs.get();
//        System.out.println(mat.channels()); //output no of channels
//        System.out.println(mat.total()); //output no of pixels

        //preprocessor
//        Mat mat1 = new Mat();
//        Mat mat1flipped = new Preprocessor().apply(mat1);
//        System.out.println(mat1flipped.equals(mat1)); // matrix comparison should be false

        //compute contours - requires captured frame
        //FrameSupplier fs1 = new FrameSupplier();
        //Thread.sleep(1000);//initialise camera
        //Mat mat3 = fs.get();
        //List<MatOfPoint> points1 = new ComputeContours().apply(mat3);
        //System.out.println(points1); // > 0

        //compute contours - passing image
//        String[] imgPath = {
//                "F:/OpenCVTestImages/0.jpg",
//                "F:/OpenCVTestImages/1.jpg",
//                "F:/OpenCVTestImages/2.jpg",
//                "F:/OpenCVTestImages/5.jpg"
//        };
//        ComputeContours cc = new ComputeContours();
//        List<MatOfPoint> contours;
//
//        for(int iterations = 0; iterations < 1000; iterations++){
//            for(String file : imgPath){
//                Mat img = Imgcodecs.imread(file);
//                contours = cc.apply(img);
//                if((file.contains("0.jpg") && contours.size() != 0) ||
//                        (file.contains("1.jpg") && contours.size() != 1)||
//                        (file.contains("2.jpg") && contours.size() != 2)||
//                        (file.contains("5.jpg") && contours.size() != 5)){
//                    System.out.println("FAILED - " + file);
//                }
//            }
//        }

        //package data
//        Point p = new Point(1,1);
//        MatOfPoint matOfPoint = new MatOfPoint(p);
//        List<MatOfPoint> points3 = new ArrayList<MatOfPoint>();
//        points3.add(matOfPoint);
//        PackageData pd = new PackageData(0); //floor 0
//        pd.accept(points3);


        /////////////////////////
        // SERIALIZATION TESTS //
        /////////////////////////

//        Gson gson = new Gson();
//        FloorData d1 = new FloorData(1, new Date(), 10);
//        FloorData d2 = new FloorData(2, new Date(), 20);
//        String d1Serialized = gson.toJson(d1);
//        FloorData d3 = gson.fromJson(d1Serialized, FloorData.class);
//        System.out.println(d3.equals(d1)); //true
//        System.out.println(d2.equals(d1)); //false





        /////////////////////
        // MESSENGER TESTS //
        /////////////////////

//        Messenger messenger = new Messenger("TEST", new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable throwable) {
//                //do nothing
//            }
//
//            @Override
//            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//                //do nothing
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//                System.out.println("Message delivered to broker");
//            }
//        });

//        List<FloorData> floorDataList = new ArrayList<>();
//
//        for(int x = 0; x < 1000; x++){
//            for(int i = 0; i < NO_OF_FLOORS; i++){
//                floorDataList.add(
//                        new FloorData(i, new Date(), RND.nextInt(30))
//                );
//            }
//        }

//        List<Integer> floorsToVisit = new ArrayList<Integer>();
//        floorsToVisit.add(2);
//        floorsToVisit.add(4);
//        floorsToVisit.add(7);
//        RouteData rd = new RouteData(6, floorsToVisit);
//        RouteData rd2 = new RouteData(1, floorsToVisit);
//        RouteData rd3 = new RouteData(4, floorsToVisit);
//
//        messenger.connect();
//
//        messenger.publish("RouteList", rd);
//        messenger.publish("RouteList", rd2);
//        messenger.publish("RouteList", rd3);
//        messenger.publish("RouteList", rd);
//        messenger.publish("RouteList", rd2);
//        messenger.publish("RouteList", rd3);


//        messenger.publish(String.format("Monitor/%d", 3), floorDataList.get(3));

        /*
        for(FloorData fd : floorDataList){
            messenger.publish(String.format("Monitor/%d", fd.getFloor()), fd);
            Thread.sleep(1000);
        }
        */

        /////////////////////
        // ALGORITHM TESTS //
        /////////////////////

//        Algorithm a =  new Algorithm();
//
//        //get cost matrix and assert size
//        //checks all array positions to ensure square array
//        double[][] matrix = a.getCostMatrix();
//        int totalMatrixPos = (int)Math.pow(a.getNoOfFloors(),2);
//        int testMatrixPos = 0;
//        for(int i = 0; i < matrix.length; i++){
//            testMatrixPos += matrix[i].length;
//        }
//        if (totalMatrixPos != testMatrixPos) throw new AssertionError();
//
//        //get print out cost matrix
//        a.printCostMatrix();


        ////////////////////////
        // BENCHMARKING TESTS //
        ////////////////////////

        ArrayList<Long> startTimes = new ArrayList<>();
        ArrayList<Long> endTimes = new ArrayList<>();

        Date now = new Date();
        FloorData fd = new FloorData(1,now,10);


        long startBenchmarkTime = System.nanoTime();
        for(int i = 0; i < 999; i++){
            System.nanoTime();
        }
        long endBenchmarkTime = System.nanoTime();
        long benchmarkDiff = (endBenchmarkTime - startBenchmarkTime) /1000;
        System.out.println("Benchmark of System.nanoTime() = " + benchmarkDiff);
//
//        final Messenger publisher = new Messenger("PublishTest", new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable throwable) {
//                //do nothing
//            }
//
//            @Override
//            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//                //do nothing
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//                //do nothing
//            }
//        });
//
//        final Messenger subscriber = new Messenger("SubscribeTest", new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable throwable) {
//                //do nothing
//            }
//
//            @Override
//            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//                //record time
//                endTimes.add(System.nanoTime());
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//                //do nothing
//            }
//        });
//
//        subscriber.connect();
//        subscriber.subscribe("TEST");
//
//        for(int i = 0; i<11; i++){
//            try {
//                startTimes.add(System.nanoTime());
//                publisher.connect();
//                publisher.publish("TEST", fd);
//                publisher.disconnect();
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//            //Thread.sleep(5000);
//        }
//
//        //ArrayList<Long> times = new ArrayList<>();
//        for(int i = 0; i < startTimes.size(); i++){
//            long calculated = ((endTimes.get(i) - benchmarkDiff) - (startTimes.get(i) - benchmarkDiff));
//            //times.add(calculated);
//            System.out.println(calculated);
//        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        List<RouteData> dataList = new ArrayList<>();
        Algorithm a = new Algorithm();
        BasicAlgorithm ba = new BasicAlgorithm();

        for(int i = 0; i < 8; i++) {
            for (BitSet b : a.getPermutations()) {
                RouteData rd = new RouteData();
                rd.setCurrentFloor(i);
                for (int j = 0; j < 8; j++) {
                    if (b.get(j)) {
                        rd.addFloorToVisit(new Call(j, CallType.CAR));
                    }
                }
                dataList.add(rd);
            }
        }

        System.out.println(dataList.size());

        class Pass{
            private int number;
            private long min;
            private long max;
            private long avg;

            public Pass(int number, long min, long max, long avg){
                this.number = number;
                this.min = min;
                this.max = max;
                this.avg = avg;
            }

            public int getNumber() {
                return number;
            }

            public long getMin() {
                return min;
            }

            public long getMax() {
                return max;
            }

            public long getAvg() {
                return avg;
            }
        }

        class BenchmarkTest{
            private int number;
            private List<Pass> passes = new ArrayList<>();
            public BenchmarkTest(int testNum){
                this.number = testNum;
            }

            public int getNumber() {
                return number;
            }

            public List<Pass> getPasses() {
                return passes;
            }

            public void addPass(Pass p){
                this.passes.add(p);
            }
        }



        List<BenchmarkTest> testBenchmarks = new ArrayList<>();

        //10 tests - 2 passes
        for(int tests = 1; tests <= 100; tests++){
            //a = new Algorithm();//new algorithm instance to reset calculated routes
            ba = new BasicAlgorithm();
            BenchmarkTest test = new BenchmarkTest(tests);
            for(int pass = 1; pass <= 2; pass++){
                startTimes = new ArrayList<>();
                endTimes = new ArrayList<>();
//                if(pass == 0){
//                    System.out.println("PASS 1 - CALCULATIONS REQUIRED");
//                }else{
//                    System.out.println("PASS 2 - RECALLING FROM MAP");
//                }
                for(RouteData rd : dataList){
                    startTimes.add(System.nanoTime());
                    //a.computeOptimum(rd);
                    ba.computeOptimum(rd);
                    endTimes.add(System.nanoTime());
                }

                long min = 0L;
                long max = 0L;
                long sum = 0L;

                for(int i = 0; i < startTimes.size(); i++){
                    long calculated = ((endTimes.get(i) - benchmarkDiff) - (startTimes.get(i) - benchmarkDiff));
                    sum+=calculated;
                    if(min == 0L || calculated < min){
                        min = calculated;
                    }
                    if(max == 0L || calculated > max){
                        max = calculated;
                    }
                    //System.out.println(calculated);
                }

                long average = sum/startTimes.size();
                //System.out.println("MIN: " + min);
                //System.out.println("MAX: " + max);
                //System.out.println("AVG: " + average);

                test.addPass(new Pass(pass, min, max, average));
            }
            testBenchmarks.add(test);
        }

        System.out.println("Test\tPass\tMin\t\tMax\t\t\t\tAvg");

        for(BenchmarkTest test : testBenchmarks){
            for(Pass p : test.getPasses()){
                System.out.println(String.format("%d\t\t%d\t\t%d\t\t%d\t\t%d", test.getNumber(), p.getNumber(), p.getMin(), p.getMax(), p.getAvg()));
            }
        }

    }
}

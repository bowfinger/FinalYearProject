import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Jamie on 06/11/2015.
 *
 * Main entry point for the Monitoring System Application
 * There are 3 implementations to avoid creation of additional entry points (main)
 *
 * - OpenCVApplication is the main application which requires a camera device to be connected to the system.
 * - DummyMonitoringPublisher acts as a simulation of the OpenCVApplication and supplies randomly generated data.
 * - DummyCallsPublisher simulates the Elevator Control System and publishes RouteData objects with a current floor
 *   and a list of floors to visit
 *
 *   Each instance is intended to be used separately. The "dummy" code can be used to test functionality of the
 *   algorithm without requiring a camera. Comment/Uncomment as required.
 */
public class EntryPoint {

    public static void main(String[] args) throws InterruptedException, MqttException {

        int floorNum = 0;
        if(args.length == 1){
            try {
                floorNum = Integer.parseInt(args[0]);
            }catch (NumberFormatException ex){
                System.exit(-1);
            }
        }

        //main application - requires access to camera source - not tested with embedded laptop cameras
//        OpenCVApplication app = new OpenCVApplication(floorNum);
//        app.run();

        //dummy publisher to generate counts of people waiting at floors every 1 minute
//        DummyMonitoringPublisher dummyMonitor = new DummyMonitoringPublisher();
//        dummyMonitor.run();

        //dummy publisher to generate floor calls every 1 minute
        DummyCallsPublisher dummyCalls = new DummyCallsPublisher();
        dummyCalls.run();
    }

}
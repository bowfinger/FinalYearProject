/**
 * Created by Jamie on 11/12/2015.
 */
public class TEST {

    public static void main(String[] args){
        MQTTSubscriber sub = new MQTTSubscriber("Server Instance");
        sub.connect();
        //code afterwards still executes
    }
}

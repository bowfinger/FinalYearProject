/**
 * Created by Jamie on 10/04/2016.
 */
public class Call {
    private int floor;
    private CallType callType;

    public Call(int floor, CallType callType){
        this.floor = floor;
        this.callType = callType;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }
}

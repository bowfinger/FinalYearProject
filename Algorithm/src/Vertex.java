/**
 * Created by Jamie on 08/12/2015.
 */

//graph representation of a floor
public class Vertex {

    private int Floor;

    public Vertex(int floorNum){
        this.Floor = floorNum;
    }

    public int getFloor(){
        return this.Floor;
    }

    public void setFloor(int floorNum){
        this.Floor = floorNum;
    }
}

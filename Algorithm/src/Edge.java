/**
 * Created by Jamie on 08/12/2015.
 */

//graph representation of travel
public class Edge {

    private Vertex Source;
    private Vertex Target;

    private int EnergyCost;
    private int TimeCost;

    public Vertex getSource(){
        return this.Source;
    }

    public void setSource(int floorNum){
        this.Source = new Vertex(floorNum);
    }

    public Vertex getTarget(){
        return this.Source;
    }

    public void setTarget(int floorNum){
        this.Target = new Vertex(floorNum);
    }
}

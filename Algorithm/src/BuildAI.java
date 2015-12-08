import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jamie on 08/12/2015.
 */
public class BuildAI {

    private static int FLOORS = 8;
    private static HashMap<Integer, List<BitSet>> FLOORPERMS;
    private static BitSet BITSET = new BitSet(8);
    private static List<BitSet> BITSETLIST;

    public static void main(String[] args){
        //instantiate
        FLOORPERMS = new HashMap<Integer, List<BitSet>>();
        BITSETLIST = new LinkedList<BitSet>();

        fill(0,8);


        System.out.println(BITSETLIST.size());

        for(int i = 0; i < FLOORS; i++){
            List<BitSet> list = new LinkedList<BitSet>(BITSETLIST);
            FLOORPERMS.put(i, list);
        }

        System.out.println(FLOORPERMS.size());

        for(int j = 0; j < BITSETLIST.size(); j++){
            for(int k = 0; k < BITSETLIST.get(j).size(); k++){
                int bit = BITSETLIST.get(j).get(k) == true ? 1 : 0;
                System.out.print(bit);
            }
            System.out.println();
        }

    }

    static void fill(int k, int n)
    {
        BitSet cloned;
        if (k == n)
        {
            System.out.println(BITSET);
            return;
        }
        BITSET.set(k, false);

        //added//////////////////
        //cloned = (BitSet)BITSET.clone();
        //BITSETLIST.add(cloned);
        //////////////////////

        fill(k + 1, n);
        BITSET.set(k, true);

        //added////////////////
        cloned = (BitSet)BITSET.clone();
        BITSETLIST.add(cloned);
        ////////////////////////

        fill(k+1, n);
    }
}

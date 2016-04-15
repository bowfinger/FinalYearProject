import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Jamie on 22/03/2016.
 */
public class AlgorithmTest extends TestCase {

    private Algorithm a = new Algorithm();

    //checks that array matrix is "square"
    public void testGetCostMatrix() throws Exception {
        double[][] matrix = a.getCostMatrix();
        int totalMatrixPos = (int)Math.pow(a.getNoOfFloors(),2);
        int testMatrixPos = 0;
        for(int i = 0; i < matrix.length; i++){
            testMatrixPos += matrix[i].length;
        }
        assertEquals(totalMatrixPos,testMatrixPos);
    }

    //output only test
    public void testPrintCostMatrix() throws Exception {
        a.printCostMatrix();
    }

    public void testGetNoOfFloors() throws Exception {
        assertNotNull(a.getNoOfFloors());
    }

    public void testSetCurrentFloor() throws Exception {
        assertEquals(0,a.getCurrentFloor());
        a.setCurrentFloor(5);
        assertEquals(5,a.getCurrentFloor());
    }

    public void testGetCurrentFloor() throws Exception {
        assertNotNull(a.getCurrentFloor());
    }

    public void testConvert() throws Exception {
        BitSet testBit = new BitSet();
        //set 1st position
        testBit.set(0);
        //convert 1
        BitSet newBit = a.convert(1);
        assertEquals(testBit, newBit);
    }

    public void testComputeOptimum() throws Exception {
        ArrayList<Call> calls = new ArrayList<>();
        calls.add(new Call(1, CallType.CAR));
        calls.add(new Call(2, CallType.CAR));
        calls.add(new Call(3, CallType.CAR));
        RouteData routeData = new RouteData(3, calls);
        List<Integer> optimum = a.computeOptimum(routeData);
        assertNotNull(optimum);
        assertTrue(optimum.size() > 0);
        assertTrue(optimum.size() == routeData.getFloorsToVisit().size());
    }

    public void testPermutationsOf() throws Exception {
        List<Integer> floors = new ArrayList<>(Arrays.asList(1,2,3));
        List<List<Integer>> permutations = a.permutationsOf(floors);

        //permutations should be a factorial
        int factorialNumber = IntStream.rangeClosed(2, floors.size()).reduce(1, Math::multiplyExact);

        assertEquals(permutations.size(),factorialNumber);
    }


}
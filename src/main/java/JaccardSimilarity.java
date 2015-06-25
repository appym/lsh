
import java.util.*;

/**
 * Created by Apratim Mishra on 5/20/15.
 */
public class JaccardSimilarity {

    public static double computeJaccardSimilarity(String[][] lshCandidatePairs) {
        List<String> l1 = twoDArrayToList(lshCandidatePairs, 0);
        List<String> l2 = twoDArrayToList(lshCandidatePairs, 1);
        return computeSimilarity(l1, l2);
    }

    public static double computeJaccardSimilarity2(int[][] candidatePairs) {
        List<Integer> l1 = twoDArrayToList2(candidatePairs, 0);
        List<Integer> l2 = twoDArrayToList2(candidatePairs, 1);
        return computeSimilarity2(l1, l2);
    }

    public static double computeSimilarity(Collection<String> s1, Collection<String> s2) {
        Set<String> unionSet = new HashSet<String>(s1);
        unionSet.addAll(s2);
        Set<String> intersectionSet = new HashSet<String>(s1);
        intersectionSet.retainAll(s2);
        return unionSet.size() == 0 ? 0.0 : 1.0*intersectionSet.size()/unionSet.size();
    }

    public static double computeSimilarity2(Collection<Integer> s1, Collection<Integer> s2) {
        Set<Integer> unionSet = new HashSet<Integer>(s1);
        unionSet.addAll(s2);
        Set<Integer> intersectionSet = new HashSet<Integer>(s1);
        intersectionSet.retainAll(s2);
        return unionSet.size() == 0 ? 0.0 : 1.0*intersectionSet.size()/unionSet.size();
    }

    public static double jaccardDistance(Set<String> s1, Set<String> s2) {
        return 1 - computeSimilarity(s1, s2);
    }

    private static List<String> twoDArrayToList(String[][] twoDArray, int index) {
        List<String> list;
        String[] colArray = new String[twoDArray.length];
        for(int row = 0; row < twoDArray.length; row++)
        {
            colArray[row] = twoDArray[row][index];
        }
        list = new ArrayList<String>();
        list.addAll(Arrays.asList(colArray));
        return list;
    }

    private static List<Integer> twoDArrayToList2(int[][] twoDArray, int index) {
        List<Integer> list;
        Integer[] colArray = new Integer[twoDArray.length];
        for(int row = 0; row < twoDArray.length; row++)
        {
            colArray[row] = twoDArray[row][index];
        }
        list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(colArray));
        return list;
    }

}

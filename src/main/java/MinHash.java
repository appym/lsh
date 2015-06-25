import java.util.*;

/**
 * Created by Apratim Mishra on 5/21/15.
 * When comparing N documents where N is small use MinHash.
 * This algorithm is tailored to compare 2 documents.
 */

public class MinHash<T> {

    private int[][] hash;
    private int numHash;
    private int ZERO = 0;
    private int ONE = 1;

    /**
     *@param numHash number of hashing algorithms
     */
    public MinHash(int numHash) {
        this.numHash = numHash;
    }

    private void buildHashArray(int numShingles) {
        hash = new int[numShingles][numHash];

        Random r = new Random(11); //use prime number as seed
        for (int i = 0; i < numShingles; i++) {
          for (int j=0; j < numHash; j++) {
              int a = r.nextInt();
              int b = r.nextInt();
              int c = r.nextInt();
              int x = hash(a * b * c, a, b, c);
              hash[i][j] = x;
          }
        }
    }

    public int[][] similarity(Collection<T> set1, Collection<T> set2) {

        int numSets = 2;
        Map<T, Integer[]> bitMap = buildBitMap(set1, set2);
        buildHashArray(bitMap.size());
        int[][] minHashValues = initializeHashBuckets(numSets, numHash);

        computeMinHash(minHashValues, bitMap);
        return minHashValues;
    }

    static double computeSimilarityFromSignatures(int[][] minHashValues, int numHashFunctions) {
        int identicalCandidatePairs = 0;
        for (int i = 0; i < numHashFunctions; i++) {
            if (minHashValues[i][0] == minHashValues[i][1]) {
                identicalCandidatePairs++;
            }
        }
        return (1.0 * identicalCandidatePairs) / numHashFunctions;
    }

    /**
     *
     */
    private static int[][] initializeHashBuckets(int numSets, int numHashFunctions) {
        int[][] minHashValues = new int[numHashFunctions][numSets];

        for (int i = 0; i < numHashFunctions; i++) {
            for (int j = 0; j < numSets; j++) {
                minHashValues[i][j] = Integer.MAX_VALUE;
            }
        }
        return minHashValues;
    }

    /**
     * @param x
     * @param a
     * @param b
     * @param c
     * @return
     */
    private static int hash(int x, int a, int b, int c) {
        int hashValue = ((a * (x >> 4) + b * x + c) & 131071);
        return Math.abs(hashValue);
    }


    private void computeMinHash(int[][] minHashValues, Map<T, Integer[]> bitArray) {
        int rowIndex = 0;
        for (Object value: bitArray.values()) { // for every element in the bit array
            Integer[] val = (Integer[]) value;
            for (int i=0; i < numHash; i++) {
                if (val[ZERO] == 1) { // if the set 1 contains the element
                    int hashVal = hash[rowIndex][i]; // get the hash
                    if (hashVal < minHashValues[i][ZERO]) {
                        // if current hash is smaller than the existing hash in the slot then replace with the smaller hash value
                        minHashValues[i][ZERO] = hashVal;
                    }
                }
                if (val[ONE] == 1) { // if the set 2 contains the element
                    int hashVal = hash[rowIndex][i]; // get the hash
                    if (hashVal < minHashValues[i][ONE]) {
                        // if current hash is smaller than the existing hash in the slot then replace with the smaller hash value
                        minHashValues[i][ONE] = hashVal;
                    }
                }
            }
            rowIndex++;
        }
    }

    /**
     * @param set1
     * @param set2
     * @return
     */
    public Map<T, Integer[]> buildBitMap(Collection<T> set1, Collection<T> set2) {

        Map<T, Integer[]> bitArray = new HashMap<T, Integer[]>();

        for (T t : set1) {
            bitArray.put(t, new Integer[]{1, 0});
        }

        for (T t : set2) {
            if (bitArray.containsKey(t)) {
                // item is present in set1 and set2
                bitArray.put(t, new Integer[]{1, 1});
            } else {
                // item is not present in set1
                bitArray.put(t, new Integer[]{0, 1});
            }
        }

        return bitArray;
    }

}
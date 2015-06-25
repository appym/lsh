
/**
 * Created by Apratim Mishra on 5/28/15.
 * Computer LSH only when comparing a large number of documents for similarity.
 * In any other case use MinHash
 */
public class LSH {

    static int numBands = 20;
    static int numDocs = 2;
    static String[][] lshBands = new String[numBands][numDocs];

    public static void computeLSH(int[][] minHashValues) {
        int bandNum = 0;
        //double threshold = (1/ numBands) ^ (1/count);
        for (int i = 0; i < minHashValues.length; i=i+5) {
            lshBands[bandNum][0] = createHash(
                        minHashValues[i][0], minHashValues[i+1][0], minHashValues[i+2][0], minHashValues[i+3][0], minHashValues[i+4][0]);
            lshBands[bandNum][1] = createHash(
                    minHashValues[i][1], minHashValues[i+1][1], minHashValues[i+2][1], minHashValues[i+3][1],
                    minHashValues[i+4][1]);
            bandNum++;
        }
        double sigSimilarity = computeSimilarityFromSignatures(lshBands, numBands);
        double jaccardSimilarity = JaccardSimilarity.computeJaccardSimilarity(lshBands);
        System.out.println("Signature Similarity of LSH & MinHash = "+sigSimilarity);
        System.out.println("Jaccard Similarity of LSH & MinHash = "+jaccardSimilarity);
    }


    /**
     * String concat to create cluster ID's
     */
    private static String createHash(int h1, int h2, int h3, int h4, int h5) {
        return getString(h1) + getString(h2) + getString(h3) + getString(h4) + getString(h5);
    }

    private static String getString(int h) {
        return String.valueOf(h);
    }

    private static double computeSimilarityFromSignatures(String[][] minHashValues, int numHashFunctions) {
        int identicalCandidatePairs = 0;
        for (int i = 0; i < numHashFunctions; i++) {
            if (minHashValues[i][0].equals(minHashValues[i][1])) {
                identicalCandidatePairs++;
            }
        }
        return (1.0 * identicalCandidatePairs) / numHashFunctions;
    }

}

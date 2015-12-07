import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Apratim Mishra on 6/1/15.
 */
public class LSHTest {

    public static void main(String[] args) throws IOException {

        textFilesMinHash();
    }

    private static void textFilesMinHash() {
        int numHash = 10; //10 hash algos. 10 gives optimal results
        Set<String> doc1 = Shingles.createShingles("/Users/dt205202/Documents/DupTest/Affidavit.txt");
        Set<String> doc2 = Shingles.createShingles("/Users/dt205202/Documents/DupTest/Affidavit-1.txt");
        System.out.println(doc1.size());
        System.out.println(doc2.size());
        MinHash<String> minHash = new MinHash<String>(numHash);
        int[][] minHashValues = minHash.similarity(doc1, doc2);

        System.out.println("Minhash similarity of the two documents is "+MinHash.computeSimilarityFromSignatures
                (minHashValues, numHash));
        System.out.println("Jaccard of MinHash "+JaccardSimilarity.computeJaccardSimilarity2(minHashValues));
    }
}

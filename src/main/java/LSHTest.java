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
        //f5500Matching();
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

    private static void f5500Matching() throws IOException {
        //F5500 columns
        final int SPONS_EIN_COL = 11;
        final int SPONS_NAME_COL = 4;
        final int SPONS_ZIP_COL = 10;
        final int SPONS_CTIY = 8;
        final int SPONS_ADD_1 = 6;
        final int SPONS_ADD_2 = 7;

        //Z02 columns
        final int PLAN_TAX_ID_NMBR = 2;
        final int SPONS_CO_NAME = 5;
        final int SPONS_CO_ZIP_CDE = 11;
        final int SPONS_CO_CTIY = 9;
        final int SPONS_CO_ADD_1 = 6;
        final int SPONS_CO_ADD_2 = 7;
        final int SPONS_CO_ADD_3 = 8;

        int numHash = 100; //10 hash algos. 10 gives optimal results
        List<List<String>> doc1 = Shingles.loadRecords("/Users/dt205202/Documents/DupTest/f5500-data.csv");
        List<List<String>> doc2 = Shingles.loadRecords("/Users/dt205202/Documents/DupTest/z02.csv");
        System.out.println(doc1.size());
        System.out.println(doc2.size());
        Set<String> sponsEINCol = selectColumn(doc1, SPONS_EIN_COL);
        Set<String> f5500Names = selectColumn(doc1, SPONS_NAME_COL);
        Set<String> f5500ZipCode = selectColumn(doc1, SPONS_ZIP_COL);
        Set<String> addressF5500 = selectMultipleColumns(doc1, SPONS_ADD_1, SPONS_ADD_2);
        Set<String> sponsCity = selectColumn(doc1, SPONS_CTIY);
        Set<String> einAndName = selectMultipleColumns(doc1, SPONS_EIN_COL, SPONS_NAME_COL);
        Set<String> einAndCity = selectMultipleColumns(doc1, SPONS_EIN_COL, SPONS_CTIY);
        Set<String> einNameZip = selectMultipleColumns2(doc1, SPONS_EIN_COL, SPONS_NAME_COL, SPONS_ZIP_COL);

        Set<String> taxIdNumber = selectColumn(doc2, PLAN_TAX_ID_NMBR);
        Set<String> sponsCoName = selectColumn(doc2, SPONS_CO_NAME);
        Set<String> z02ZipCode = selectColumn(doc2, SPONS_CO_ZIP_CDE);
        Set<String> z02Address = selectMultipleAddressColumns(doc2, SPONS_CO_ADD_1, SPONS_CO_ADD_2, SPONS_CO_ADD_3);
        Set<String> sponsCoCity = selectColumn(doc2, SPONS_CO_CTIY);
        Set<String> taxIdAndName = selectMultipleColumns(doc2, PLAN_TAX_ID_NMBR, SPONS_CO_NAME);
        Set<String> taxAndCity = selectMultipleColumns(doc2, PLAN_TAX_ID_NMBR, SPONS_CO_CTIY);
        Set<String> taxNameZip = selectMultipleColumns2(doc2, PLAN_TAX_ID_NMBR, SPONS_CO_NAME, SPONS_CO_ZIP_CDE);

        MinHash<String> minHash = new MinHash<String>(numHash);
        int[][] minHashValues = minHash.similarity(sponsEINCol, taxIdNumber);
        printSimilarityResults(minHashValues, numHash, "EIN_MATCH");

        minHashValues = minHash.similarity(f5500Names, sponsCoName);
        printSimilarityResults(minHashValues, numHash, "NAME_MATCH");

        minHashValues = minHash.similarity(f5500ZipCode, z02ZipCode);
        printSimilarityResults(minHashValues, numHash, "ZIP_MATCH");

        minHashValues = minHash.similarity(addressF5500, z02Address);
        printSimilarityResults(minHashValues, numHash, "ADDRESS_MATCH");

        minHashValues = minHash.similarity(sponsCity, sponsCoCity);
        printSimilarityResults(minHashValues, numHash, "CITY_MATCH");

        minHashValues = minHash.similarity(einAndName, taxIdAndName);
        printSimilarityResults(minHashValues, numHash, "EIN_NAME_MATCH");

        minHashValues = minHash.similarity(einAndCity, taxAndCity);
        printSimilarityResults(minHashValues, numHash, "EIN_CITY_MATCH");

        minHashValues = minHash.similarity(einNameZip, taxNameZip);
        printSimilarityResults(minHashValues, numHash, "EIN_NAME_ZIP_MATCH");
    }

    private static Set<String> selectColumn(List<List<String>> records, int colIndex) {
        Set<String> col = new HashSet<String>();
        for (final List<String> rec : records) {
            col.add(rec.get(colIndex));
        }
        return col;
    }

    private static Set<String> selectMultipleColumns(List<List<String>> records, int colIndex1, int colIndex2) {
        Set<String> col = new HashSet<String>();
        for (final List<String> rec : records) {
            col.add(rec.get(colIndex1)+rec.get(colIndex2));
        }
        return col;
    }

    private static Set<String> selectMultipleColumns2(List<List<String>> records, int colIndex1, int colIndex2, int
            colIndex3) {
        Set<String> col = new HashSet<String>();
        for (final List<String> rec : records) {
            col.add(rec.get(colIndex1)+rec.get(colIndex2)+rec.get(colIndex3));
        }
        return col;
    }

    private static Set<String> selectMultipleAddressColumns(List<List<String>> records, int colIndex1, int colIndex2,
                                                            int colIndex3) {
        Set<String> col = new HashSet<String>();
        for (final List<String> rec : records) {
            col.add(checkString(rec.get(colIndex1))+checkString(rec.get(colIndex2))+ checkString(rec.get(colIndex3)));
        }
        return col;
    }

    private static String checkString(String address) {
        if (address.startsWith("ATTN") || address.startsWith("PERSONAL & CONFIDENTIAL"))
            return "";
        return address;
    }

    private static void printSimilarityResults(int[][] minHashValues, int numHash, String matchType) {
        System.out.println(matchType+"----------------------------------------------");
        System.out.println("Minhash similarity of the two documents is "+MinHash.computeSimilarityFromSignatures
                (minHashValues, numHash));
        System.out.println("Jaccard Similarity of the documents "+JaccardSimilarity.computeJaccardSimilarity2
                (minHashValues));
        System.out.println();
    }
}

import com.opencsv.CSVReader;

import java.io.*;
import java.util.*;

/**
 * Created by Apratim Mishra on 5/21/15.
 * Create Shingles from the document
 */
public class Shingles {

    public static final int N_GRAM = 9;

    public static Set<String> createShingles(String filename) {
        Set<String> docShingles = new HashSet<String>();
        Set<String> stopWords = buildStopWords("stopwords.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line;
            while ((line = br.readLine()) != null) {
                docShingles.addAll(characterShingles(line, stopWords));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docShingles;
    }

    private static Set<String> buildStopWords(String filename) {
        Set<String> stopWords = new HashSet<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line;
            while ((line = br.readLine()) != null) {
                String noWhiteSpace = line.replaceAll("\\s+","");
                stopWords.add(noWhiteSpace);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }

    public static Set<String> createShinglesForCSV(String filename) {
        Set<String> docShingles = new HashSet<String>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            String[] headers = reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                docShingles.addAll(wordShinglesForCsv(line));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docShingles;
    }

    static List<List<String>> loadRecords(String path) throws IOException {
        List<List<String>> records = new ArrayList<List<String>>();

        CSVReader reader = new CSVReader(new FileReader(path));

        //skip the header
        String[] header = reader.readNext();
        assert(header!=null);

        String[] line;
        while ((line = reader.readNext()) != null) {
            assert(line!=null);
            records.add(Arrays.asList(line));
        }

        reader.close();

        return records;
    }

    public static Set<String> characterShingles(String line, Set<String> stopWords) {

        HashSet<String> shingles = new HashSet<String>();
        StringBuilder result = new StringBuilder(line.length());
        for (String s : line.split("\\b")) {
            if (!stopWords.contains(s)) result.append(s);
        }
        String noWhiteSpace = result.toString().replaceAll("\\s+","");
        for (int i = 0; i < noWhiteSpace.length() - N_GRAM + 1; i++) {
            // extract an ngram

            String shingle = noWhiteSpace.substring(i, i + N_GRAM);
            shingles.add(shingle);
        }
        return shingles;
    }

    public static Map<String, Integer> wordShingles(String line, Set<String> stopWords) {

        Map<String, Integer> shinglesMap = new HashMap<String, Integer>();
        String[] words = line.split("\\b");
        for (int i=0; i < words.length; i++ ) {
            String shingle = words[i-1] + words[i];
            shinglesMap.put(shingle,!shinglesMap.containsKey(shingle) ?  1 : shinglesMap.get(shingle) +1);
        }
        return shinglesMap;
    }

    public static Set<String> wordShinglesForCsv(String[] line) {

        HashSet<String> shingles = new HashSet<String>();

        for (int i = 1; i < line.length; i++) {
            // extract an bi-gram
            String shingle = line[i-1] + line[i];
            shingles.add(shingle);
        }
        return shingles;
    }

    /**
     * Use ax + b mod m
     * Choose m as 40127
     * @param shingles
     * @return
     */
    public static Map<String, Integer> hashShingles(Set<String> shingles) {
        MyRandom random = new MyRandom();
        Map<String, Integer> shinglesMap = new HashMap<String, Integer>();
        int index=0;
        for(String s : shingles) {
            shinglesMap.put(s,createHash(index, random.nextNonNegative(), random.nextNonNegative()));
            index++;
        }
        return shinglesMap;
    }

    private static int createHash(int x, int a, int b) {
        return (a*x + b) % 40127;
    }

}

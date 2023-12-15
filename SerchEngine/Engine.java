package a1_2101040056;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Engine {
    List<Doc> listDocs = new ArrayList<>();

    public Doc[] documents = new Doc[10];
    public int loadDocs(String dirname) {
        File directory = new File(dirname);

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("The specified directory does not exist.");
            return 0;
        }
        int fileLoaded = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));

                        StringBuilder contentBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            contentBuilder.append(line).append("\n");
                        }
                        String content = contentBuilder.toString();
                        Doc doc = new Doc(content); // Create a new Doc object
                        listDocs.add(doc);
                        reader.close();
                        fileLoaded++;
                    } catch (IOException e) {
                        System.err.println("Error loading file!");
                        e.printStackTrace();
                    }
                }
            }
        }
        listDocs.toArray(documents);
        return fileLoaded;
    }
    public Doc[] getDocs() {
        return documents;
    }
    public List<Result> search(Query q) {
        List<Result> results = new ArrayList<>();

        // Loop through all the loaded documents
        for (Doc doc : documents) {
            List<Match> matches = q.matchAgainst(doc);
            Result result = new Result(doc, matches);
            // If there are matches(not empty), create a Result and add it to the results list
            if (!matches.isEmpty()) {
                results.add(result);
            }
        }
        // Sort the results based on the specified criteria
        results.sort((result1, result2) -> {
            // 1. Compare by match count (descending order)
            int matchCountComparison = Integer.compare(result2.getMatches().size(), result1.getMatches().size());
            if (matchCountComparison != 0) {
                return matchCountComparison;
            }

            // 2. Compare by total frequency (descending order)
            int totalFrequencyComparison = Integer.compare(result2.getTotalFrequency(), result1.getTotalFrequency());
            if (totalFrequencyComparison != 0) {
                return totalFrequencyComparison;
            }

            // 3. Compare by average first index (ascending order)
            double avgFirstIndexThis = result1.getAverageFirstIndex();
            double avgFirstIndexOther = result2.getAverageFirstIndex();

            return Double.compare(avgFirstIndexThis, avgFirstIndexOther);
        });
        return results;
    }
    public String htmlResult(List<Result> results) {
        StringBuilder htmlBuilder = new StringBuilder();

        for (Result result : results) {
            String resultHtml = result.htmlHighlight(); // Assuming Result has an htmlHighlight method

            // Append the result HTML to the StringBuilder
            htmlBuilder.append(resultHtml);
        }
        return htmlBuilder.toString();
    }
    public static void main(String[] args) {
        Engine engine = new Engine();
        int loadDocs = engine.loadDocs("docs");
        System.out.println(loadDocs);
        Doc[] docs = engine.getDocs();
        for (Doc doc : docs) {
            System.out.println(doc.getTitle());
            System.out.println(doc.getBody());
            System.out.println();
        }
        // Create a Query object with a search phrase
//        // Perform the search
//        System.out.println(engine.search(query));
        // Display the search results

        }
        }

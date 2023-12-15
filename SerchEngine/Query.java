package a1_2101040056;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Query {
    private List<Word> keywords;
    public Query(String searchPhrase) {
        keywords = extractKeywords(searchPhrase);
    }
    private List<Word> extractKeywords(String searchPhrase) {
        List<Word> keywordList = new ArrayList<>();

        // Split the searchPhrase into words
        String[] words = searchPhrase.split("\\s+");

        for (String word : words) {
            // Create Word objects for each word in the searchPhrase
            Word keywordWord = Word.createWord(word);

            // Check if the keyword is a valid keyword
            if (keywordWord.isKeyword()) {
                keywordList.add(keywordWord);
            }
        }

        return keywordList;
    }
    public List<Word> getKeywords() {
        return keywords;
    }
    public List<Match> matchAgainst(Doc d) {
        List<Match> matches = new ArrayList<>();

        // Combine the title and body of the document into a single list of words
        List<Word> documentWords = new ArrayList<>(d.getTitle());
        documentWords.addAll(d.getBody());
        for (Word keyword : keywords) {
            for (int i = 0; i < documentWords.size(); i++) {
                Word documentWord = documentWords.get(i);
                if (documentWord.equals(keyword)) {
                    // Calculate the frequency and first index
                    int frequency = calculateFrequency(documentWords, keyword);

                    // Create a Match object and add it to the list of matches
                    Match match = new Match(d, keyword, frequency, i);
                    matches.add(match);
                    break;
                }
            }
        }
        // Sort matches by the first index
        Collections.sort(matches);
        return matches;
    }
    private int calculateFrequency(List<Word> words, Word keyword) {
        int frequency = 0;
        for (Word word : words) {
            if (word.equals(keyword)) {
                frequency++;
            }
        }
        return frequency;
    }
}

package a1_2101040056;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Word {
    public static Set<String> stopWords;
    private String prefix = "";
    private String suffix = "";
    private String text = "";
    public Word() {
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getText() {
        return text;
    }

    public static boolean isValid(String text) {
    int lengthText = text.length();
    char apoS = '\'';
        char hyP = '-';
    if (lengthText == 0) {
        return false;
    }
    for (int i = 0; i < lengthText; i++) {
        char chaR = text.charAt(i);
        if (Character.isDigit(chaR)) {
            return false;
        }
        if (chaR != hyP && chaR != apoS && !Character.isLetter(chaR)) {
            return false;
        }
    }
    return true;
    }
    public static Word createWord(String rawText) {
        Word word = new Word();

        if (rawText == null || rawText.isEmpty()) {
            return word;
        }
            // Extract the text
            int endIndex = rawText.length();
            int start = 0;
            while (start < endIndex && !Character.isLetterOrDigit(rawText.charAt(start))) {
                start++;
            }
            if (start < endIndex) {
                word.prefix = rawText.substring(0, start);
                word.text = rawText.substring(start, endIndex);
            }
            // Check for 's at the end
            int i = endIndex - 1;
            while (i >= 0 && !Character.isLetterOrDigit(rawText.charAt(i))) {
                word.suffix = rawText.charAt(i) + word.suffix;
                i--;
            }
            if (endIndex != word.prefix.length() && endIndex != word.suffix.length()) {
                word.text = rawText.substring(start, i + 1);
            }
            int n = word.text.length() - 1;
            if (n >= 1) {
                if (word.text.charAt(n) == 's' && word.text.charAt(n - 1) == '\'') {
                    word.suffix = "'s" + word.suffix;
                    word.text = word.text.substring(0, n - 1);
                }
            }
        if (!isValid(word.text) ) {
            word.prefix = "";
            word.suffix = "";
            word.text = rawText;
        }
        return word;
    }

    public static boolean loadStopWords(String fileName) {
        stopWords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.add(line.trim()); // Print each line to the console
            }
            return true;
        } catch (FileNotFoundException fnf) {
            return false;
        } catch (IOException e) {
            System.err.println("File can not be read!");
            return false;
        }
    }

    public boolean equals(Object o) {
        // Check if the object is a Word
        if (o instanceof Word) {
            Word otherWord = (Word) o;
            // Compare the text parts case-insensitively
            return this.text.equalsIgnoreCase(otherWord.text);
        }
        return false;
    }

    public boolean isKeyword() {
        // Check if the text is in the stopWords set
        if (stopWords.contains(text.toLowerCase())) {
            return false; // It's a stop word, so not a keyword
        }
        // Check if the text part is not empty
        if (!text.isEmpty()) {
            // Define a regular expression pattern to match valid keyword text
            Pattern pattern = Pattern.compile("^[a-zA-Z'-]+$");
            Matcher matcher = pattern.matcher(text);

            // Check if the text matches the pattern and ends with 's
            return matcher.matches() || text.endsWith("'s");
        }
        return false; // Empty text or not following keyword rules
    }
    @Override
    public String toString() {
        return prefix + text + suffix;
    }

    public static void main(String[] args) {
        Word.loadStopWords("stopwords.txt");
            Word wordOjb = Word.createWord("'s's");
        System.out.println(wordOjb.isKeyword());
            System.out.println("Prefix: " + wordOjb.getPrefix());
                System.out.println("Text: " + wordOjb.getText());
            System.out.println("Suffix: " + wordOjb.getSuffix());
        }
//        System.out.println("isKeyWord: " + word4.isKeyword());
//            if (Word.createWord(" and").isKeyword())
//                System.out.println("Word.createWord(): ' and' should be treated as an invalid word (not a keyword because it contains a space)");
//        }
    }




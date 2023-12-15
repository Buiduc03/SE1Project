package a1_2101040056;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Result implements Comparable<Result> {
    private Doc doc;
    private List<Match> matches;

    public Result(Doc d, List<Match> matches) {
        this.doc = d;
        this.matches = matches;

    }
    public List<Match> getMatches() {
        return matches;
    }

    public Doc getDoc() {
        return doc;
    }
    public int getTotalFrequency() {
        int totalFrequency = 0;
        for (Match match : matches) {
            totalFrequency += match.getFreq();
        }
        return totalFrequency;
    }
    public double getAverageFirstIndex() {
        if (matches.isEmpty()) {
            return 0.0;
        }

        int totalFirstIndex = 0;
        for (Match match : matches) {
            totalFirstIndex += match.getFirstIndex();
        }

        return (double) totalFirstIndex / matches.size();
    }
    public String htmlHighlight() {
        StringBuilder t = new StringBuilder();
        t.append("<h3>");
        for (Word w : getDoc().getTitle()) {
            if (isSearch(w)) {
                t.append(w.getPrefix() + "<u>" + w.getText() + "</u> " + w.getSuffix());
            } else {
                t.append(w.getPrefix()+ w.getText()+ w.getSuffix()).append(" ");
            }
        }

        t.deleteCharAt(t.length() - 1);
        t.append("</h3>");


        StringBuilder b = new StringBuilder();
        b.append("<p>");
        for (Word w : getDoc().getBody()) {
            if (isSearch(w)) {
                b.append(String.format("%s<b>%s</b>%s ", w.getPrefix(), w.getText(), w.getSuffix()));
            } else {
                b.append(w.getPrefix()+ w.getText()+ w.getSuffix()).append(" ");
            }
        }
        b.deleteCharAt(b.length() - 1);
        b.append("</p>");
        return t.toString()
                + b.toString();
    }

    public boolean isSearch(Word word) {
        for (Match m : matches) {
            Word searchWord = m.getWord();
            if (word.equals(searchWord)) return true;
        }
        return false;
    }

    @Override
    public int compareTo(Result o) {
        // Compare based on the specified criteria

        // 1. Compare by match count (descending order)
        int matchCountComparison = Integer.compare(o.matches.size(), this.matches.size());
        if (matchCountComparison != 0) {
            return matchCountComparison;
        }

        // 2. Compare by total frequency (descending order)
        int totalFrequencyComparison = Integer.compare(o.getTotalFrequency(), this.getTotalFrequency());
        if (totalFrequencyComparison != 0) {
            return totalFrequencyComparison;
        }

        // 3. Compare by average first index (ascending order)
        double avgFirstIndexThis = this.getAverageFirstIndex();
        double avgFirstIndexOther = o.getAverageFirstIndex();

        return Double.compare(avgFirstIndexThis, avgFirstIndexOther);
    }
}
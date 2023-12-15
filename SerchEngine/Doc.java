package a1_2101040056;
import java.util.ArrayList;
import java.util.List;
public class Doc {
    private List<Word> title;
    private List<Word> body;

    public Doc(String content) {
        String[] lines = content.split("\n");
        title = new ArrayList<>();
        body = new ArrayList<>();


        if (lines.length >= 1) {
            title.addAll(extractWords(lines[0]));
        }
        if (lines.length >= 2) {
            body.addAll(extractWords(lines[1]));
        }
    }
    private List<Word> extractWords(String text) {
        String[] words = text.split("\\s+"); // Split by whitespace
        List<Word> wordList = new ArrayList<>();

        for (String word : words) {
            wordList.add(Word.createWord(word));
        }

        return wordList;
    }

    public List<Word> getTitle() {
        return title;
    }

    public List<Word> getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doc otherDoc = (Doc) o;
        return compareWordLists(this.title, otherDoc.title) && compareWordLists(this.body, otherDoc.body);
    }

    private boolean compareWordLists(List<Word> list1, List<Word> list2) {
        if (list1 == list2) return true;
        if (list1 == null || list2 == null) return false;
        if (list1.size() != list2.size()) return false;
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        Engine engine = new Engine();

        // Replace this path with the directory where your sample text files are located
        String directoryPath = "docs";

        int loadedDocs = engine.loadDocs(directoryPath);
        System.out.println("Loaded " + loadedDocs + " documents.");

        // Retrieve the loaded documents
        Doc[] docs = engine.getDocs();

        // Iterate through the documents and print their titles and bodies
        for (Doc doc : docs) {
            System.out.println("Title: " + doc.getTitle());
            System.out.println("Body: " + doc.getBody());
            System.out.println("-------------------------------");
        }
    }
}
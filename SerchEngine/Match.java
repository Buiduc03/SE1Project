        package a1_2101040056;

        public class Match implements Comparable<Match> {
            private int frequency;
            private int firstIndex;
            private Doc document;
            private Word word;
            public Match(Doc d, Word w, int freq, int firstIndex) {
                this.document = d;
                this.word = w;
                this.frequency = freq;
                this.firstIndex = firstIndex;
            }
            public Word getWord() {
                return word;
            }
            public int getFreq() {
                return frequency;
            }
            public int getFirstIndex() {
                return firstIndex;
            }
            public int compareTo(Match o) {
                // Compare matches based on the first index
                return Integer.compare(this.firstIndex, o.firstIndex);
            }

        }

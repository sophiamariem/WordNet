/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Outcast {
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = null;
        int max = 0;

        for (String noun : nouns) {
            int distance = 0;

            for (String n : nouns) {
                if (!noun.equals(n)) {
                    distance += this.wordnet.distance(noun, n);
                }
            }

            if (distance > max) {
                max = distance;
                outcast = noun;
            }
        }

        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
        Outcast out = new Outcast(new WordNet("files/synsets.txt", "files/hypernyms.txt"));
        String[] nouns = { "food_poisoning", "food_cache", "food_court" }; // food_poisoning

        System.out.println(out.outcast(nouns));
    }
}

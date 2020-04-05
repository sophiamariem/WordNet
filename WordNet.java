import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {
    private final Map<String, List<Integer>> nouns = new HashMap<>();
    private final List<String> synsets = new ArrayList<String>();
    private final Digraph G;
    private int size = 0;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        processSynsets(synsets);
        this.G = new Digraph(this.size);
        processHypernyms(hypernyms);
        validateGraph();
        this.sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        return synsets.get(sap.ancestor(nouns.get(nounA), nouns.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet w = new WordNet("files/synsets.txt", "files/hypernyms.txt");
        System.out.println(w.sap("treacle", "aperitif")); // substance
        System.out.println(w.distance("treacle", "aperitif")); // 12

        System.out.println(w.distance("methyl_ethyl_ketone", "consequence")); // 11

        w = new WordNet("files/synsets6.txt", "files/hypernyms6TwoAncestors.txt");
        System.out.println(w.distance("a", "b")); // 1

        w = new WordNet("files/synsets8.txt", "files/hypernyms8ModTree.txt");
        System.out.println(w.distance("a", "h")); // 4

        w = new WordNet("files/synsets1000-subgraph.txt", "files/hypernyms1000-subgraph.txt");
        System.out.println(w.distance("cortex", "immunoglobulin")); // 11
    }

    private void processSynsets(String synsetsFile) {
        In in = new In(synsetsFile);

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] info = line.split(",");
            this.synsets.add(info[1]);

            String[] nounsArray = info[1].split(" ");
            for (String noun : nounsArray) {
                List<Integer> bag = nouns.get(noun);
                if (bag == null) {
                    bag = new ArrayList<Integer>();
                    nouns.put(noun, bag);
                }
                bag.add(size);
            }
            size++;
        }
    }

    private void processHypernyms(String hypernymsFile) {
        In in = new In(hypernymsFile);

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] info = line.split(",");

            int hyponym = Integer.parseInt(info[0]);
            for (int i = 1; i < info.length; i++) {
                int hypernym = Integer.parseInt(info[i]);
                G.addEdge(hyponym, hypernym);
            }
        }
    }

    private void validateGraph() {
        DirectedCycle cycle = new DirectedCycle(this.G);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException();
        }

        int rooted = 0;
        for (int i = 0; i < G.V(); i++) {
            if (!this.G.adj(i).iterator().hasNext()) {
                if (rooted == 1) {
                    throw new IllegalArgumentException();
                }
                else {
                    rooted++;
                }
            }
        }
    }

    private void validateNoun(String noun) {
        if (!isNoun(noun)) {
            throw new IllegalArgumentException();
        }
    }

}
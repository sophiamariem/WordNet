/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateWithinBounds(v, w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return getPathLength(ancestor(v, w), bfsV, bfsW);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateWithinBounds(v, w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return getAnsestor(bfsV, bfsW);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v, w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return getPathLength(ancestor(v, w), bfsV, bfsW);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v, w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return getAnsestor(bfsV, bfsW);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        Digraph d = new Digraph(13);
        d.addEdge(7, 3);
        d.addEdge(8, 3);
        d.addEdge(3, 1);
        d.addEdge(4, 1);

        SAP sap = new SAP(d);

        System.out.println(sap.ancestor(7, 3)); // 3
        System.out.println(sap.ancestor(4, 1)); // 1
        System.out.println(sap.ancestor(4, 8)); // 1
        System.out.println(sap.ancestor(5, 8)); // -1
    }

    private void validateWithinBounds(int v, int w) {
        if (v < 0 || v > G.V() - 1 || w < 0 || w > G.V() - 1) {
            throw new IllegalArgumentException();
        }
    }

    private void validateVertices(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        for (Integer i : v) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer i : w) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private int getPathLength(int ancestor, BreadthFirstDirectedPaths bfsV,
                              BreadthFirstDirectedPaths bfsW) {
        int pathLength = -1;
        if (ancestor > pathLength) {
            return bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
        }
        return pathLength;
    }

    private int getAnsestor(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
        int shortestLength = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i = 0; i < this.G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int currentLength = bfsV.distTo(i) + bfsW.distTo(i);
                if (currentLength < shortestLength) {
                    shortestLength = currentLength;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

}

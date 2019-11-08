package DomainLayer.Algoritmos.LZ78;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private TrieNode root;
    private int indexCount;

    public Trie() {
        root = new TrieNode();
        indexCount = 0;
    }

    public int insert(List<Byte> bytes) {
        int lastIndex = root.index;
        HashMap<Byte, TrieNode> edges = root.edges;

        for(int i=0; i < bytes.size(); i++) {
            byte b = bytes.get(i);

            TrieNode t = root;
            if(edges.containsKey(b)){
                t = edges.get(b);
                lastIndex = t.index;
            } else {
                t = new TrieNode(b, ++indexCount);
                edges.put(b, t);
            }

            edges = t.edges;

            //set leaf node
            if(i==bytes.size()-1)
                t.isLeaf = true;
        }

        return lastIndex;
    }

    // Returns if the word is in the trie.
    public boolean search(List<Byte> bytes) {
        TrieNode t = searchNode(bytes);
        return t != null && t.isLeaf;
    }

    public TrieNode searchNode(List<Byte> bytes){
        Map<Byte, TrieNode> edges = root.edges;
        TrieNode t = null;
        for (byte c : bytes) {
            if (edges.containsKey(c)) {
                t = edges.get(c);
                edges = t.edges;
            } else {
                return null;
            }
        }
        return t;
    }

    public boolean isFull() {
        return 0x3FFFFF == indexCount;
    }

    static class TrieNode {
        byte c;
        int index;
        HashMap<Byte, TrieNode> edges = new HashMap<>();
        boolean isLeaf;

        TrieNode() {}

        TrieNode(byte c, int index){
            this.c = c;
            this.index = index;
        }
    }
}

package DomainLayer.Algoritmos.LZ78.CompileTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompileTree {
    private CompileTreeNode root;
    private int indexCount;

    public CompileTree() {
        root = new CompileTreeNode();
        indexCount = 0;
    }

    // Inserts a word into the trie.
    public void insert(byte[] bytes) {
        HashMap<Byte, CompileTreeNode> edges = root.edges;

        for(int i=0; i < bytes.length; i++) {
            byte b = bytes[i];

            CompileTreeNode t;
            if(edges.containsKey(b)){
                t = edges.get(b);
            } else {
                t = new CompileTreeNode(b, indexCount++);
                edges.put(b, t);
            }

            edges = t.edges;

            //set leaf node
            if(i==bytes.length-1)
                t.isLeaf = true;
        }
    }

    // Returns if the word is in the trie.
    public boolean search(byte[] bytes) {
        CompileTreeNode t = searchNode(bytes);

        if(t != null && t.isLeaf)
            return true;
        else
            return false;
    }

    public CompileTreeNode searchNode(byte[] bytes){
        Map<Byte, CompileTreeNode> edges = root.edges;
        CompileTreeNode t = null;
        for(int i=0; i<bytes.length; i++){
            byte c = bytes[i];
            if(edges.containsKey(c)){
                t = edges.get(c);
                edges = t.edges;
            }else{
                return null;
            }
        }
        return t;
    }

    // Inserts a word into the trie.
    public int insert(List<Byte> bytes) {
        int lastIndex = root.index;
        HashMap<Byte, CompileTreeNode> edges = root.edges;

        for(int i=0; i < bytes.size(); i++) {
            byte b = bytes.get(i);

            CompileTreeNode t = root;
            if(edges.containsKey(b)){
                t = edges.get(b);
                lastIndex = t.index;
            } else {
                t = new CompileTreeNode(b, ++indexCount);
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
        CompileTreeNode t = searchNode(bytes);

        if(t != null && t.isLeaf)
            return true;
        else
            return false;
    }

    public CompileTreeNode searchNode(List<Byte> bytes){
        Map<Byte, CompileTreeNode> edges = root.edges;
        CompileTreeNode t = null;
        for(int i=0; i<bytes.size(); i++){
            byte c = bytes.get(i);
            if(edges.containsKey(c)){
                t = edges.get(c);
                edges = t.edges;
            }else{
                return null;
            }
        }
        return t;
    }

    public boolean full() {
        return 65536 == indexCount;
    }
}

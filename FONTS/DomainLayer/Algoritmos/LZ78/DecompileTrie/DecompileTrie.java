package DomainLayer.Algoritmos.LZ78.DecompileTrie;

import java.util.ArrayList;
import java.util.List;

public class DecompileTrie {
    private DecompileTrieNode root;

    public DecompileTrie() {
        root = new DecompileTrieNode();
    }

    public List<Byte> insertAfterIndex(byte b, int index) {
        return insertAfterIndexRec(root, b, index);
    }

    private List<Byte> insertAfterIndexRec(DecompileTrieNode t, byte b, int index) {
        if(t.index == index) {
            DecompileTrieNode leaf = new DecompileTrieNode(b, index);
            leaf.isLeaf = true;
            t.isLeaf = false;
            t.edges.add(leaf);
            List<Byte> list = new ArrayList<>();
            list.add(b);
            return list;
        }
        else if(!t.isLeaf) {
            List<DecompileTrieNode> edges = t.edges;
            for(DecompileTrieNode node : edges) {
                List<Byte> word = insertAfterIndexRec(node, b, index);
                if(word != null) return word;
            }
        }
        return null;
    }
}

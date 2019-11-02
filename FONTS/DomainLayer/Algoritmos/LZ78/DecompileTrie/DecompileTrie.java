package DomainLayer.Algoritmos.LZ78.DecompileTrie;

import java.util.ArrayList;
import java.util.List;

public class DecompileTrie {
    private DecompileTrieNode root;
    private int indexCount;

    public DecompileTrie() {
        root = new DecompileTrieNode();
        indexCount = 0;
    }

    public List<Byte> insertAfterIndex(byte b, int index) {
        return insertAfterIndexRec(root, b, index);
    }

    private List<Byte> insertAfterIndexRec(DecompileTrieNode t, byte b, int index) {
        if(t.index == index) {
            DecompileTrieNode leaf = new DecompileTrieNode(b, ++indexCount);
            t.isLeaf = false;
            t.edges.add(leaf);

            List<Byte> list = new ArrayList<>();
            list.add(b);
            if(t.index != 0) list.add(t.c);
            return list;
        }
        else if(!t.isLeaf) {
            List<DecompileTrieNode> edges = t.edges;
            for(DecompileTrieNode node : edges) {
                List<Byte> word = insertAfterIndexRec(node, b, index);
                if(word != null) {
                    if(t.index != 0) word.add(t.c);
                    return word;
                }
            }
        }
        return null;
    }
}

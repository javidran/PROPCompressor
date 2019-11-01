package DomainLayer.Algoritmos.LZ78.DecompileTrie;

import java.util.ArrayList;
import java.util.List;

class DecompileTrieNode {
    byte c;
    int index;
    List<DecompileTrieNode> edges = new ArrayList<>();
    boolean isLeaf;

    public DecompileTrieNode() {}

    public DecompileTrieNode(byte c, int index){
        this.c = c;
        this.index = index;
    }
}
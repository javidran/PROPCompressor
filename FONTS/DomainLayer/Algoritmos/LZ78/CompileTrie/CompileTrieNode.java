package DomainLayer.Algoritmos.LZ78.CompileTrie;

import java.util.HashMap;

class CompileTrieNode {
    byte c;
    int index;
    HashMap<Byte, CompileTrieNode> edges = new HashMap<>();
    boolean isLeaf;

    public CompileTrieNode() {}

    public CompileTrieNode(byte c, int index){
        this.c = c;
        this.index = index;
    }
}
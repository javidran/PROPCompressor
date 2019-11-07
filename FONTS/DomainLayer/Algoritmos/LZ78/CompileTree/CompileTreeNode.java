package DomainLayer.Algoritmos.LZ78.CompileTree;

import java.util.HashMap;

class CompileTreeNode {
    byte c;
    int index;
    HashMap<Byte, CompileTreeNode> edges = new HashMap<>();
    boolean isLeaf;

    public CompileTreeNode() {}

    public CompileTreeNode(byte c, int index){
        this.c = c;
        this.index = index;
    }
}
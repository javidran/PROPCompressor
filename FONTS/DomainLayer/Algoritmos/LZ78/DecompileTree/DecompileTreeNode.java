package DomainLayer.Algoritmos.LZ78.DecompileTree;

import java.util.ArrayList;
import java.util.List;

class DecompileTreeNode {
    byte c;
    int index;
    List<DecompileTreeNode> edges = new ArrayList<>();
    boolean isLeaf;

    public DecompileTreeNode() {
        isLeaf = true;
    }

    public DecompileTreeNode(byte c, int index){
        this.c = c;
        this.index = index;
        isLeaf = true;
    }
}
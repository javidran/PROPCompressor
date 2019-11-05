package DomainLayer.Algoritmos.LZ78.DecompileTree;

import java.util.ArrayList;
import java.util.List;

public class DecompileTree {
    private DecompileTreeNode root;
    private int indexCount;

    public DecompileTree() {
        root = new DecompileTreeNode();
        indexCount = 0;
    }

    public List<Byte> insertAfterIndex(byte b, int index) {
        return insertAfterIndexRec(root, b, index);
    }

    private List<Byte> insertAfterIndexRec(DecompileTreeNode t, byte b, int index) {
        if(t.index == index) {
            DecompileTreeNode leaf = new DecompileTreeNode(b, ++indexCount);
            t.isLeaf = false;
            t.edges.add(leaf);

            List<Byte> list = new ArrayList<>();
            list.add(b);
            if(t.index != 0) list.add(t.c);
            return list;
        }
        else if(!t.isLeaf) {
            List<DecompileTreeNode> edges = t.edges;
            for(DecompileTreeNode node : edges) {
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

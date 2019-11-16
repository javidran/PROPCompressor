package DomainLayer.Algoritmos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Estructura de Datos Trie que consiste en un árbol con pesos en las ramas de cada nodo. Esta estructura es utilizada por LZ78.
 */
class Trie {
    /**
     * Nodo raíz desde el que empiezan todas las ramas.
     */
    private TrieNode root;
    /**
     * Contador del tamaño del diccionario. Su rango es entre 0 y 4194303.
     */
    private int indexCount;

    /**
     * Constructora.
     */
    Trie() {
        root = new TrieNode();
        indexCount = 0;
    }

    /**
     * Añade al árbol la secuencia de bytes aportada por parámetro.
     * @param bytes Conjunto de bytes a añadir al árbol.
     * @return Devuelve el índice que precede al nuevo nodo añadido.
     */
    int insert(List<Byte> bytes) {
        int lastIndex = root.index;
        HashMap<Byte, TrieNode> edges = root.edges;

        for(int i=0; i < bytes.size(); i++) {
            byte b = bytes.get(i);

            TrieNode t;
            if(edges.containsKey(b)){
                t = edges.get(b);
                lastIndex = t.index;
            } else {
                t = new TrieNode(b, ++indexCount);
                edges.put(b, t);
            }

            edges = t.edges;

            if(i==bytes.size()-1)
                t.isLeaf = true;
        }

        return lastIndex;
    }


    /**
     * Busca si la secuencia de bytes se encuentra en el árbol o no.
     * @param bytes Secuencia de bytes a localizar en el árbol.
     * @return Devuelve un booleano indicando si la secuencia de bytes se encuentra dentro del árbol o no.
     */
    boolean search(List<Byte> bytes) {
        TrieNode t = searchNode(bytes);
        return t != null && t.isLeaf;
    }

    /**
     * Retorna el último nodo de la secuencia de bytes solicitada por parámetro. Si la secuencia no existe el valor retornado es null.
     * @param bytes Secuencia de bytes a localizar en el árbol.
     * @return Devuelve el último nodo de la secuencia de bytes solicitada. Si no existe el valor es null.
     */
    private TrieNode searchNode(List<Byte> bytes){
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

    /**
     * Indica si el árbol ha alcanzado su límite de tamaño.
     * @return Devuelve un booleano indicando si el árbol se ha llenado.
     */
    boolean isFull() {
        return 0x3FFFFF == indexCount;
    }

    /**
     * Clase que representa cada nodo dentro del árbol Trie.
     */
    static class TrieNode {
        /**
         * Byte que forma parte de un conjunto de secuencias de bytes. Siempre es el último byte de una y solo una secuencia.
         */
        byte c;
        /**
         * Indica el número de la secuencia que acaba en este nodo.
         */
        int index;
        /**
         * Ramas que surgen del nodo con pesos que indican el byte del nodo correspondiente.
         */
        HashMap<Byte, TrieNode> edges = new HashMap<>();
        /**
         * Indica si el nodo no tiene hijos.
         */
        boolean isLeaf;

        /**
         * Constructora sin parámetros.
         */
        TrieNode() {}

        /**
         * Constructora con los parámetros básicos del nodo.
         * @param c Byte perteneciente a una secuencia de bytes seguida.
         * @param index Indica el índice del byte que precede al actual.
         */
        TrieNode(byte c, int index){
            this.c = c;
            this.index = index;
        }
    }
}

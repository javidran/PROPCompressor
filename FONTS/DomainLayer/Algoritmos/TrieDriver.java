package DomainLayer.Algoritmos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrieDriver {
    public static void main(String[] args) {
        System.out.print("Bienvenido al driver para Trie.\n");
        Scanner scanner = new Scanner(System.in);
        Trie trie = new Trie();
        boolean b = true;
        while (b) {
            try {
                System.out.print("\nIntroduzca uno de los siguientes comandos disponibles:\n\n1. vaciaArbol\n2. buscaSecuencia\n3. añadeSecuencia\n4. estaLleno? \n5. salir\n");
                String comando = scanner.nextLine();
                String s;
                switch (comando) {
                    case "vaciaArbol":
                    case "1":
                        trie = new Trie();
                        System.out.println("El árbol se ha vaciado correctamente.");
                        break;
                    case "buscaSecuencia":
                    case "2":
                        System.out.println("Introduzca la secuencia a buscar:");
                        byte[] arraySearch = scanner.nextLine().getBytes();
                        List<Byte> byteListSearch = new ArrayList<>();
                        for(byte a : arraySearch) {
                            byteListSearch.add(a);
                        }
                        System.out.println("La secuencia " + (trie.search(byteListSearch)?"":"no ") + "se encuentra dentro del árbol.");
                        break;
                    case "añadeSecuencia":
                    case "3":
                        if(trie.isFull()) {
                            System.out.println("El árbol está lleno, no se podrá añadir ninguna secuencia más hasta vaciarlo.");
                            break;
                        }
                        System.out.println("Introduzca la secuencia a insertar, toda la secuencia excepto el último byte debe existir previamente en el árbol:");
                        byte[] arrayInsert = scanner.nextLine().getBytes();
                        List<Byte> byteListInsert = new ArrayList<>();
                        boolean introducible = false;
                        for(int i=0; i<arrayInsert.length; ++i) {
                            byteListInsert.add(arrayInsert[i]);
                            if(i==0) introducible = true;
                            if(i == arrayInsert.length-2) introducible = trie.search(byteListInsert);
                            if(i == arrayInsert.length-1) introducible = introducible && !trie.search(byteListInsert);
                        }

                        if(introducible) {
                            int indice = trie.insert(byteListInsert);
                            System.out.println("La secuencia se ha insertado en el árbol. El índice del penúltimo byte es "+ indice);
                        }
                        else System.out.println("La secuencia no es introducible en el árbol, ya que hay más de un byte que no existe en el árbol.");
                        break;
                    case "estaLleno?":
                    case "4":
                        if(trie.isFull()) System.out.println("El árbol está lleno, no se podrá añadir ninguna secuencia más hasta vaciarlo.");
                        else System.out.println("El árbol no está lleno, puedes agregar más secuencias de bytes sin problema.");
                        break;
                    case "salir":
                    case "5":
                        b = false;
                        break;
                    default:
                        System.out.print("Comando incorrecto!\n");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

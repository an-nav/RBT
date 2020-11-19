import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        BST<Character, Integer> bst = new BST<>();
        String s = "SEARCHXMPL";
        for (int i = 0; i < s.length(); i++){
            bst.put(s.charAt(i), i);
        }
        // bst.delete('E');
        bst.layerTraversal();
        bst.inorderTraversal();

    }
}


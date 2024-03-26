import java.util.ArrayList;
import java.util.HashMap;


/**Practice 4 TrieNode*/
class TrieNode {
    HashMap<Character, TrieNode> children;
    ArrayList<String> terms;

    TrieNode() {
        children = new HashMap<>();
        terms = new ArrayList<>();
    }
}

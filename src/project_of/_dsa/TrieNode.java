
package project_of._dsa;

public class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;
    String vehiclePrefix;

    public TrieNode() {
        children = new TrieNode[36];
        isEndOfWord = false;
        vehiclePrefix = null;
    }
}

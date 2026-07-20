
package project_of._dsa;

import java.util.ArrayList;
import java.util.List;

public class VehicleTrie {
    private TrieNode root;

    public VehicleTrie() {
        root = new TrieNode();
    }

    public void insert(String vehicleNo) {
        TrieNode current = root;
        for (char ch : vehicleNo.toUpperCase().toCharArray()) {
            int index;
            if (ch >= 'A' && ch <= 'Z') {
                index = ch - 'A';
            } else if (ch >= '0' && ch <= '9') {
                index = 26 + (ch - '0');
            } else {
                index = 35;
            }
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
        current.vehiclePrefix = vehicleNo;
    }

    public List<String> searchPrefix(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode current = root;
        for (char ch : prefix.toUpperCase().toCharArray()) {
            int index;
            if (ch >= 'A' && ch <= 'Z') {
                index = ch - 'A';
            } else if (ch >= '0' && ch <= '9') {
                index = 26 + (ch - '0');
            } else {
                return results;
            }
            if (current.children[index] == null) {
                return results;
            }
            current = current.children[index];
        }
        collectAllWords(current, results);
        return results;
    }

    private void collectAllWords(TrieNode node, List<String> results) {
        if (node == null) {
            return;
        }
        if (node.isEndOfWord) {
            results.add(node.vehiclePrefix);
        }
        for (TrieNode child : node.children) {
            if (child != null) {
                collectAllWords(child, results);
            }
        }
    }

    public boolean search(String vehicleNo) {
        TrieNode current = root;
        for (char ch : vehicleNo.toUpperCase().toCharArray()) {
            int index;
            if (ch >= 'A' && ch <= 'Z') {
                index = ch - 'A';
            } else if (ch >= '0' && ch <= '9') {
                index = 26 + (ch - '0');
            } else {
                return false;
            }
            if (current.children[index] == null) {
                return false;
            }
            current = current.children[index];
        }
        return current != null && current.isEndOfWord;
    }

    public void delete(String vehicleNo) {
        delete(root, vehicleNo.toUpperCase(), 0);
    }

    private boolean delete(TrieNode node, String vehicleNo, int depth) {
        if (node == null) {
            return false;
        }
        if (depth == vehicleNo.length()) {
            if (!node.isEndOfWord) {
                return false;
            }
            node.isEndOfWord = false;
            node.vehiclePrefix = null;
            return hasNoChildren(node);
        }
        char ch = vehicleNo.charAt(depth);
        int index;
        if (ch >= 'A' && ch <= 'Z') {
            index = ch - 'A';
        } else if (ch >= '0' && ch <= '9') {
            index = 26 + (ch - '0');
        } else {
            return false;
        }
        boolean childDeleted = delete(node.children[index], vehicleNo, depth + 1);
        if (childDeleted && node.children[index] != null) {
            node.children[index] = null;
        }
        return !node.isEndOfWord && hasNoChildren(node);
    }

    private boolean hasNoChildren(TrieNode node) {
        for (TrieNode child : node.children) {
            if (child != null) {
                return false;
            }
        }
        return true;
    }
}

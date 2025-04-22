package com.example;

public class Trie {
    class Node {
        boolean endOfWord;
        TrieDataStructure nextLevel;

        Node() {
            endOfWord = false;
            nextLevel = null;
        }
    }

    class TrieDataStructure {
        Node[] list;

        TrieDataStructure() {
            list = new Node[26];
        }
    } 

    Node root;

    Trie() {
        root = new Node();
    }

    Node loadTrie(String[] words) {
        for (String word : words) {
            Node currentNode = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (currentNode.nextLevel == null) {
                    currentNode.nextLevel = new TrieDataStructure();
                }
                if (currentNode.nextLevel.list[index] == null) {
                    currentNode.nextLevel.list[index] = new Node();
                }
                currentNode = currentNode.nextLevel.list[index];
            }
            currentNode.endOfWord = true;
        }
        return root;
    }

    boolean search(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        Node currentNode = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (currentNode.nextLevel == null || currentNode.nextLevel.list[index] == null) {
                return false;
            }
            currentNode = currentNode.nextLevel.list[index];
        }
        return currentNode.endOfWord;
    }   

    boolean delete(String word) {
        if (word == null || word.isEmpty() || !search(word)) {
            return false;
        }
        return deleteHelper(root, word, 0);
    }

    private boolean deleteHelper(Node currentNode, String word, int index) {
        if (index == word.length()) {
            currentNode.endOfWord = false;
            return true;
        }
        int charIndex = word.charAt(index) - 'a';
        Node nextNode = currentNode.nextLevel.list[charIndex];
        boolean deleted = deleteHelper(nextNode, word, index + 1);
        
        // Check if we can delete this node
        if (deleted && !nextNode.endOfWord && nextNode.nextLevel == null) {
            currentNode.nextLevel.list[charIndex] = null;
            // Check if we can delete the entire TrieDataStructure
            boolean canDeleteTrieDataStructure = true;
            for (Node node : currentNode.nextLevel.list) {
                if (node != null) {
                    canDeleteTrieDataStructure = false;
                    break;
                }
            }
            if (canDeleteTrieDataStructure) {
                currentNode.nextLevel = null;
            }
        }
        return deleted;
    }

    boolean doesDictionaryContainsTwoWords(String word) {
        if (word == null || word.isEmpty() || search(word)) {
            return false;
        }
        
        Node currentNode = root;
        // Try all possible splits
        for (int i = 0; i < word.length() - 1; i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            
            // If we can't continue in the trie, break early
            if (currentNode.nextLevel == null || currentNode.nextLevel.list[index] == null) {
                break;
            }
            
            currentNode = currentNode.nextLevel.list[index];
            
            // If we found a complete word at this position
            if (currentNode.endOfWord) {
                // Check if the remaining part is also a complete word
                String remaining = word.substring(i + 1);
                if (search(remaining)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canBeSplitIntoTwoWords(String word) {
        // Check if it can be formed by two complete words
        for (int i = 1; i < word.length(); i++) {
            String firstPart = word.substring(0, i);
            String secondPart = word.substring(i);
            if (search(firstPart) && search(secondPart)) {
                return true;
            }
        }
        return false;
    }
}

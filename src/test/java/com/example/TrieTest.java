package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrieTest {
    
    @Test
    public void testLoadAndSearch() {
        Trie trie = new Trie();
        String[] words = {"hello", "world", "hell", "word"};
        trie.loadTrie(words);
        
        assertTrue(trie.search("hello"));
        assertTrue(trie.search("world"));
        assertTrue(trie.search("hell"));
        assertTrue(trie.search("word"));
        assertFalse(trie.search("helloworld"));
        assertFalse(trie.search("wor"));
        assertFalse(trie.search(""));
    }
    
    @Test
    public void testDelete() {
        Trie trie = new Trie();
        String[] words = {"hello", "world", "hell", "word"};
        trie.loadTrie(words);
        
        assertTrue(trie.delete("hello"));
        assertFalse(trie.search("hello"));
        assertTrue(trie.search("hell"));
        assertTrue(trie.search("world"));
        
        assertTrue(trie.delete("hell"));
        assertFalse(trie.search("hell"));
        
        assertFalse(trie.delete("nonexistent"));
    }
    
    @Test
    public void testCompoundWords() {
        Trie trie = new Trie();
        String[] words = {"hello", "world", "hell", "o", "world"};
        trie.loadTrie(words);
        
        assertTrue(trie.doesDictionaryContainsTwoWords("helloworld"));
        assertFalse(trie.doesDictionaryContainsTwoWords("hello"));
        assertFalse(trie.doesDictionaryContainsTwoWords(""));
    }
    
    @Test
    public void testEmptyTrie() {
        Trie trie = new Trie();
        String[] words = {};
        trie.loadTrie(words);
        
        assertFalse(trie.search("hello"));
        assertFalse(trie.delete("hello"));
        assertFalse(trie.doesDictionaryContainsTwoWords("hello"));
    }
} 
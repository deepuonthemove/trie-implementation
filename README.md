# Trie Data Structure Implementation

This project implements a Trie (prefix tree) data structure in Java with the following features:

- Word insertion and dictionary building
- Word search
- Word deletion
- Compound word detection (checking if a word can be formed by combining two dictionary words)

## Project Structure

```
src/
├── main/java/com/example/
│   └── Trie.java
└── test/java/com/example/
    └── TrieTest.java
```

## Features

- **Word Search**: Efficiently search for words in the dictionary
- **Word Deletion**: Remove words from the dictionary while maintaining trie structure
- **Compound Word Detection**: Check if a word can be formed by combining two dictionary words
- **Memory Efficient**: Optimized trie structure implementation

## Building the Project

This project uses Maven for build management. To build the project:

```bash
mvn clean install
```

## Running Tests

To run the tests:

```bash
mvn test
```

## Usage Example

```java
Trie trie = new Trie();
String[] words = {"hello", "world"};
trie.loadTrie(words);

// Search for words
boolean exists = trie.search("hello"); // returns true

// Check for compound words
boolean isCompound = trie.doesDictionaryContainsTwoWords("helloworld"); // returns true
```

## License

This project is open source and available under the MIT License.

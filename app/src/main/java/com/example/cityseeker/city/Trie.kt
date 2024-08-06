package com.example.cityseeker.city

class TrieNode {
    val children: MutableMap<Char , TrieNode> = mutableMapOf()
    var isEndOfWord: Boolean = false
}

class Trie {
    private val root = TrieNode()

    fun insert(word: String) {
        var node = root
        for (char in word) {
            node = node.children.getOrPut(char) { TrieNode() }
        }
        node.isEndOfWord = true
    }

    fun searchByPrefix(prefix: String): List<String> {
        val result = mutableListOf<String>()
        var node = root
        for (char in prefix) {
            node = node.children[char] ?: return result
        }
        findAllWords(node, StringBuilder(prefix), result)
        return result
    }

    private fun findAllWords(node: TrieNode, prefix: StringBuilder, result: MutableList<String>) {
        if (node.isEndOfWord) {
            result.add(prefix.toString())
        }
        for ((char, childNode) in node.children) {
            findAllWords(childNode, prefix.append(char), result)
            prefix.setLength(prefix.length - 1) // backtrack
        }
    }
}
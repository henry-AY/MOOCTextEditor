package spelling;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// TODO: Auto-generated Javadoc
/**
 *  
 * An trie data structure that implements the Dictionary and the AutoComplete ADT.
 *
 * @author You
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    /** The root. */
    private TrieNode root;
    
    /** The size. */
    private int size;
    
    //TODO: Add the constructor here
	public AutoCompleteDictionaryTrie() {
		root = new TrieNode();
		size = 0;
	}
	
	/**
     * Insert a word into the trie.
     * You should convert the 
     * string to all lower case before you insert it. 
     * 
     * This method adds a word by creating and linking the necessary trie nodes 
     * into the trie, as described in the slides for this week. It 
     * should appropriately use existing nodes in the trie, only creating new 
     * nodes when necessary. E.g. If the word "no" is already in the trie, 
     * then adding the word "now" would add only one additional node 
     * (for the 'w').
     *
     * @param word the word
     * @return true if the word was successfully added or false if it already exists
     * in the dictionary.
     */
	public boolean addWord(String word)
	{
		TrieNode parent = new TrieNode();
		parent = root;
		word = word.toLowerCase();
		if(isWord(word)) {
			return false;
		} else {
			for(char c : word.toCharArray()) {
				if(parent.getChild(c) == null) {
					parent.insert(c);
				}
				parent = parent.getChild(c);
			}
			parent.setEndsWord(true);
			size++;
			return true;
		}
	}
	
	/**
	 *  
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 *
	 * @return the int
	 */
	public int size()
	{
	    return size;
	}
	
	
	/**
	 *  Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week.
	 *
	 * @param s the s
	 * @return true, if is word
	 */
	@Override
	public boolean isWord(String s) 
	{
		String word = s.toLowerCase();
		TrieNode parent = new TrieNode();
		parent = root;
		for(char c : word.toCharArray()) {
			if(parent.getChild(c) == null) {
				return false;
			}
			parent = parent.getChild(c);
		}
		if(parent.endsWord()) {
			return true;
		}
		return false;
	}

	/** 
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions 
     * of the prefix string. All legal completions must be valid words in the 
     * dictionary. If the prefix itself is a valid word, it is included 
     * in the list of returned words. 
     * 
     * The list of completions must contain 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list
    	 // 2. Once the stem is found, perform a levelorder (breadth first) search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
    	 //    Create a list of completions to return (initially empty)
    	 //    While the queue is not empty and you don't have enough completions:
    	 //       remove the first Node from the queue
    	 //       If it is a word, add it to the completions list
    	 //       Add all of its child nodes to the back of the queue
    	 // Return the list of completions
    	 LinkedList<String> emptyLL = new LinkedList<String> ();
    	 LinkedList<TrieNode> queue;
    	 LinkedList<String> completedPredictions;
    	 TrieNode parent;
    	 TrieNode n;
    	 
    	 String stem = prefix.toLowerCase();
    	 parent = findStem(prefix);
    	 if (stem == null) { return emptyLL; }
    	 if(parent == null) { return emptyLL; }
    	 queue = new LinkedList<TrieNode>();
    	 completedPredictions = new LinkedList<String>();
    	 queue.add(parent);
    	 while(!queue.isEmpty() && completedPredictions.size() < numCompletions) {
    		 n = queue.remove();
    		 if(n.endsWord()) {
    			 completedPredictions.add(n.getText());
    		 }
    		 addChildrenToQueue(queue, n);
    	 }
    	 return completedPredictions;
     }


     /**
      * Find stem - finds the partial word that matches what the user has already typed
      *
      * @param stem the stem to match
      * @return the trie node
      */
     private TrieNode findStem(String stem) {
    	 // TODO: Implement this method
    	 // This method should find the "stem" in the Trie, and return the TrieNode of the last letter
    	 // in the stem, if it exists in the Trie. Return null if the stem does not exist in the Trie
    	 TrieNode node = root;
    	 for(int i = 0; i < stem.length(); i++) {
    		 char currChar = stem.charAt(i);
    		 if(node.getChild(currChar) == null) {
    			 return null;
    		 }
    		 node = node.getChild(currChar);
    	 }
    	 return node;
     }
     
     /**
      * Adds the children nodes to breadth-first search queue.
      *
      * @param queue the queue
      * @param parent the parent
      */
     private void addChildrenToQueue(Queue<TrieNode> queue, TrieNode parent) {
    	 // TODO: Implement this method by getting the list of all existing children characters (keys) of the parent
    	 //       node, and then add the corresponding TrieNode to the queue
    	 for(char c : parent.getValidNextCharacters()) {
    		 queue.add(parent.getChild(c));
    	 }
     }
     
     
 	/**
	  * Prints the tree from root
	  */
	 // For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/**
	  *  Do a pre-order traversal from this node down.
	  *
	  * @param curr the curr
	  */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}
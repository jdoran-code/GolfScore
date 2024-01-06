/*
 * ChainedHashTable.java
 *
 * Computer Science 112, Boston University
 * 
 * Modifications and additions by:
 *     name: Justin Doran
 *     email: justin.doran2002@gmail.com
 */

import java.util.*;     // to allow for the use of Arrays.toString() in testing

/*
 * A class that implements a hash table using separate chaining.
 */
public class ChainedHashTable implements HashTable {
    /* 
     * Private inner class for a node in a linked list
     * for a given position of the hash table
     */
    private class Node {
        private Object key;
        private LLQueue<String> values;
        private Node next;
        
        private Node(Object key, String value) {
            this.key = key;
            values = new LLQueue<String>();
            values.insert(value);
            next = null;
        }
    }
    
    private Node[] table;      // the hash table itself
    private int numKeys;       // the total number of keys in the table
        
    /* hash function */
    public int h1(Object key) {
        int h1 = key.hashCode() % table.length;
        if (h1 < 0) {
            h1 += table.length;
        }
        return h1;
    }
    
    /*** Add your constructor here ***/
    public ChainedHashTable(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        table = new Node[size];
        numKeys = 0;
    }
    
    
    /*
     * insert - insert the specified (key, value) pair in the hash table.
     * Returns true if the pair can be added and false if there is overflow.
     */
    public boolean insert(Object key, String value) {
        /** Replace the following line with your implementation. **/
        if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }

        int hash = h1(key);
        Node toInsert;

        if (table[hash] == null) {
            toInsert =  new Node(key, value);
            table[hash] = toInsert;
        } else {
            Node trav = table[hash];
            while (trav != null) {
                if (trav.key.equals(key)) {
                    trav.values.insert(value);
                    return true;
                }
                trav = trav.next;
            }

            toInsert = new Node(key, value);
            toInsert.next = table[hash];
            table[hash] = toInsert;
        }

        numKeys++;
        return true;
    }
    
    /*
     * search - search for the specified key and return the
     * associated collection of values, or null if the key 
     * is not in the table
     */
    public LLQueue<String> search(Object key) {
        /** Replace the following line with your implementation. **/
        if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }

        int hash = h1(key);

        if (table[hash] == null) {
            return null;
        } else {
            Node trav = table[hash];
            while (trav != null) {
                if (trav.key.equals(key)) {
                    return trav.values;
                }
                trav = trav.next;
            }

            return null;
        }
    }
    
    /* 
     * remove - remove from the table the entry for the specified key
     * and return the associated collection of values, or null if the key 
     * is not in the table
     */
    public LLQueue<String> remove(Object key) {
        /** Replace the following line with your implementation. **/
        if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }

        int hash = h1(key);

        if (table[hash] == null) {
            return null;
        } else {
            Node trav = table[hash];
            Node trail = null;
            while (trav != null) {
                if (trav.key.equals(key)) {
                    numKeys--;
                    if (trail == null) {
                        table[hash] = null;
                    } else {
                        trail.next = trav.next;
                    }
                    return trav.values;
                }
                trail = trav;
                trav = trav.next;
            }

            return null;
        }
    }
}

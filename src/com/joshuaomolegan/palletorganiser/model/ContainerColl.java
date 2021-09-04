package com.joshuaomolegan.palletorganiser.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

/** Collection of boxes ordered by their surface areas*/
public class ContainerColl {

    /** Temporary array containing box nodes*/
    private Node[] tmpBoxNodes;

    /** Index of the last added box to tmpBoxNodes */
    private int i;

    /** Set containing keys of boxes we are deleting*/
    private HashSet<String> boxSet = new HashSet<>();

    /** Linked list dummy head*/
    Node header = new Node(null, null);

    /** Keeps track of the node before the current node*/
    Node lastNode = header;

    /** Class constructor
     * @param n number of boxes this collection will hold*/
    public ContainerColl(int n) {
        this.tmpBoxNodes = new Node[3*n]; // Will contain nodes for all possible box rotations
    }

    /** Function to add box to the box collection
     * @param boxLabel label for the box
     * @param width width of the box
     * @param depth depth of the box
     * @param height height of the box*/
    public void addBox(String boxLabel, int width, int depth, int height){
        Container newContainer = new Container(boxLabel, width, depth, height);

        for (int j = 0; j < 3; j++){
            tmpBoxNodes[i+j] = new Node(boxLabel, newContainer);
            newContainer = newContainer.rotate();
        }
        i += 3;
    }

    /** Function to sort boxes in order of surface area and add them to a linked list */
    public void sortBoxes() {
        Arrays.sort(tmpBoxNodes, Comparator.comparingInt(x -> -1*x.box.getSurfaceArea())); // Sort nodes by the size of their areas in increasing order

        Node current = header;
        for (Node n : tmpBoxNodes){
            current.next = new Node(n.boxLbl, n.box);
            current = current.next;
        }
    }

    /** Function to get the current box
     * @return the current box in the linked list and null if we've reached the end*/
    public Container getCurrentBox() {
        // Delete nodes that are no longer in hashtable
        while (lastNode.next != null && boxSet.contains(lastNode.next.boxLbl)){
            lastNode.next = lastNode.next.next;
        }

        if (lastNode.next != null) {
            return lastNode.next.box;
        } else {
            return null;
        }
    }

    /** Function to get the next node. Requires hasNext to be run first*/
    public void next() {
        // Delete nodes that are no longer in hashtable
        while (lastNode.next != null && boxSet.contains(lastNode.next.boxLbl)){
            lastNode.next = lastNode.next.next;
        }
        lastNode = lastNode.next;
    }

    /** Function to check if we are at the end of the list
     * @return boolean representing whether we are at the end of the list*/
    public boolean hasNext() {
        return lastNode.next == null;
    }

    /** Function to go back to the start of the linked list*/
    public void resetList() {
        lastNode = header;
    }

    /** Function to delete box
     * @param boxLbl String representing label of box to be deleted*/
    public void deleteBox(String boxLbl) {
        boxSet.add(boxLbl);
    }
}

/** Node class for linked list*/
class Node {
    String boxLbl;
    Container box;
    Node next = null;

    public Node(String lbl, Container box){
        this.boxLbl = lbl;
        this.box = box;
    }
}
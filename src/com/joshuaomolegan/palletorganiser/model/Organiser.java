package com.joshuaomolegan.palletorganiser.model;

import java.util.ArrayList;
import java.util.Arrays;

/** Class to handle the arrangement of boxes on the pallet*/
public class Organiser {

    private int palletWidth;
    private int palletDepth;
    private int palletHeight;

    private int numBoxes;

    private ContainerColl collection;

    /** 2D array representing space on the pallet*/
    int[][] space;

    /** Array to store box length width and height in order of how they will be placed*/
    private ArrayList<int[]> resultArray;

    /** Index of current result*/
    private int index;

    /** Function to get the information to place next box
     * @return next result if we haven't reached the end otherwise null*/
    public int[] getNextResult() {
        if (index < resultArray.size()) {
            index += 1;
            return resultArray.get(index-1);
        } else {
            return null;
        }
    }

    /**Setter function for pallet width
     * @param newPalletWidth - new width for pallet*/
    public void setPalletWidth(int newPalletWidth) {
        this.palletWidth = newPalletWidth;
    }

    /**
     * Getter function for pallet width
     *
     * @return current pallet width
     */
    public int getPalletWidth() {
        return palletWidth;
    }

    /**
     * Setter function for pallet length
     *
     * @param newPalletLength - new length for pallet
     */
    public void setPalletDepth(int newPalletLength) {
        this.palletDepth = newPalletLength;
    }

    /**
     * Getter function for pallet length
     *
     * @return current pallet length
     */
    public int getPalletDepth() {
        return palletDepth;
    }

    /**
     * setter function for pallet height
     *
     * @param newPalletHeight - new pallet height
     */
    public void setPalletHeight(int newPalletHeight) {
        this.palletHeight = newPalletHeight;
    }

    /**
     * Getter function for pallet height
     *
     * @return current pallet height
     */
    public int getPalletHeight() {
        return palletHeight;
    }


    /**
     * Setter function for number of Boxes
     *
     * @param newNumBoxes - new length for pallet
     */
    public void setNumBoxes(int newNumBoxes) {
        this.numBoxes = newNumBoxes;
        collection = new ContainerColl(this.numBoxes);
    }

    /**
     * Getter function for number of boxes
     *
     * @return number of boxes
     */
    public int getNumBoxes() {
        return numBoxes;
    }

    /**
     * Function to add box to be place on the pallet
     * @param boxLabel Label for the box
     * @param dim1 dimension 1 of the box
     * @param dim2 dimension 2 of the box
     * @param dim3 dimension 3 of the box
     */
    public void addBox(String boxLabel, int dim1, int dim2, int dim3) {
        // Width, depth and height should be in decreasing order
        int[] a = {dim1, dim2, dim3};
        Arrays.sort(a);

        collection.addBox(boxLabel, a[0], a[1], a[2]);
    }

    /** Function to find the optimal arrangement of boxes on the pallet
     * @return true if an arrangement exists and false otherwise*/
    public boolean arrangeBoxes() {
        collection.sortBoxes();

        space = new int[palletWidth][palletDepth];
        int[][] helperArray = new int[palletWidth][palletDepth];
        resultArray = new ArrayList<>();
        // Algorithm to arrange boxes
        fillHelperArray(helperArray);
        while (numBoxes > 0) {
            Container currentBox = collection.getCurrentBox();
            int[] boxPos = getFirstFit(space, helperArray, currentBox, false);

            if (boxPos != null) {
                int placementHeight = space[boxPos[0]][boxPos[2]];

                // start width, end width, start depth, end depth, start height, end height
                resultArray.add(new int[]{boxPos[0], boxPos[1], boxPos[2], boxPos[3], placementHeight, placementHeight+currentBox.getHeight()});

                for (int i = boxPos[0]; i < boxPos[1]; i++) {
                    for (int j = boxPos[2]; j < boxPos[3]; j++) {
                        space[i][j] += currentBox.getHeight();
                    }
                }
                collection.deleteBox(currentBox.getLabel());
                numBoxes -= 1;
                collection.resetList();

            } else if (!collection.hasNext()) {
                // No arrangement exists
                return false;
            } else {
                collection.next();
            }
        }
        return true;
    }

    /** Function to fill the helper array
     * @param helperArray helper array used in the getFirstFit function*/
    private void fillHelperArray(int[][] helperArray) {
        for (int i = palletWidth - 2; i >= 0; i--) {
            for (int j = palletDepth - 1; j >= 0; j--) {
                if (space[i][j] == space[i + 1][j]) {
                    helperArray[i][j] = helperArray[i + 1][j] + 1;
                }
            }
        }
    }

    /** Function to find the first space the current box fits into
     * @param space Array representing the space on the pallet
     * @param helper Helper array
     * @param box box we are trying to fit into space
     * @param toSwap boolean representing whether to swap width and height
     * @return Int array containing start and stop rows and columns where the box will be placed*/
    private int[] getFirstFit(int[][] space, int[][] helper, Container box, boolean toSwap) {
        // https://stackoverflow.com/questions/4656706/how-to-find-same-value-rectangular-areas-of-a-given-size-in-a-matrix-most-effici
        int width;
        int depth;

        // Swap width and depth if toSwap is true
        if (toSwap) {
            width = box.getDepth();
            depth = box.getWidth();
        } else {
            width = box.getWidth();
            depth = box.getDepth();
        }

        for (int i = 0; i < space.length; i++) {
            int currentWidth = 0;
            for (int j = 0; j < space[0].length; j++) {
                if (helper[i][j] < depth - 1) {
                    // this column has different numbers in it, no game
                    currentWidth = 0;
                    continue;
                }

                if (currentWidth > 0) {
                    // this column should consist of the same numbers as the one before
                    if (space[i][j] != space[i][j - 1]) {
                        currentWidth = 1; // start streak anew, from the current column
                        continue;
                    }
                }

                ++currentWidth;
                if (currentWidth >= width) {
                    // we've found a rectangle!
                    return new int[]{i, i + depth, j - width + 1, j};
                }
            }
        }

        // If we can't find a place for the box, turn the box and see if it fits that way
        if (!toSwap) {
            return getFirstFit(space, helper, box, true);
        } else {
            return null;
        }
    }
}

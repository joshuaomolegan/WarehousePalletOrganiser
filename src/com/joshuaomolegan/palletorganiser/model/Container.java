package com.joshuaomolegan.palletorganiser.model;

/** Class that represents boxes to be packed onto pallet */

public class Container {
    private final String boxLabel;
    private int width;
    private int depth;
    private int height;

    /** Surface area of the current bottom face of the box */
    private final int surfaceArea;

    /** Class constructor
     * @param width An int representing the box's length (longest)
     * @param depth An int representing the box's width
     * @param height An int representing the box's height (shortest)*/
    public Container(String label, int width, int depth, int height){
        this.boxLabel = label;
        setWidth(width);
        setDepth(depth);
        setHeight(height);
        this.surfaceArea = this.width * this.depth;
    }

    /** Gets Box label
     * @return String representing the box label*/
    public String getLabel() {
        return this.boxLabel;
    }

    /** Gets box length
     * @return An int representing the box's length*/
    public int getWidth() {
        return this.width;
    }

    /** Sets box length
     * @param newLen An int representing the new box length*/
    private void setWidth(int newLen) {
        if (newLen < 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        } else {
            this.width = newLen;
        }
    }

    /** Gets box width
     * @return int representing the box's width*/
    public int getDepth() {
        return this.depth;
    }

    /** Sets box width
     * @param newWidth An int representing the new box width*/
    private void setDepth(int newWidth) {
        if (newWidth < 0) {
            throw new IllegalArgumentException("Width must be greater than 0");
        } else {
            this.depth = newWidth;
        }
    }

    /** Gets box height
     * @return int representing the box's height*/
    public int getHeight() {
        return this.height;
    }

    /** Sets box height
     * @param newHeight An int representing the new box height*/
    private void setHeight(int newHeight) {
        if (newHeight < 0) {
            throw new IllegalArgumentException("Height must be greater than 0");
        } else {
            this.height = newHeight;
        }
    }

    /** Gets area of the bottom box face
     * @return int representing the area of the bottom box face*/
    public int getSurfaceArea() {
        return this.surfaceArea;
    }

    /** Rotates the box by interchanging dimensions
     * @return a new container representing the rotated box*/
    public Container rotate(){
        return new Container(this.boxLabel, this.depth, this.height, this.width);
    }

}
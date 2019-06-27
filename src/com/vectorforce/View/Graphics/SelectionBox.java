package com.vectorforce.View.Graphics;

import org.eclipse.swt.graphics.Rectangle;

import java.util.LinkedList;

// Rectangle SelectionBox
public class SelectionBox {
    private int SINGLE_RECTANGLE_SIZE = 10;
    private LinkedList<Rectangle> rectangles;

    public SelectionBox(){
        rectangles = new LinkedList<>();
    }

    public SelectionBox(int x1, int y1, int x2, int y2){

    }

    public SelectionBox(int x, int y, int length){
        Rectangle rectangle = new Rectangle(x, y, length, length);
    }

    public boolean contains(int x, int y){
        for(Rectangle currentRectangle : rectangles){
            if(currentRectangle.contains(x, y) == true){
                return true;
            }
        }
        return false;
    }

    // Setters
    public void getRectangleList(LinkedList<Rectangle> rectangleList){
        this.rectangles = rectangleList;
    }

    // Getters
    public LinkedList<Rectangle> getRectangles(){
        return rectangles;
    }
}

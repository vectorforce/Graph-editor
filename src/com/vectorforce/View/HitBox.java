package com.vectorforce.View;

import org.eclipse.swt.graphics.Rectangle;

// Rectangle hitBox
public class HitBox {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Rectangle rectangle;

    public HitBox(){
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        rectangle = new Rectangle(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    public HitBox(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        rectangle = new Rectangle(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    public boolean contains(int x, int y){
        return true;
    }

    // Setters
    public void setX1(int x1){
        this.x1 = x1;
    }

    public void setY1(int y1){
        this.y1 = y1;
    }

    public void setX2(int x2){
        this.x2 = x2;
    }

    public void setY2(int y2){
        this.y2 = y2;
    }

    // Getters
    public int getX1(){
        return x1;
    }

    public int getY1(){
        return y1;
    }

    public int getX2(){
        return x2;
    }

    public int getY2(){
        return y2;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }
}

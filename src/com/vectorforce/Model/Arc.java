package com.vectorforce.Model;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

public class Arc {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Rectangle hitBox;
    private Color color;
    private Color selectColor;
    private Color deselectColor;
    private Vertex fromVertex;
    private Vertex toVertex;
    private boolean isOriented;
    //    private boolean isBinary;
    private boolean isSelected;

    public Arc(Vertex fromVertex, Vertex toVertex) {
        this.color = new Color(null, 0, 0, 0);
        this.selectColor = new Color(null, 0, 255, 0);
        this.deselectColor = color;
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        initHitBox();
        isOriented = false;
        isSelected = false;
        // Connect verteces with arc
        fromVertex.addOutgoingArc(this);
        fromVertex.addIngoingArc(this);
        toVertex.addOutgoingArc(this);
        toVertex.addIngoingArc(this);
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
    }
    // Calculate rectangle for hitBox
    private void initHitBox(){
        int x = 0;
        int y = 0;
        if (fromVertex.getX() < toVertex.getX()) {
            x = fromVertex.getX();
        } else {
            x = toVertex.getX();
        }
        if (fromVertex.getY() < toVertex.getY()) {
            y = fromVertex.getY();
        } else {
            y = toVertex.getY();
        }
        int width = Math.abs(fromVertex.getX() - toVertex.getX());
        int height = Math.abs(fromVertex.getY() - toVertex.getY());
        hitBox = new Rectangle(x, y, width, height);
    }

    //Setters
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

    public void select(boolean select) {
        this.isSelected = select;
        if (select == true) {
            color = selectColor;
        } else {
            color = deselectColor;
        }
    }

    public void setOriented(boolean isOriented){
        if(isOriented == true){
            fromVertex.getIngoingArcs().remove(this);
            toVertex.getOutgoingArcs().remove(this);
        } else{
            if(this.isOriented == true){
                fromVertex.addIngoingArc(this);
                toVertex.addOutgoingArc(this);
            }
        }
        this.isOriented = isOriented;
    }

    public void setColor(Color color) {
        this.color = color;
        deselectColor = color;
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

    public boolean isOriented() {
        return isOriented;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Color getColor() {
        return color;
    }

    public Vertex getFromVertex() {
        return fromVertex;
    }

    public Vertex getToVertex() {
        return toVertex;
    }

    public Rectangle getHitBox(){
        return hitBox;
    }
}

package com.vectorforce.Model;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

public class Arc {
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
    public void select(boolean select) {
        this.isSelected = select;
        if (select == true) {
            color = selectColor;
        } else {
            color = deselectColor;
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // Getters
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

package com.vectorforce.Model;

import org.eclipse.swt.graphics.Color;

public class Arc {
//    private int startX;
//    private int startY;
//    private int endX;
//    private int endY;
    private Color color;
    private Color selectColor;
    private Color deselectColor;
    private Vertex fromVertex;
    private Vertex toVertex;
    private boolean isOriented;
    private boolean isSelected;

    public Arc(Vertex fromVertex, Vertex toVertex){
        this.color = new Color(null, 0, 0, 0);
        this.selectColor = new Color(null, 0, 255, 0);
        this.deselectColor = color;
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        isOriented = false;
        isSelected = false;
    }

    //Setters
    public void select(boolean select){
        this.isSelected = select;
        if(select == true){
            color = selectColor;
        } else {
            color = deselectColor;
        }
    }

    // Getters
    public boolean isOriented(){
        return isOriented;
    }

    public boolean isSelected(){
        return isSelected;
    }
}

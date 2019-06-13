package com.vectorforce.Model;

import org.eclipse.swt.graphics.Color;

public class Arc {
//    private int startX;
//    private int startY;
//    private int endX;
//    private int endY;
    private Color color;
    private Vertex fromVertex;
    private Vertex toVertex;
    private boolean isOriented;

    public Arc(Vertex fromVertex, Vertex toVertex){
        this.color = new Color(null, 0, 0, 0);
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        isOriented = false;
    }

    // Getters
    public boolean isOriented(){
        return isOriented;
    }
}

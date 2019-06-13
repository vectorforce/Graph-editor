package com.vectorforce.Model;

import org.eclipse.swt.graphics.Color;

import java.util.LinkedList;

public class Vertex {
    private int CORRECTING_SHIFT = 12; // For the exact location of the node relative to the cursor

    private int x;
    private int y;
    private int radius = 25;
    private Color color;
    private LinkedList<Arc> ingoingArcs;
    private LinkedList<Arc> outgoingArcs;

    public Vertex(int x, int y){
        this.x = x - CORRECTING_SHIFT;
        this.y = y - CORRECTING_SHIFT;
        this.color = new Color(null, 0, 0, 0);
        ingoingArcs = new LinkedList<>();
        outgoingArcs = new LinkedList<>();
    }

    public void addIngoingArc(Arc arc){
        ingoingArcs.add(arc);
    }

    public void addOutgoingArc(Arc arc){
        outgoingArcs.add(arc);
    }

    public LinkedList<Arc> getIngoingArcs(){
        return ingoingArcs;
    }

    public LinkedList<Arc> getOutgoingArcs(){
        return outgoingArcs;
    }

    // Setters
    public void setColor(Color color){
        this.color = color;
    }

    // Getters
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getRadius(){
        return radius;
    }

}

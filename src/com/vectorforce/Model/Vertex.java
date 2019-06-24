package com.vectorforce.Model;

import org.eclipse.swt.graphics.Color;

import java.util.LinkedList;

public class Vertex {
    private int x;
    private int y;
    private int diameter = 25;
    private Color color;
    private Color selectColor;
    private Color deselectColor;
    private LinkedList<Arc> ingoingArcs;
    private LinkedList<Arc> outgoingArcs;
    private boolean isSelected;

    public Vertex(int x, int y){
        this.x = x - diameter / 2;
        this.y = y - diameter / 2;
        this.color = new Color(null, 0, 0, 0);
        this.selectColor = new Color(null, 0, 255, 0);
        this.deselectColor = color;
        ingoingArcs = new LinkedList<>();
        outgoingArcs = new LinkedList<>();
        isSelected = false;
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
        this.deselectColor = color;
    }

    public void select(boolean select){
        this.isSelected = select;
        if(select == true){
            color = selectColor;
        } else {
            color = deselectColor;
        }
    }

    // Setters
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setDiameter(int diameter){
        this.diameter = diameter;
    }

    // Getters
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getDiameter(){
        return diameter;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public Color getColor(){
        return color;
    }

}

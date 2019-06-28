package com.vectorforce.Model;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

import java.util.LinkedList;

public class Node<Type> {
    private String ID;
    private int x;
    private int y;
    private int diameter = 25;
    private LinkedList<Arc> ingoingArcs;
    private LinkedList<Arc> outgoingArcs;
    private GraphicalShell graphicalShell;
    private Type data;

    public Node(int x, int y) {
        ID = null;
        this.x = x - diameter / 2;
        this.y = y - diameter / 2;
        graphicalShell = new GraphicalShell();
        ingoingArcs = new LinkedList<>();
        outgoingArcs = new LinkedList<>();
        data = null;
    }

    // Check for the presence of a point on the arc
    public boolean contains(Point point) {
        if ((point.x > x) && (point.x < x + diameter)) {
            if ((point.y > y) && (point.y < y + diameter)) {
                return true;
            }
        }
        return false;
    }

    public void addIngoingArc(Arc arc) {
        ingoingArcs.add(arc);
    }

    public void addOutgoingArc(Arc arc) {
        outgoingArcs.add(arc);
    }

    public LinkedList<Arc> getIngoingArcs() {
        return ingoingArcs;
    }

    public LinkedList<Arc> getOutgoingArcs() {
        return outgoingArcs;
    }

    // Setters
    public void setID(String ID){
        this.ID = ID;
    }

    public void setData(Type data) {
        this.data = data;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    // Getters
    public String getID(){
        return ID;
    }

    public Type getData() {
        return data;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDiameter() {
        return diameter;
    }

    public GraphicalShell getGraphicalShell() {
        return graphicalShell;
    }
}

package com.vectorforce.model.node;

import com.vectorforce.model.Arc;
import com.vectorforce.model.GraphicalShell;
import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.graphics.Point;

import java.util.LinkedList;
import java.util.Random;

public class Node {
    private String ID;
    private String internalID;
    private int x;
    private int y;
    private static int diameter = 25;
    private LinkedList<Arc> ingoingArcs;
    private LinkedList<Arc> outgoingArcs;
    private GraphicalShell graphicalShell;
    private NodeType nodeType;

    public Node(int x, int y) {
        ID = null;
        this.x = x - diameter / 2;
        this.y = y - diameter / 2;
        // Assigning a unique internal identifier
        internalID = String.valueOf(new Random().nextInt((x * 30) + Math.abs(x - y)));
        graphicalShell = new GraphicalShell();
        graphicalShell.setColor(ColorSetupComponent.getNodeColor());
        nodeType = new NodeType();
        ingoingArcs = new LinkedList<>();
        outgoingArcs = new LinkedList<>();
    }

    // Check for the presence of a point on the arc
    public boolean contains(Point point) {
        if ((point.x > x) && (point.x < x + diameter)) {
            return (point.y > y) && (point.y < y + diameter);
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
    public void setInternalID(String internalID){
        this.internalID = internalID;
    }

    public void setType(NodeType.nodeType status){
        nodeType.setStatus(status);
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Getters
    public String getInternalID(){
        return internalID;
    }

    public NodeType.nodeType getType() {
        return nodeType.getStatus();
    }

    public String getID(){
        return ID;
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

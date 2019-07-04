package com.vectorforce.model;

import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.graphics.Point;

public class Arc {
    private String ID;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Node fromNode;
    private Node toNode;
    private int weight;
    private boolean isOriented;
    private GraphicalShell graphicalShell;
    private boolean isBinary;

    public Arc(Node fromNode, Node toNode) {
        ID = null;
        graphicalShell = new GraphicalShell();
        graphicalShell.setColor(ColorSetupComponent.getArcColor());
        this.fromNode = fromNode;
        this.toNode = toNode;
        weight = 0;
        isOriented = true;
        isBinary = false;
        // Connecting nodes with arc
        fromNode.addOutgoingArc(this);
        fromNode.addIngoingArc(this);
        toNode.addOutgoingArc(this);
        toNode.addIngoingArc(this);
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
    }

    // Check for the presence of a point on the arc
    public boolean contains(Point point) {
        int delta = 3; // deviation from arc
        Point fromPoint = new Point(fromNode.getX(), fromNode.getY());
        Point toPoint = new Point(toNode.getX(), toNode.getY());
        int lengthSumSegments = distance(fromPoint, point) + distance(point, toPoint);
        int arcLength = distance(fromPoint, toPoint);
        if (lengthSumSegments > arcLength - delta && lengthSumSegments < arcLength + delta) {
            return true;
        }
        return false;
    }

    private int distance(Point from, Point to) {
        int distance = (int) Math.sqrt((Math.pow((to.x - from.x), 2) + Math.pow((to.y - from.y), 2)));
        return distance;
    }

    public void changeDirection(){
        if(isOriented == true){
            fromNode.getOutgoingArcs().remove(this);
            toNode.getIngoingArcs().remove(this);
            Node changingNode = toNode;
            toNode = fromNode;
            fromNode = changingNode;
            fromNode.addOutgoingArc(this);
            toNode.addIngoingArc(this);
        }
    }

    //Setters
    public void setWeight(int weight){
        this.weight = weight;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void setOriented(boolean isOriented) {
        if (isOriented == true) {
            if(this.isOriented == false){
                fromNode.addIngoingArc(this);
                toNode.addOutgoingArc(this);
            }
        } else {
            if (this.isOriented == true) {
                fromNode.getIngoingArcs().remove(this);
                toNode.getOutgoingArcs().remove(this);
            }
        }
        this.isOriented = isOriented;
    }

    public void setBinary(boolean binary){
        isBinary = binary;
    }

    // Getters
    public int getWeight(){
        return weight;
    }

    public String getID(){
        return ID;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public boolean isOriented() {
        return isOriented;
    }

    public boolean isBinary(){
        return isBinary;
    }

    public boolean isSelected() {
        return graphicalShell.isSelected();
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public GraphicalShell getGraphicalShell(){
        return graphicalShell;
    }
}

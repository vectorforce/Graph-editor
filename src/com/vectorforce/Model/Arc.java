package com.vectorforce.Model;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class Arc {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
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

    // Check for the presence of a point on the arc
    public boolean contains(Point point) {
        int delta = 3; // deviation from arc
        Point fromPoint = new Point(fromVertex.getX(), fromVertex.getY());
        Point toPoint = new Point(toVertex.getX(), toVertex.getY());
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
            fromVertex.getOutgoingArcs().remove(this);
            toVertex.getIngoingArcs().remove(this);
            Vertex changingVertex = toVertex;
            toVertex = fromVertex;
            fromVertex = changingVertex;
            fromVertex.addOutgoingArc(this);
            toVertex.addIngoingArc(this);
        }
    }

    //Setters
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

    public void select(boolean select) {
        this.isSelected = select;
        if (select == true) {
            color = selectColor;
        } else {
            color = deselectColor;
        }
    }

    public void setOriented(boolean isOriented) {
        if (isOriented == true) {
            fromVertex.getIngoingArcs().remove(this);
            toVertex.getOutgoingArcs().remove(this);
        } else {
            if (this.isOriented == true) {
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
}

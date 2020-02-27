package com.vectorforce.model;

import com.vectorforce.model.node.Node;
import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.graphics.Point;

public class Arc {
    private String ID;
    private Node fromNode;
    private Node toNode;
    private int weight;
    private boolean isOriented;
    private GraphicalShell graphicalShell;
    private boolean isBinary;

    public Arc(Node fromNode, Node toNode) {
        if (fromNode.getInternalID().equals(toNode.getInternalID())) {
            return;
        }

        // Check the link
        boolean isLink = false;
        for (int index = 0; index < fromNode.getIngoingArcs().size(); index++) {
            final Arc currentArc = fromNode.getIngoingArcs().get(index);
            if (currentArc.getFromNode().getInternalID().equals(toNode.getInternalID())) {
                isLink = true;
            }
        }
        for (int index = 0; index < fromNode.getOutgoingArcs().size(); index++) {
            final Arc currentArc = fromNode.getOutgoingArcs().get(index);
            if (currentArc.getToNode().getInternalID().equals(toNode.getInternalID())) {
                isLink = true;
            }
        }
        if (isLink) {
            return;
        }

        graphicalShell = new GraphicalShell();
        graphicalShell.setColor(ColorSetupComponent.getArcColor());
        this.fromNode = fromNode;
        this.toNode = toNode;
        ID = String.valueOf(fromNode.getInternalID()) + "Xabf" + String.valueOf(toNode.getInternalID());
        weight = 1;
        isOriented = true;
        isBinary = false;
        // Connecting nodes with arc
        fromNode.addOutgoingArc(this);
        toNode.addIngoingArc(this);
    }

    // Check for the presence of a point on the arc
    public boolean contains(Point point) {
        int delta = 3; // deviation from arc
        final Point fromPoint = new Point(fromNode.getX(), fromNode.getY());
        final Point toPoint = new Point(toNode.getX(), toNode.getY());
        int lengthSumSegments = distance(fromPoint, point) + distance(point, toPoint);
        int arcLength = distance(fromPoint, toPoint);
        return lengthSumSegments > arcLength - delta && lengthSumSegments < arcLength + delta;
    }

    private int distance(Point from, Point to) {
        return (int) Math.sqrt((Math.pow((to.x - from.x), 2) + Math.pow((to.y - from.y), 2)));
    }

    public void changeDirection() {
        if (isOriented) {
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
    public void setFromNode(Node node) {
        this.fromNode = node;
        node.addOutgoingArc(this);
    }

    public void setToNode(Node node) {
        this.toNode = node;
        node.addIngoingArc(this);
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setOriented(boolean isOriented) {
        if (isOriented) {
            if (!this.isOriented) {
                fromNode.addIngoingArc(this);
                toNode.addOutgoingArc(this);
            }
        } else {
            if (this.isOriented) {
                fromNode.getIngoingArcs().remove(this);
                toNode.getOutgoingArcs().remove(this);
            }
        }
        this.isOriented = isOriented;
    }

    public void setBinary(boolean binary) {
        isBinary = binary;
    }

    // Getters
    public int getWeight() {
        return weight;
    }

    public String getID() {
        return ID;
    }

    public boolean isOriented() {
        return isOriented;
    }

    public boolean isBinary() {
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

    public GraphicalShell getGraphicalShell() {
        return graphicalShell;
    }
}

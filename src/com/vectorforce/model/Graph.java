package com.vectorforce.model;

import com.vectorforce.model.node.Node;

import java.util.LinkedList;

public class Graph implements IGraph {
    private String ID;
    private LinkedList<Node> nodes;
    private LinkedList<Arc> arcs;
    private boolean isOriented;
    private boolean isMixed;
    private boolean isFull;

    public Graph() {
        ID = null;
        nodes = new LinkedList<>();
        arcs = new LinkedList<>();
        isOriented = false;
        isMixed = false;
        isFull = false;
    }

    public void buildFullGraph() {
        if (nodes.size() > 2) {
            deleteAllArcs();
            for (int indexFrom = 0; indexFrom < nodes.size() - 1; indexFrom++) {
                for (int indexTo = 1; indexTo < nodes.size(); indexTo++) {
                    // Check the link
                    boolean isLink = false;
                    for (int index = 0; index < nodes.get(indexFrom).getIngoingArcs().size(); index++) {
                        Arc currentArc = (Arc) nodes.get(indexFrom).getIngoingArcs().get(index);
                        if (currentArc.getFromNode().getInternalID().equals(nodes.get(indexTo).getInternalID())) {
                            isLink = true;
                        }
                    }
                    for (int index = 0; index < nodes.get(indexFrom).getOutgoingArcs().size(); index++) {
                        Arc currentArc = (Arc) nodes.get(indexFrom).getOutgoingArcs().get(index);
                        if (currentArc.getToNode().getInternalID().equals(nodes.get(indexTo).getInternalID())) {
                            isLink = true;
                        }
                    }
                    if (isLink == false) {
                        Arc arc = new Arc(nodes.get(indexFrom), nodes.get(indexTo));
                        arc.setOriented(false);
                        if (arc.getID() != null) {
                            arcs.add(arc);
                        }
                    }
                }
            }
        }
    }

    public void addNode(Node node) {
        nodes.add(node);
        updateGraphData();
    }

    public void deleteNode(Node node) {
        // Delete all adjacent arcs
        for (int index = 0; index < node.getIngoingArcs().size(); index++) {
            if (deleteArc((Arc) node.getIngoingArcs().get(index)) == true) {
                index--;
            }
        }
        for (int index = 0; index < node.getOutgoingArcs().size(); index++) {
            if (deleteArc((Arc) node.getOutgoingArcs().get(index)) == true) {
                index--;
            }
        }
        nodes.remove(node);
        updateGraphData();
    }

    public Node searchNode(String internalID) {
        for (Node currentNode : getNodes()) {
            if (currentNode.getInternalID().equals(internalID)) {
                return currentNode;
            }
        }
        return null;
    }

    public void addArc(Arc arc) {
        arcs.add(arc);
        updateGraphData();
    }

    public boolean deleteArc(Arc arc) {
        boolean deleteFlag = false;
        if (arcs.contains(arc) == true) {
            deleteFlag = true;
        }
        removeLinks(arc);
        return deleteFlag;
    }

    public void deleteAllArcs() {
        if (arcs.size() > 0) {
            for (int index = 0; index < arcs.size(); index++) {
                deleteArc(arcs.get(index--));
            }
        }
    }

    public void deleteAllNodes() {
        for (int index = 0; index < nodes.size(); index++) {
            deleteNode(nodes.get(index--));
        }
    }

    private void removeLinks(Arc arc) {
        arc.getFromNode().getOutgoingArcs().remove(arc);
        arc.getFromNode().getIngoingArcs().remove(arc);
        arc.getToNode().getOutgoingArcs().remove(arc);
        arc.getToNode().getIngoingArcs().remove(arc);
        arcs.remove(arc);
        updateGraphData();
    }

    public void removeSelection() {
        for (Node currentNode : getNodes()) {
            currentNode.getGraphicalShell().select(false);
        }
        for (Arc currentArc : getArcs()) {
            currentArc.getGraphicalShell().select(false);
        }
    }

    // Method for update information of graph
    public void updateGraphData() {
        if (nodes.size() == 0) {
            isMixed = false;
            isFull = false;
            isOriented = false;
            return;
        }
        int counterOriented = 0;
        int counterNonOriented = 0;
        for (int index = 0; index < arcs.size(); index++) {
            if (arcs.get(index).isOriented() == true) {
                counterOriented++;
            } else {
                counterNonOriented++;
            }
        }
        if (counterNonOriented > 0 && counterOriented > 0) {
            isMixed = true;
            isOriented = false;
        } else if (counterNonOriented == 0 && counterOriented > 0) {
            isOriented = true;
            isMixed = false;
        } else if (counterNonOriented > 0 && counterOriented == 0) {
            isOriented = false;
            isMixed = false;
        } else if (counterNonOriented == 0 && counterOriented == 0) {
            isOriented = false;
            isMixed = false;
            isFull = false;
        }
        // Check on full
        if (isOriented == false && isMixed == false) {
            int amountNodes = getNodes().size();
            if ((amountNodes * (amountNodes - 1) / 2) == getArcs().size()) {
                isFull = true;
            } else {
                isFull = false;
            }
        }
    }

    // Setters
    public void setID(String ID) {
        this.ID = ID;
    }

    // Getters
    public String getID() {
        return ID;
    }

    public boolean isOriented() {
        return isOriented;
    }

    public boolean isMixed() {
        return isMixed;
    }

    public boolean isFull() {
        return isFull;
    }

    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public LinkedList<Arc> getArcs() {
        return arcs;
    }
}

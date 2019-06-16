package com.vectorforce.Model;

import java.util.LinkedList;

public class Graph {
    private LinkedList<Vertex> verteces;
    private LinkedList<Arc> arcs;
    private boolean isOriented;
    private boolean isMixed;
    private boolean isFull;

    public Graph() {
        verteces = new LinkedList<>();
        arcs = new LinkedList<>();
        isOriented = false;
        isMixed = false;
        isFull = false;
    }

    public void addVertex(Vertex vertex) {
        verteces.add(vertex);
        graphCheck();
    }

    public void deleteVertex(Vertex vertex) {
        // Delete all adjacent arcs
        for(Arc currentArc : vertex.getIngoingArcs()){
            deleteArc(currentArc);
        }
        for(Arc currentArc : vertex.getOutgoingArcs()){
            deleteArc(currentArc);
        }
//        arcs.remove(vertex.getIngoingArcs());
//        arcs.remove(vertex.getOutgoingArcs());
//        for (Arc currentArc : vertex.getOutgoingArcs()) {
//            currentArc = null;
//        }
//        for (Arc currentArc : vertex.getIngoingArcs()) {
//            currentArc = null;
//        }
        verteces.remove(vertex);
        graphCheck();
    }

    public void addArc(Arc arc) {
        arcs.add(arc);
        graphCheck();
    }

    public void deleteArc(Arc arc) {
        arc.getFromVertex().getOutgoingArcs().remove(arc);
        arc.getFromVertex().getIngoingArcs().remove(arc);
        arc.getToVertex().getOutgoingArcs().remove(arc);
        arc.getToVertex().getIngoingArcs().remove(arc);
        arcs.remove(arc);
        graphCheck();
    }

    public void removeSelection() {
        for (Vertex currentVertex : getVerteces()) {
            currentVertex.select(false);
        }
        for (Arc currentArc : getArcs()) {
            currentArc.select(false);
        }
    }

    // Method for update information of graph
    private void graphCheck() {
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
    }

    // Getters
    public boolean isOriented() {
        return isOriented;
    }

    public boolean isMixed() {
        return isMixed;
    }

    public boolean isFull() {
        return isFull;
    }

    public LinkedList<Vertex> getVerteces() {
        return verteces;
    }

    public LinkedList<Arc> getArcs() {
        return arcs;
    }
}

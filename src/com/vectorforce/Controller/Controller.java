package com.vectorforce.Controller;

import com.vectorforce.Model.Arc;
import com.vectorforce.Model.Graph;
import com.vectorforce.Model.OperationType;
import com.vectorforce.Model.Vertex;

import java.util.LinkedList;

public class Controller {
    private LinkedList<Graph> graphs;
    private int indexCurrentGraph;
    private OperationType operationType;

    public Controller(){
        indexCurrentGraph = 0;
        graphs = new LinkedList<>();
        operationType = new OperationType();
    }

    public void addGraph(){
        Graph graph = new Graph();
        graphs.add(graph);
        indexCurrentGraph = graphs.size() - 1;
    }

    public void deleteGraph(){
        graphs.remove(indexCurrentGraph);
    }

    public void addVertex(Vertex vertex){
        graphs.get(indexCurrentGraph).addVertex(vertex);
    }

    public void deleteVertex(Vertex vertex){
        graphs.get(indexCurrentGraph).deleteVertex(vertex);
    }

    public void addArc(Arc arc){
        graphs.get(indexCurrentGraph).addArc(arc);
    }

    public void deleteArc(Arc arc){
        graphs.get(indexCurrentGraph).deleteArc(arc);
    }

    public void removeSelection(){
        graphs.get(indexCurrentGraph).removeSelection();
    }

    // Drawing methods


    // Setters
    public void setIndexCurrentGraph(int indexCurrentGraph){
        this.indexCurrentGraph = indexCurrentGraph;
    }

    public void setStatus(OperationType.operationType status){
        operationType.setStatus(status);
    }

    // Getters
    public Graph getGragh(){
        return graphs.get(indexCurrentGraph);
    }

    public int getIndexCurrentGraph(){
        return indexCurrentGraph;
    }

    public OperationType.operationType getStatus() {
        return operationType.getStatus();
    }

//    public LinkedList<Vertex> getVerteces(){
//        return graphs.get(indexCurrentGraph).getVerteces();
//    }
//
//    public LinkedList<Arc> getArcs(){
//        return graphs.get(indexCurrentGraph).getArcs();
//    }
}

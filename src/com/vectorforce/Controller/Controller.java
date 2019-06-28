package com.vectorforce.Controller;

import com.vectorforce.Model.Arc;
import com.vectorforce.Model.Graph;
import com.vectorforce.Controller.Common.OperationType;
import com.vectorforce.Model.Node;

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

    public void addNode(Node node){
        graphs.get(indexCurrentGraph).addNode(node);
    }

    public void deleteNode(Node node){
        graphs.get(indexCurrentGraph).deleteNode(node);
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

    public void changeDirection(Arc arc){
        arc.changeDirection();
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
}

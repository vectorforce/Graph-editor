package com.vectorforce.controller;

import com.vectorforce.model.Arc;
import com.vectorforce.model.Graph;
import com.vectorforce.controller.common.OperationType;
import com.vectorforce.model.Node;

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
    public void setWeight(Arc arc, int weight){
        arc.setWeight(weight);
    }

    public void setID(Node node, String ID){
        node.setID(ID);
    }

    public void setID(Arc arc, String ID){
        arc.setID(ID);
    }

    public void setOriented(Arc arc, boolean orientation){
        arc.setOriented(orientation);
    }

    public void setBinary(Arc arc, boolean binary){
        arc.setBinary(binary);
    }

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

package com.vectorforce.controller;

import com.vectorforce.model.Arc;
import com.vectorforce.model.Graph;
import com.vectorforce.controller.common.OperationType;
import com.vectorforce.model.node.Node;

import java.io.File;
import java.util.LinkedList;

public class Controller {
    private final LinkedList<Graph> graphs;
    private final LinkedList<File> files;
    private int indexCurrentGraph;
    private OperationType operationType;

    public Controller() {
        indexCurrentGraph = 0;
        graphs = new LinkedList<>();
        files = new LinkedList<>();
        operationType = new OperationType();
    }

    public void buildFullGraph() {
        getCurrentGraph().buildFullGraph();
    }

    public void createGraph(String path) {
        final Graph graph = new Graph();
        graphs.add(graph);
        // Creating file for the graph
        final File file = new File(path);
        files.add(file);
        indexCurrentGraph = graphs.size() - 1;
    }

    public void deleteAllNodes() {
        getCurrentGraph().deleteAllNodes();
    }

    public void deleteGraph(Graph graph) {
        files.remove(files.get(graphs.indexOf(graph)));
        graphs.remove(graph);
    }

    public void addNode(Node node) {
        graphs.get(indexCurrentGraph).addNode(node);
    }

    public void deleteNode(Node node) {
        graphs.get(indexCurrentGraph).deleteNode(node);
    }

    public Node searchNode(String internalID) {
        return getCurrentGraph().searchNode(internalID);
    }

    public void addArc(Arc arc) {
        graphs.get(indexCurrentGraph).addArc(arc);
    }

    public void deleteArc(Arc arc) {
        graphs.get(indexCurrentGraph).deleteArc(arc);
    }

    public void removeSelection() {
        graphs.get(indexCurrentGraph).removeSelection();
    }

    public void changeDirection(Arc arc) {
        arc.changeDirection();
    }

    // Drawing methods


    // Setters
    public void setCurrentGraph(Graph graph) {
        for (Graph currentGraph : graphs) {
            if (currentGraph == graph) {
                indexCurrentGraph = graphs.indexOf(currentGraph);
            }
        }
    }

    public void setWeight(Arc arc, int weight) {
        arc.setWeight(weight);
    }

    public void setID(Node node, String ID) {
        node.setID(ID);
    }

    public void setID(Arc arc, String ID) {
        arc.setID(ID);
    }

    public void setOriented(Arc arc, boolean orientation) {
        arc.setOriented(orientation);
    }

    public void setBinary(Arc arc, boolean binary) {
        arc.setBinary(binary);
    }

    public void setStatus(OperationType.operationType status) {
        operationType.setStatus(status);
    }

    // Getters
    public LinkedList<File> getFiles() {
        return files;
    }

    public File getCurrentFile() {
        return files.get(indexCurrentGraph);
    }

    public Graph getCurrentGraph() {
        return graphs.get(indexCurrentGraph);
    }

    public OperationType.operationType getStatus() {
        return operationType.getStatus();
    }
}

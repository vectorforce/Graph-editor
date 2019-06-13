package com.vectorforce.Controller;

import com.vectorforce.Model.Arc;
import com.vectorforce.Model.Graph;
import com.vectorforce.Model.Vertex;

import java.util.LinkedList;

public class Controller {
    private LinkedList<Graph> graphs;
    private int indexCurrentGraph;

    public Controller(){
        indexCurrentGraph = 0;
        graphs = new LinkedList<>();
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

    // Drawing methods


    // Setters
    public void setIndexCurrentGraph(int indexCurrentGraph){
        this.indexCurrentGraph = indexCurrentGraph;
    }

    // Getters
    public int getIndexCurrentGraph(){
        return indexCurrentGraph;
    }
}

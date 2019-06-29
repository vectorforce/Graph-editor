package com.vectorforce.model;

public interface IGraph {
    void addNode(Node node);
    void deleteNode(Node node);
    void addArc(Arc arc);
    boolean deleteArc(Arc arc);
}

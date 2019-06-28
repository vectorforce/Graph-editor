package com.vectorforce.Model;

public interface IGraph {
    void addNode(Node node);
    void deleteNode(Node node);
    void addArc(Arc arc);
    boolean deleteArc(Arc arc);
}

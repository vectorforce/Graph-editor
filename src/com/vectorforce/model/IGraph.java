package com.vectorforce.model;

import com.vectorforce.model.node.Node;

public interface IGraph {
    void addNode(Node node);
    void deleteNode(Node node);
    void addArc(Arc arc);
    boolean deleteArc(Arc arc);
}

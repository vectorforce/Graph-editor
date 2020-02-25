package com.vectorforce.model.node;

public class NodeType {
    public enum nodeType {EMPTY, CLASS, NREL, RREL, LINK}
    private nodeType status;

    NodeType(){
        status = nodeType.EMPTY;
    }

    nodeType getStatus() {
        return status;
    }

    void setStatus(nodeType status) {
        this.status = status;
    }
}

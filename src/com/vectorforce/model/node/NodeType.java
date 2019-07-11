package com.vectorforce.model.node;

public class NodeType {
    public enum nodeType {EMPTY, CLASS, NREL, RREL, LINK}
    private nodeType status;

    public NodeType(){
        status = nodeType.EMPTY;
    }

    // Setters
    public void setStatus(nodeType status){
        this.status = status;
    }

    // Getters
    public nodeType getStatus(){
        return status;
    }
}

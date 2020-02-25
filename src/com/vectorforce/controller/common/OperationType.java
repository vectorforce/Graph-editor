package com.vectorforce.controller.common;

public class OperationType {
    public enum operationType {CURSOR, ARC}
    private operationType status;

    public OperationType(){
        status = operationType.CURSOR;
    }

    public operationType getStatus() {
        return status;
    }

    public void setStatus(operationType status) {
        this.status = status;
    }
}

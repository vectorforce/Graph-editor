package com.vectorforce.Controller.Operations;

public class OperationType {
    public enum operationType {CURSOR, ARC}
    private operationType status;

    public OperationType(){
        status = operationType.CURSOR;
    }

    // Setters
    public void setStatus(operationType status){
        this.status = status;
    }

    // Getters
    public operationType getStatus(){
        return status;
    }
}

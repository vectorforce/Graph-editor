package com.vectorforce.view.graphics;

// This class created for store selected object on graphic component
public class GraphicObject<Type> {
    private Type object;

    GraphicObject(){
        object = null;
    }

    // Setters
    void setObject(Type object){
        this.object = object;
    }

    // Getters
    public Type getObject() {
        return object;
    }
}

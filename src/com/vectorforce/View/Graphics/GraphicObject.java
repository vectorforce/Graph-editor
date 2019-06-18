package com.vectorforce.View.Graphics;

// This class created for store selected object on graphic component
public class GraphicObject<Type> {
    private Type object;

    public GraphicObject(){
        object = null;
    }

    // Setters
    public void setObject(Type object){
        this.object = object;
    }

    // Getters
    public Type getObject() {
        return object;
    }
}

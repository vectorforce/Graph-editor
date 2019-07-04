package com.vectorforce.model;

import org.eclipse.swt.graphics.Color;

public class GraphicalShell {
    private Color color;
    private Color selectColor;
    private Color deselectColor;
    private boolean isSelected;

    public GraphicalShell(){
        this.color = new Color(null, 0, 0, 0);
        this.selectColor = new Color(null, 0, 255, 0);
        this.deselectColor = color;
        isSelected = false;
    }

    public void select(boolean select) {
        this.isSelected = select;
        if (select == true) {
            color = selectColor;
        } else {
            color = deselectColor;
        }
    }

    // Setters
    public void setColor(Color color) {
        this.color = color;
        this.deselectColor = color;
    }

    public void setSelectColor(Color selectColor){
        this.selectColor = selectColor;
    }

    // Getters
    public boolean isSelected() {
        return isSelected;
    }

    public Color getColor() {
        return color;
    }
}

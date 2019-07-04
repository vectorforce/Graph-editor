package com.vectorforce.view.setup;

import org.eclipse.swt.graphics.Color;

public class ColorSetupComponent {
    private static Color nodeColor;
    private static Color arcColor;
    private static Color selectColor;
    private static Color characterColor;
    private static Color backgroundColor;
    private static Color gridColor;
    private static boolean darkTheme;

    public ColorSetupComponent(){
        setLightTheme();
    }

    // Setters
    public static void setDarkTheme(){
        darkTheme = true;
        nodeColor = new Color(null, 92, 248, 255);
        arcColor = new Color(null, 230, 243, 255);
        selectColor = new Color(null, 0, 255, 0);
        characterColor = new Color(null, 255, 255, 255);
        backgroundColor = new Color(null, 59, 63, 64);
        gridColor = new Color(null, 249, 245, 104);
    }

    public static void setLightTheme(){
        darkTheme = false;
        nodeColor = new Color(null, 0, 0, 0);
        arcColor = new Color(null, 0, 0, 0);
        selectColor = new Color(null, 0, 255, 0);
        characterColor = new Color(null, 0, 0, 0);
        backgroundColor = new Color(null, 255, 255, 255);
        gridColor = new Color(null, 0, 163, 20);
    }

    public static void setGridColor(Color gridColorArg){
        gridColor = gridColorArg;
    }

    public static void setCharacterColor(Color characterColorArg){
        characterColor = characterColorArg;
    }

    public static void setArcColor(Color arcColorArg) {
        arcColor = arcColorArg;
    }

    public static void setNodeColor(Color nodeColorArg) {
        nodeColor = nodeColorArg;
    }

    public static void setSelectColor(Color selectColorArg) {
        selectColor = selectColorArg;
    }

    public static void setBackgroundColor(Color backgroundColorArg) {
        backgroundColor = backgroundColorArg;
    }

    // Getters
    public static boolean isDarkTheme(){
        return darkTheme;
    }

    public static Color getNodeColor(){
        return nodeColor;
    }

    public static Color getArcColor(){
        return arcColor;
    }

    public static Color getSelectColor(){
        return selectColor;
    }

    public static Color getBackgroundColor(){
        return backgroundColor;
    }

    public static Color getCharacterColor(){
        return characterColor;
    }

    public static Color getGridColor(){
        return gridColor;
    }
}

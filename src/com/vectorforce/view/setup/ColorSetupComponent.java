package com.vectorforce.view.setup;

import org.eclipse.swt.graphics.Color;

public class ColorSetupComponent {
    private static Color nodeColor;
    private static Color arcColor;
    private static Color selectColor;
    private static Color graphicComponentCharacterColor;
    private static Color graphicComponentBackgroundColor;
    private static Color gridColor;
    private static boolean darkTheme;
    // Default colors
    private static Color tableHeaderBackgroundColor = new Color(null, 80, 80, 80);
    private static Color tableHeaderForegroundColor = new Color(null, 179, 179, 255);
    private static Color tabFolderSelectionForeground = new Color(null, 109, 174, 204);
    private static Color tabFolderForeground = new Color(null, 100, 100, 100);
    private static Color mainWindowsColor = new Color(null, 40, 40, 40);
    private static Color windowsCompositesForegroundColor = new Color(null, 55, 55, 55);
    private static Color buttonsForegroundColor = new Color(null, 199, 199, 199);
    private static Color textBackgroundColor = new Color(null, 233, 233, 233);
    private static Color defaultObjectColorLightTheme = new Color(null, 0, 0, 0);
    private static Color defaultNodeColorDarkTheme = new Color(null, 92, 248, 255);
    private static Color defaultArcColorDarkTheme = new Color(null, 230, 243, 255);

    public ColorSetupComponent() {
        setLightTheme();
    }

    // Setters
    public static void setDarkTheme() {
        darkTheme = true;
        nodeColor = defaultNodeColorDarkTheme;
        arcColor = defaultArcColorDarkTheme;
        selectColor = new Color(null, 0, 255, 0);
        graphicComponentCharacterColor = new Color(null, 255, 255, 255);
        graphicComponentBackgroundColor = new Color(null, 59, 63, 64);
        gridColor = new Color(null, 249, 245, 104);
    }

    public static void setLightTheme() {
        darkTheme = false;
        nodeColor = defaultObjectColorLightTheme;
        arcColor = defaultObjectColorLightTheme;
        selectColor = new Color(null, 0, 255, 0);
        graphicComponentCharacterColor = new Color(null, 0, 0, 0);
        graphicComponentBackgroundColor = new Color(null, 255, 255, 255);
        gridColor = new Color(null, 0, 163, 20);
    }

    public static void setGridColor(Color gridColorArg) {
        gridColor = gridColorArg;
    }

    public static void setGraphicComponentCharacterColor(Color characterColorArg) {
        graphicComponentCharacterColor = characterColorArg;
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

    public static void setGraphicComponentBackgroundColor(Color backgroundColorArg) {
        graphicComponentBackgroundColor = backgroundColorArg;
    }

    // Getters
    public static Color getTableHeaderForegroundColor(){
        return tableHeaderForegroundColor;
    }

    public static Color getTableHeaderBackgroundColor(){
        return tableHeaderBackgroundColor;
    }

    public static Color getTabFolderSelectionForeground(){
        return tabFolderSelectionForeground;
    }

    public static Color getTabFolderForeground(){
        return tabFolderForeground;
    }

    public static Color getTextBackgroundColor(){
        return textBackgroundColor;
    }

    public static Color getWindowsCompositesForegroundColor(){
        return windowsCompositesForegroundColor;
    }

    public static Color getButtonsForegroundColor(){
        return buttonsForegroundColor;
    }

    public static Color getMainWindowsColor(){
        return mainWindowsColor;
    }

    public static Color getDefaultObjectColorLightTheme() {
        return defaultObjectColorLightTheme;
    }

    public static Color getDefaultNodeColorDarkTheme() {
        return defaultNodeColorDarkTheme;
    }

    public static Color getDefaultArcColorDarkTheme() {
        return defaultArcColorDarkTheme;
    }

    public static boolean isDarkTheme() {
        return darkTheme;
    }

    public static Color getNodeColor() {
        return nodeColor;
    }

    public static Color getArcColor() {
        return arcColor;
    }

    public static Color getSelectColor() {
        return selectColor;
    }

    public static Color getGraphicComponentBackgroundColor() {
        return graphicComponentBackgroundColor;
    }

    public static Color getGraphicComponentCharacterColor() {
        return graphicComponentCharacterColor;
    }

    public static Color getGridColor() {
        return gridColor;
    }
}

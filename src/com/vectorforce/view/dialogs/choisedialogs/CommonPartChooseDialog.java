package com.vectorforce.view.dialogs.choisedialogs;

import com.vectorforce.controller.Controller;
import com.vectorforce.view.graphics.GraphicComponent;
import org.eclipse.swt.widgets.Button;

import java.util.ArrayList;

public class CommonPartChooseDialog {
    private final Controller controller;
    private final ArrayList<Button> buttons;
    private final GraphicComponent graphicComponent;

    CommonPartChooseDialog(Controller controller, GraphicComponent graphicComponent){
        this.controller = controller;
        this.graphicComponent = graphicComponent;
        buttons = new ArrayList<>();
    }

    void addButton(Button button){
        buttons.add(button);
    }

    void removeSelection() {
        for (Button currentButton : buttons) {
            currentButton.setSelection(false);
        }
    }

    // Getters
    ArrayList<Button> getButtons(){
        return buttons;
    }

    GraphicComponent getGraphicComponent(){
        return graphicComponent;
    }

    public Controller getController(){
        return controller;
    }
}

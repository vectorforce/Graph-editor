package com.vectorforce.view.dialogs.choisedialogs;

import com.vectorforce.controller.Controller;
import com.vectorforce.view.graphics.GraphicComponent;
import org.eclipse.swt.widgets.Button;

import java.util.ArrayList;

public class CommonPartChooseDialog {
    private Controller controller;
    private ArrayList<Button> buttons;
    private GraphicComponent graphicComponent;

    public CommonPartChooseDialog(Controller controller, GraphicComponent graphicComponent){
        this.controller = controller;
        this.graphicComponent = graphicComponent;
        buttons = new ArrayList<>();
    }

    public void addButton(Button button){
        buttons.add(button);
    }

    public void removeSelection() {
        for (Button currentButton : buttons) {
            currentButton.setSelection(false);
        }
    }

    // Getters
    public ArrayList<Button> getButtons(){
        return buttons;
    }

    public GraphicComponent getGraphicComponent(){
        return graphicComponent;
    }

    public Controller getController(){
        return controller;
    }
}

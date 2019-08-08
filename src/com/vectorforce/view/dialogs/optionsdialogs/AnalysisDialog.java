package com.vectorforce.view.dialogs.optionsdialogs;

import com.vectorforce.controller.Controller;
import com.vectorforce.view.setup.ColorSetupComponent;
import com.vectorforce.view.setup.FontSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

public class AnalysisDialog {
    private Display display;
    private Shell shell;
    private Controller controller;

    private String imagePath = System.getProperty("user.dir") + "\\src\\resources\\";

    public AnalysisDialog(Display display, Controller controller) {
        this.controller = controller;
        this.display = display;
        shell = new Shell(display, SWT.CLOSE | SWT.APPLICATION_MODAL);
        shell.setText("Анализ графа");
        shell.setImage(new Image(display, imagePath + "graphEditor.png"));
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());
        initForm();

        run();
    }

    private void run() {
        shell.pack();
        Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
        shell.open();

        while(shell.isDisposed() == false){
            if(display.readAndDispatch() == true){
                display.sleep();
            }
        }
    }

    private void initForm(){
        ArrayList<Label> labels = new ArrayList<>();

        Label labelID = new Label(shell, SWT.NONE);
        labels.add(labelID);
        String ID = controller.getCurrentGragh().getID();
        labelID.setText("ID графа: " + ID);

        Label labelCounterNodes = new Label(shell, SWT.NONE);
        labels.add(labelCounterNodes);
        String counterNodes = String.valueOf(controller.getCurrentGragh().getNodes().size());
        labelCounterNodes.setText("Количество узлов: " + counterNodes);

        Label labelCounterArcs = new Label(shell, SWT.NONE);
        labels.add(labelCounterArcs);
        String counterArcs = String.valueOf(controller.getCurrentGragh().getArcs().size());
        labelCounterArcs.setText("Количество дуг: " + counterArcs);

        Label labelIsOriented = new Label(shell, SWT.NONE);
        labels.add(labelIsOriented);
        String isOriented = "";
        if(controller.getCurrentGragh().isOriented() == true){
            isOriented = "да";
        } else {
            isOriented = "нет";
        }
        labelIsOriented.setText("Ориентированный: " + isOriented);

        Label labelIsMixed = new Label(shell, SWT.NONE);
        labels.add(labelIsMixed);
        String isMixed = "";
        if(controller.getCurrentGragh().isMixed() == true){
            isMixed = "да";
        } else {
            isMixed = "нет";
        }
        labelIsMixed.setText("Смешанный: " + isMixed);

        Label labelIsFull = new Label(shell, SWT.NONE);
        labels.add(labelIsFull);
        String isFull=  "";
        if(controller.getCurrentGragh().isFull() == true){
            isFull = "да";
        } else {
            isFull = "нет";
        }
        labelIsFull.setText("Полный: " + isFull);

        for(Label currentLabel : labels){
            currentLabel.setBackground(ColorSetupComponent.getMainWindowsColor());
            currentLabel.setForeground(ColorSetupComponent.getButtonsForegroundColor());
            currentLabel.setFont(FontSetupComponent.getLabelsFont());
        }
    }
}

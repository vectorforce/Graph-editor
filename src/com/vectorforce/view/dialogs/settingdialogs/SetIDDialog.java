package com.vectorforce.view.dialogs.settingdialogs;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import com.vectorforce.model.node.Node;
import com.vectorforce.view.graphics.GraphicObject;
import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class SetIDDialog {
    private Display display;
    private Shell shell;
    private Controller controller;
    private GraphicObject graphicObject;
    private SettingForm settingForm;

    private String imagePath = System.getProperty("user.dir") + "\\src\\resources\\";

    public SetIDDialog(Display display, Controller controller, GraphicObject graphicObject) {
        this.controller = controller;
        this.graphicObject = graphicObject;
        this.display = display;
        shell = new Shell(display, SWT.CLOSE | SWT.APPLICATION_MODAL);
        shell.setLayout(new GridLayout(1, false));
        shell.setText("Установите идентификатор");
        shell.setImage(new Image(display, imagePath + "graphEditor.png"));
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());
        initForm();

        run();
    }

    private void initForm() {
        settingForm = new SettingForm(shell);
        settingForm.setCompositeText("Новый идентификатор");
        settingForm.setButtonText("Установить");

        settingForm.getButton().addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String ID;
                if (settingForm.getText().getText().isEmpty() == true) {
                    ID = null;
                } else {
                    ID = settingForm.getText().getText();
                }
                if (graphicObject.getObject() instanceof Node) {
                    controller.setID((Node) graphicObject.getObject(), ID);
                } else if (graphicObject.getObject() instanceof Arc) {
                    controller.setID((Arc) graphicObject.getObject(), ID);
                }
                shell.close();
            }
        });
    }

    private void run() {
        shell.pack();
        Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
        shell.open();

        while (shell.isDisposed() == false) {
            if (display.readAndDispatch() == true) {
                display.sleep();
            }
        }
    }
}

package com.vectorforce.view.dialogs.settingdialogs;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class SetWeightDialog {
    private Display display;
    private Shell shell;

    private final Controller controller;
    private final Arc arc;

    private SettingForm settingForm;

    public SetWeightDialog(Display display, Controller controller, Arc arc) {
        this.controller = controller;
        this.display = display;
        this.arc = arc;
        shell = new Shell(display, SWT.CLOSE | SWT.APPLICATION_MODAL);
        shell.setText("Установить вес");
        final String imagePath = "src/resources/";
        shell.setImage(new Image(display, imagePath + "graph.png"));
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());
        initForm();

        run();
    }

    private void initForm() {
        settingForm = new SettingForm(shell);
        settingForm.setCompositeText("Вес ребра");
        settingForm.setButtonText("Установить");
        // Protection against incorrect input
        settingForm.getText().addListener(SWT.Verify, e -> {
            String string = e.text;
            char[] chars = new char[string.length()];
            string.getChars(0, chars.length, chars, 0);
            for (char aChar : chars) {
                if (!('0' <= aChar && aChar <= '9')) {
                    e.doit = false;
                    return;
                }
            }
        });

        settingForm.getButton().addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!settingForm.getText().getText().isEmpty()) {
                    if (Integer.valueOf(settingForm.getText().getText()) != 0) {
                        controller.setWeight(arc, Integer.valueOf(settingForm.getText().getText()));
                    }
                }
                shell.close();
            }
        });
    }

    private void run() {
        shell.pack();
        final Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
        shell.open();

        while (!shell.isDisposed()) {
            if (display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}

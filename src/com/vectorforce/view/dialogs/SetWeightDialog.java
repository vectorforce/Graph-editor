package com.vectorforce.view.dialogs;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import java.util.Set;

public class SetWeightDialog {
    private Display display;
    private Shell shell;
    private Controller controller;
    private Arc arc;
    private SettingForm settingForm;
    private int weight;

    public SetWeightDialog(Display display, Controller controller, Arc arc) {
        this.controller = controller;
        this.display = display;
        this.arc = arc;
        shell = new Shell(display, SWT.CLOSE | SWT.APPLICATION_MODAL);
        shell.setLayout(new GridLayout(1, false));
        initForm();

        run();
    }

    private void initForm() {
        settingForm = new SettingForm(shell);
        settingForm.setCompositeText("Вес ребра");
        settingForm.setButtonText("Установить");
        // Protection against incorrect input
        settingForm.getText().addListener(SWT.Verify, new Listener() {
            public void handleEvent(Event e) {
                String string = e.text;
                char[] chars = new char[string.length()];
                string.getChars(0, chars.length, chars, 0);
                for (int i = 0; i < chars.length; i++) {
                    if (!('0' <= chars[i] && chars[i] <= '9') == true) {
                        e.doit = false;
                        return;
                    }
                }
            }
        });

        settingForm.getButton().addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (settingForm.getText().getText().isEmpty() == false) {
                    controller.setWeight(arc, Integer.valueOf(settingForm.getText().getText()));
                }
                shell.close();
            }
        });
    }

    private void run() {
        shell.pack();
        shell.open();

        while (shell.isDisposed() == false) {
            if (display.readAndDispatch() == true) {
                display.sleep();
            }
        }
    }
}

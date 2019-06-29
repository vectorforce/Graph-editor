package com.vectorforce.view.dialogs.choose;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import com.vectorforce.view.graphics.GraphicComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;

public class ChooseArc {
    private Display display;
    private Shell shell;
    private Controller controller;
    private Arc arc;
    private ArrayList<Button> buttons;
    private GraphicComponent graphicComponent;

    public ChooseArc(Display display, Controller controller, Arc arc, GraphicComponent graphicComponent) {
        this.graphicComponent = graphicComponent;
        buttons = new ArrayList<>();
        this.controller = controller;
        this.arc = arc;
        this.display = display;
        shell = new Shell(display, SWT.CLOSE | SWT.APPLICATION_MODAL);
        shell.setText("Выберите тип");
        shell.setLayout(new GridLayout(1, true));
        initButtons();

        run();
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

    private void removeSelection() {
        for (Button currentButton : buttons) {
            currentButton.setSelection(false);
        }
    }

    private void initButtons() {
        Composite compositeTypesButtons = new Composite(shell, SWT.NONE);
        compositeTypesButtons.setLayout(new GridLayout(2, true));

        Composite compositeOriented = new Group(compositeTypesButtons, SWT.NONE);
        ((Group) compositeOriented).setText("Ориентированная");
        compositeOriented.setLayout(new GridLayout(1, false));

        Button buttonOriented = new Button(compositeOriented, SWT.TOGGLE);
        buttons.add(buttonOriented);
        buttonOriented.setText("-------->");
        Button buttonOrientedBinary = new Button(compositeOriented, SWT.TOGGLE);
        buttons.add(buttonOrientedBinary);
        buttonOrientedBinary.setText("========>");

        Composite compositeNonOriented = new Group(compositeTypesButtons, SWT.NONE);
        ((Group) compositeNonOriented).setText("Неориентированная");
        compositeNonOriented.setLayout(new GridLayout(1, true));

        Button buttonNonOriented = new Button(compositeNonOriented, SWT.TOGGLE);
        buttons.add(buttonNonOriented);
        buttonNonOriented.setText("---------");
        Button buttonNonOrientedBinary = new Button(compositeNonOriented, SWT.TOGGLE);
        buttons.add(buttonNonOrientedBinary);
        buttonNonOrientedBinary.setText("========");

        Composite compositeOK = new Composite(shell, SWT.NONE);
        compositeOK.setLayout(new GridLayout(1, false));
        compositeOK.setLayoutData(new GridData(SWT.END, SWT.FILL, true, true));

        Button buttonOK = new Button(compositeOK, SWT.PUSH);
        buttonOK.setText("Выбрать");
        GridData buttonOKData = new GridData(SWT.END, SWT.CENTER, false, false);
        buttonOKData.widthHint = 120;
        buttonOKData.heightHint = 40;
        buttonOK.setLayoutData(buttonOKData);

        // Listeners
        buttonOriented.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelection();
                buttonOriented.setSelection(true);
            }
        });

        buttonOrientedBinary.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelection();
                buttonOrientedBinary.setSelection(true);
            }
        });

        buttonNonOriented.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelection();
                buttonNonOriented.setSelection(true);
            }
        });

        buttonNonOrientedBinary.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelection();
                buttonNonOrientedBinary.setSelection(true);
            }
        });

        buttonOK.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (Button currentButton : buttons) {
                    if (currentButton.getSelection() == true) {
                        if (currentButton == buttonOriented) {
                            controller.setOriented(arc, true);
                            controller.setBinary(arc, false);
                            break;
                        } else if (currentButton == buttonNonOriented) {
                            controller.setOriented(arc, false);
                            controller.setBinary(arc, false);
                            break;
                        } else if (currentButton == buttonOrientedBinary) {
                            controller.setOriented(arc, true);
                            controller.setBinary(arc, true);
                            break;
                        } else if (currentButton == buttonNonOrientedBinary) {
                            controller.setOriented(arc, false);
                            controller.setBinary(arc, true);
                            break;
                        }
                    }
                }
                graphicComponent.redraw();
                shell.close();
            }
        });
    }
}

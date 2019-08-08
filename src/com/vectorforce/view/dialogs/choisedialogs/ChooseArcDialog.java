package com.vectorforce.view.dialogs.choisedialogs;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import com.vectorforce.view.graphics.GraphicComponent;
import com.vectorforce.view.setup.ColorSetupComponent;
import com.vectorforce.view.setup.FontSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class ChooseArcDialog {
    private Display display;
    private Shell shell;
    private CommonPartChooseDialog commonPartChooseDialog;
    private Arc arc;

    private String imagePath = System.getProperty("user.dir") + "\\src\\resources\\";

    public ChooseArcDialog(Display display, Controller controller,  GraphicComponent graphicComponent, Arc arc) {
        commonPartChooseDialog = new CommonPartChooseDialog(controller, graphicComponent);
        this.arc = arc;
        this.display = display;
        shell = new Shell(display, SWT.CLOSE | SWT.APPLICATION_MODAL);
        shell.setText("Выберите тип");
        shell.setImage(new Image(display, imagePath + "graphEditor.png"));
        shell.setLayout(new GridLayout(1, true));
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());
        initButtons();

        run();
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

    private void initButtons() {
        Composite compositeTypesButtons = new Composite(shell, SWT.NONE);
        compositeTypesButtons.setLayout(new GridLayout(2, true));
        compositeTypesButtons.setBackground(ColorSetupComponent.getWindowsCompositesForegroundColor());

        Composite compositeOriented = new Group(compositeTypesButtons, SWT.NONE);
        compositeOriented.setBackground(ColorSetupComponent.getWindowsCompositesForegroundColor());
        compositeOriented.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        ((Group) compositeOriented).setText("Ориентированная");
        compositeOriented.setLayout(new GridLayout(1, false));

        Button buttonOriented = new Button(compositeOriented, SWT.TOGGLE);
        commonPartChooseDialog.addButton(buttonOriented);
        buttonOriented.setText("-------->");
        Button buttonOrientedBinary = new Button(compositeOriented, SWT.TOGGLE);
        commonPartChooseDialog.addButton(buttonOrientedBinary);
        buttonOrientedBinary.setText("========>");

        Composite compositeNonOriented = new Group(compositeTypesButtons, SWT.NONE);
        compositeNonOriented.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        compositeNonOriented.setBackground(ColorSetupComponent.getWindowsCompositesForegroundColor());
        ((Group) compositeNonOriented).setText("Неориентированная");
        compositeNonOriented.setLayout(new GridLayout(1, true));

        Button buttonNonOriented = new Button(compositeNonOriented, SWT.TOGGLE);
        commonPartChooseDialog.addButton(buttonNonOriented);
        buttonNonOriented.setText("---------");
        Button buttonNonOrientedBinary = new Button(compositeNonOriented, SWT.TOGGLE);
        commonPartChooseDialog.addButton(buttonNonOrientedBinary);
        buttonNonOrientedBinary.setText("========");

        Composite compositeOK = new Composite(shell, SWT.NONE);
        compositeOK.setBackground(ColorSetupComponent.getMainWindowsColor());
        compositeOK.setLayout(new GridLayout(1, false));
        compositeOK.setLayoutData(new GridData(SWT.END, SWT.FILL, true, true));

        Button buttonOK = new Button(compositeOK, SWT.PUSH | SWT.BORDER);
        buttonOK.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonOK.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonOK.setFont(FontSetupComponent.getButtonsFont());
        buttonOK.setText("Выбрать");
        GridData buttonOKData = new GridData(SWT.END, SWT.CENTER, false, false);
        buttonOKData.widthHint = 120;
        buttonOKData.heightHint = 40;
        buttonOK.setLayoutData(buttonOKData);

        // Listeners
        buttonOriented.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                commonPartChooseDialog.removeSelection();
                buttonOriented.setSelection(true);
            }
        });

        buttonOrientedBinary.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                commonPartChooseDialog.removeSelection();
                buttonOrientedBinary.setSelection(true);
            }
        });

        buttonNonOriented.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                commonPartChooseDialog.removeSelection();
                buttonNonOriented.setSelection(true);
            }
        });

        buttonNonOrientedBinary.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                commonPartChooseDialog.removeSelection();
                buttonNonOrientedBinary.setSelection(true);
            }
        });

        buttonOK.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (Button currentButton : commonPartChooseDialog.getButtons()) {
                    if (currentButton.getSelection() == true) {
                        if (currentButton == buttonOriented) {
                            commonPartChooseDialog.getController().setOriented(arc, true);
                            commonPartChooseDialog.getController().setBinary(arc, false);
                            break;
                        } else if (currentButton == buttonNonOriented) {
                            commonPartChooseDialog.getController().setOriented(arc, false);
                            commonPartChooseDialog.getController().setBinary(arc, false);
                            break;
                        } else if (currentButton == buttonOrientedBinary) {
                            commonPartChooseDialog.getController().setOriented(arc, true);
                            commonPartChooseDialog.getController().setBinary(arc, true);
                            break;
                        } else if (currentButton == buttonNonOrientedBinary) {
                            commonPartChooseDialog.getController().setOriented(arc, false);
                            commonPartChooseDialog.getController().setBinary(arc, true);
                            break;
                        }
                    }
                }
                commonPartChooseDialog.getGraphicComponent().redraw();
                shell.close();
            }
        });
    }
}

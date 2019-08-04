package com.vectorforce.view.dialogs.generategraph;

import com.vectorforce.controller.Controller;
import com.vectorforce.view.graphics.GraphicComponent;
import com.vectorforce.view.setup.ColorSetupComponent;
import com.vectorforce.view.setup.FontSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class BuildGraphDialog {
    private Display display;
    private Shell shell;

    private Controller controller;
    private GraphicComponent graphicComponent;
    private Combo[] combos;

    public BuildGraphDialog(Display display, Controller controller, GraphicComponent graphicComponent) {
        this.controller = controller;
        this.graphicComponent = graphicComponent;
        this.display = display;
        shell = new Shell(display, SWT.CLOSE | SWT.APPLICATION_MODAL);
        shell.setText("Генерация графа");
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());

        initCombos();
        initButtonNext();

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

    private void initCombos() {
        combos = new Combo[2];

        Composite compositeMatrixCombo = new Group(shell, SWT.NONE);
        compositeMatrixCombo.setLayout(new GridLayout(1, false));
        compositeMatrixCombo.setBackground(ColorSetupComponent.getWindowsCompositesForegroundColor());
        compositeMatrixCombo.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        ((Group) compositeMatrixCombo).setText("Задать матрицей");
        Combo matrixCombo = new Combo(compositeMatrixCombo, SWT.DROP_DOWN | SWT.READ_ONLY);
        combos[0] = matrixCombo;
        matrixCombo.setText("Выберите матрицу");
        // Combo items
        String itemsMatrix[] = {"Матрица смежности", "Матрица инцидентности"};
        matrixCombo.setItems(itemsMatrix);

        Composite compositeListsCombo = new Group(shell, SWT.NONE);
        compositeListsCombo.setLayout(new GridLayout(1, false));
        compositeListsCombo.setBackground(ColorSetupComponent.getWindowsCompositesForegroundColor());
        compositeListsCombo.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        ((Group) compositeListsCombo).setText("Задать списком");
        GridData gridDataComboList = new GridData(SWT.FILL, SWT.FILL, true, true);
        compositeListsCombo.setLayoutData(gridDataComboList);
        Combo listCombo = new Combo(compositeListsCombo, SWT.DROP_DOWN | SWT.READ_ONLY);
        combos[1] = listCombo;
        listCombo.setText("Выберите список");
        listCombo.setLayoutData(gridDataComboList);
        // Combo items
        String itemsList[] = {"Список смежности"};
        listCombo.setItems(itemsList);

        // Listeners
        matrixCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                listCombo.deselectAll();
            }
        });

        listCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                matrixCombo.deselectAll();
            }
        });

    }

    private void initButtonNext() {
        Button button = new Button(shell, SWT.PUSH | SWT.BORDER);
        button.setBackground(ColorSetupComponent.getMainWindowsColor());
        button.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        button.setFont(FontSetupComponent.getButtonsFont());
        button.setText("Далее");
        GridData buttonGenerateGraphData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        buttonGenerateGraphData.widthHint = 100;
        buttonGenerateGraphData.heightHint = 45;
        button.setLayoutData(buttonGenerateGraphData);

        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (int index = 0; index < combos.length; index++) {
                    String stringComboText = combos[index].getText();
                    if (!stringComboText.equals("")) {
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                        messageBox.setMessage("При продолжении текущий граф будет удален.\nПродолжить?");
                        int buttonID = messageBox.open();
                        switch (buttonID) {
                            case SWT.YES:
                                controller.deleteAllNodes();
                                switch (stringComboText) {
                                    case "Матрица смежности":
                                        shell.close();
                                        new BuildGraphAdjMatrixListDialog(controller, graphicComponent, 1);
                                        return;

                                    case "Матрица инцидентности":
                                        shell.close();
                                        new BuildGraphIncMatrixDialog(controller, graphicComponent);
                                        return;

                                    case "Список смежности":
                                        shell.close();
                                        new BuildGraphAdjMatrixListDialog(controller, graphicComponent, 2);
                                        return;

                                    default:
                                        return;
                                }

                            case SWT.NO:
                                return;
                        }
                    }
                }
            }
        });
    }
}

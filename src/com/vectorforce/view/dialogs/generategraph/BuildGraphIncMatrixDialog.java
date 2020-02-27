package com.vectorforce.view.dialogs.generategraph;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import com.vectorforce.model.node.Node;
import com.vectorforce.view.dialogs.optionsdialogs.algorithmdialogs.GraphTable;
import com.vectorforce.view.graphics.GraphicComponent;
import com.vectorforce.view.setup.ColorSetupComponent;
import com.vectorforce.view.setup.FontSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Random;

class BuildGraphIncMatrixDialog {
    private Display display;
    private Shell shell;

    private final Controller controller;
    private GraphTable table;
    private final GraphicComponent graphicComponent;
    private int arcCounter = 0;


    BuildGraphIncMatrixDialog(Controller controller, GraphicComponent graphicComponent) {
        this.controller = controller;
        this.graphicComponent = graphicComponent;
        this.display = Display.getCurrent();
        shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.MAX | SWT.MIN);
        shell.setText("Задать граф");
        final String imagePath = "src/resources/";
        shell.setImage(new Image(display, imagePath + "graph.png"));
        shell.setSize(1044, 768);
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());
        shell.setLayout(new GridLayout(1, false));

        initTable();
        initButtons();

        run();
    }

    private void run() {
        shell.open();
        Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);

        while (!(shell.isDisposed())) {
            if (display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void addCellsEditor(TableItem item, TableEditor editor, int indexNodes) {
        // Clean up any previous editor control
        Control oldEditor = editor.getEditor();
        if (oldEditor != null) {
            oldEditor.dispose();
        }
        // Identify the selected row
        if (item == null) {
            return;
        }
        Text newEditor = new Text(table.getTable(), SWT.NONE);
        Listener textListener = e -> {
            switch (e.type) {
                case SWT.FocusOut:
                    item.setText(indexNodes, newEditor.getText());
                    newEditor.dispose();
                    break;
                case SWT.Traverse:
                    switch (e.detail) {
                        case SWT.TRAVERSE_RETURN:
                            item.setText(indexNodes, newEditor.getText());
                            // FALL THROUGH
                        case SWT.TRAVERSE_ESCAPE:
                            newEditor.dispose();
                            e.doit = false;
                    }
                    break;
            }
        };
        newEditor.addListener(SWT.FocusOut, textListener);
        newEditor.addListener(SWT.Traverse, textListener);
        // Protection against incorrect input
        newEditor.addListener(SWT.Verify, e -> {
            String string = e.text;
            char[] chars = new char[string.length()];
            string.getChars(0, chars.length, chars, 0);
            for (char aChar : chars) {
                if (!('0' <= aChar && aChar <= '9' || aChar == '-')) {
                    e.doit = false;
                    return;
                }
            }
        });
        newEditor.setText(item.getText(indexNodes));
        newEditor.addModifyListener(me -> {
            Text text = (Text) editor.getEditor();
            if (text.getText().equals("") || text.getText() == null) {
                editor.getItem().setText(indexNodes, "0");
            } else {
                editor.getItem().setText(indexNodes, text.getText());
            }
        });
        newEditor.selectAll();
        newEditor.setFocus();
        editor.setEditor(newEditor, item, indexNodes);
    }

    private void addNodes() {
        int nodesXCounter = 0;
        int nodesYCounter = 0;
        int startX = 100;
        int startY = 50;
        int stepX = 100;
        int stepY = 150;
        for (TableItem currentItem : table.getTable().getItems()) {
            if (nodesXCounter == 17) {
                nodesXCounter = 0;
                nodesYCounter++;
            }
            final Node node = new Node((startX + nodesXCounter++ * stepX), (startY + nodesYCounter * stepY));
            node.setInternalID(currentItem.getText(0));
            controller.addNode(node);
            graphicComponent.drawNode(node);
        }
    }

    // Initialization methods
    private void initTable() {
        table = new GraphTable(shell);
        table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

        table.addColumn("");
        initTableEditor();
    }

    private void initTableEditor() {
        TableEditor editor = new TableEditor(table.getTable());
        editor.horizontalAlignment = SWT.CENTER;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;

        // Set editor for table
        table.getTable().addListener(SWT.MouseDown, event -> {
            final Rectangle clientArea = table.getTable().getClientArea();
            final Point point = new Point(event.x, event.y);
            int index = table.getTable().getTopIndex();
            while (index < table.getTable().getItemCount()) {
                boolean visible = false;
                TableItem item = table.getTable().getItem(index);
                for (int indexNodes = 0; indexNodes < table.getTable().getColumnCount(); indexNodes++) {
                    final Rectangle rect = item.getBounds(indexNodes);
                    if (rect.contains(point)) {
                        if (indexNodes != 0) {
                            addCellsEditor(item, editor, indexNodes);
                        }
                    }
                    if (!visible && rect.intersects(clientArea)) {
                        visible = true;
                    }
                }
                if (!visible)
                    return;
                index++;
            }
        });
    }

    private void initButtons() {
        Composite compositeButtons = new Composite(shell, SWT.NONE);
        compositeButtons.setLayout(new GridLayout(5, false));
        compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        compositeButtons.setBackground(ColorSetupComponent.getMainWindowsColor());

        Button buttonAddNode = new Button(compositeButtons, SWT.PUSH);
        buttonAddNode.setText("Добавить узел");
        buttonAddNode.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonAddNode.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonAddNode.setFont(FontSetupComponent.getButtonsFont());
        int width = 200;
        int height = 60;
        GridData buttonAddNodeData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        buttonAddNodeData.widthHint = width;
        buttonAddNodeData.heightHint = height;
        buttonAddNode.setLayoutData(buttonAddNodeData);

        buttonAddNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String[] strings = new String[table.getTable().getColumnCount()];
                for (int index = 0; index < strings.length; index++) {
                    if (index == 0) {
                        strings[index] = String.valueOf(new Random().nextInt(1 + 125000));
                    } else {
                        strings[index] = "0";
                    }
                }
                table.addItem(strings);
            }
        });

        Button buttonAddArc = new Button(compositeButtons, SWT.PUSH);
        buttonAddArc.setText("Добавить дугу");
        buttonAddArc.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonAddArc.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonAddArc.setFont(FontSetupComponent.getButtonsFont());
        buttonAddArc.setLayoutData(buttonAddNodeData);

        buttonAddArc.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String ID = "arc" + String.valueOf(arcCounter++);
                table.addColumn(ID);
                for (int index = 0; index < table.getTable().getItemCount(); index++) {
                    if (table.getTable().getItemCount() > 0 && index < table.getTable().getItemCount()) {
                        // Filling new empty cells of previous items
                        table.getTable().getItem(index).setText(table.getTable().getColumnCount() - 1, "0");
                    }
                }
            }
        });

        Button buttonDeleteNode = new Button(compositeButtons, SWT.PUSH);
        buttonDeleteNode.setText("Удалить узел");
        buttonDeleteNode.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonDeleteNode.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonDeleteNode.setFont(FontSetupComponent.getButtonsFont());
        buttonDeleteNode.setLayoutData(buttonAddNodeData);

        buttonDeleteNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (table.getTable().getItemCount() > 0) {
                    table.getTable().getItem(table.getTable().getItemCount() - 1).dispose();
                }
            }
        });

        Button buttonDeleteArc = new Button(compositeButtons, SWT.PUSH);
        buttonDeleteArc.setText("Удалить дугу");
        buttonDeleteArc.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonDeleteArc.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonDeleteArc.setFont(FontSetupComponent.getButtonsFont());
        buttonDeleteArc.setLayoutData(buttonAddNodeData);

        buttonDeleteArc.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (table.getTable().getColumnCount() > 1) {
                    table.getTable().getColumn(table.getTable().getColumnCount() - 1).dispose();
                    arcCounter--;
                }
            }
        });

        Button buttonBuildGraph = new Button(compositeButtons, SWT.PUSH);
        buttonBuildGraph.setText("Построить граф");
        buttonBuildGraph.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonBuildGraph.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonBuildGraph.setFont(FontSetupComponent.getButtonsFont());
        buttonBuildGraph.setLayoutData(buttonAddNodeData);

        buttonBuildGraph.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addNodes();
                int weight = 0;
                for (int indexColumn = 1; indexColumn < table.getTable().getColumnCount(); indexColumn++) {
                    Node fromNode = null;
                    Node toNode = null;
                    for (int indexRow = 0; indexRow < table.getTable().getItemCount(); indexRow++) {
                        String string = table.getTable().getItem(indexRow).getText(indexColumn);
                        if (!string.equals("0")) {
                            if (string.substring(0, 1).equals("-")) {
                                fromNode = controller.searchNode(table.getTable().getItem(indexRow).getText(0));
                            } else {
                                final String[] strings = string.split("-");
                                final StringBuilder stringBuilder = new StringBuilder();
                                for (String currentString : strings) {
                                    stringBuilder.append(currentString);
                                }
                                string = stringBuilder.toString();
                                toNode = controller.searchNode(table.getTable().getItem(indexRow).getText(0));
                                weight = Integer.valueOf(string);
                            }
                        }
                    }
                    if(fromNode != null && toNode != null){
                        final Arc arc = new Arc(fromNode, toNode);
                        controller.addArc(arc);
                        arc.setWeight(weight);
                        graphicComponent.drawArc(arc);
                    }
                }
                graphicComponent.redraw();
                shell.close();
            }
        });
    }
}

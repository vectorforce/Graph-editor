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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Random;

class BuildGraphAdjMatrixListDialog {
    private Display display;
    private Shell shell;

    private GraphTable table;
    private Controller controller;
    private GraphicComponent graphicComponent;
    private final int windowType;
    // Main buttons
    private Button buttonAddNode;
    private Button buttonDeleteNode;
    private Button buttonBuildGraph;

    BuildGraphAdjMatrixListDialog(final Controller controller, final GraphicComponent graphicComponent, final int windowType) {
        this.windowType = windowType;
        if (windowType != 1 && windowType != 2) {
            return;
        }
        this.controller = controller;
        this.graphicComponent = graphicComponent;
        display = Display.getCurrent();
        shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.MAX | SWT.MIN);
        shell.setText("Задать граф");
        final String imagePath = "src/resources/";
        shell.setImage(new Image(display, imagePath + "graph.png"));
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());
        if (windowType == 1) {
            shell.setSize(1024, 768);
            initMatrixTable();
            initButtons();
            initMatrixButtonsListeners();
        } else {
            shell.setSize(680, 550);
            initListTable();
            initButtons();
            initListButtonsListeners();
        }
        run();
    }

    private void run() {
        shell.open();
        final Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);

        while (!shell.isDisposed()) {
            if (display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void addCellsEditor(TableItem item, TableEditor editor, int indexNodes) {
        // Clean up any previous editor control
        final Control oldEditor = editor.getEditor();
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
            final char[] chars = new char[string.length()];
            string.getChars(0, chars.length, chars, 0);
            for (char aChar : chars) {
                if (windowType == 1) {
                    if (!('0' <= aChar && aChar <= '9')) {
                        e.doit = false;
                        return;
                    }
                } else if (windowType == 2) {
                    if (!('0' <= aChar && aChar <= '9' || aChar == ' ')) {
                        e.doit = false;
                        return;
                    }
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
    private void initTableEditor() {
        final TableEditor editor = new TableEditor(table.getTable());
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
                            if (windowType == 1) {
                                if (indexNodes != index + 1) {
                                    addCellsEditor(item, editor, indexNodes);
                                }
                            } else if (windowType == 2) {
                                addCellsEditor(item, editor, indexNodes);
                            }
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

    private void initListTable() {
        Composite composite = new Composite(shell, SWT.NONE);
        composite.setBackground(ColorSetupComponent.getMainWindowsColor());
        composite.setLayout(new FillLayout());
        table = new GraphTable(shell);
        table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        // Adding main columns
        table.addColumn("Узел");
        table.addColumn("Смежные узлы", 520);

        initTableEditor();
    }

    private void initMatrixTable() {
        Composite composite = new Composite(shell, SWT.NONE);
        composite.setBackground(ColorSetupComponent.getMainWindowsColor());
        composite.setLayout(new FillLayout());
        table = new GraphTable(shell);
        table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        // Adding empty first cell
        table.addColumn("");

        initTableEditor();
    }

    private void initButtons() {
        Composite compositeButtons = new Composite(shell, SWT.NONE);
        compositeButtons.setLayout(new GridLayout(3, false));
        compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        compositeButtons.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonAddNode = new Button(compositeButtons, SWT.PUSH);
        buttonAddNode.setText("Добавить узел");
        buttonAddNode.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonAddNode.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonAddNode.setFont(FontSetupComponent.getButtonsFont());
        // GridData buttonAddNodeData
        int width = 200;
        int height = 60;
        GridData buttonAddNodeData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        buttonAddNodeData.widthHint = width;
        buttonAddNodeData.heightHint = height;
        buttonAddNode.setLayoutData(buttonAddNodeData);

        buttonDeleteNode = new Button(compositeButtons, SWT.PUSH);
        buttonDeleteNode.setText("Удалить узел");
        buttonDeleteNode.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonDeleteNode.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonDeleteNode.setFont(FontSetupComponent.getButtonsFont());
        // GridData buttonGenerateData
        GridData buttonDeleteNodeData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        buttonDeleteNodeData.widthHint = width;
        buttonDeleteNodeData.heightHint = height;
        buttonDeleteNode.setLayoutData(buttonDeleteNodeData);

        buttonBuildGraph = new Button(compositeButtons, SWT.PUSH);
        buttonBuildGraph.setText("Построить граф");
        buttonBuildGraph.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonBuildGraph.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonBuildGraph.setFont(FontSetupComponent.getButtonsFont());
        // GridData buttonGenerateData
        GridData buttonGenerateData = new GridData(SWT.END, SWT.CENTER, true, false);
        buttonGenerateData.widthHint = width;
        buttonGenerateData.heightHint = height;
        buttonBuildGraph.setLayoutData(buttonGenerateData);
    }

    private void initListButtonsListeners() {
        // Listeners
        buttonAddNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                final String[] strings = new String[2];
                // Generating random node
                strings[0] = String.valueOf(new Random().nextInt(1 + 125000));
                strings[1] = "";
                table.addItem(strings);
                table.getTable().update();
            }
        });

        buttonDeleteNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (table.getTable().getItemCount() > 0) {
                    table.getTable().getItem(table.getTable().getItemCount() - 1).dispose();
                    table.getTable().update();
                }
            }
        });

        buttonBuildGraph.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addNodes();
                for (TableItem currentItem : table.getTable().getItems()) {
                    final String[] nodes = currentItem.getText(1).split(" ");
                    final Node fromNode = controller.searchNode(currentItem.getText(0));
                    for (final String node : nodes) {
                        final Node toNode = controller.searchNode(node);
                        if (toNode != null && fromNode != null) {
                            final Arc arc = new Arc(fromNode, toNode);
                            if (arc.getID() != null) {
                                controller.addArc(arc);
                                graphicComponent.drawArc(arc);
                            }
                        }
                    }
                }
                graphicComponent.redraw();
                shell.close();
            }
        });
    }

    private void initMatrixButtonsListeners() {
        // Listeners
        buttonAddNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Generating internal ID of node
                final String stringID = String.valueOf(new Random().nextInt(1 + 125000));
                table.addColumn(stringID);
                // Filling the cells
                String[] strings = new String[table.getTable().getColumnCount()];
                for (int index = 0; index < table.getTable().getColumnCount(); index++) {
                    if (table.getTable().getItemCount() > 0 && index < table.getTable().getItemCount()) {
                        // Filling new empty cells of previous items
                        table.getTable().getItem(index).setText(table.getTable().getColumnCount() - 1, "0");
                    }
                    if (index == 0) {
                        strings[index] = stringID;
                    } else {
                        strings[index] = "0";
                    }
                }
                table.addItem(strings);
                table.getTable().update();
            }
        });

        buttonDeleteNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (table.getTable().getColumnCount() < 2) {
                    return;
                } else {
                    // Removing node(removing last column and last item from table)
                    table.getTable().deselectAll();
                    table.getTable().getColumn(table.getTable().getColumnCount() - 1).dispose();
                    table.getTable().getItem(table.getTable().getItemCount() - 1).dispose();
                    table.getTable().update();
                }
            }
        });

        buttonBuildGraph.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addNodes();
                for (int indexItem = 0; indexItem < table.getTable().getItemCount(); indexItem++) {
                    for (int indexColumn = 1; indexColumn < table.getTable().getColumnCount(); indexColumn++) {
                        final String weight = table.getTable().getItem(indexItem).getText(indexColumn);
                        if (!weight.equals("0")) {
                            final Node fromNode = controller.searchNode(table.getTable().getItem(indexItem).getText(0));
                            final Node toNode = controller.searchNode(table.getTable().getItem(indexColumn - 1).getText(0));
                            if (fromNode != null && toNode != null) {
                                final Arc arc = new Arc(fromNode, toNode);
                                controller.addArc(arc);
                                arc.setWeight(Integer.valueOf(weight));
                                graphicComponent.drawArc(arc);
                            }
                        }
                    }
                }
                graphicComponent.redraw();
                shell.close();
            }
        });
    }
}

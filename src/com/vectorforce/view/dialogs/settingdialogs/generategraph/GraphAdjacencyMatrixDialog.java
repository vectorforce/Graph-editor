package com.vectorforce.view.dialogs.settingdialogs.generategraph;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import com.vectorforce.model.node.Node;
import com.vectorforce.view.dialogs.optionsdialogs.algorithmdialogs.MatrixTable;
import com.vectorforce.view.graphics.GraphicComponent;
import com.vectorforce.view.setup.ColorSetupComponent;
import com.vectorforce.view.setup.FontSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Random;

public class GraphAdjacencyMatrixDialog {
    private Display display;
    private Shell shell;
    private MatrixTable table;
    private Controller controller;
    private GraphicComponent graphicComponent;

    public GraphAdjacencyMatrixDialog(Controller controller, GraphicComponent graphicComponent) {
        this.controller = controller;
        this.graphicComponent = graphicComponent;
        display = Display.getCurrent();
        shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.MAX | SWT.MIN);
        shell.setSize(1024, 768);
        shell.setText("Задать граф");
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());

        initTable();
        initButtonAddNode();
        run();
    }

    private void run() {
        shell.open();
        Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);

        while (shell.isDisposed() == false) {
            if (display.readAndDispatch() == true) {
                display.sleep();
            }
        }
    }

    private void initTable() {
        Composite composite = new Composite(shell, SWT.NONE);
        composite.setBackground(ColorSetupComponent.getMainWindowsColor());
        composite.setLayout(new FillLayout());
        table = new MatrixTable(shell);
        table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        // Adding empty first cell
        table.addColumn("");

        TableEditor editor = new TableEditor(table.getTable());
        editor.horizontalAlignment = SWT.CENTER;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;

        // Set editor for table
        table.getTable().addListener(SWT.MouseDown, new Listener() {
            public void handleEvent(Event event) {
                Rectangle clientArea = table.getTable().getClientArea();
                Point point = new Point(event.x, event.y);
                int index = table.getTable().getTopIndex();
                while (index < table.getTable().getItemCount()) {
                    boolean visible = false;
                    TableItem item = table.getTable().getItem(index);
                    for (int indexNodes = 0; indexNodes < table.getTable().getColumnCount(); indexNodes++) {
                        Rectangle rect = item.getBounds(indexNodes);
                        if (rect.contains(point)) {
                            if (indexNodes != 0 && indexNodes != index + 1) {
                                // Clean up any previous editor control
                                Control oldEditor = editor.getEditor();
                                if (oldEditor != null) {
                                    oldEditor.dispose();
                                }
                                // Identify the selected row
                                TableItem currentItem = (TableItem) item;
                                if (currentItem == null) {
                                    return;
                                }

                                Text newEditor = new Text(table.getTable(), SWT.NONE);
                                // Protection against incorrect input
                                newEditor.addListener(SWT.Verify, new Listener() {
                                    public void handleEvent(Event e) {
                                        String string = e.text;
                                        char[] chars = new char[string.length()];
                                        string.getChars(0, chars.length, chars, 0);
                                        for (int i = 0; i < chars.length; i++) {
                                            if (('0' <= chars[i] && chars[i] <= '9') == false) {
                                                e.doit = false;
                                                return;
                                            }
                                        }
                                    }
                                });
                                newEditor.setText(item.getText(indexNodes));
                                int currentIndex = indexNodes;
                                newEditor.addModifyListener(new ModifyListener() {
                                    public void modifyText(ModifyEvent me) {
                                        Text text = (Text) editor.getEditor();
                                        if (text.getText().equals("") || text.getText() == null) {
                                            editor.getItem().setText(currentIndex, "0");
                                        } else {
                                            editor.getItem().setText(currentIndex, text.getText());
                                        }
                                    }
                                });
                                newEditor.selectAll();
                                newEditor.setFocus();
                                editor.setEditor(newEditor, currentItem, indexNodes);
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
            }
        });

    }

    private void initButtonAddNode() {
        Composite compositeButtons = new Composite(shell, SWT.NONE);
        compositeButtons.setLayout(new GridLayout(2, false));
        compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        compositeButtons.setBackground(ColorSetupComponent.getMainWindowsColor());
        Button buttonAddNode = new Button(compositeButtons, SWT.PUSH);
        buttonAddNode.setText("Добавить вершину");
        buttonAddNode.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonAddNode.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonAddNode.setFont(FontSetupComponent.getButtonsFont());
        // GridData buttonAddNodeData
        int width = 200;
        int height = 60;
        GridData buttonAddNodeData = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
        buttonAddNodeData.widthHint = width;
        buttonAddNodeData.heightHint = height;
        buttonAddNode.setLayoutData(buttonAddNodeData);

        Button buttonGenerate = new Button(compositeButtons, SWT.PUSH);
        buttonGenerate.setText("Построить граф");
        buttonGenerate.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonGenerate.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonGenerate.setFont(FontSetupComponent.getButtonsFont());
        // GridData buttonGenerateData
        GridData buttonGenerateData = new GridData(SWT.END, SWT.CENTER, true, false);
        buttonGenerateData.widthHint = width;
        buttonGenerateData.heightHint = height;
        buttonGenerate.setLayoutData(buttonGenerateData);

        // Listeners
        buttonAddNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Generating internal ID of node
                String stringID = String.valueOf(new Random().nextInt(1 + 125000));
                table.addColumn(stringID);
                // Filling the cells
                String[] strings = new String[table.getTable().getColumnCount()];
                for (int index = 0; index < table.getTable().getColumnCount(); index++) {
                    if (table.getTable().getItemCount() > 0 && index < table.getTable().getItemCount()) {
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

        buttonGenerate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
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
                    Node node = new Node((startX + nodesXCounter++ * stepX), (startY + nodesYCounter * stepY));
                    node.setInternalID(currentItem.getText(0));
                    controller.addNode(node);
                    graphicComponent.drawNode(node);
                }
                for (int indexItem = 0; indexItem < table.getTable().getItemCount(); indexItem++) {
                    for (int indexColumn = 1; indexColumn < table.getTable().getColumnCount(); indexColumn++) {
                        String weight = table.getTable().getItem(indexItem).getText(indexColumn);
                        if (weight.equals("0") == false) {
                            Node fromNode = controller.searchNode(table.getTable().getItem(indexItem).getText(0));
                            Node toNode = controller.searchNode(table.getTable().getItem(indexColumn - 1).getText(0));
                            if (fromNode != null && toNode != null) {
                                // Check on arc between these nodes
                                boolean isArc = false;
                                for (Arc currentArc : controller.getCurrentGragh().getArcs()) {
                                    if (currentArc.getFromNode() == fromNode
                                            && currentArc.getToNode() == toNode) {
                                        isArc = true;
                                    } else if (currentArc.getToNode() == fromNode
                                            && currentArc.getFromNode() == toNode) {
                                        isArc = true;
                                    }
                                }
                                if (isArc == false) {
                                    Arc arc = new Arc(fromNode, toNode);
                                    controller.addArc(arc);
                                    arc.setWeight(Integer.valueOf(weight));
                                    graphicComponent.drawArc(arc);
                                }
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

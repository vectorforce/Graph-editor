package com.vectorforce.view.dialogs.settingdialogs.generategraph;

import com.vectorforce.view.dialogs.optionsdialogs.algorithmdialogs.MatrixTable;
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
    private int nodesCounter = 0;

    public GraphAdjacencyMatrixDialog() {
        display = Display.getCurrent();
        shell = new Shell(display);
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
                    for (int indexNodes = 0; indexNodes < nodesCounter; indexNodes++) {
                        Rectangle rect = item.getBounds(indexNodes);
                        if (rect.contains(point)) {
                            if (indexNodes != 0) {
                                // Clean up any previous editor control
                                Control oldEditor = editor.getEditor();
                                if (oldEditor != null){
                                    oldEditor.dispose();
                                }
                                // Identify the selected row
                                TableItem itemm = (TableItem) item;
                                if (itemm == null){
                                    return;
                                }

                                Text newEditor = new Text(table.getTable(), SWT.NONE);
                                newEditor.setText(item.getText(indexNodes));
                                int q = indexNodes;
                                newEditor.addModifyListener(new ModifyListener() {
                                    public void modifyText(ModifyEvent me) {
                                        Text text = (Text) editor.getEditor();

                                        editor.getItem().setText(q, text.getText());
                                    }
                                });
                                newEditor.selectAll();
                                newEditor.setFocus();
                                editor.setEditor(newEditor, itemm, indexNodes);
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
        Button buttonAddNode = new Button(shell, SWT.PUSH);
        buttonAddNode.setText("+");
        buttonAddNode.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonAddNode.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonAddNode.setFont(FontSetupComponent.getButtonsFont());

        buttonAddNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Generating internal ID of node
                String stringID = String.valueOf(new Random().nextInt(1 + 125000));
                table.addColumn(stringID);
                nodesCounter++;
                // Filling the cells
                String[] strings = new String[nodesCounter];
                for (int index = 0; index < nodesCounter; index++) {
                    if (index != nodesCounter - 1) {
                        table.getTable().getItem(index).setText(nodesCounter - 1, "0");
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
    }
}

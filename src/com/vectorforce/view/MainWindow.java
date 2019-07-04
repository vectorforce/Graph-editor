package com.vectorforce.view;

import com.vectorforce.controller.Controller;
import com.vectorforce.controller.common.OperationType;
import com.vectorforce.model.Arc;
import com.vectorforce.model.Node;
import com.vectorforce.view.graphics.GraphicComponent;
import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class MainWindow {
    private Display display;
    private Shell shell;

    private GraphicComponent graphicComponent;

    private Controller controller;

    public MainWindow() {
        display = new Display();
        shell = new Shell(display);
        shell.setText("Графовый редактор");
        shell.setLayout(new GridLayout(5, false));

        controller = new Controller();
        initMenuBar();
        initToolBarFile();
        initGraphicComponent();
        initToolBarEdit();

        // Default settings
        defaultSettings();
        run();
    }

    private void run(){
        shell.open();

        while (shell.isDisposed() == false) {
            if (display.readAndDispatch() == true) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private void initMenuBar() {
        Menu menuBar = new Menu(shell, SWT.BAR);
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
        MenuItem fileMenuItem = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuItem.setText("Файл");
        fileMenuItem.setMenu(fileMenu);
        MenuItem editMenuItem = new MenuItem(menuBar, SWT.CASCADE);
        editMenuItem.setText("Редактировать");
        editMenuItem.setMenu(editMenu);

        MenuItem fileNewItem = new MenuItem(fileMenu, SWT.PUSH);
        fileNewItem.setText("Новый");
        MenuItem fileOpenItem = new MenuItem(fileMenu, SWT.PUSH);
        fileOpenItem.setText("Открыть");
        MenuItem fileCloseItem = new MenuItem(fileMenu, SWT.PUSH);
        fileCloseItem.setText("Закрыть");
        MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
        fileSaveItem.setText("Сохранить\tCtrl+S");
        fileSaveItem.setAccelerator(SWT.CTRL + 'S');
        MenuItem fileSaveAsItem = new MenuItem(fileMenu, SWT.PUSH);
        fileSaveAsItem.setText("Сохранить как\tCtrl+Shift+S");
        fileSaveAsItem.setAccelerator(SWT.CTRL + SWT.SHIFT + 'S');
        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileExitItem.setText("Выход\tCtrl+Q");
        fileExitItem.setAccelerator(SWT.CTRL + 'Q');

        // Initialization of listeners
        fileExitItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });

        shell.setMenuBar(menuBar);
    }

    // Initialization methods
    private void initGraphicComponent(){
        Composite compositeGraphicComponent = new Composite(shell, SWT.NONE);
        compositeGraphicComponent.setLayout(new GridLayout(1, false));
        compositeGraphicComponent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        graphicComponent = new GraphicComponent(compositeGraphicComponent, SWT.BORDER | SWT.DOUBLE_BUFFERED, controller);
        initGenerateGraphButton(compositeGraphicComponent);
    }

    private void initGenerateGraphButton(Composite composite){
        Button buttonGenerateGraph = new Button(composite, SWT.PUSH);
        buttonGenerateGraph.setText("Сгенерировать граф");
        GridData buttonGenerateGraphData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        buttonGenerateGraphData.widthHint = 200;
        buttonGenerateGraphData.heightHint = 50;
        buttonGenerateGraph.setLayoutData(buttonGenerateGraphData);

        buttonGenerateGraph.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Node node1 = new Node(200, 300);
                Node node2 = new Node(500, 300);
                Arc arc = new Arc(node2, node1);
                controller.getGragh().addNode(node1);
                controller.getGragh().addNode(node2);
                controller.getGragh().addArc(arc);
                graphicComponent.drawNode(node1);
                graphicComponent.drawNode(node2);
                graphicComponent.drawArc(arc);
                graphicComponent.redraw();
            }
        });
    }

    private void initToolBarFile(){
        ToolBar toolBarFile = new ToolBar(shell, SWT.VERTICAL);
        ToolItem itemNew = new ToolItem(toolBarFile, SWT.PUSH);
        itemNew.setText("Новый");
        ToolItem itemOpen = new ToolItem(toolBarFile, SWT.PUSH);
        itemOpen.setText("Открыть");
        ToolItem itemClose = new ToolItem(toolBarFile, SWT.PUSH);
        itemClose.setText("Закрыть");
        ToolItem itemSave = new ToolItem(toolBarFile, SWT.PUSH);
        itemSave.setText("Сохранить");
        ToolItem itemSaveAs = new ToolItem(toolBarFile, SWT.PUSH);
        itemSaveAs.setText("Сохранить как");
    }

    private void initToolBarEdit(){
        ToolBar toolBarEdit = new ToolBar(shell, SWT.VERTICAL);
        ToolItem itemCursor = new ToolItem(toolBarEdit, SWT.PUSH);
        itemCursor.setText("Cursor");
        ToolItem itemArc = new ToolItem(toolBarEdit, SWT.PUSH);
        itemArc.setText("Arc");
        ToolItem itemSetTheme = new ToolItem(toolBarEdit, SWT.PUSH);
        itemSetTheme.setText("Dark/Light");
//        toolBar.pack();

        // Item listeners
        itemCursor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                controller.setStatus(OperationType.operationType.CURSOR);
//                itemCursor.setSelection(true);
                controller.removeSelection();
                graphicComponent.redraw();
                graphicComponent.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
            }
        });

        itemArc.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                controller.setStatus(OperationType.operationType.ARC);
//                itemArc.setSelection(true);
                controller.removeSelection();
                graphicComponent.redraw();
                graphicComponent.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
            }
        });

        itemSetTheme.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {        // !!!CHECK LINKS for colors
                graphicComponent.changeTheme();
            }
        });
    }

    // Create new graph
    public void createGraph(){
        controller.addGraph();
    }

    // Methods with default settings for testing the app !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void defaultSettings(){
        createGraph();
    }
}

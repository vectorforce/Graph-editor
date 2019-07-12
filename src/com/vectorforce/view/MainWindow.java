package com.vectorforce.view;

import com.vectorforce.controller.Controller;
import com.vectorforce.controller.common.OperationType;
import com.vectorforce.model.Arc;
import com.vectorforce.model.node.Node;
import com.vectorforce.view.graphics.GraphicComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.File;
import java.util.ArrayList;

public class MainWindow {
    private Display display;
    private Shell shell;

    private GraphicComponent graphicComponent;
    private CTabFolder tabFolder;
    private ArrayList<CTabItem> tabItems;
    private ArrayList<File> files;

    private Controller controller;

    public MainWindow() {
        display = new Display();
        shell = new Shell(display);
        shell.setText("Графовый редактор");
        shell.setLayout(new GridLayout(5, false));

        tabItems = new ArrayList<>();
        files = new ArrayList<>();
        controller = new Controller();
        initMenuBar();
        initToolBarFile();
        initGraphicComponent();
        initToolBarEdit();

        // Start page of MainWindow
        startForm();

        run();
    }

    private void run() {
        shell.open();

        while (shell.isDisposed() == false) {
            if (display.readAndDispatch() == true) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private void createTabItem(String fileName) {
        CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE | SWT.CLOSE);
        tabItems.add(tabItem);
        if(fileName != null){
            tabItem.setText(fileName);
        } else {
            tabItem.setText("Пустой файл");
        }
        Composite compositeTabItem = new Composite(tabFolder, SWT.NONE);
        compositeTabItem.setLayout(new GridLayout(1, false));
        compositeTabItem.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        graphicComponent = new GraphicComponent(compositeTabItem, SWT.BORDER | SWT.DOUBLE_BUFFERED, controller);
        createGraph();
        tabItem.setControl(compositeTabItem);

        // Adding DisposeListener for every tabItem
        tabItem.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent disposeEvent) {
                System.out.println("Deleted!!!");
            }
        });
    }

    // Initialization methods
    private void initGraphicComponent() {
        Composite compositeGraphicComponent = new Composite(shell, SWT.NONE);
        compositeGraphicComponent.setLayout(new GridLayout(1, true));
        compositeGraphicComponent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Composite compositeTabFolder = new Composite(compositeGraphicComponent, SWT.NONE);
        compositeTabFolder.setLayout(new FillLayout());
        compositeTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        tabFolder = new CTabFolder(compositeTabFolder, SWT.NONE);

        tabFolder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println(tabFolder.getSelectionIndex());
            }
        });

        initGenerateGraphButton(compositeGraphicComponent);
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

    private void initGenerateGraphButton(Composite composite) {
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

    private void initToolBarFile() {
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

        itemNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                createTabItem("Testing");
                tabFolder.redraw();
            }
        });
    }

    private void initToolBarEdit() {
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
    public void createGraph() {
        controller.addGraph();
    }

    // Methods with default settings for testing the app !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void startForm() {
        createTabItem(null);
    }
}

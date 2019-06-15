package com.vectorforce.View;

import com.vectorforce.Controller.Controller;
import com.vectorforce.Model.OperationType;
import com.vectorforce.Model.Vertex;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class MainWindow {
    private Display display;
    private Shell shell;

    private GraphicComponent graphicComponent;

    private Controller controller;

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
        graphicComponent = new GraphicComponent(shell, SWT.NONE | SWT.DOUBLE_BUFFERED, controller);
    }

    private void initToolBar(){
        ToolBar toolBar = new ToolBar(shell, SWT.VERTICAL);
        ToolItem itemCursor = new ToolItem(toolBar, SWT.PUSH);
        itemCursor.setText("Cursor");
        ToolItem itemArc = new ToolItem(toolBar, SWT.PUSH);
        itemArc.setText("Arc");
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
                graphicComponent.setCursor(new Cursor(display, SWT.CURSOR_UPARROW));
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

    public MainWindow() {
        display = new Display();
        shell = new Shell(display);
        shell.setText("Графовый редактор");
        shell.setLayout(new GridLayout(5, false));

        controller = new Controller();
        initMenuBar();
        initGraphicComponent();
        initToolBar();

        // Default settings
        defaultSettings();

        shell.open();

        while (!shell.isDisposed()) {
            if (display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}

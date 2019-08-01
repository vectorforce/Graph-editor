package com.vectorforce.view;

import com.vectorforce.controller.Controller;
import com.vectorforce.controller.common.OperationType;
import com.vectorforce.model.Arc;
import com.vectorforce.model.Graph;
import com.vectorforce.model.algorithms.list.AdjacencyList;
import com.vectorforce.model.algorithms.matrix.AdjacencyMatrix;
import com.vectorforce.model.algorithms.matrix.IncidenceMatrix;
import com.vectorforce.model.node.Node;
import com.vectorforce.parser.DOMWriter;
import com.vectorforce.parser.SAXReader;
import com.vectorforce.view.dialogs.optionsdialogs.AnalysisDialog;
import com.vectorforce.view.dialogs.optionsdialogs.algorithmdialogs.ListDialog;
import com.vectorforce.view.dialogs.optionsdialogs.algorithmdialogs.MatrixDialog;
import com.vectorforce.view.dialogs.settingdialogs.generategraph.GraphAdjacencyMatrixDialog;
import com.vectorforce.view.graphics.GraphicComponent;
import com.vectorforce.view.setup.ColorSetupComponent;
import com.vectorforce.view.setup.FontSetupComponent;
import javafx.util.Pair;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.HashMap;

public class MainWindow {
    private Display display;
    private Shell shell;
    private Color mainWindowColor;

    private GraphicComponent currentGraphicComponent;
    private CTabFolder tabFolder;
    private Text textCurrentInformation;
    private HashMap<Pair, CTabItem> tabItemHashMap;

    private Controller controller;

    public MainWindow() {
        display = new Display();
        shell = new Shell(display);
        shell.setText("Графовый редактор");
        shell.setLayout(new GridLayout(5, false));
        mainWindowColor = ColorSetupComponent.getMainWindowsColor();
        shell.setBackground(mainWindowColor);

        tabItemHashMap = new HashMap<>();
        controller = new Controller();
        initMenuBar();
        initToolBarFile();
        initGraphicComponent();
        initToolBarEdit();

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

    private void createTabItem(String path) {
        if (path != null) {
            for (File currentFile : controller.getFiles()) {
                if (currentFile.getPath().equals(path)) {
                    MessageBox message = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
                    message.setMessage("Файл уже открыт!");
                    message.open();
                    return;
                }
            }
        } else {
            return;
        }
        CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE | SWT.CLOSE);
        Composite compositeTabItem = new Composite(tabFolder, SWT.NONE);
        compositeTabItem.setBackground(mainWindowColor);
        compositeTabItem.setLayout(new GridLayout(1, false));
        compositeTabItem.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tabItem.setControl(compositeTabItem);
        // Creating new GraphicComponent
        currentGraphicComponent = new GraphicComponent(compositeTabItem, textCurrentInformation, SWT.DOUBLE_BUFFERED, controller);
        // Creating new graph
        controller.createGraph(path);
        tabItem.setFont(FontSetupComponent.getTabItemsFont());
        tabItem.setText(controller.getCurrentFile().getName());
        // Creating Pair that will link graphicComponent and graph
        Pair<Graph, GraphicComponent> graphGraphicComponentPair = new Pair<>(controller.getCurrentGragh(), currentGraphicComponent);
        // Creating HashMap that will link previous Pair and appropriate TabItem
        tabItemHashMap.put(graphGraphicComponentPair, tabItem);

        tabFolder.setSelection(tabItem);
    }

    private void changeTheme() {
        if (ColorSetupComponent.isDarkTheme() == true) {
            ColorSetupComponent.setLightTheme();
        } else {
            ColorSetupComponent.setDarkTheme();
        }
    }

    private void setCursor() {
        if (tabFolder.getItemCount() == 0) {
            return;
        }
        controller.setStatus(OperationType.operationType.CURSOR);
        controller.removeSelection();
        currentGraphicComponent.redraw();
        currentGraphicComponent.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
    }

    private void setArc() {
        if (tabFolder.getItemCount() == 0) {
            return;
        }
        controller.setStatus(OperationType.operationType.ARC);
        controller.removeSelection();
        currentGraphicComponent.redraw();
        currentGraphicComponent.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
    }

    // Initialization methods
    private void initGraphicComponent() {
        Composite compositeGraphicComponent = new Composite(shell, SWT.NONE);
        compositeGraphicComponent.setBackground(mainWindowColor);
        compositeGraphicComponent.setLayout(new GridLayout(1, true));
        compositeGraphicComponent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Composite compositeTabFolder = new Composite(compositeGraphicComponent, SWT.NONE);
        compositeTabFolder.setBackground(mainWindowColor);
        compositeTabFolder.setLayout(new FillLayout());
        compositeTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        tabFolder = new CTabFolder(compositeTabFolder, SWT.NONE);
        tabFolder.setBackground(mainWindowColor);
        tabFolder.setSelectionBackground(mainWindowColor);
        tabFolder.setSelectionForeground(ColorSetupComponent.getTabFolderSelectionForeground());
        tabFolder.setForeground(ColorSetupComponent.getTabFolderForeground());

        tabFolder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (HashMap.Entry<Pair, CTabItem> entry : tabItemHashMap.entrySet()) {
                    if (entry.getValue() == tabFolder.getSelection()) {
                        Pair<Graph, GraphicComponent> currentPair = entry.getKey();
                        currentGraphicComponent = currentPair.getValue();
                        controller.setCurrentGraph(currentPair.getKey());
                        currentGraphicComponent.applyCurrentTheme();
                    }
                }
            }
        });

        tabFolder.addCTabFolder2Listener(new CTabFolder2Listener() {
            @Override
            public void close(CTabFolderEvent cTabFolderEvent) {
                closeFile(cTabFolderEvent);
            }

            @Override
            public void minimize(CTabFolderEvent cTabFolderEvent) {

            }

            @Override
            public void maximize(CTabFolderEvent cTabFolderEvent) {

            }

            @Override
            public void restore(CTabFolderEvent cTabFolderEvent) {

            }

            @Override
            public void showList(CTabFolderEvent cTabFolderEvent) {

            }
        });

        initGenerateGraphButton(compositeGraphicComponent);
    }

    private void initMenuBar() {
        Menu menuBar = new Menu(shell, SWT.BAR);
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        MenuItem fileMenuItem = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuItem.setText("Файл");
        fileMenuItem.setMenu(fileMenu);
        Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
        MenuItem editMenuItem = new MenuItem(menuBar, SWT.CASCADE);
        editMenuItem.setText("Редактировать");
        editMenuItem.setMenu(editMenu);
        Menu optionsMenu = new Menu(shell, SWT.DROP_DOWN);
        MenuItem optionsMenuItem = new MenuItem(menuBar, SWT.CASCADE);
        optionsMenuItem.setText("Опции");
        optionsMenuItem.setMenu(optionsMenu);

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
        final MenuItem separator = new MenuItem(fileMenu, SWT.SEPARATOR);
        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileExitItem.setText("Выход\tCtrl+Q");
        fileExitItem.setAccelerator(SWT.CTRL + 'Q');

        // Listeners
        fileNewItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newFile();
            }
        });

        fileSaveItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (controller.getFiles().size() == 0) {
                    return;
                }
                saveFile(controller.getCurrentFile());
            }
        });

        fileSaveAsItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                saveAsFile();
            }
        });

        fileCloseItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tabFolder.getItemCount() == 0) {
                    return;
                }
                closeFile(tabFolder.getSelection());
            }
        });

        fileExitItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });

        MenuItem editCursorItem = new MenuItem(editMenu, SWT.PUSH);
        editCursorItem.setText("Курсор");
        editCursorItem.setAccelerator('1');
        MenuItem editArcItem = new MenuItem(editMenu, SWT.PUSH);
        editArcItem.setText("Дуга");
        editArcItem.setAccelerator('2');

        // Listeners
        editCursorItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setCursor();
            }
        });

        editArcItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setArc();
            }
        });

        MenuItem optionsAnalysisItem = new MenuItem(optionsMenu, SWT.PUSH);
        optionsAnalysisItem.setText("Анализ");
        MenuItem optionsAlgorithmItem = new MenuItem(optionsMenu, SWT.CASCADE);
        optionsAlgorithmItem.setText("Алгоритмы");
        Menu menuAlgorithms = new Menu(shell, SWT.DROP_DOWN);
        optionsAlgorithmItem.setMenu(menuAlgorithms);
        MenuItem buildMatrix = new MenuItem(menuAlgorithms, SWT.CASCADE);
        buildMatrix.setText("Построить матрицу");
        Menu menuMatrix = new Menu(shell, SWT.DROP_DOWN);
        buildMatrix.setMenu(menuMatrix);
        MenuItem adjacencyMatrixItem = new MenuItem(menuMatrix, SWT.PUSH);
        adjacencyMatrixItem.setText("Матрица смежности");
        MenuItem incidenceMatrixItem = new MenuItem(menuMatrix, SWT.PUSH);
        incidenceMatrixItem.setText("Матрица инцидентности");
        MenuItem buildList = new MenuItem(menuAlgorithms, SWT.PUSH);
        buildList.setText("Построить список смежности");

        // Listeners
        optionsAnalysisItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (controller.getFiles().size() == 0) {
                    return;
                }
                new AnalysisDialog(display, controller);
            }
        });

        adjacencyMatrixItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (controller.getFiles().size() == 0) {
                    return;
                }
                new MatrixDialog<Integer>(display, AdjacencyMatrix.buildMatrix(controller.getCurrentGragh()));
            }
        });

        buildList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (controller.getFiles().size() == 0) {
                    return;
                }
                new ListDialog(display, AdjacencyList.buildList(controller.getCurrentGragh()));
            }
        });

        incidenceMatrixItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (controller.getFiles().size() == 0) {
                    return;
                }
                new MatrixDialog<String>(display, IncidenceMatrix.buildMatrix(controller.getCurrentGragh()));
            }
        });

        shell.setMenuBar(menuBar);
    }

    private void initGenerateGraphButton(Composite composite) {
        Composite compositeTextButton = new Composite(composite, SWT.NONE);
        compositeTextButton.setBackground(ColorSetupComponent.getMainWindowsColor());
        compositeTextButton.setLayout(new GridLayout(2, false));
        compositeTextButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        Button buttonGenerateGraph = new Button(compositeTextButton, SWT.PUSH | SWT.BORDER);
        buttonGenerateGraph.setBackground(ColorSetupComponent.getMainWindowsColor());
        buttonGenerateGraph.setFont(FontSetupComponent.getButtonsFont());
        buttonGenerateGraph.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        buttonGenerateGraph.setText("Сгенерировать граф");
        GridData buttonGenerateGraphData = new GridData(SWT.END, SWT.CENTER, false, false);
        buttonGenerateGraphData.widthHint = 200;
        buttonGenerateGraphData.heightHint = 50;
        buttonGenerateGraph.setLayoutData(buttonGenerateGraphData);

        initCurrentGraphicObjectInformationText(compositeTextButton);

        buttonGenerateGraph.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new GraphAdjacencyMatrixDialog(controller, currentGraphicComponent);
//                if (tabFolder.getItemCount() == 0) {
//                    return;
//                }
//                new GenerateGraphDialog(display, controller, currentGraphicComponent);
            }
        });
    }

    private void initCurrentGraphicObjectInformationText(Composite composite) {
        textCurrentInformation = new Text(composite, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL);
        textCurrentInformation.setBackground(ColorSetupComponent.getTextBackgroundColor());
        GridData textCurrentInformationData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        textCurrentInformationData.heightHint = 50;
        textCurrentInformation.setLayoutData(textCurrentInformationData);
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

        itemNew.addSelectionListener(new SelectionAdapter() { // A new tabItem and file is created by pressing
            @Override
            public void widgetSelected(SelectionEvent e) {
                newFile();
            }
        });

        itemOpen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                openFile();
            }
        });

        itemSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (controller.getFiles().size() == 0) {
                    return;
                }
                saveFile(controller.getCurrentFile());
//                textCurrentInformation.setText("Файл сохранен.");
            }
        });

        itemSaveAs.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                saveAsFile();
            }
        });

        itemClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tabFolder.getItemCount() == 0) {
                    return;
                }
                closeFile(tabFolder.getSelection());
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

        // Item listeners
        itemCursor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setCursor();
            }
        });

        itemArc.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setArc();
            }
        });

        itemSetTheme.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {        // !!!CHECK LINKS for colors
                if (tabFolder.getItemCount() == 0) {
                    return;
                }
                controller.removeSelection();
                changeTheme();
                currentGraphicComponent.applyCurrentTheme();
            }
        });
    }

    // Methods for work with files
    private FileDialog createFileDialog(int style) {
        FileDialog dialog = new FileDialog(shell, style);
        String extensions[] = {"*.ugff"};
        dialog.setFilterExtensions(extensions);
        return dialog;
    }

    private void newFile() {
        String path = createFileDialog(SWT.SAVE).open();
        if (path == null) {
            return;
        }
        createTabItem(path);
        saveFile(controller.getCurrentFile());
        tabFolder.redraw();
    }

    private void openFile() {
        String path = createFileDialog(SWT.OPEN).open();
        if (path == null) {
            return;
        }
        createTabItem(path);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SAXReader saxReader = new SAXReader(controller);
            saxParser.parse(controller.getCurrentFile(), saxReader);
            // Drawing graph
            for(Node currentNode : controller.getCurrentGragh().getNodes()){
                currentGraphicComponent.drawNode(currentNode);
            }
            for(Arc currentArc : controller.getCurrentGragh().getArcs()){
                currentGraphicComponent.drawArc(currentArc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabFolder.redraw();
        currentGraphicComponent.redraw();
    }

    private void saveFile(File file) {
        controller.removeSelection();
        DOMWriter xmlWriter = new DOMWriter(controller, file);
        xmlWriter.write();
    }

    private void saveAsFile() {
        if (controller.getFiles().size() == 0) {
            return;
        }
        String path = createFileDialog(SWT.SAVE).open();
        if (path == null) {
            return;
        }
        File file = new File(path);
        saveFile(file);
    }

    private void closeFile(CTabFolderEvent cTabFolderEvent) {
        Pair<Graph, GraphicComponent> currentPair = null;
        for (HashMap.Entry<Pair, CTabItem> entry : tabItemHashMap.entrySet()) {
            if (entry.getValue() == cTabFolderEvent.item) {
                currentPair = entry.getKey();
            }
        }
        if (currentPair != null) {
            tabItemHashMap.remove(currentPair);
            controller.deleteGraph(currentPair.getKey());
            currentPair.getValue().dispose();
        }
    }

    private void closeFile(CTabItem tabItem) {
        Pair<Graph, GraphicComponent> currentPair = null;
        for (HashMap.Entry<Pair, CTabItem> entry : tabItemHashMap.entrySet()) {
            if (entry.getValue() == tabItem) {
                currentPair = entry.getKey();
            }
        }
        if (currentPair != null) {
            tabItemHashMap.remove(currentPair);
            controller.deleteGraph(currentPair.getKey());
            currentPair.getValue().dispose();
        }
        tabItem.dispose();
    }
}

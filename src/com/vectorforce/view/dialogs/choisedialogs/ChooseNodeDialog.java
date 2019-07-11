package com.vectorforce.view.dialogs.choisedialogs;

import com.sun.prism.impl.BufferUtil;
import com.vectorforce.controller.Controller;
import com.vectorforce.model.node.Node;
import com.vectorforce.model.node.NodeType;
import com.vectorforce.view.graphics.GraphicComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;

public class ChooseNodeDialog {
    private Display display;
    private Shell shell;
    private Node node;
    private CommonPartChooseDialog commonPartChooseDialog;

    public ChooseNodeDialog(Display display, Controller controller,  GraphicComponent graphicComponent, Node node){
        this.display = display;
        shell = new Shell(display, SWT.CLOSE | SWT.APPLICATION_MODAL);
        shell.setText("Выберите тип");
        shell.setLayout(new GridLayout(1, false));
        this.node = node;
        commonPartChooseDialog = new CommonPartChooseDialog(controller, graphicComponent);
        initButtons();

        run();
    }

    private void run(){
        shell.pack();
        Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
        shell.open();

        while(shell.isDisposed() == false){
            if(display.readAndDispatch() == true){
                display.sleep();
            }
        }
    }

    private void initButtons(){
        Composite composite = new Group(shell, SWT.NONE);
        composite.setLayout(new GridLayout(5, false));

        Button buttonEmpty = new Button(composite, SWT.TOGGLE);
        buttonEmpty.setText("Пустой");
        commonPartChooseDialog.addButton(buttonEmpty);
        Button buttonClass = new Button(composite, SWT.TOGGLE);
        buttonClass.setText("Класс");
        commonPartChooseDialog.addButton(buttonClass);
        Button buttonNrel = new Button(composite, SWT.TOGGLE);
        buttonNrel.setText("Нерол. отн.");
        commonPartChooseDialog.addButton(buttonNrel);
        Button buttonRrel = new Button(composite, SWT.TOGGLE);
        buttonRrel.setText("Рол. отн");
        commonPartChooseDialog.addButton(buttonRrel);
        Button buttonLink = new Button(composite, SWT.TOGGLE);
        buttonLink.setText("Связка");
        commonPartChooseDialog.addButton(buttonLink);

        Composite compositeOK = new Composite(shell, SWT.NONE);
        compositeOK.setLayout(new GridLayout(1, false));
        compositeOK.setLayoutData(new GridData(SWT.END, SWT.FILL, true, true));

        Button buttonOK = new Button(compositeOK, SWT.PUSH);
        buttonOK.setText("Выбрать");
        GridData buttonOKData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        buttonOKData.widthHint = 120;
        buttonOKData.heightHint = 40;
        buttonOK.setLayoutData(buttonOKData);

        // Listeners
        buttonEmpty.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                commonPartChooseDialog.removeSelection();
                buttonEmpty.setSelection(true);
            }
        });

        buttonClass.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                commonPartChooseDialog.removeSelection();
                buttonClass.setSelection(true);
            }
        });

        buttonNrel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                commonPartChooseDialog.removeSelection();
                buttonNrel.setSelection(true);
            }
        });

        buttonRrel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                commonPartChooseDialog.removeSelection();
                buttonRrel.setSelection(true);
            }
        });

        buttonLink.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                commonPartChooseDialog.removeSelection();
                buttonLink.setSelection(true);
            }
        });

        buttonOK.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (Button currentButton : commonPartChooseDialog.getButtons()) {
                    if (currentButton.getSelection() == true) {
                        if(currentButton == buttonEmpty){
                            node.setStatus(NodeType.nodeType.EMPTY);
                        } else if(currentButton == buttonNrel){
                            node.setStatus(NodeType.nodeType.NREL);
                        } else if(currentButton == buttonRrel){
                            node.setStatus(NodeType.nodeType.RREL);
                        } else if(currentButton == buttonLink){
                            node.setStatus(NodeType.nodeType.LINK);
                        } else if(currentButton == buttonClass){
                            node.setStatus(NodeType.nodeType.CLASS);
                        }
                    }
                }
                commonPartChooseDialog.getGraphicComponent().redraw();
                shell.close();
            }
        });
    }
}

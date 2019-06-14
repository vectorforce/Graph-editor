package com.vectorforce.View;

import com.vectorforce.Controller.Controller;
import com.vectorforce.Model.Arc;
import com.vectorforce.Model.OperationType;
import com.vectorforce.Model.Vertex;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

public class GraphicComponent extends Canvas {
    // Data for graphic component
    private Color backgroundColor;
    private Controller controller;
    // Data for moving objects
    private int CORRECTING_SHIFT = 12;
    private int startX;
    private int startY;
    private boolean moveCheck;
    // Context menu for right click
    private Menu popupMenuVertex;
    private Menu popupMenuArc;

    // Constructor
    public GraphicComponent(Composite parent, int style, Controller controller){
        super(parent, style);

        this.controller = controller;

        backgroundColor = new Color(null, 255, 255, 255);
        setBackground(backgroundColor);
        setLayoutData(new GridData(GridData.FILL_BOTH));
        moveCheck = false;
        initPopupMenu();

        addListeners();
    }

    private void initPopupMenu(){
        popupMenuVertex = new Menu(this);
        popupMenuArc = new Menu(this);
        // Adding items to menu
        setBasicMenuItems(popupMenuVertex);
        setBasicMenuItems(popupMenuArc);
        this.setMenu(popupMenuVertex);
    }

    // Method for select the objects
    private void selectObject(MouseEvent e){
        // Check the objects in area of mouse click
        controller.removeSelection();
        for(int index = 0; index < controller.getVerteces().size(); index++){
            Vertex currentVertex = controller.getVerteces().get(index);
            if((e.x > currentVertex.getX()) && (e.x < currentVertex.getX() + currentVertex.getRadius()) ){
                if((e.y > currentVertex.getY()) && (e.y < currentVertex.getY() + currentVertex.getRadius())){
                    currentVertex.select(true);
                    moveCheck = true;
                    startX = e.x;
                    startY = e.y;
                }
            }
        }
        // Cycle for select only one vertex
        boolean isSelectedVertex = false;
        for(Vertex currentVertex : controller.getVerteces()){
            if(currentVertex.isSelected() == true && isSelectedVertex == false){
                isSelectedVertex = true;
            } else if(currentVertex.isSelected() == true && isSelectedVertex == true){
                currentVertex.select(false);
            }
        }
    }

    // Adding listeners
    private void addListeners() {

        // Call context menu
        this.addListener(SWT.MenuDetect, new Listener()
        {
            @Override
            public void handleEvent(Event e)
            {
                boolean isSelected = false;
                Point location = toControl(e.x, e.y);
                if(location.x > getBounds().width){
                    e.doit = false;
                }
                for(Vertex currentVertex : controller.getVerteces()){
                    if(currentVertex.isSelected() == true){
                        setPopupMenu(popupMenuVertex);
                        return;
                    }
                }
                if(isSelected == false){
                    for(Arc currentArc : controller.getArcs()){
                        if(currentArc.isSelected() == true){
                            setPopupMenu(popupMenuArc);
                            return;
                        }
                    }
                }
                if(isSelected == false){
                    e.doit = false;
                }
            }
        });

        // Listeners for mouse click
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                if(e.button == 1){
                    if(controller.getStatus().equals(OperationType.operationType.CURSOR)){
                        Vertex vertex = new Vertex(e.x, e.y);
                        controller.addVertex(vertex);
                        drawVertex(vertex);
                        redraw();
                    }
                }
            }

            @Override
            public void mouseDown(MouseEvent e) {
                if(e.button == 1){ // Left button click
                    switch(controller.getStatus()){
                        case CURSOR:
                            selectObject(e);
                            redraw();
                            break;

                        case ARC:
                            controller.removeSelection();
                            redraw();
                            break;
                    }
                } else if(e.button == 3){ // Right button click
                    selectObject(e);
                    moveCheck = false;
                    redraw();
                }
            }

            @Override
            public void mouseUp(MouseEvent e) {
                // Stop of moving
                if(e.button == 1){
                    moveCheck = false;
                }
            }
        });
        // Listeners for mouse move
        this.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                // Check moving flag on mouse pressed
                if(moveCheck == true){
                    if(e.x == startX && e.y == startY){
                        return;
                    } else if(e.x != -1 && e.y != -1){
                        Vertex selectedVertex = null;
                        for(Vertex currentVertex : controller.getVerteces()){
                            if(currentVertex.isSelected() == true){
                                selectedVertex = currentVertex;
                            }
                        }
                        if(selectedVertex != null){
                            selectedVertex.setX(e.x - CORRECTING_SHIFT);
                            selectedVertex.setY(e.y - CORRECTING_SHIFT);
                            redraw();
                        }
                    }
                }
            }
        });

    }
    // Draw methods
    public void drawVertex(Vertex vertex){
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                e.gc.setLineWidth(5);
                e.gc.setForeground(vertex.getColor());
                e.gc.drawOval(vertex.getX(), vertex.getY(), vertex.getRadius(), vertex.getRadius());
            }
        });
    }

    public void drawArc(Arc arc){
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                e.gc.setLineWidth(5);
            }
        });
    }
    // Setters
    private void setPopupMenu(Menu popMenu){
        this.setMenu(popMenu);
    }

    private void setBasicMenuItems(Menu menu){
        MenuItem itemAddText = new MenuItem(menu, SWT.NONE);
        itemAddText.setText("Идентифицировать");
        MenuItem itemChooseType = new MenuItem(menu, SWT.NONE);
        itemChooseType.setText("Выбрать тип");
        MenuItem itemChangeColor = new MenuItem(menu, SWT.NONE);
        itemChangeColor.setText("Изменить цвет");
        MenuItem itemDelete = new MenuItem(menu, SWT.NONE);
        itemDelete.setText("Удалить");

        // Listeners for items
        itemChangeColor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ColorDialog colorDialog = new ColorDialog(getShell());
//                colorDialog.setRGB(colorLabel.getBackground().getRGB());
                colorDialog.setText("Выберите цвет");
                colorDialog.open();
            }
        });

        itemDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

            }
        });
    }
}

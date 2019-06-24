package com.vectorforce.View.Graphics;

import com.vectorforce.Controller.Controller;
import com.vectorforce.Model.Arc;
import com.vectorforce.Controller.Common.OperationType;
import com.vectorforce.Model.Vertex;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

public class GraphicComponent extends Canvas {
    // Data for graphic component
    private int lineWidth;
    private Color backgroundColor;
    private Controller controller;
    // Data for moving objects
    private int startX;
    private int startY;
    private boolean moveCheck;
    private boolean startDrawArc;
    private Vertex fromVertex;
    // Context menu for right click
    private Menu popupMenuVertex;
    private Menu popupMenuArc;
    // Variables for change selected objects
    private GraphicObject selectedObject;
    private Color colorSelectedObject;

    // Constructor
    public GraphicComponent(Composite parent, int style, Controller controller) {
        super(parent, style);

        this.controller = controller;

        lineWidth = 5;
        backgroundColor = new Color(null, 255, 255, 255);
        colorSelectedObject = null;
        selectedObject = new GraphicObject();
        setBackground(backgroundColor);
        setLayoutData(new GridData(GridData.FILL_BOTH));
        moveCheck = false;
        startDrawArc = false;
        fromVertex = null;
        initPopupMenu();

        addListeners();
    }

    private void initPopupMenu() {
        popupMenuVertex = new Menu(this);
        popupMenuArc = new Menu(this);
        // Adding items to menu
        setBasicMenuItems(popupMenuVertex);
        setBasicMenuItems(popupMenuArc);
        this.setMenu(popupMenuVertex);
    }

    // Method for select the objects on graphic component
    private void selectObject(MouseEvent e) {
        // Check the objects in area of mouse click
        controller.removeSelection();
        // Check on vertex
        for (Vertex currentVertex : controller.getGragh().getVerteces()) {
            if ((e.x > currentVertex.getX()) && (e.x < currentVertex.getX() + currentVertex.getDiameter())) {
                if ((e.y > currentVertex.getY()) && (e.y < currentVertex.getY() + currentVertex.getDiameter())) {
                    currentVertex.select(true);
                    moveCheck = true;
                    startX = e.x;
                    startY = e.y;
                }
            }
        }
        // Cycle for select only one vertex
        boolean isSelectedVertex = false;
        for (Vertex currentVertex : controller.getGragh().getVerteces()) {
            if (currentVertex.isSelected() == true && isSelectedVertex == false) {
                isSelectedVertex = true;
            } else if (currentVertex.isSelected() == true && isSelectedVertex == true) {
                currentVertex.select(false);
            }
        }
        setSelectedObject();
        // Check on arc
        if (selectedObject.getObject() != null) {
            return;
        }
        for (Arc currentArc : controller.getGragh().getArcs()) {
            if (currentArc.getHitBox().contains(e.x, e.y)) {
                currentArc.select(true);
            }
        }
        // Cycle for select only one arc
        boolean isSelectedArc = false;
        for (Arc currentArc : controller.getGragh().getArcs()) {
            if (currentArc.isSelected() == true && isSelectedArc == false) {
                isSelectedArc = true;
            } else if (currentArc.isSelected() == true && isSelectedArc == true) {
                currentArc.select(false);
            }
        }
        setSelectedObject();
    }

    // Method for update parameters of selected object after change
    private void updateSelectedObject() {
        if (selectedObject.getObject() == null) {
            return;
        }
        if (selectedObject.getObject() instanceof Vertex) {
            ((Vertex) selectedObject.getObject()).setColor(colorSelectedObject);
        } else if (selectedObject.getObject() instanceof Arc) {
            ((Arc) selectedObject.getObject()).setColor(colorSelectedObject);
        }
    }

    // Adding listeners
    private void addListeners() {
        // Call context menu
        this.addListener(SWT.MenuDetect, new Listener() {
            @Override
            public void handleEvent(Event e) {
                Point location = toControl(e.x, e.y);
                if (location.x > getBounds().width) {
                    e.doit = false;
                }
                setSelectedObject();
                if (selectedObject.getObject() == null) {
                    e.doit = false;
                } else if (selectedObject.getObject() instanceof Vertex) {
                    setPopupMenu(popupMenuVertex);
                } else if (selectedObject.getObject() instanceof Arc) {
                    setPopupMenu(popupMenuArc);
                }
            }
        });
        // Listeners for mouse click
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                if (e.button == 1) {
                    if (controller.getStatus().equals(OperationType.operationType.CURSOR)) {
                        Vertex vertex = new Vertex(e.x, e.y);
                        controller.addVertex(vertex);
                        drawVertex(vertex);
                        redraw();
                    }
                }
            }

            @Override
            public void mouseDown(MouseEvent e) {
                if (e.button == 1) { // Left button click
                    switch (controller.getStatus()) {
                        case CURSOR:
                            selectObject(e);
                            redraw();
                            break;

                        case ARC:
                            controller.removeSelection();
                            selectObject(e);
                            if (selectedObject.getObject() instanceof Vertex) {
                                if (startDrawArc == false) {
                                    fromVertex = ((Vertex) selectedObject.getObject());
                                    startDrawArc = true;
                                    setCursor(SWT.CURSOR_UPARROW);

                                } else {
                                    startDrawArc = false;
                                    setCursor(SWT.CURSOR_ARROW);
                                    // Check on arc between these verteces
                                    if (controller.getGragh().isOriented() == false) {
                                        for (Arc currentArc : controller.getGragh().getArcs()) {
                                            if (currentArc.getFromVertex() == fromVertex
                                                    && currentArc.getToVertex() == ((Vertex) selectedObject.getObject())) {
                                                return;
                                            } else if (currentArc.getFromVertex() == ((Vertex) selectedObject.getObject())
                                                    && currentArc.getToVertex() == fromVertex) {
                                                return;
                                            }
                                        }
                                    }
                                    Arc arc = new Arc(fromVertex, ((Vertex) selectedObject.getObject()));
                                    controller.addArc(arc);
                                    drawArc(arc);
                                }
                            } else if (selectedObject.getObject() == null) {
                                if (fromVertex != null) {
                                    fromVertex = null;
                                    startDrawArc = false;
                                    setCursor(SWT.CURSOR_ARROW);
                                }
                            }
                            redraw();
                            break;
                    }
                } else if (e.button == 3) { // Right button click
                    selectObject(e);
                    moveCheck = false;
                    redraw();
                }
            }

            @Override
            public void mouseUp(MouseEvent e) {
                // Stop of moving
                switch (controller.getStatus()) {
                    case CURSOR:
                        if (e.button == 1) {
                            moveCheck = false;
                        }
                        break;

                    case ARC:

                        break;
                }
            }
        });
        // Listeners for mouse move
        this.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                switch (controller.getStatus()) {
                    case CURSOR:
                        // Check moving flag on mouse pressed
                        if (moveCheck == true) {
                            if (e.x == startX && e.y == startY) {
                                return;
                            } else if (e.x != -1 && e.y != -1) {
                                Vertex selectedVertex = null;
                                for (Vertex currentVertex : controller.getGragh().getVerteces()) {
                                    if (currentVertex.isSelected() == true) {
                                        selectedVertex = currentVertex;
                                    }
                                }
                                if (selectedVertex != null) {
                                    selectedVertex.setX(e.x - (selectedVertex.getDiameter() / 2));
                                    selectedVertex.setY(e.y - (selectedVertex.getDiameter() / 2));
                                    redraw();
                                }
                            }
                        }
                        break;

                    case ARC:
                        if (startDrawArc == true) {

                        }
                        break;
                }
            }
        });

    }

    // Draw methods
    public void drawVertex(Vertex vertex) {
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                // Draw default node
                if (controller.getGragh().getVerteces().contains(vertex) == false) { // ???CHECK THIS MOMENT
                    return;
                }
                e.gc.setLineWidth(lineWidth);
                e.gc.setForeground(vertex.getColor());
                e.gc.drawOval(vertex.getX(), vertex.getY(), vertex.getDiameter(), vertex.getDiameter());
            }
        });
    }

    public void drawArc(Arc arc) {
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                // Draw default arc
                if (controller.getGragh().getArcs().contains(arc) == false) {
                    return;
                }
                e.gc.setLineWidth(lineWidth);
                int correctingShift = lineWidth - 2;
                e.gc.setForeground(arc.getColor());
                // Getting center coordinates
                int x1 = arc.getFromVertex().getX() + arc.getToVertex().getDiameter() / 2;
                int y1 = arc.getFromVertex().getY() + arc.getToVertex().getDiameter() / 2;
                int x2 = arc.getToVertex().getX() + arc.getToVertex().getDiameter() / 2;
                int y2 = arc.getToVertex().getY() + arc.getToVertex().getDiameter() / 2;
                // Calculating triangle legs
                int difX = x2 - x1;
                int difY = y2 - y1;
                // Calculating rotation angle for the fromVertex
                double rotationAngleDegrees = Math.toDegrees(Math.atan2(difY,difX));
                double rotationAngleRadians = rotationAngleDegrees * Math.PI / 180;
                x1 += (int)((arc.getToVertex().getDiameter() / 2 + correctingShift) * Math.cos(rotationAngleRadians));
                y1 += (int)((arc.getToVertex().getDiameter() / 2 + correctingShift) * Math.sin(rotationAngleRadians));
                // Calculating rotation angle for the toVertex
                rotationAngleDegrees -= 180;
                rotationAngleRadians = rotationAngleDegrees * Math.PI / 180;
                x2 += (int)((arc.getToVertex().getDiameter() / 2 + correctingShift) * Math.cos(rotationAngleRadians));
                y2 += (int)((arc.getToVertex().getDiameter() / 2 + correctingShift) * Math.sin(rotationAngleRadians));
                arc.setX1(x1);
                arc.setY1(y1);
                arc.setX2(x2);
                arc.setY2(y2);
                e.gc.drawLine(x1, y1, x2, y2);
            }
        });
    }

    // Setters
    private void setSelectedObject() {
        for (Vertex currentVertex : controller.getGragh().getVerteces()) {
            if (currentVertex.isSelected() == true) {
                selectedObject.setObject(currentVertex);
                return;
            }
        }
        for (Arc currentArc : controller.getGragh().getArcs()) {
            if (currentArc.isSelected() == true) {
                selectedObject.setObject(currentArc);
                return;
            }
        }
        selectedObject.setObject(null);
    }

    private void setCursor(int style) {
        this.setCursor(new Cursor(getDisplay(), style));
    }

    private void setPopupMenu(Menu popMenu) {
        this.setMenu(popMenu);
    }

    private void setBasicMenuItems(Menu menu) {
        MenuItem itemAddText = new MenuItem(menu, SWT.NONE);
        itemAddText.setText("Идентифицировать");
        MenuItem itemChooseType = new MenuItem(menu, SWT.NONE);
        itemChooseType.setText("Выбрать тип");
        MenuItem itemChangeColor = new MenuItem(menu, SWT.NONE);
        itemChangeColor.setText("Выбрать цвет");
        MenuItem itemDelete = new MenuItem(menu, SWT.NONE);
        itemDelete.setText("Удалить");

        // Listeners for items
        itemChangeColor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ColorDialog colorDialog = new ColorDialog(getShell());
                colorDialog.setText("Выберите цвет");
                RGB rgb = colorDialog.open();
                if (rgb != null) {
                    colorSelectedObject = new Color(null, rgb);
                    updateSelectedObject();
                }
                redraw();
            }
        });

        itemDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (selectedObject.getObject() instanceof Vertex) {
                    controller.getGragh().deleteVertex((Vertex) selectedObject.getObject());
                } else if (selectedObject.getObject() instanceof Arc) {
                    controller.getGragh().deleteArc((Arc) selectedObject.getObject());
                }
                selectedObject.setObject(null);
                redraw();
            }
        });
    }
}

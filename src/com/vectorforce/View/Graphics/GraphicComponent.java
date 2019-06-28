package com.vectorforce.View.Graphics;

import com.vectorforce.Controller.Controller;
import com.vectorforce.Model.Arc;
import com.vectorforce.Model.Node;
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
    private Node fromNode;
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
        fromNode = null;
        initPopupMenu();

        addListeners();
    }

    private void initPopupMenu() {
        popupMenuVertex = new Menu(this);
        popupMenuArc = new Menu(this);
        // Adding items to menu
        setBasicMenuItems(popupMenuVertex);
        setBasicMenuItems(popupMenuArc);
        // Set popupMenuVertex

        // Set popupMenuArc
        MenuItem itemChangeDirection = new MenuItem(popupMenuArc, SWT.NONE);
        itemChangeDirection.setText("Изменить направление");

        itemChangeDirection.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (selectedObject.getObject() instanceof Arc) {
                    controller.changeDirection((Arc) selectedObject.getObject());
                    redraw();
                }
            }
        });
        this.setMenu(popupMenuVertex);
    }

    // Method for select the objects on graphic component
    private void selectObject(MouseEvent e) {
        // Check the objects in area of mouse click
        controller.removeSelection();
        // Check on node
        for (Node currentNode : controller.getGragh().getNodes()) {
            if (currentNode.contains(new Point(e.x, e.y))) {
                currentNode.getGraphicalShell().select(true);
//                currentNode.select(true);
                moveCheck = true;
                startX = e.x;
                startY = e.y;
            }
        }
        // Cycle for select only one node
        boolean isSelectedVertex = false;
        for (Node currentNode : controller.getGragh().getNodes()) {
            if (currentNode.getGraphicalShell().isSelected() == true && isSelectedVertex == false) {
                isSelectedVertex = true;
            } else if (currentNode.getGraphicalShell().isSelected() == true && isSelectedVertex == true) {
                currentNode.getGraphicalShell().select(false);
            }
        }
        setSelectedObject();
        // Check on arc
        if (selectedObject.getObject() != null) {
            return;
        }
        for (Arc currentArc : controller.getGragh().getArcs()) {
            if (currentArc.contains(new Point(e.x, e.y)) == true) {
                currentArc.getGraphicalShell().select(true);
            }
        }
        // Cycle for select only one arc
        boolean isSelectedArc = false;
        for (Arc currentArc : controller.getGragh().getArcs()) {
            if (currentArc.isSelected() == true && isSelectedArc == false) {
                isSelectedArc = true;
            } else if (currentArc.isSelected() == true && isSelectedArc == true) {
                currentArc.getGraphicalShell().select(false);
            }
        }
        setSelectedObject();
    }

    // Method for update parameters of selected object after change
    private void updateSelectedObject() {
        if (selectedObject.getObject() == null) {
            return;
        }
        if (selectedObject.getObject() instanceof Node) {
            ((Node) selectedObject.getObject()).getGraphicalShell().setColor(colorSelectedObject);
        } else if (selectedObject.getObject() instanceof Arc) {
            ((Arc) selectedObject.getObject()).getGraphicalShell().setColor(colorSelectedObject);
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
                } else if (selectedObject.getObject() instanceof Node) {
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
                    switch (controller.getStatus()) {
                        case CURSOR:
                            Node node = new Node(e.x, e.y);
                            controller.addNode(node);
                            drawNode(node);
                            redraw();
                            break;

                        case ARC:

                            break;
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
                            if (selectedObject.getObject() instanceof Node) {
                                if (startDrawArc == false) {
                                    fromNode = ((Node) selectedObject.getObject());
                                    startDrawArc = true;
                                    setCursor(SWT.CURSOR_UPARROW);
                                } else {
                                    startDrawArc = false;
                                    setCursor(SWT.CURSOR_ARROW);
                                    // Check on arc between these nodes
                                    for (Arc currentArc : controller.getGragh().getArcs()) {
                                        if (currentArc.getFromNode() == fromNode
                                                && currentArc.getToNode() == ((Node) selectedObject.getObject())) {
                                            return;
                                        } else if (currentArc.getToNode() == fromNode
                                                && currentArc.getFromNode() == ((Node) selectedObject.getObject())) {
                                            return;
                                        }
                                    }
                                    Arc arc = new Arc(fromNode, ((Node) selectedObject.getObject()));
                                    controller.addArc(arc);
                                    drawArc(arc);
                                }
                            } else if (selectedObject.getObject() == null) {
                                if (fromNode != null) {
                                    fromNode = null;
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
                                Node selectedNode = null;
                                for (Node currentNode : controller.getGragh().getNodes()) {
                                    if (currentNode.getGraphicalShell().isSelected() == true) {
                                        selectedNode = currentNode;
                                    }
                                }
                                if (selectedNode != null) {
                                    selectedNode.setX(e.x - (selectedNode.getDiameter() / 2));
                                    selectedNode.setY(e.y - (selectedNode.getDiameter() / 2));
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

    // Drawing methods
    public void drawNode(Node node) {
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (controller.getGragh().getNodes().contains(node) == false) { // ???CHECK THIS MOMENT
                    return;
                }
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        // Draw default node
                        setGC(e.gc);
                        e.gc.setForeground(node.getGraphicalShell().getColor());
                        e.gc.drawOval(node.getX(), node.getY(), node.getDiameter(), node.getDiameter());
                    }
                });
            }
        });
    }

    public void drawArc(Arc arc) {
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (controller.getGragh().getArcs().contains(arc) == false) {
                    return;
                }
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        // Drawing default arc
                        setGC(e.gc);
                        e.gc.setForeground(arc.getGraphicalShell().getColor());
                        int correctingShift = lineWidth - 2;
                        int correctingShiftOrientedArc = correctingShift + 7;
                        int x1 = arc.getFromNode().getX() + arc.getToNode().getDiameter() / 2;
                        int y1 = arc.getFromNode().getY() + arc.getToNode().getDiameter() / 2;
                        int x2 = arc.getToNode().getX() + arc.getToNode().getDiameter() / 2;
                        int y2 = arc.getToNode().getY() + arc.getToNode().getDiameter() / 2;
                        int xTip = x2;
                        int yTip = y2;
                        // Calculating triangle legs
                        int difX = x2 - x1;
                        int difY = y2 - y1;
                        // Calculating rotation angle for the fromNode
                        double rotationAngle = Math.atan2(difY, difX);
                        x1 += (int) ((arc.getToNode().getDiameter() / 2 + correctingShift) * Math.cos(rotationAngle));
                        y1 += (int) ((arc.getToNode().getDiameter() / 2 + correctingShift) * Math.sin(rotationAngle));
                        // Calculating rotation angle for the toVertex
                        double rotationAngleSecondVertex = rotationAngle - Math.PI;
                        int shift = correctingShift;
                        if (arc.isOriented() == true) {
                            // Drawing tip of arc
                            int widthTip = 60;
                            int heightTip = 60;
                            int arcAngleTip = 40;
                            xTip += (int) ((arc.getToNode().getDiameter() / 2 + correctingShift) * Math.cos(rotationAngleSecondVertex));
                            yTip += (int) ((arc.getToNode().getDiameter() / 2 + correctingShift) * Math.sin(rotationAngleSecondVertex));
                            e.gc.setBackground(arc.getGraphicalShell().getColor());
                            e.gc.fillArc(xTip - widthTip / 2, yTip - heightTip / 2, widthTip, heightTip,
                                    (180 - (int) Math.toDegrees((double) (rotationAngle)) - arcAngleTip / 2), arcAngleTip);
                            shift = correctingShiftOrientedArc;
                        }
                        x2 += (int) ((arc.getToNode().getDiameter() / 2 + shift) * Math.cos(rotationAngleSecondVertex));
                        y2 += (int) ((arc.getToNode().getDiameter() / 2 + shift) * Math.sin(rotationAngleSecondVertex));
                        arc.setX1(x1);
                        arc.setY1(y1);
                        arc.setX2(x2);
                        arc.setY2(y2);
                        e.gc.drawLine(x1, y1, x2, y2);
                    }
                });
            }
        });
    }

    // Setters
    private void setGC(GC gc) {
        gc.setAntialias(SWT.ON);
        gc.setLineWidth(lineWidth);
    }

    private void setSelectedObject() {
        for (Node currentNode : controller.getGragh().getNodes()) {
            if (currentNode.getGraphicalShell().isSelected() == true) {
                selectedObject.setObject(currentNode);
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
        itemChooseType.setText("Изменить тип");
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
                if (selectedObject.getObject() instanceof Node) {
                    controller.deleteNode((Node) selectedObject.getObject());
                } else if (selectedObject.getObject() instanceof Arc) {
                    controller.deleteArc((Arc) selectedObject.getObject());
                }
                selectedObject.setObject(null);
                redraw();
            }
        });
    }
}

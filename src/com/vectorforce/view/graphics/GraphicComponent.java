package com.vectorforce.view.graphics;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import com.vectorforce.model.node.Node;
import com.vectorforce.view.dialogs.settingdialogs.SetIDDialog;
import com.vectorforce.view.dialogs.settingdialogs.SetWeightDialog;
import com.vectorforce.view.dialogs.choisedialogs.ChooseArcDialog;
import com.vectorforce.view.dialogs.choisedialogs.ChooseNodeDialog;
import com.vectorforce.view.setup.ColorSetupComponent;
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
    private Menu popupMenuNode;
    private Menu popupMenuArc;
    // Variables for change selected objects
    private GraphicObject selectedObject;
    private Color colorSelectedObject;
    // Variables for scrolling
    private final ScrollBar vBar;
    private final ScrollBar hBar;
    private final Point origin = new Point(0, 0); // Point showing the origin and offset by scrolling(using only for drawing objects)
    private Rectangle rectangle;

    // Constructor
    public GraphicComponent(Composite parent, int style, Controller controller) {
        super(parent, style | SWT.H_SCROLL | SWT.V_SCROLL);

        this.controller = controller;

        ColorSetupComponent.setDarkTheme();
        lineWidth = 5;
        backgroundColor = ColorSetupComponent.getBackgroundColor();
        colorSelectedObject = ColorSetupComponent.getSelectColor();
        selectedObject = new GraphicObject();
        setBackground(backgroundColor);
        setLayoutData(new GridData(GridData.FILL_BOTH));
        moveCheck = false;
        startDrawArc = false;
        fromNode = null;
        rectangle = null;
        vBar = getVerticalBar();
        hBar = getHorizontalBar();
        initPopupMenu();

        addListeners();
    }

    private void initPopupMenu() {
        popupMenuNode = new Menu(this);
        popupMenuArc = new Menu(this);
        // Adding items to menu
        setBasicMenuItems(popupMenuNode, this);
        setBasicMenuItems(popupMenuArc, this);
        // Set popupMenuNode

        // Set popupMenuArc
        MenuItem itemChangeDirection = new MenuItem(popupMenuArc, SWT.NONE);
        itemChangeDirection.setText("Изменить направление");

        MenuItem itemSetWeight = new MenuItem(popupMenuArc, SWT.NONE);
        itemSetWeight.setText("Установить вес");

        itemSetWeight.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (selectedObject.getObject() instanceof Arc) {
                    new SetWeightDialog(getDisplay(), controller, (Arc) selectedObject.getObject());
                }
                redraw();
            }
        });

        itemChangeDirection.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (selectedObject.getObject() instanceof Arc) {
                    controller.changeDirection((Arc) selectedObject.getObject());
                    redraw();
                }
            }
        });
        this.setMenu(popupMenuNode);
    }

    // Method for select the objects on graphic component
    private void selectObject(MouseEvent e) {
        // Check the objects in area of mouse click
        controller.removeSelection();
        // Check on node
        for (Node currentNode : controller.getCurrentGragh().getNodes()) {
            if (currentNode.contains(new Point(e.x - origin.x, e.y - origin.y))) {
                currentNode.getGraphicalShell().select(true);
//                currentNode.select(true);
                moveCheck = true;
                startX = e.x - origin.x;
                startY = e.y - origin.y;
            }
        }
        // Cycle for select only one node
        boolean isSelectedVertex = false;
        for (Node currentNode : controller.getCurrentGragh().getNodes()) {
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
        for (Arc currentArc : controller.getCurrentGragh().getArcs()) {
            if (currentArc.contains(new Point(e.x - origin.x, e.y - origin.y)) == true) {
                currentArc.getGraphicalShell().select(true);
            }
        }
        // Cycle for select only one arc
        boolean isSelectedArc = false;
        for (Arc currentArc : controller.getCurrentGragh().getArcs()) {
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

    private void scrollRectangleUpdate() {
        int xMax = 0;
        int yMax = 0;
        for (Node currentNode : controller.getCurrentGragh().getNodes()) {
            if (currentNode.getX() > xMax) {
                xMax = currentNode.getX() + currentNode.getDiameter();
            }
            if (currentNode.getY() > yMax) {
                yMax = currentNode.getY() + currentNode.getDiameter();
            }
        }
        rectangle = new Rectangle(0, 0, xMax, yMax);
    }

    // Adding listeners
    private void addListeners() {
        // Resize listener for setting scrollBars
        this.addListener(SWT.Resize, new Listener() {
            public void handleEvent(Event e) {
                scrollRectangleUpdate();
                Rectangle client = getClientArea();
                hBar.setMaximum(rectangle.width);
                vBar.setMaximum(rectangle.height);
                hBar.setThumb(Math.min(rectangle.width, client.width));
                vBar.setThumb(Math.min(rectangle.height, client.height));
                int hPage = rectangle.width - client.width;
                int vPage = rectangle.height - client.height;
                int hSelection = hBar.getSelection();
                int vSelection = vBar.getSelection();
                if (hSelection >= hPage) {
                    if (hPage <= 0) {
                        hSelection = 0;
                    }
                    origin.x = -hSelection;
                }
                if (vSelection >= vPage) {
                    if (vPage <= 0) {
                        vSelection = 0;
                    }
                    origin.y = -vSelection;
                }
                redraw();
            }
        });

        // ScrollBar listeners
        vBar.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                scrollRectangleUpdate();
                int vSelection = vBar.getSelection();
                int destY = vSelection + origin.y;
                scroll(0, destY, 0, 0, rectangle.width, rectangle.height, false);
                origin.y = -vSelection;
                redraw();
            }
        });

        hBar.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                scrollRectangleUpdate();
                int hSelection = hBar.getSelection();
                int destX = hSelection + origin.x;
                scroll(destX, 0, 0, 0, rectangle.width, rectangle.height, false);
                origin.x = -hSelection;
                redraw();
            }
        });

        // Call context menu
        this.addListener(SWT.MenuDetect, new Listener() {
            @Override
            public void handleEvent(Event e) {
                Point location = toControl(e.x + origin.x, e.y + origin.y);
                if (location.x > getBounds().width) {
                    e.doit = false;
                }
                setSelectedObject();
                if (selectedObject.getObject() == null) {
                    e.doit = false;
                } else if (selectedObject.getObject() instanceof Node) {
                    setPopupMenu(popupMenuNode);
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
                            Node node = new Node(e.x - origin.x, e.y - origin.y);
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
                            if (selectedObject.getObject() instanceof Node && selectedObject.getObject() != fromNode) {   // !!!!Change for drawing loops
                                if (startDrawArc == false) {
                                    fromNode = ((Node) selectedObject.getObject());
                                    startDrawArc = true;
                                    setCursor(SWT.CURSOR_UPARROW);
                                } else {
                                    startDrawArc = false;
                                    setCursor(SWT.CURSOR_ARROW);
                                    // Check on arc between these nodes
                                    for (Arc currentArc : controller.getCurrentGragh().getArcs()) {
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
                            } else if (selectedObject.getObject() == null || selectedObject.getObject() instanceof Arc) {  // !!!!Change for drawing loops
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
                    fromNode = null;
                    startDrawArc = false;
                    setCursor(SWT.CURSOR_ARROW);
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
                            if (e.x + origin.x == startX && e.y + origin.y == startY) {
                                return;
                            } else if (e.x + origin.x != -1 && e.y + origin.y != -1) {
                                Node selectedNode = null;
                                for (Node currentNode : controller.getCurrentGragh().getNodes()) {
                                    if (currentNode.getGraphicalShell().isSelected() == true) {
                                        selectedNode = currentNode;
                                    }
                                }
                                if (selectedNode != null) {
                                    selectedNode.setX(e.x - origin.x - (selectedNode.getDiameter() / 2));
                                    selectedNode.setY(e.y - origin.y - (selectedNode.getDiameter() / 2));
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
    private void drawNodeLink(Node node, PaintEvent e) {
        int correctingShift = 1;
        // Drawing horizontal line
        e.gc.drawLine(node.getX() + origin.x, node.getY() + origin.y + node.getDiameter() / 2 + correctingShift,
                node.getX() + origin.x + node.getDiameter(), node.getY() + origin.y + node.getDiameter() / 2 + correctingShift);
    }

    private void drawNodeNrel(Node node, PaintEvent e) {
        int correctingShift = 2;
        e.gc.drawLine(node.getX() + origin.x + correctingShift, node.getY() + origin.y + correctingShift,
                node.getX() + origin.x + node.getDiameter() - correctingShift, node.getY() + origin.y + node.getDiameter() - correctingShift);
        e.gc.drawLine(node.getX() + origin.x + node.getDiameter() - correctingShift, node.getY() + origin.y + correctingShift,
                node.getX() + origin.x + correctingShift, node.getY() + origin.y + node.getDiameter() - correctingShift);
    }

    private void drawNodeRrel(Node node, PaintEvent e) {
        int correctingShift = 1;
        drawNodeLink(node, e);
        // Drawing vertical line
        e.gc.drawLine(node.getX() + origin.x + node.getDiameter() / 2 + correctingShift, node.getY() + origin.y,
                node.getX() + origin.x + node.getDiameter() / 2 + correctingShift, node.getY() + origin.y + node.getDiameter());
    }

    public void drawNode(Node node) {
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (controller.getCurrentGragh().getNodes().contains(node) == false) { // ???CHECK THIS MOMENT
                    return;
                }
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        // Drawing default node
                        setGC(e.gc);
                        e.gc.setForeground(node.getGraphicalShell().getColor());
                        e.gc.drawOval(node.getX() + origin.x, node.getY() + origin.y, node.getDiameter(), node.getDiameter());
                        // Drawing visual ID of type of node
                        e.gc.setLineWidth(3);
                        switch (node.getStatus()) {
                            case EMPTY:
                                break;

                            case LINK:
                                drawNodeLink(node, e);
                                break;

                            case NREL:
                                drawNodeNrel(node, e);
                                break;

                            case RREL:
                                drawNodeRrel(node, e);
                                break;

                            case CLASS:
                                drawNodeNrel(node, e);
                                drawNodeLink(node, e);
                                break;
                        }
                        if (node.getID() != null) {
                            e.gc.drawText(node.getID(), (node.getX() + origin.x + node.getDiameter()),
                                    (int) (node.getY() + origin.y - node.getDiameter() / 1.5), true);
                        }
                    }
                });
            }
        });
    }

    public void drawArc(Arc arc) {
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (controller.getCurrentGragh().getArcs().contains(arc) == false) {
                    return;
                }
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        // Drawing default arc
                        setGC(e.gc);
                        e.gc.setForeground(arc.getGraphicalShell().getColor());
                        int correctingShift = lineWidth - 2;
                        int correctingShiftOrientedArc = correctingShift + 15;
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
                            int angleTipCorrectingShift = 3;
                            e.gc.setBackground(arc.getGraphicalShell().getColor());
                            // Drawing tip of arc
                            e.gc.fillArc(xTip + origin.x - widthTip / 2, yTip + origin.y - heightTip / 2, widthTip, heightTip,
                                    ((int) Math.toDegrees(Math.PI) - (int) Math.toDegrees((double) (rotationAngle)) - (arcAngleTip + angleTipCorrectingShift) / 2), arcAngleTip);
                            shift = correctingShiftOrientedArc;
                        }
                        x2 += (int) ((arc.getToNode().getDiameter() / 2 + shift) * Math.cos(rotationAngleSecondVertex));
                        y2 += (int) ((arc.getToNode().getDiameter() / 2 + shift) * Math.sin(rotationAngleSecondVertex));
                        arc.setX1(x1);
                        arc.setY1(y1);
                        arc.setX2(x2);
                        arc.setY2(y2);
                        // Calculating weightText coordinates
                        int arcLength = (int) Math.sqrt(Math.pow((double) difX, 2) + Math.pow((double) difY, 2.0));
                        int textCorrectingShift = 40;
                        int indentFromArcX = 7;
                        int indentFromArcY = 7;
                        // Changing the side of the display textWeight to avoid overlapping of the arc on the textWeight
                        // Change occurs in the second and fourth quarters
                        if (((rotationAngle < -1.5) && (rotationAngle > -3.14)) ||
                                ((rotationAngle < 1.5) && (rotationAngle > 0))) {
                            indentFromArcY = -25;
                        }
                        int xWeight = x1 + indentFromArcX + (int) (((arcLength - textCorrectingShift) / 2) * Math.cos(rotationAngle));
                        int yWeight = y1 + indentFromArcY + (int) (((arcLength - textCorrectingShift) / 2) * Math.sin(rotationAngle));
                        // Drawing arc
                        if (arc.isBinary() == true) {
                            // Drawing main line
                            e.gc.setLineWidth(10);
                            e.gc.drawLine(x1 + origin.x, y1 + origin.y, x2 + origin.x, y2 + origin.y);
                            // Drawing textWeight
                            e.gc.drawText(String.valueOf(arc.getWeight()), xWeight + origin.x, yWeight + origin.y, true);
                            // Drawing center line for separate main line
                            Color colorSeparateLine = ColorSetupComponent.getBackgroundColor();
                            int separateLineWidth = 4;
                            e.gc.setForeground(colorSeparateLine);
                            e.gc.setLineWidth(separateLineWidth);
                            if (arc.isOriented() == true) {
                                x2 += (int) ((arc.getToNode().getDiameter() / 2 + correctingShift) * Math.cos(rotationAngleSecondVertex));
                                y2 += (int) ((arc.getToNode().getDiameter() / 2 + correctingShift) * Math.sin(rotationAngleSecondVertex));
                            }
                            e.gc.drawLine(x1 + origin.x, y1 + origin.y, x2 + origin.x, y2 + origin.y);
                        } else {
                            e.gc.drawLine(x1 + origin.x, y1 + origin.y, x2 + origin.x, y2 + origin.y);
                            e.gc.drawText(String.valueOf(arc.getWeight()), xWeight + origin.x, yWeight + origin.y, true);
                        }
                    }
                });
            }
        });
    }

    public void changeTheme() {
        if (ColorSetupComponent.isDarkTheme() == true) {
            ColorSetupComponent.setLightTheme();
        } else {
            ColorSetupComponent.setDarkTheme();
        }
        for (Node currentNode : controller.getCurrentGragh().getNodes()) {
            if (ColorSetupComponent.isDarkTheme() == false) {
                if (currentNode.getGraphicalShell().getColor().equals(ColorSetupComponent.getDefaultNodeColorDarkTheme()) == true) {
                    currentNode.getGraphicalShell().setColor(ColorSetupComponent.getNodeColor());
                }
            } else {
                if(currentNode.getGraphicalShell().getColor().equals(ColorSetupComponent.getDefaultObjectColorLightTheme()) == true){
                    currentNode.getGraphicalShell().setColor(ColorSetupComponent.getNodeColor());
                }
            }
        }
        for (Arc currentArc : controller.getCurrentGragh().getArcs()) {
            if (ColorSetupComponent.isDarkTheme() == false) {
                if (currentArc.getGraphicalShell().getColor().equals(ColorSetupComponent.getDefaultArcColorDarkTheme()) == true) {
                    currentArc.getGraphicalShell().setColor(ColorSetupComponent.getArcColor());
                }
            } else {
                if(currentArc.getGraphicalShell().getColor().equals(ColorSetupComponent.getDefaultObjectColorLightTheme()) == true){
                    currentArc.getGraphicalShell().setColor(ColorSetupComponent.getArcColor());
                }
            }
        }
        setBackground(ColorSetupComponent.getBackgroundColor());
        redraw();
    }

    // Setters
    private void setGC(GC gc) {
        gc.setAntialias(SWT.ON);
        gc.setLineWidth(lineWidth);
    }

    private void setSelectedObject() {
        for (Node currentNode : controller.getCurrentGragh().getNodes()) {
            if (currentNode.getGraphicalShell().isSelected() == true) {
                selectedObject.setObject(currentNode);
                return;
            }
        }
        for (Arc currentArc : controller.getCurrentGragh().getArcs()) {
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

    private void setBasicMenuItems(Menu menu, GraphicComponent graphicComponent) {
        MenuItem itemSetID = new MenuItem(menu, SWT.NONE);
        itemSetID.setText("Установить идентификатор");
        MenuItem itemChooseType = new MenuItem(menu, SWT.NONE);
        itemChooseType.setText("Изменить тип");
        MenuItem itemChangeColor = new MenuItem(menu, SWT.NONE);
        itemChangeColor.setText("Выбрать цвет");
        MenuItem itemDelete = new MenuItem(menu, SWT.NONE);
        itemDelete.setText("Удалить");

        // Listeners for items
        itemSetID.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (selectedObject.getObject() instanceof Arc) {
                    new SetIDDialog(getDisplay(), controller, selectedObject);
                } else if (selectedObject.getObject() instanceof Node) {
                    new SetIDDialog(getDisplay(), controller, selectedObject);
                }
                redraw();
            }
        });

        itemChooseType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (selectedObject.getObject() instanceof Arc) {
                    new ChooseArcDialog(getDisplay(), controller, graphicComponent, (Arc) selectedObject.getObject());
                } else if (selectedObject.getObject() instanceof Node) {
                    new ChooseNodeDialog(getDisplay(), controller, graphicComponent, (Node) selectedObject.getObject());
                }
            }
        });

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

package com.vectorforce.View;

import com.vectorforce.Controller.Controller;
import com.vectorforce.Model.Vertex;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class GraphicComponent extends Canvas {
    // Data for graphic component
    private Color backroundColor;
    Controller controller;

    public GraphicComponent(Composite parent, int style, Controller controller){
        super(parent, style);

        this.controller = controller;

        backroundColor = new Color(null, 255, 255, 255);
        setBackground(backroundColor);
        setLayoutData(new GridData(GridData.FILL_BOTH));

        addListeners();
    }

    // Adding listeners
    private void addListeners() {

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {


                Vertex vertex = new Vertex(e.x, e.y);
                controller.addVertex(vertex);
                drawVertex(vertex);
                redraw();
                System.out.println(e.x);
                System.out.println(e.y);
            }

            @Override
            public void mouseDown(MouseEvent e) {

            }

            @Override
            public void mouseUp(MouseEvent e) {

            }
        });

    }

    public void drawVertex(Vertex vertex){
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                e.gc.setLineWidth(5);
                e.gc.drawOval(vertex.getX(), vertex.getY(), vertex.getRadius(), vertex.getRadius());
            }
        });
    }
}

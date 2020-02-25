package com.vectorforce.parser;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import com.vectorforce.model.node.Node;
import com.vectorforce.model.node.NodeType;
import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.graphics.Color;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAXReader extends DefaultHandler {
    private Controller controller;
    private Node node;
    private Arc arc;

    private boolean bType = false;
    private boolean bFromNode = false;
    private boolean bToNode = false;

    public SAXReader(Controller controller) {
        this.controller = controller;
    }

    private Color getColor(String string) {
        // Split the string for getting necessary RGB codes
        String[] colorString = string.split(" ");
        for (int index = 1; index < colorString.length - 1; index++) {
            if (index == 1) {
                colorString[index] = colorString[index].substring(1, colorString[index].length() - 1);
            } else {
                colorString[index] = colorString[index].substring(0, colorString[index].length() - 1);
            }

        }
        return new Color(null, Integer.valueOf(colorString[1]), Integer.valueOf(colorString[2]), Integer.valueOf(colorString[3]));
    }

    @Override
    public void startElement(
            String uri, String localName, String qName, Attributes attributes) {

        switch (qName) {
            case "node": { // Creating node
                String ID = attributes.getValue("ID");
                String internalID = attributes.getValue("internalID");
                int x = Integer.valueOf(attributes.getValue("x"));
                int y = Integer.valueOf(attributes.getValue("y"));

                node = new Node(x, y);
                node.setID(ID);
                node.setInternalID(internalID);
                Color color = getColor(attributes.getValue("color"));
                if (!color.equals(ColorSetupComponent.getDefaultNodeColorDarkTheme()) &&
                        !color.equals(ColorSetupComponent.getDefaultObjectColorLightTheme())) {
                    node.getGraphicalShell().setColor(color);
                } else {
                    node.getGraphicalShell().setColor(ColorSetupComponent.getDefaultNodeColorDarkTheme());
                }
                break;
            }
            case "nodeType":
                bType = true;
                break;
            case "arc": { // Creating arc
                // Temporarily take two random nodes for creating arc
                arc = new Arc(new Node(30, 20), new Node(70, 50));
                controller.getCurrentGragh().getNodes().get(0).getOutgoingArcs().remove(arc);
                controller.getCurrentGragh().getNodes().get(1).getIngoingArcs().remove(arc);
                String ID = attributes.getValue("ID");
                boolean isBinary = false;
                boolean isOriented = false;
                if (attributes.getValue("isBinary").equals("true")) {
                    isBinary = true;
                }
                if (attributes.getValue("isOriented").equals("true")) {
                    isOriented = true;
                }
                int weight = Integer.valueOf(attributes.getValue("weight"));
                arc.setID(ID);
                arc.setWeight(weight);
                arc.setBinary(isBinary);
                arc.setOriented(isOriented);
                Color color = getColor(attributes.getValue("color"));
                if (!color.equals(ColorSetupComponent.getDefaultArcColorDarkTheme()) &&
                        !color.equals(ColorSetupComponent.getDefaultObjectColorLightTheme())) {
                    arc.getGraphicalShell().setColor(color);
                } else {
                    arc.getGraphicalShell().setColor(ColorSetupComponent.getDefaultArcColorDarkTheme());
                }
                break;
            }
            case "fromNode":
                bFromNode = true;
                break;
            case "toNode":
                bToNode = true;
                break;
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) {

        if (qName.equalsIgnoreCase("node")) {
            controller.addNode(node);

        } else if (qName.equals("arc")) {
            controller.addArc(arc);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {

        if (bType) {
            switch (new String(ch, start, length)) {
                case "EMPTY":
                    node.setType(NodeType.nodeType.EMPTY);
                    break;

                case "CLASS":
                    node.setType(NodeType.nodeType.CLASS);
                    break;

                case "LINK":
                    node.setType(NodeType.nodeType.LINK);
                    break;

                case "NREL":
                    node.setType(NodeType.nodeType.NREL);
                    break;

                case "RREL":
                    node.setType(NodeType.nodeType.RREL);
                    break;
            }
            bType = false;
        } else if (bToNode) {
            for (Node currentNode : controller.getCurrentGragh().getNodes()) {
                if (currentNode.getInternalID().equals(new String(ch, start, length))) {
                    arc.setToNode(currentNode);
                }
            }
            bToNode = false;
        } else if (bFromNode) {
            for (Node currentNode : controller.getCurrentGragh().getNodes()) {
                if (currentNode.getInternalID().equals(new String(ch, start, length))) {
                    arc.setFromNode(currentNode);
                }
            }
            bFromNode = false;
        }
    }
}

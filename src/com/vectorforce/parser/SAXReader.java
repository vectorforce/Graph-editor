package com.vectorforce.parser;

import com.vectorforce.controller.Controller;
import com.vectorforce.model.Arc;
import com.vectorforce.model.node.Node;
import com.vectorforce.model.node.NodeType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
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

    @Override
    public void startElement(
            String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equals("node")) { // Creating node
            String ID = attributes.getValue("ID");
            String internalID = attributes.getValue("internalID");
            int x = Integer.valueOf(attributes.getValue("x"));
            int y = Integer.valueOf(attributes.getValue("y"));

            node = new Node(x, y);
            node.setID(ID);
            node.setInternalID(internalID);

            controller.addNode(node);
        } else if (qName.equals("nodeType")) {
            bType = true;
        } else if (qName.equals("arc")) { // Creating arc
            // Temporarily take two random nodes for creating arc
            arc = new Arc(controller.getCurrentGragh().getNodes().get(0), controller.getCurrentGragh().getNodes().get(1));
            String ID = attributes.getValue("ID");
            boolean isBinary = false;
            boolean isOriented = false;
            if (attributes.getValue("isBinary").equals("true")) {
                isBinary = true;
            } else if (attributes.getValue("isOriented").equals("true")) {
                isOriented = true;
            }
            int weight = Integer.valueOf(attributes.getValue("weight"));
            arc.setID(ID);
            arc.setWeight(weight);
            arc.setBinary(isBinary);
            arc.setOriented(isOriented);
        } else if(qName.equals("fromNode")){
            bFromNode = true;
        } else if(qName.equals("toNode")){
            bToNode = true;
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("node")) {
            controller.addNode(node);

        } else if (qName.equals("arc")) {
            controller.addArc(arc);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (bType == true) {
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
            for(Node currentNode : controller.getCurrentGragh().getNodes()){
                if(currentNode.getInternalID().equals(new String(ch, start, length))){
                    arc.setToNode(currentNode);
                }
            }
            bToNode = false;
        } else if (bFromNode) {
            for(Node currentNode : controller.getCurrentGragh().getNodes()){
                if(currentNode.getInternalID().equals(new String(ch, start, length))){
                    arc.setFromNode(currentNode);
                }
            }
            bFromNode = false;
        }
    }
}

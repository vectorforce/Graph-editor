package com.vectorforce.parser;

import com.vectorforce.controller.Controller;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.vectorforce.model.Arc;
import com.vectorforce.model.node.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DOMWriter {
    private final Controller controller;
    private final File file;

    public DOMWriter(Controller controller, File file) {
        this.controller = controller;
        this.file = file;
    }

    public void write() {
        try {

            final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            final DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            final Document document = documentBuilder.newDocument();

            // root element
            final Element root = document.createElement("graph");
            root.setAttribute("ID", controller.getCurrentGraph().getID());
            root.setAttribute("isOriented", String.valueOf(controller.getCurrentGraph().isOriented()));
            root.setAttribute("isMixed", String.valueOf(controller.getCurrentGraph().isMixed()));
            root.setAttribute("isFull", String.valueOf(controller.getCurrentGraph().isFull()));
            document.appendChild(root);

            // employee element
            for (final Node currentNode : controller.getCurrentGraph().getNodes()) {
                final Element node = document.createElement("node");
                node.setAttribute("ID", currentNode.getID());
                node.setAttribute("internalID", currentNode.getInternalID());
                node.setAttribute("x", String.valueOf(currentNode.getX()));
                node.setAttribute("y", String.valueOf(currentNode.getY()));
                node.setAttribute("color", String.valueOf(currentNode.getGraphicalShell().getColor()));
                final Element nodeType = document.createElement("nodeType");
                String type = "";
                switch (currentNode.getType()) {
                    case EMPTY:
                        type = "EMPTY";
                        break;

                    case CLASS:
                        type = "CLASS";
                        break;

                    case LINK:
                        type = "LINK";
                        break;

                    case NREL:
                        type = "NREL";
                        break;

                    case RREL:
                        type = "RREL";
                        break;
                }
                nodeType.appendChild(document.createTextNode(type));
                node.appendChild(nodeType);
                root.appendChild(node);
            }

            for (final Arc currentArc : controller.getCurrentGraph().getArcs()) {
                final Element arc = document.createElement("arc");
                arc.setAttribute("ID", currentArc.getID());
                arc.setAttribute("color", String.valueOf(currentArc.getGraphicalShell().getColor()));
                arc.setAttribute("weight", String.valueOf(currentArc.getWeight()));
                arc.setAttribute("isOriented", String.valueOf(currentArc.isOriented()));
                arc.setAttribute("isBinary", String.valueOf(currentArc.isBinary()));
                final Element fromNode = document.createElement("fromNode");
                fromNode.appendChild(document.createTextNode(currentArc.getFromNode().getInternalID()));
                arc.appendChild(fromNode);
                final Element toNode = document.createElement("toNode");
                toNode.appendChild(document.createTextNode(currentArc.getToNode().getInternalID()));
                arc.appendChild(toNode);
                root.appendChild(arc);
            }

            // create the xml file
            //transform the DOM Object to an XML File
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource domSource = new DOMSource(document);
            final StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }
}

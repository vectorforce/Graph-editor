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
import org.eclipse.swt.graphics.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DOMWriter {
    private Controller controller;
    private File file;
    private Point origin;

    public DOMWriter(Controller controller, File file) {
        this.controller = controller;
        this.file = file;
    }

    public void write() {
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("graph");
            root.setAttribute("ID", controller.getCurrentGragh().getID());
            root.setAttribute("isOriented", String.valueOf(controller.getCurrentGragh().isOriented()));
            root.setAttribute("isMixed", String.valueOf(controller.getCurrentGragh().isMixed()));
            root.setAttribute("isFull", String.valueOf(controller.getCurrentGragh().isFull()));
            document.appendChild(root);

            // employee element
            for(Node currentNode : controller.getCurrentGragh().getNodes()){
                Element node = document.createElement("node");
                node.setAttribute("ID", currentNode.getID());
                node.setAttribute("internalID", currentNode.getInternalID());
                node.setAttribute("x", String.valueOf(currentNode.getX()));
                node.setAttribute("y", String.valueOf(currentNode.getY()));
                node.setAttribute("color", String.valueOf(currentNode.getGraphicalShell().getColor()));
                Element nodeType = document.createElement("nodeType");
                String type = "";
                switch (currentNode.getType()){
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

            for(Arc currentArc : controller.getCurrentGragh().getArcs()){
                Element arc = document.createElement("arc");
                arc.setAttribute("ID", currentArc.getID());
                arc.setAttribute("color", String.valueOf(currentArc.getGraphicalShell().getColor()));
                arc.setAttribute("weight", String.valueOf(currentArc.getWeight()));
                arc.setAttribute("isOriented", String.valueOf(currentArc.isOriented()));
                arc.setAttribute("isBinary", String.valueOf(currentArc.isBinary()));
                Element fromNode = document.createElement("fromNode");
                fromNode.appendChild(document.createTextNode(currentArc.getFromNode().getInternalID()));
                arc.appendChild(fromNode);
                Element toNode = document.createElement("toNode");
                toNode.appendChild(document.createTextNode(currentArc.getToNode().getInternalID()));
                arc.appendChild(toNode);
                root.appendChild(arc);
            }

            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}

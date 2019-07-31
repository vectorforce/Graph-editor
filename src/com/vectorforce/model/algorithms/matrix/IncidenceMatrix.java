package com.vectorforce.model.algorithms.matrix;

import com.vectorforce.model.Graph;

import java.util.ArrayList;

public class IncidenceMatrix {
    public static ArrayList<ArrayList<String>> buildMatrix(Graph graph) {
        if (graph.getArcs().size() == 0) {
            return null;
        }
        ArrayList<ArrayList<String>> matrix = new ArrayList<>();
        // Building first line of matrix
        ArrayList<String> firstLine = new ArrayList<>();
        for (int indexArc = 0; indexArc <= graph.getArcs().size(); indexArc++) {
            if (indexArc == 0) {
                firstLine.add(indexArc, ""); // matrix[0][0] = -1000
            } else {
                firstLine.add(indexArc, graph.getArcs().get(indexArc - 1).getID());
            }
        }
        matrix.add(firstLine);
        // Building other lines
        int rowCounter = 1;
        for (int index = 0; index < graph.getNodes().size(); index++) {
            ArrayList<String> currentLine = new ArrayList<>();
            currentLine.add(0, String.valueOf(graph.getNodes().get(index).getInternalID()));
            for (int indexNode = 1; indexNode <= graph.getArcs().size(); indexNode++) {
                currentLine.add(indexNode, String.valueOf(0));
            }
            matrix.add(currentLine);
        }
        // Filling the matrix
        for (int indexArc = 0; indexArc < graph.getArcs().size(); indexArc++) {
            int fromIndex = 0;
            int toIndex = 0;
            for (int indexLine = 0; indexLine < matrix.size(); indexLine++) {
                if (matrix.get(indexLine).get(0).equals(String.valueOf(graph.getArcs().get(indexArc).getFromNode().getInternalID())) == true) {
                    fromIndex = indexLine;
                }
                if (matrix.get(indexLine).get(0).equals(String.valueOf(graph.getArcs().get(indexArc).getToNode().getInternalID())) == true) {
                    toIndex = indexLine;
                }
            }
            matrix.get(fromIndex).remove(indexArc + 1);
            matrix.get(fromIndex).add(indexArc + 1, String.valueOf(graph.getArcs().get(indexArc).getWeight()));
            matrix.get(toIndex).remove(indexArc + 1);
            matrix.get(toIndex).add(indexArc + 1, String.valueOf(-1));
        }
        return matrix;
    }
}

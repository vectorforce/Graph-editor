package com.vectorforce.model.matrix;

import com.vectorforce.model.Arc;
import com.vectorforce.model.Graph;

import java.util.ArrayList;

public class AdjacencyMatrix {
    public static ArrayList<ArrayList<Integer>> buildMatrix(Graph graph){
        if(graph.getNodes().size() == 0){
            return null;
        }
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        // Building first line of matrix
        ArrayList<Integer> firstLine = new ArrayList<>();
        for (int indexNode = 0; indexNode <= graph.getNodes().size(); indexNode++) {
            if (indexNode == 0) {
                firstLine.add(indexNode, -1); // matrix[0][0] = -1
            } else {
                firstLine.add(indexNode, Integer.valueOf(graph.getNodes().get(indexNode - 1).getInternalID()));
            }
        }
        matrix.add(firstLine);
        // Building other lines
        int rowCounter = 1;
        for (int index = 0; index < graph.getNodes().size(); index++) {
            ArrayList<Integer> currentLine = new ArrayList<>();
            currentLine.add(0, firstLine.get(rowCounter++));
            for (int indexRow = 1; indexRow <= graph.getNodes().size(); indexRow++) {
                currentLine.add(indexRow, 0);
            }
            matrix.add(currentLine);
        }
        // Filling the matrix
        for (int indexNode = 0; indexNode < graph.getNodes().size(); indexNode++) {
            if (graph.getNodes().get(indexNode).getOutgoingArcs().size() != 0) {
                int lineIndex = 0;
                for (int currentLineIndex = 0; currentLineIndex < matrix.size(); currentLineIndex++) {
                    if (matrix.get(currentLineIndex).get(0).equals(Integer.valueOf(graph.getNodes().get(indexNode).getInternalID())) == true) {
                        lineIndex = currentLineIndex;
                    }
                }
                for (int arcIndex = 0; arcIndex < graph.getNodes().get(indexNode).getOutgoingArcs().size(); arcIndex++) {
                    Arc currentArc = (Arc) graph.getNodes().get(indexNode).getOutgoingArcs().get(arcIndex);
                    int rowIndex = firstLine.indexOf(Integer.valueOf(currentArc.getToNode().getInternalID()));
                    matrix.get(lineIndex).remove(rowIndex);
                    matrix.get(lineIndex).add(rowIndex, currentArc.getWeight());
                }
            }
        }
        return matrix;
    }
}

package com.vectorforce.model.list;

import com.vectorforce.model.Graph;
import com.vectorforce.model.matrix.AdjacencyMatrix;

import java.util.ArrayList;

public class AdjacencyList {
    public static ArrayList<ArrayList<Integer>> buildList(Graph graph){
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        ArrayList<ArrayList<Integer>> matrix = AdjacencyMatrix.buildMatrix(graph);
        for(int indexLine = 1; indexLine < matrix.size(); indexLine++){
            ArrayList<Integer> currentLine = new ArrayList<>();
            currentLine.add(0, matrix.get(indexLine).get(0));
            for(int indexRow = 1; indexRow < matrix.get(indexLine).size(); indexRow++){
                if(matrix.get(indexLine).get(indexRow) > 0){
                    currentLine.add(matrix.get(0).get(indexRow));
                }
            }
            list.add(currentLine);
        }
        return list;
    }
}
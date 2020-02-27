package com.vectorforce.view.dialogs.optionsdialogs.algorithmdialogs;

import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

public class MatrixDialog<Type> {
    private Display display;
    private Shell shell;

    private ArrayList<ArrayList<Type>> matrix;

    public MatrixDialog(Display display, final ArrayList<ArrayList<Type>> matrix) {
        if (matrix == null) {
            return;
        }
        this.display = display;
        shell = new Shell(display, SWT.CLOSE | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
        shell.setSize(1024, 768);
        if(matrix.get(0).get(0) instanceof Integer){
            shell.setText("Матрица смежности");
        } else if(matrix.get(0).get(0) instanceof  String) {
            shell.setText("Матрица инцидентности");
        }
        final String imagePath = "src/resources/";
        shell.setImage(new Image(display, imagePath + "graph.png"));
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());
        shell.setLayout(new FillLayout());
        this.matrix = matrix;
        initTable();

        run();
    }

    private void run() {
        final Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
        shell.open();

        while (!shell.isDisposed()) {
            if (display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void initTable() {
        final GraphTable graphTable = new GraphTable(shell);
        for (int indexLine = 0; indexLine < matrix.size(); indexLine++) {
            final String[] strings = new String[matrix.get(indexLine).size()];
            for (int indexRow = 0; indexRow < matrix.get(indexLine).size(); indexRow++) {
                if (indexLine == 0) {
                    if (indexRow == 0) {
                        graphTable.addColumn("");
                    } else {
                        graphTable.addColumn(String.valueOf(matrix.get(indexLine).get(indexRow)));
                    }
                } else {
                    strings[indexRow] = String.valueOf(matrix.get(indexLine).get(indexRow));
                    if (indexRow == matrix.get(indexLine).size() - 1) {
                        graphTable.addItem(strings);
                    }
                }
            }
        }
    }
}

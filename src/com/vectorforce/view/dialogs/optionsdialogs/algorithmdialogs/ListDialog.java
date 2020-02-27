package com.vectorforce.view.dialogs.optionsdialogs.algorithmdialogs;

import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

public class ListDialog {
    private Display display;
    private Shell shell;

    private ArrayList<ArrayList<Integer>> list;

    public ListDialog(Display display, ArrayList<ArrayList<Integer>> list){
        if(list == null){
            return;
        }
        this.list = list;
        this.display = display;
        shell = new Shell(display, SWT.CLOSE | SWT.MAX | SWT.RESIZE | SWT.APPLICATION_MODAL);
        shell.setLayout(new FillLayout());
        shell.setBackground(ColorSetupComponent.getMainWindowsColor());
        shell.setSize(450, 480);
        shell.setText("Список смежности");
        final String imagePath = "src/resources/";
        shell.setImage(new Image(display, imagePath + "graph.png"));
        initTable();

        run();
    }

    private void run(){
        final Rectangle screenSize = display.getPrimaryMonitor().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
        shell.open();

        while(!shell.isDisposed()){
            if(display.readAndDispatch()){
                display.sleep();
            }
        }
    }

    private void initTable(){
        final GraphTable table = new GraphTable(shell);
        table.addColumn("Узел");
        table.addColumn("Смежные узлы", 350);

        for (ArrayList<Integer> aList : list) {
            final String[] strings = new String[2];
            strings[1] = "";
            for (int indexRow = 0; indexRow < aList.size(); indexRow++) {
                if (indexRow == 0) {
                    strings[indexRow] = String.valueOf(aList.get(indexRow));
                } else {
                    strings[1] += "  " + String.valueOf(aList.get(indexRow));
                }
            }
            table.addItem(strings);
        }
    }
}

package com.vectorforce.view.dialogs;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class GenerateGraphDialog {
    private Display display;
    private Shell shell;

    public void GenerateGraphDialog(Display display){
        this.display = display;
        shell = new Shell(display);
        shell.setText("Генерация графа");
        shell.setLayout(new GridLayout(5, false));

        run();
    }

    private void run(){
        shell.open();

        while(shell.isDisposed() == false){
            if(display.readAndDispatch() == true){
                display.sleep();
            }
        }
    }
}

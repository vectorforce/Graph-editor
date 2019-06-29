package com.vectorforce.view.dialogs.choose;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ChooseNode {
    private Display display;
    private Shell shell;

    public ChooseNode(Display display){
        this.display = display;
        shell = new Shell(display, SWT.CLOSE | SWT.APPLICATION_MODAL);
        shell.setText("Выберите тип");
        shell.setLayout(new GridLayout(1, true));

        run();
    }

    private void run(){
        shell.pack();
        shell.open();

        while(shell.isDisposed() == false){
            if(display.readAndDispatch() == true){
                display.sleep();
            }
        }
    }
}

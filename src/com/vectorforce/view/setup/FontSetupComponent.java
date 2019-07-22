package com.vectorforce.view.setup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public class FontSetupComponent {
    private static Font buttonsFont = new Font(Display.getCurrent(), "Arial", 10, SWT.BOLD);
    private static Font tabItemsFont = new Font(Display.getCurrent(), "Arial", 9, SWT.BOLD);

    public FontSetupComponent(){

    }

    // Getters
    public static Font getButtonsFont(){
        return buttonsFont;
    }

    public static Font getTabItemsFont(){
        return tabItemsFont;
    }
}

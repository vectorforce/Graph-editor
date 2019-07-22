package com.vectorforce.view.dialogs.settingdialogs;

import com.vectorforce.view.setup.ColorSetupComponent;
import com.vectorforce.view.setup.FontSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class SettingForm {
    private Composite composite;
    private Text text;
    private Button button;

    public SettingForm(Shell shell){
        composite = new Group(shell, SWT.NONE);
        composite.setBackground(ColorSetupComponent.getWindowsCompositesForegroundColor());
        composite.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        composite.setLayout(new GridLayout(1, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        text = new Text(composite, SWT.SINGLE);
        text.setBackground(ColorSetupComponent.getTextBackgroundColor());
        GridData textData = new GridData(SWT.FILL, SWT.FILL, true, true);
        textData.widthHint = 200;
        textData.heightHint = 25;
        text.setLayoutData(textData);

        button = new Button(shell, SWT.PUSH);
        button.setBackground(ColorSetupComponent.getMainWindowsColor());
        button.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        button.setFont(FontSetupComponent.getButtonsFont());
        GridData buttonData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        buttonData.widthHint = 120;
        buttonData.heightHint = 40;
        button.setLayoutData(buttonData);
    }

    // Setters
    public void setCompositeText(String compositeText){
        ((Group) composite).setText(compositeText);
    }

    public void setButtonText(String buttonText){
        button.setText(buttonText);
    }

    // Getters
    public Composite getComposite(){
        return composite;
    }

    public Text getText(){
        return text;
    }

    public Button getButton(){
        return button;
    }
}

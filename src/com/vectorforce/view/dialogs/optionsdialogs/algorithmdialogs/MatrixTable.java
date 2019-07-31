package com.vectorforce.view.dialogs.optionsdialogs.algorithmdialogs;

import com.vectorforce.view.setup.ColorSetupComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class MatrixTable {
    private Table table;

    public MatrixTable(Composite composite) {
        table = new Table(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY | SWT.BORDER | SWT.FULL_SELECTION);
        table.setBackground(ColorSetupComponent.getWindowsCompositesForegroundColor());
        table.setForeground(ColorSetupComponent.getButtonsForegroundColor());
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setHeaderBackground(ColorSetupComponent.getTableHeaderBackgroundColor());
        table.setHeaderForeground(ColorSetupComponent.getTableHeaderForegroundColor());
    }

    public void addColumn(String string) {
        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setWidth(70);
        column.setText(string);
    }

    public void addColumn(String string, int width){
        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setWidth(width);
        column.setText(string);
    }

    public void addItem(String[] strings) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(strings);
    }

    public Table getTable(){
        return table;
    }
}

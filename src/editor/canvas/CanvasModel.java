package editor.canvas;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class CanvasModel extends AbstractTableModel {
    private ArrayList<Component> elements = new ArrayList<Component>();
    private int length = 0;

    public String getColumnName(int col) {
        return null;
    }
    public int getRowCount() { 
        return elements.size();
    }
    public int getColumnCount() { 
        return 1; 
    }
    public Object getValueAt(int row, int col) {
        return elements.get(row);
    }
    public boolean isCellEditable(int row, int col) { 
        return true; 
    }
    public void setValueAt(Object value, int row, int col) {
        elements.set(row, (Component)value);
        fireTableCellUpdated(row, col);
    }
    public void addElement(Component value) {
        elements.add(value);
    }
}
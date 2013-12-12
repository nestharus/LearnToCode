package editor.canvas;

import editor.toolbars.Toolbar;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;

import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY;
import static javax.swing.TransferHandler.MOVE;

import javax.swing.JPanel;
import java.awt.Component;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;

import javax.swing.JTable;
import java.util.ArrayList;
import javax.swing.event.*;
import javax.swing.table.TableCellEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Color;

import static javax.swing.TransferHandler.COPY_OR_MOVE;

/*
*   Expects data in Object[] form from either another Canvas or the Toolbar.
*/
public class Canvas extends JTable {
    private class Editor extends AbstractCellEditor  implements TableCellEditor {
        public Editor() {
            
        }
        
        //Implement the one CellEditor method that AbstractCellEditor doesn't.
        public Object getCellEditorValue() {
            return null;
        }

        //Implement the one method defined by TableCellEditor.
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {
            
            return (Component)value;
        }
    }
    private class Renderer extends Component implements TableCellRenderer {
        public Renderer() {
            setOpaque(true);
        }
        
        @Override public Component getTableCellRendererComponent(
                final JTable table, final Object value, final boolean isSelected, final boolean cellHasFocus, final int row, final int column
        ) {
            Component component = (Component) value;
            
            if (isSelected) {
                component.setBackground(Color.BLUE);
            } 
            else {
                component.setBackground(Color.WHITE);
            }

            //setToolTipText(...); //Discussed in the following section
            
            if (value instanceof JPanel) {
                //component.setForeground (java.awt.Color.white);
                //component.setBackground (isSelected ? javax.swing.UIManager.getColor("Table.focusCellForeground") : java.awt.Color.white);
                
                if (isSelected) {
                    //component.setForeground (java.awt.Color.BLUE);
                }
                
                return component;
            } //if
            
            return (Component)value;
        }
    } //Renderer
    
    public Canvas() {
        super(new CanvasModel());
        
        setDefaultRenderer(Object.class, new Renderer());
        setDefaultEditor(Object.class, new Editor());
        
        setDragEnabled(true);
        
        //getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        
        if (true) return;
        setTransferHandler(new TransferHandler() {
            private final DataFlavor localObjectFlavor = 
                    new ActivationDataFlavor (
                        Object[].class,
                        DataFlavor.javaJVMLocalObjectMimeType,
                        "List of elements"
                    );
            
            /*
            *   Bundles data up in preparation for transfer
            */
            @Override protected Transferable createTransferable(JComponent component) {
                return new DataHandler(((JList)component).getSelectedValuesList().toArray(), localObjectFlavor.getMimeType());
            } //Transferable

            /*
            *   What actions transfer works with
            */
            @Override public int getSourceActions(JComponent component) {
                return COPY_OR_MOVE;
            } //getSourceActions

            /*
            *   Called when transfer is done
            */
            @Override protected void exportDone(JComponent component, Transferable data, int action) {
                if (action == 0) return;
                
                JList list = (JList)component;
                DefaultListModel listModel = (DefaultListModel)list.getModel();
                
                int[] indices = list.getSelectedIndices();
                
                for (int index = indices.length - 1; index >= 0; --index) {
                    listModel.removeElementAt(indices[index]);
                }
            } //exportDone

            /*
            *   Calls component mouse is currently over to see if it supports drop or not
            */
            @Override public boolean canImport(TransferHandler.TransferSupport info) {
                /*
                *   Can only take from the toolbar or the canvas
                */

                Class componentClass = info.getComponent().getClass();

                return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor) && (componentClass == Toolbar.class || componentClass == Canvas.class);
            } //canImport

            /*
            *   Called when data is dropped on to the component. Returns true if import
            *   was successful.
            */
            @Override public boolean importData(TransferHandler.TransferSupport info) {
                if(!canImport(info)) {
                    return false;
                }

                JList target = (JList)info.getComponent();
                JList.DropLocation dropLocation = (JList.DropLocation)info.getDropLocation();
                DefaultListModel listModel = (DefaultListModel)target.getModel();

                int index = dropLocation.getIndex();
                int max = listModel.getSize();

                if(index < 0 || index > max) {
                    index = max;
                }

                try {
                    Object[] elements = (Object[])info.getTransferable().getTransferData(localObjectFlavor);
                    
                    for (Object element : elements) {
                        listModel.add(index++, element);
                    }
                    
                    return true;
                } //try
                catch(Exception exception) {
                    exception.printStackTrace();
                } //catch

                return false;
            } //importData
        });
    } //Canvas
} //Canvas
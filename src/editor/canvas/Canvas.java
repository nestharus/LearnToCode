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
import java.awt.event.MouseEvent;

import java.awt.Color;
import java.util.EventObject;

import static javax.swing.TransferHandler.COPY_OR_MOVE;

import javax.swing.JLabel;

/*
*   Expects data in Object[] form from either another Canvas or the Toolbar.
*/
public class Canvas extends JTable {
    private class Editor extends AbstractCellEditor implements TableCellEditor {
        private Component editValue;
        private Component value;
        
        //Implement the one CellEditor method that AbstractCellEditor doesn't.
        public Object getCellEditorValue() {
            System.out.println("finished");
            
            return value;
        }

        //Implement the one method defined by TableCellEditor.
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {
            
            this.value = (Component)value;
            
            return (Component)value;
        }
        
        public boolean editCellAt(int row, int column, EventObject e) {
            return false;
        }
        
        @Override public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) { return ((MouseEvent)e).getClickCount() >= 2; }

            return false;
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
            
            int height = component.getPreferredSize().height;
            
            if (height < 10) {
                height = 10;
            }
            
            table.setRowHeight(row, height);
            
            if (isSelected) {
                component.setBackground(Color.GRAY);
            } 
            else {
                component.setBackground(Color.WHITE);
            }
            
            //setToolTipText(...); //Discussed in the following section
            
            return component;
        }
    } //Renderer
    
    public Canvas() {
        super(new CanvasModel());
        
        setDefaultRenderer(Object.class, new Renderer());
        setDefaultEditor(Object.class, new Editor());
        
        setDragEnabled(true);
        
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setShowHorizontalLines(false);
        setAutoscrolls(true);
        
        setTransferHandler(new TransferHandler() {
            private Component[] addedElements;
            
            private final DataFlavor localObjectFlavor = 
                    new ActivationDataFlavor (
                        int[].class,
                        DataFlavor.javaJVMLocalObjectMimeType,
                        "List of elements"
                    );
            
            /*
            *   Bundles data up in preparation for transfer
            */
            @Override protected Transferable createTransferable(JComponent component) {
                return new DataHandler(((JTable)component).getSelectedRows(), localObjectFlavor.getMimeType());
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
                
                JTable table = (JTable)component;
                CanvasModel canvasModel = (CanvasModel)table.getModel();
                
                int[] indices = table.getSelectedRows();
                
                for (int index = indices.length - 1; index >= 0; --index) {
                    canvasModel.removeElement(indices[index]);
                }
                
                Component element;
                
                clearSelection();
                
                for (int index = canvasModel.getRowCount() - 1; index >= 0; --index) {
                    element = (Component)canvasModel.getValueAt(index, 0);
                    
                    for (Component other : addedElements) {
                        if (other == element) {
                            changeSelection(index, 0, true, false);
                        }
                    }
                } //for
                
                addedElements = null;
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

                JTable target = (JTable)info.getComponent();
                JTable.DropLocation dropLocation = (JTable.DropLocation)info.getDropLocation();
                CanvasModel canvasModel = (CanvasModel)target.getModel();

                int index = dropLocation.getRow();
                int max = canvasModel.getRowCount();

                if(index < 0 || index > max) {
                    index = max;
                }

                try {
                    int[] indices = (int[])info.getTransferable().getTransferData(localObjectFlavor);
                    Component[] elements = new Component[indices.length];
                    int elementCount = 0;
                    
                    addedElements = elements;
                    
                    for (int position : indices) {
                        elements[elementCount++] = (Component)canvasModel.getValueAt(position, 0);
                    }
                    
                    for (Component element : elements) {
                        canvasModel.addElement(index++, element);
                        
                        if (isCellSelected(index - 1, 0)){
                            changeSelection(index - 1, 0, true, false);
                        } //if
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
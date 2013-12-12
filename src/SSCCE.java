import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import editor.canvas.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import javax.activation.*;
import javax.swing.TransferHandler.*;

public class SSCCE {
    private static class CanvasModel extends AbstractTableModel {
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

            int size = elements.size() - 1;
            fireTableRowsInserted(size, size);
        }
        public void removeElement(int row) {
            elements.remove(row);

            int size = elements.size() - 1;
            fireTableRowsDeleted(size, size);
        }
        public void addElement(int row, Component value) {
            elements.add(row, value);
            fireTableRowsInserted(row, row);
        }
    }
    
    private static class Canvas extends JTable {
        private class Editor extends AbstractCellEditor implements TableCellEditor {
            private Component editValue;
            private Component value;

            public Object getCellEditorValue() {
                return value;
            }

            public Component getTableCellEditorComponent(
                    JTable table, Object value, boolean isSelected, int row, int column
            ) {

                this.value = (Component)value;

                return (Component)value;
            }

            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }

            @Override public boolean isCellEditable(EventObject e) {
                //if (e instanceof MouseEvent) { return ((MouseEvent)e).getClickCount() >= 2; }

                return true;
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
                
                return component;
            }
        } //Renderer

        /*
        *   Disable drag selection
        */
        @Override protected void processMouseEvent(MouseEvent event) {
            if (
                event.getID() == MouseEvent.MOUSE_PRESSED
                && SwingUtilities.isLeftMouseButton(event)
                && !event.isShiftDown() && !event.isControlDown()) {
                    Point point = event.getPoint();

                    int row = rowAtPoint(point);
                    int col = columnAtPoint(point);
                    if (row >= 0 && col >= 0 && !super.isCellSelected(row, col)) {
                        changeSelection(row, col, false, false);
                    } //if
            } //if

            super.processMouseEvent(event);
       }

        public Canvas() {
            super(new CanvasModel());

            setDefaultRenderer(Object.class, new Renderer());
            setDefaultEditor(Object.class, new Editor());

            setDragEnabled(true);

            getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            setShowHorizontalLines(false);
            setAutoscrolls(true);

            setTransferHandler(new TransferHandler() {
                private final DataFlavor localObjectFlavor = 
                        new ActivationDataFlavor (
                            Component[].class,
                            DataFlavor.javaJVMLocalObjectMimeType,
                            "List of elements"
                        );

                /*
                *   Bundles data up in preparation for transfer
                */
                @Override protected Transferable createTransferable(JComponent component) {
                    int[] rows = ((JTable)component).getSelectedRows();
                    CanvasModel model = (CanvasModel)((JTable)component).getModel();

                    Component[] elements = new Component[rows.length];
                    int elementCount = 0;

                    for (int row : rows) {
                        elements[elementCount++] = (Component)model.getValueAt(row, 0);
                    }

                    return new DataHandler(elements, localObjectFlavor.getMimeType());
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
                } //exportDone

                /*
                *   Calls component mouse is currently over to see if it supports drop or not
                */
                @Override public boolean canImport(TransferHandler.TransferSupport info) {
                    /*
                    *   Can only take from the toolbar or the canvas
                    */
                    Class componentClass = info.getComponent().getClass();

                    return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
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
                        Component[] elements = (Component[])info.getTransferable().getTransferData(localObjectFlavor);

                        for (Component element : elements) {
                            canvasModel.addElement(index++, element);
                            System.out.println(getRowCount());

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
    
    private static JLabel getLabel(String str) {
        JLabel label = new JLabel(str);
        
        label.setOpaque(true);
        
        return label;
    }
    
    private static Object[] getCanvas(String[] str, boolean nested) {
        Canvas canvas = new Canvas();
        CanvasModel canvasList = (CanvasModel)canvas.getModel();
        
        canvasList.addElement(getLabel(str[0]));
        canvasList.addElement(getLabel(str[1]));
        canvasList.addElement(getLabel(str[2]));
        
        JPanel ipanel = new JPanel();
        ipanel.setLayout(new BoxLayout(ipanel, BoxLayout.LINE_AXIS));
        ipanel.setPreferredSize(new Dimension(200, 25));
        ipanel.add(new JCheckBox());
        JTextField field = new JTextField();
        field.setColumns(15);
        ipanel.add(field);
        canvasList.addElement(ipanel);
        
        canvas.setDropMode(DropMode.INSERT);
        
        JScrollPane pane = new JScrollPane(canvas);
        if (nested) {
            pane.setPreferredSize(new Dimension(200, 100));
            pane.setMaximumSize(new Dimension(200, 100));
        }
        else {
            pane.setPreferredSize(new Dimension(200, 300));
        }
              
        if (nested) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
            
            Dimension dimension = new Dimension(15, 0);
            panel.add(new Box.Filler(dimension, dimension, dimension));
            
            panel.add(pane);
            
            return new Object[]{canvas, canvasList, panel};
        } //if
        else {
            return new Object[]{canvas, canvasList, pane};
        }
        
    }
    
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                
                frame.setSize(400, 400);
                frame.setLocationRelativeTo(null);
                
                JPanel panel = new JPanel();
                
                /*
                *   Canvas, CanvasModel, Container
                */
                Object[] canvas1 = getCanvas(new String[]{"test 1", "test 2", "test 3"}, false);
                Object[] canvas2 = getCanvas(new String[]{"test 4", "test 5", "test 6"}, true);
                
                ((CanvasModel)canvas1[1]).addElement((Component)canvas2[2]);
                
                panel.add((Component)canvas1[2]);
                
                frame.add(panel);
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
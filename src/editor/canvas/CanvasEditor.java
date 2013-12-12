package editor.canvas;

import java.util.EventObject;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Component;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.awt.event.MouseEvent;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;

import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import static javax.swing.TransferHandler.COPY;
import static javax.swing.TransferHandler.COPY_OR_MOVE;
import static javax.swing.TransferHandler.MOVE;

public class CanvasEditor extends JTree {
    private static class Editor extends DefaultTreeCellEditor {
        private Component value;
        
        public Editor(JTree tree, DefaultTreeCellRenderer renderer) {
            super(tree, renderer);
        } //Editor
        
        /*
        *   Finish editing
        */
        @Override public Object getCellEditorValue() {
            return value;
        } //getCellEditorValue

        /*
        *   Begin editing
        */
        @Override public Component getTreeCellEditorComponent(  final JTree tree,
                                                                final Object value,
                                                                final boolean isSelected,
                                                                final boolean isExpanded,
                                                                final boolean isLeaf,
                                                                final int row) {
            
            Object obj = ((DefaultMutableTreeNode)value).getUserObject();
            
            if (obj == null || !(obj instanceof Component)) {
                this.value = super.getTreeCellEditorComponent(tree, value, isSelected, isExpanded, isLeaf, row);
                return this.value;
            }
            
            this.value = (Component)((DefaultMutableTreeNode)value).getUserObject();
            
            return this.value;
        } //getTreeCellEditorComponent
        
        /*
        *   Can edit
        */
        @Override public boolean isCellEditable(EventObject e) {
            System.out.println(e.getSource().getClass().toString());
            
            if (e instanceof MouseEvent) { return ((MouseEvent)e).getClickCount() >= 1; }
            
            return true;
        } //isCellEditable
    } //Editor
    
    private static class Renderer extends DefaultTreeCellRenderer implements TreeCellRenderer {
        public Renderer() {
            super();
            
            setOpaque(true);
        }
        
        @Override public Component getTreeCellRendererComponent(final JTree tree,
                                                                final Object value,
                                                                final boolean isSelected,
                                                                final boolean isExpanded,
                                                                final boolean isLeaf,
                                                                final int row,
                                                                boolean hasFocus) {
            
            Object obj = ((DefaultMutableTreeNode)value).getUserObject();
            
            Component component;
            
            if (obj == null || !(obj instanceof Component)) {
                component = super.getTreeCellRendererComponent(tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
            } //if
            else {
                component = (Component)obj;
            } //else
            
            if (isSelected) {
                component.setBackground(Color.GRAY);
            } 
            else {
                component.setBackground(Color.WHITE);
            }

            return component;
        } //getTreeCellEditorComponent
    } //Renderer
    
    private static class Transfer extends TransferHandler {
        private DataFlavor nodesFlavor;  
        private DataFlavor[] flavors;
        private DefaultMutableTreeNode[] nodesToRemove;
        
        /*
        *   Essentially an array
        */
        private static class Array<E> {
            public E[] elements;
            public int count;
            
            public Array(E[] elements) {
                this.elements = elements;
                count = 0;
            }
            
            public void add(E element) {
                elements[count++] = element;
            }
        } //Buffer
        
        private class NodesTransferable implements Transferable {  
            DefaultMutableTreeNode[] nodes;

            public NodesTransferable(DefaultMutableTreeNode[] nodes) {  
                this.nodes = nodes;  
             }  //NodesTransferable

            public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {  
                if(!isDataFlavorSupported(flavor)) {
                    throw new UnsupportedFlavorException(flavor);  
                }
                
                return nodes;  
            }  //getTransferData

            public DataFlavor[] getTransferDataFlavors() {  
                return flavors;  
            } //getTransferDataFlavors

            public boolean isDataFlavorSupported(DataFlavor flavor) {  
                return nodesFlavor.equals(flavor);  
            } //isDataFlavorSupported
        } //NodesTransferable
        
        public Transfer() {
            try {  
                flavors = new DataFlavor[1];
                
                String mimeType =   DataFlavor.javaJVMLocalObjectMimeType +  
                                    ";class=\"" +  
                                    javax.swing.tree.DefaultMutableTreeNode[].class.getName() +  
                                    "\"";
                
                nodesFlavor = new DataFlavor(mimeType);  
                
                flavors[0] = nodesFlavor;  
            } //try
            catch(ClassNotFoundException classNotFoundException) {  
                System.out.println("Class Not Found: " + classNotFoundException.getMessage());  
            } //catch
        } //Transfer

        /*
        *   Bundles data up in preparation for transfer
        */
        @Override protected Transferable createTransferable(JComponent component) {
            JTree tree = (JTree)component;
            
            /*
            *   Get selected paths
            */
            TreePath[] paths = tree.getSelectionPaths();
            
            if (paths != null) {  
                /*
                *   Make an array of copies to transfer
                *   Make an array of nodes that will be removed
                */
                Array<DefaultMutableTreeNode> copies = new Array(new DefaultMutableTreeNode[paths.length]);
                Array<DefaultMutableTreeNode> toRemove = new Array(new DefaultMutableTreeNode[paths.length]);
                
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[0].getLastPathComponent();  
                DefaultMutableTreeNode copy = new DefaultMutableTreeNode(node);  
                
                copies.add(copy);  
                toRemove.add(node);
                
                DefaultMutableTreeNode next;
                
                for (TreePath path : paths) {
                    next = (DefaultMutableTreeNode)path.getLastPathComponent();
                    
                    if (next.getLevel() < node.getLevel()) {
                        /*
                        *   Parent
                        */
                        break;
                    }
                    else if (next.getLevel() > node.getLevel()) {
                        /*
                        *   Child
                        */
                        copy.add(new DefaultMutableTreeNode(next));
                    }
                    else {
                        /*
                        *   Sibling
                        */
                        copies.add(new DefaultMutableTreeNode(next));
                        toRemove.add(next);
                    }
                } //for
                
                nodesToRemove = toRemove.elements;
                
                return new NodesTransferable(copies.elements);  
            } //if
            
            return null; 
        } //Transferable

        /*
        *   What actions transfer works with
        */
        @Override public int getSourceActions(JComponent component) {
            return COPY_OR_MOVE;
        } //getSourceActions
        
        @Override public boolean canImport(TransferHandler.TransferSupport support) {
            if (!support.isDrop()) {  
                return false;  
            } //if
            
            support.setShowDropLocation(true); 
            
            if (!support.isDataFlavorSupported(nodesFlavor)) {  
                return false;  
            } //if
            // Do not allow a drop on the drag source selections.  
            JTree.DropLocation dl =  
                    (JTree.DropLocation)support.getDropLocation();  
            JTree tree = (JTree)support.getComponent();  
            int dropRow = tree.getRowForPath(dl.getPath());  
            int[] selRows = tree.getSelectionRows();  
            for(int i = 0; i < selRows.length; i++) {  
                if(selRows[i] == dropRow) {  
                    return false;  
                }  
            }  
            // Do not allow MOVE-action drops if a non-leaf node is  
            // selected unless all of its children are also selected.  
            int action = support.getDropAction();  
            if(action == MOVE) {  
                return haveCompleteNode(tree);  
            }  
            // Do not allow a non-leaf node to be copied to a level  
            // which is less than its source level.  
            TreePath dest = dl.getPath();  
            DefaultMutableTreeNode target =  
                (DefaultMutableTreeNode)dest.getLastPathComponent();  
            TreePath path = tree.getPathForRow(selRows[0]);  
            DefaultMutableTreeNode firstNode =  
                (DefaultMutableTreeNode)path.getLastPathComponent();  
            if(firstNode.getChildCount() > 0 &&  
                   target.getLevel() < firstNode.getLevel()) {  
                return false;  
            }  
            return true;  
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

            /*
            Component element;

            clearSelection();

            for (int index = canvasModel.getRowCount() - 1; index >= 0; --index) {
                element = (Component)canvasModel.getValueAt(index, 0);

                for (Component other : addedElements) {
                    if (other == element) {
                        //changeSelection(index, 0, true, false);
                    }
                }
            } //for
            */
        } //exportDone

        /*
        *   Calls component mouse is currently over to see if it supports drop or not
        */
    }
    
    public CanvasEditor() {
        setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
        
        setRootVisible(false);
        
        Renderer renderer = new Renderer();
        
        setCellRenderer(renderer);
        setCellEditor(new Editor(this, renderer));
        setEditable(true);
        
        setDragEnabled(true);
        
        getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        setAutoscrolls(true);
        
        setRowHeight(0);
        
        //setTransferHandler();
    } //CanvasEditor
    
    private void expand(final int position) {
        final int rowCount = getRowCount();
        
        for(int i = position; i < rowCount; ++i) {
            expandRow(i);
        } //for
        
        if(getRowCount() != rowCount) {
            expand(rowCount);
        } //if
    } //expand
    
    public void expand() {
        expand(0);
    } //expand
} //CanvasEditor

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

import javax.swing.DropMode;

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
                ArrayList<DefaultMutableTreeNode> copies = new ArrayList();
                ArrayList<DefaultMutableTreeNode> toRemove = new ArrayList();
                
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
                
                nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
                
                return new NodesTransferable(copies.toArray(new DefaultMutableTreeNode[copies.size()]));  
            } //if
            
            return null; 
        } //Transferable

        /*
        *   What actions transfer works with
        */
        @Override public int getSourceActions(JComponent component) {
            return COPY_OR_MOVE;
        } //getSourceActions
        
        /*
        *   Debug purposes
        */
        private boolean childrenSelected(JTree tree) {
            int[] selRows = tree.getSelectionRows();  
            TreePath path = tree.getPathForRow(selRows[0]);  
            DefaultMutableTreeNode first =  
                (DefaultMutableTreeNode)path.getLastPathComponent();  
            int childCount = first.getChildCount();  
            // first has children and no children are selected.  
            if(childCount > 0 && selRows.length == 1)  
                return false;  
            // first may have children.  
            for(int i = 1; i < selRows.length; i++) {  
                path = tree.getPathForRow(selRows[i]);  
                DefaultMutableTreeNode next =  
                    (DefaultMutableTreeNode)path.getLastPathComponent();  
                if(first.isNodeChild(next)) {  
                    // Found a child of first.  
                    if(childCount > selRows.length-1) {  
                        // Not all children of first are selected.  
                        return false;  
                    }  
                }  
            }  
            return true;  
        }  
        
        @Override public boolean canImport(TransferHandler.TransferSupport support) {
            if (!support.isDrop()) {
                return false;
            } //if
            support.setShowDropLocation(true); 
            
            if (!support.isDataFlavorSupported(nodesFlavor)) {  
                return false;  
            } //if
             
            JTree.DropLocation dropLocation = (JTree.DropLocation)support.getDropLocation();
            JTree tree = (JTree)support.getComponent();  
            
            int dropRow = tree.getRowForPath(dropLocation.getPath());  
            int[] selectedRows = tree.getSelectionRows();
            
            /*
            *   Don't allow dropping on itself
            */
            for (int row : selectedRows) {
                if (row == dropRow) {
                    return false;
                } //if
            } //for
            
            /*
            *   All children will be selected in final implementation
            */
            int action = support.getDropAction();  
            if (action == MOVE) {  
                return childrenSelected(tree);  
            } //if
            
            /*
            *   A parent node can't be moved into its children
            */
            TreePath destination = dropLocation.getPath(); 
            DefaultMutableTreeNode target = (DefaultMutableTreeNode)destination.getLastPathComponent();  
            TreePath path = tree.getPathForRow(selectedRows[0]);  
            DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode)path.getLastPathComponent();  
            if (firstNode.getChildCount() > 0 && target.isNodeSibling(firstNode)) {  
                return false;
            } //if
            
            return true;  
        } //canImport

        /*
        *   Called when data is dropped on to the component. Returns true if import
        *   was successful.
        */
        @Override public boolean importData(TransferHandler.TransferSupport support) {
            if (!canImport(support)) {  
                return false;  
            } //if
            
            /*
            *   Get the transfer data
            */
            DefaultMutableTreeNode[] nodes = null;  
            try {  
                Transferable transfer = support.getTransferable();  
                nodes = (DefaultMutableTreeNode[])transfer.getTransferData(nodesFlavor);  
            } //try
            catch(UnsupportedFlavorException exception) {
                System.out.println("UnsupportedFlavor: " + exception.getMessage());  
            } //catch
            catch(java.io.IOException exception) {
                System.out.println("I/O error: " + exception.getMessage());  
            } //catch
            
            /*
            *   Get drop location
            */
            JTree.DropLocation dropLocation = (JTree.DropLocation)support.getDropLocation();  
            int childIndex = dropLocation.getChildIndex();  
            TreePath destination = dropLocation.getPath();  
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)destination.getLastPathComponent();  
            JTree tree = (JTree)support.getComponent();  
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();  
            
            /*
            *   Prepare drop
            */
            int index = childIndex;
            if (childIndex == -1) {
                index = parent.getChildCount();  
            } //if
            
            // Add data to model. 
            for (DefaultMutableTreeNode node : nodes) {
                model.insertNodeInto((DefaultMutableTreeNode)node.getUserObject(), parent, index++);  
            } //for
            
            return true;
        } //importData

        /*
        *   Called when transfer is done
        */
        @Override protected void exportDone(JComponent source, Transferable data, int action) {
            /*
            *   If the action was a move, remove it
            */
            if ((action & MOVE) == MOVE) {  
                JTree tree = (JTree)source; 
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                
                for (DefaultMutableTreeNode node : nodesToRemove) {
                    model.removeNodeFromParent(node);
                } //for
            } //if
        } //exportDone
    }
    
    public CanvasEditor() {
        setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
        
        setRootVisible(false);
        
        Renderer renderer = new Renderer();
        
        setCellRenderer(renderer);
        setCellEditor(new Editor(this, renderer));
        setEditable(true);
        
        setDragEnabled(true);
        setDropMode(DropMode.ON_OR_INSERT);
        
        getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        setAutoscrolls(true);
        
        //setRowHeight(0);
        
        setTransferHandler(new Transfer());
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

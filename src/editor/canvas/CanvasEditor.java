package editor.canvas;

import java.util.EventObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Arrays;

import java.awt.Color;
import java.awt.Component;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

import javax.swing.TransferHandler;

import javax.swing.DropMode;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import static javax.swing.TransferHandler.COPY_OR_MOVE;
import static javax.swing.TransferHandler.MOVE;

public class CanvasEditor extends JTree { /*
*************************************************************************************
*
*   Draggable Information (for accepting and passing data)
*
*       DataFlavor((
*               DataFlavor.javaJVMLocalObjectMimeType + 
*               ";class=\"" + javax.swing.tree.DefaultMutableTreeNode[].class.getName() + "\""))
*
*       getTransferData -> DefaultMutableTreeNode[]
*
*   Use TreeModelListener to track data changes, don't rely on the CanvasEditor
*   to store the data.
*
*************************************************************************************
*
*   Currently does not support data validation for dragging. Wlil need to
*   add validation listeners for modular design.
*
*       Consider
*
*           integer c
*
*           function hi
*               integer a
*           function b
*               integer c
*
*               set c = 5
*           function o
*               integer a
*
*   the global integer c should not be able to be dragged into function b
*
*   function b's set c = 5 should only be able to either go global or function b
*
*   int a can only go into either global or function b (if shadowing is used)
*
*************************************************************************************
*
*   For delete, if a single element is selected, when that element is deleteted,
*   the next element should be selected. While delete is held, selected elements should
*   be deleted.
*
************************************************************************************/
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
            return true;
        } //isCellEditable
    } //Editor
    
    private static class Renderer extends DefaultTreeCellRenderer implements TreeCellRenderer {
        public Renderer() {
            super();
            
            setOpaque(true);
        } //Renderer
        
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
            } //if
            else {
                component.setBackground(Color.WHITE);
            } //else

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
                if (!isDataFlavorSupported(flavor)) {
                    throw new UnsupportedFlavorException(flavor);  
                } //if
                
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
                DefaultMutableTreeNode copy = new DefaultMutableTreeNode(node.getUserObject());  
                
                DefaultMutableTreeNode next;
                
                DefaultMutableTreeNode parent = null;
                Enumeration e;
                DefaultMutableTreeNode n;
                DefaultMutableTreeNode copyNode;
                
                for (TreePath path : paths) {
                    next = (DefaultMutableTreeNode)path.getLastPathComponent();
                    
                    parent = new DefaultMutableTreeNode(next.getUserObject());
                    parent.setAllowsChildren(next.getAllowsChildren());

                    copies.add(parent);
                    toRemove.add(next);

                    e = next.breadthFirstEnumeration();
                    
                    e.nextElement();

                    while(e.hasMoreElements()) {  
                        n = (DefaultMutableTreeNode)e.nextElement();   

                        copyNode = new DefaultMutableTreeNode(n.getUserObject());
                        copyNode.setAllowsChildren(n.getAllowsChildren());
                        parent.add(copyNode);
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
            *   A parent node can't be moved into its children
            */
            TreePath destination = dropLocation.getPath(); 
            DefaultMutableTreeNode target = (DefaultMutableTreeNode)destination.getLastPathComponent(); 
            TreePath path = tree.getPathForRow(selectedRows[0]);  
            DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode)path.getLastPathComponent();
            
            if (firstNode.getChildCount() > 0 && firstNode.isNodeChild(target)) {
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
            
            /*
            *   Add
            */
            for (DefaultMutableTreeNode node : nodes) {
                model.insertNodeInto(node, parent, index);
                tree.expandPath(new TreePath(node.getPath()));
            } //for
            
            tree.expandPath(new TreePath(parent.getPath()));
            
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
    
    private static class OneLevelSelection implements TreeSelectionListener {
        @Override public void valueChanged(TreeSelectionEvent e) {
            JTree tree = (JTree)e.getSource();
            TreeSelectionModel selectionModel = tree.getSelectionModel();
            
            TreePath[] selectedPaths = tree.getSelectionPaths();
            
            if (selectedPaths == null) {
                return;
            } //if
            
            TreePath[] changedPaths = e.getPaths();
            
            ArrayList<TreePath> addedPaths = new ArrayList<TreePath>();
            
            DefaultMutableTreeNode parent = null;
            
            /*
            *   Retrieve all added paths in changed paths
            *
            *   If a path was removed, set that to the path
            */
            for (TreePath path : changedPaths) {
                if (e.isAddedPath(path)) {
                    addedPaths.add(path);
                } //if
                else {
                    parent = (DefaultMutableTreeNode)path.getLastPathComponent();
                } //else
            } //for
            
            /*
            *   If no path was found, look in the currently selected paths
            */
            if (parent == null) {
                for (TreePath path : tree.getSelectionPaths()) {
                    if (!addedPaths.contains(path)) {
                        parent = (DefaultMutableTreeNode)path.getLastPathComponent();
                        
                        break;
                    } //if
                } //for
            } //if
            
            /*
            *   If a path was still not found, all components have the same
            *   path
            */
            if (parent != null) {
                /*
                *   Retrieve the parent of the path
                */
                parent = (DefaultMutableTreeNode)parent.getParent();
                
                ArrayList<TreePath> keep = new ArrayList<TreePath>();
                
                for (TreePath path : selectedPaths) {
                    if (((DefaultMutableTreeNode)path.getLastPathComponent()).getParent() == parent) {
                        keep.add(path);
                    } //if
                } //for
                
                if (keep.size() > 0) {
                    selectionModel.setSelectionPaths(keep.toArray(new TreePath[keep.size()]));
                } //if
            } //if
        } //valueChanged
    } //OneLevelSelection
    
    private static class SortedSelection implements TreeSelectionListener {
        private static class Compare implements java.util.Comparator {
            private JTree tree;
            
            public Compare(JTree tree) {
                this.tree = tree;
            } //Compare
            
            @Override public int compare(Object left, Object right) {
                /*
                *   -       left < right
                *   0       left == right
                *   +       left > right
                */
                
                return tree.getRowForPath((TreePath)right) - tree.getRowForPath((TreePath)left);
            } //compare
        } //Compare
        
        @Override public void valueChanged(TreeSelectionEvent e) {
            JTree tree = (JTree)e.getSource();
            TreeSelectionModel selectionModel = tree.getSelectionModel();
            
            TreePath[] selectedPaths = tree.getSelectionPaths();
            
            if (selectedPaths == null) {
                return;
            }
            
            java.util.List<TreePath> selectedPathList = Arrays.asList(selectedPaths);
            
            java.util.Collections.sort(selectedPathList, new Compare(tree));
            
            selectionModel.setSelectionPaths(selectedPathList.toArray(selectedPaths));
        } //valueChanged
    } //SortedSelection
    
    private static class DeleteElement extends KeyAdapter {
        @Override public void keyTyped(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == KeyEvent.VK_DELETE) {
                JTree tree = (JTree)keyEvent.getSource();
                DefaultTreeModel model = (DefaultTreeModel)(tree.getModel());
                
                TreePath[] selectedPaths = tree.getSelectionPaths();
                
                if (selectedPaths == null) {
                    return;
                } //if
                
                for (TreePath path : selectedPaths) {
                    model.removeNodeFromParent((DefaultMutableTreeNode)path.getLastPathComponent());
                } //for
            } //if
        } //keyTyped
    } //DeleteElement
    
    public CanvasEditor() {
        setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
        
        setRootVisible(false);
        
        Renderer renderer = new Renderer();
        
        setCellRenderer(renderer);
        setCellEditor(new Editor(this, renderer));
        setEditable(true);
        
        setDragEnabled(true);
        setDropMode(DropMode.ON_OR_INSERT);
        
        getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        setAutoscrolls(true);
        
        setTransferHandler(new Transfer());
        
        addTreeSelectionListener(new OneLevelSelection());
        addTreeSelectionListener(new SortedSelection());
        
        addKeyListener(new DeleteElement());
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
package editor.canvas;

import java.util.EventObject;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Component;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.awt.event.MouseEvent;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;

import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;

import static javax.swing.TransferHandler.COPY;
import static javax.swing.TransferHandler.MOVE;
import static javax.swing.TransferHandler.COPY_OR_MOVE;

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
    
    public CanvasEditor() {
        setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
        
        Renderer renderer = new Renderer();
        
        setCellRenderer(renderer);
        setCellEditor(new Editor(this, renderer));
        setEditable(true);
        
        setDragEnabled(true);
        
        getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        setAutoscrolls(true);
        
        setRowHeight(25);
        
        setPreferredSize(new java.awt.Dimension(300, 300));
    } //CanvasEditor
} //CanvasEditor

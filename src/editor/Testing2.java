package editor;

import editor.canvas.Canvas;
import editor.Components;
import editor.toolbars.Toolbar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import editor.canvas.*;
import javax.swing.tree.*;

public class Testing2 {
    private static JLabel getLabel(String str) {
        JLabel label = new JLabel(str);
        
        label.setOpaque(true);
        
        return label;
    }
    
    private static void addNode(DefaultTreeModel canvasTree, DefaultMutableTreeNode parent, Component component, int position) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(component);
        
        node.setAllowsChildren(false);
        
        canvasTree.insertNodeInto(node, parent, position);
    }
    
    public static void populateCanvas(DefaultTreeModel canvasTree, DefaultMutableTreeNode parent, String[] str) {
        DefaultMutableTreeNode node;
        
        for (int i = 0; i < str.length; i++)
            addNode(canvasTree, parent, getLabel(str[i]), 0);

        
        JPanel ipanel = new JPanel();
        ipanel.setLayout(new BoxLayout(ipanel, BoxLayout.LINE_AXIS));
        ipanel.setPreferredSize(new Dimension(100, 25));
        Dimension d = new Dimension(15, 0);
        ipanel.add(new Box.Filler(d, d, d));
        ipanel.add(new JCheckBox());        
        JTextField field = new JTextField();
        field.setColumns(15);
        ipanel.add(field);
        addNode(canvasTree, parent, ipanel, str.length);
        
        ipanel = new JPanel();
        ipanel.setLayout(new BoxLayout(ipanel, BoxLayout.LINE_AXIS));
        ipanel.setPreferredSize(new Dimension(100, 25));
        Components.addButton("testing", 0f, ipanel, 100, 50);
        addNode(canvasTree, parent, ipanel, str.length);
        
        ipanel = new JPanel();
        ipanel.setLayout(new BoxLayout(ipanel, BoxLayout.LINE_AXIS));
        ipanel.setPreferredSize(new Dimension(100, 25));
        Components.addPanel(0f, ipanel, 100, 25);
        addNode(canvasTree, parent, ipanel, str.length);
    }
    
    /*public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                
                frame.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
                frame.setLocationRelativeTo(null);
                
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
                
                CanvasEditor editor = new CanvasEditor();
                
                DefaultTreeModel tree = (DefaultTreeModel)editor.getModel();
                DefaultMutableTreeNode nested = new DefaultMutableTreeNode(new JLabel("nested"));
                ((JLabel)nested.getUserObject()).setOpaque(true);
                
                populateCanvas(tree, (DefaultMutableTreeNode)tree.getRoot(), new String[]{"test 1", "test 2", "test 3"});
                tree.reload();
                
                tree.insertNodeInto(nested, (MutableTreeNode)tree.getRoot(), editor.getRowCount());
                
                populateCanvas(tree, nested, new String[]{"test 4", "test 5", "test 6"});
                
                //editor.setPreferredSize(new Dimension(300, 400));
                
                JScrollPane scrollPane = new JScrollPane(editor);
                frame.add(scrollPane, BorderLayout.CENTER);
                
                panel.add(scrollPane);
                
                frame.add(panel);
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                
                editor.expand();
            }
        });
    }*/
}
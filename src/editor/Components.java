package editor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

public final class Components {

    public static void addMenu(Container container) {
        JMenuBar jMenuBar1 = new javax.swing.JMenuBar();
        JMenu jMenu1 = new javax.swing.JMenu();
        JMenuItem jMenuItem1 = new javax.swing.JMenuItem();
        JMenuItem jMenuItem2 = new javax.swing.JMenuItem();
        JMenuItem jMenuItem3 = new javax.swing.JMenuItem();
        JMenu jMenu2 = new javax.swing.JMenu();
        JMenu jMenu3 = new javax.swing.JMenu();

        jMenu1.setText("File");

        jMenuItem1.setText("New Project");
        jMenu1.add(jMenuItem1);
        jMenuItem2.setText("Open Project");
        jMenu1.add(jMenuItem2);
        jMenuItem3.setText("Save Project");
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Something");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Something else");
        jMenuBar1.add(jMenu3);

        container.add(jMenuBar1);
    }

    public static void addButton(String name, float alignment, Container container, int width, int height) {
        JButton button = new JButton(name);

        button.setAlignmentX(alignment);

        Dimension dimension = new Dimension(width, height);
        button.setMaximumSize(dimension);
        button.setMinimumSize(dimension);
        button.setPreferredSize(dimension);

        //button.setTransferHandler(new TransferHandler("text"));
        container.add(button);
    }

    public static void addLabel(String name, float alignment, Container container, int width, int height/*, MouseListener listener*/) {
        JLabel label = new JLabel(name);

        label.setAlignmentX(alignment);

        Dimension dimension = new Dimension(width, height);
        label.setMaximumSize(dimension);
        label.setMinimumSize(dimension);
        label.setPreferredSize(dimension);

        container.add(label);
    }

    public static void addTextField(String name, float alignment, Container container, int width, int height) {
        JTextField textField = new JTextField(name);

        textField.setAlignmentX(alignment);

        Dimension dimension = new Dimension(width, height);
        textField.setMaximumSize(dimension);
        textField.setMinimumSize(dimension);
        textField.setPreferredSize(dimension);
        //textField.setDragEnabled(true);

        container.add(textField);
    }

    public static void addPanel(float alignment, Container container, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.setAlignmentX(alignment);

        Dimension dimension = new Dimension(width, height);
        panel.setMaximumSize(dimension);
        panel.setMinimumSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setBackground(Color.red);

        container.add(panel);
    }

    public static void addComponents(JPanel panel) {

        Dimension maximumS = new Dimension(0, 175);
        Dimension minimumS = new Dimension(0, 175);
        Dimension preferredS = new Dimension(0, 175);

        panel.add(new Box.Filler(minimumS, preferredS, maximumS));

        Dimension maximumSize = new Dimension(0, 10);
        Dimension minimumSize = new Dimension(0, 10);
        Dimension preferredSize = new Dimension(0, 10);

        /*
         addLabel(" Declare Function", Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addLabel(" Call Function", Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addLabel(" If_statements", Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addLabel(" Loops", Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addLabel(" Declare Variable", Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addLabel(" Assing Value to Variable", Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumS, preferredS, maximumS));
        
        
         addPanel(Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addPanel(Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addPanel(Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addPanel(Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addPanel(Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addPanel(Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
         addPanel(Component.CENTER_ALIGNMENT, panel, 145, 50);
         panel.add(new Box.Filler(minimumSize, preferredSize, maximumSize));
        
         */
    }
}

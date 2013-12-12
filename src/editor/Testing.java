package editor;

import editor.canvas.Canvas;
import editor.toolbars.Toolbar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import editor.canvas.*;

public class Testing {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                //Editor.createAndShowGUI();
                
                JFrame frame = new JFrame();
                
                frame.setTitle("Lists");
                frame.setSize(400, 400);
                frame.setLocationRelativeTo(null);
                
                JPanel panel = new JPanel();
                
                panel.setSize(new Dimension(400, 200));
                
                panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                
                /*
                    Toolbar
                */
                Toolbar toolbar = new Toolbar();
                
                toolbar.add(new JLabel("test 1"));
                toolbar.add(new JLabel("test 2"));
                toolbar.add(new JLabel("test 3"));
                
                toolbar.setSize(new Dimension(200, 300));
                
                //toolbar.setDropMode(DropMode.INSERT);
                
                panel.add(toolbar);
                
                /*
                    Canvas
                */
                Canvas canvas = new Canvas();
                CanvasModel canvasList = (CanvasModel)canvas.getModel();
                CanvasModel original = canvasList;
                
                JLabel label;
                
                label = new JLabel("test 1");
                label.setOpaque(true);
                canvasList.addElement(label);
                label = new JLabel("test 2");
                label.setOpaque(true);
                canvasList.addElement(label);
                label = new JLabel("test 3");
                label.setOpaque(true);
                canvasList.addElement(label);
                
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
                JScrollPane firstPane = pane;
                pane.setPreferredSize(new Dimension(200, 300));
                
                panel.add(pane);
                
                canvas = new Canvas();
                canvasList = (CanvasModel)canvas.getModel();
                
                label = new JLabel("test 1");
                label.setOpaque(true);
                canvasList.addElement(label);
                label = new JLabel("test 2");
                label.setOpaque(true);
                canvasList.addElement(label);
                label = new JLabel("test 3");
                label.setOpaque(true);
                canvasList.addElement(label);
                
                ipanel = new JPanel();
                ipanel.setLayout(new BoxLayout(ipanel, BoxLayout.LINE_AXIS));
                ipanel.setPreferredSize(new Dimension(200, 25));
                ipanel.add(new JCheckBox());
                field = new JTextField();
                field.setColumns(15);
                ipanel.add(field);
                canvasList.addElement(ipanel);
                
                canvas.setDropMode(DropMode.INSERT);
                
                pane = new JScrollPane(canvas);
                pane.setPreferredSize(new Dimension(200, 90));
                
                JPanel newPanel = new JPanel();
                newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.LINE_AXIS));
                Dimension dimension = new Dimension(15, 0);
                newPanel.add(new Box.Filler(dimension, dimension, dimension));
                newPanel.add(pane);
                
                original.addElement(newPanel);
                
                panel.add(firstPane);
                
                frame.add(panel);
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
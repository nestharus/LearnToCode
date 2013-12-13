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
                
                frame.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
                frame.setLocationRelativeTo(null);
                
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
                
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
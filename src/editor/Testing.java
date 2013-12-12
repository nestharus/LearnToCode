package editor;

import editor.toolbars.Toolbar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.*;
import javax.swing.*;

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
                DefaultListModel<Object> canvasList = (DefaultListModel<Object>)canvas.getModel();
                
                canvasList.addElement("test 1");
                canvasList.addElement("test 2");
                canvasList.addElement("test 3");
                canvasList.addElement("test 1");
                
                canvas.setSize(new Dimension(200, 300));
                
                canvas.setDropMode(DropMode.INSERT);
                
                panel.add(canvas);
                
                frame.add(panel);
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
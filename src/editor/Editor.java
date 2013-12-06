package editor;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

public final class Editor extends JFrame {
    public Editor() {
        init();
        addComponents();
        
        setVisible(true);
        
        /*
            Show that windows close correctly
        */
        /*
            javax.swing.JFrame window = new javax.swing.JFrame();

            window.setSize(200, 300);
            window.setLocationRelativeTo(this);
            window.setVisible(true);
        */
    } //Editor
    
    private void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        JPanel top = new JPanel();
        panel.add(top, BorderLayout.PAGE_START);
        
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        
        /*
            Add context menu here
        */
        
        JPanel center = new JPanel();
        //not sure what the layout of the center space should be, or what it should have in it
        //this is where the widgets are dragged on to, so we'll need a MouseMotion event and a MouseAdapter thing too
        
        add(panel);
    } //addComponents
    
    private void init() {
        /*
                The name of the program
        */
        setTitle("Learn To Code");
        
        /*
                Set window size to take up entire screen by default
                
                Center window
        */
        setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                /*
                        add a prompt to save first
                */
                
                
                /*
                        close all windows
                */
                System.gc();  
                
                WindowEvent closingEvent;
                
                for (java.awt.Window window : java.awt.Window.getWindows()) {
                    if (!window.equals(windowEvent.getSource())) {
                        closingEvent = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
                        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
                    }
                } //for
                
                /*
                    terminate current window
                */
                System.exit(0);
            } //windowClosing
        });
    } //init
}

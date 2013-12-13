package editor.browsers;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.Dimension;
import java.awt.GridLayout;

public class StringBrowser extends JPanel
                      implements TreeSelectionListener {
    //private JEditorPane editorPane;
    private JTree tree;
    
    private static boolean DEBUG = false;

    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
    
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;

    public StringBrowser() {
        super(new GridLayout(1,0));

        //Create the nodes:
        DefaultMutableTreeNode top =
            new DefaultMutableTreeNode("All Functions");
        createNodes(top);

        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);
        
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        // ==========================================================
        // =============== Splitting into browsers: =================
        // ==========================================================
        
        JScrollPane boolBrowser = new JScrollPane(tree);
        Dimension minimumSize = new Dimension(100, 50);
        boolBrowser.setMinimumSize(minimumSize);
        add(boolBrowser);
        
        /*      
        //Create the scroll pane and add the tree to it:
        
        JScrollPane boolBrowser = new JScrollPane(tree);
        JScrollPane funcBrowser = new JScrollPane();
        JScrollPane numBrowser = new JScrollPane();
        JScrollPane strBrowser = new JScrollPane();
        JScrollPane valBrowser = new JScrollPane();

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        splitPane.setTopComponent(boolBrowser); // HERE!
        splitPane.setBottomComponent(splitPane1);
        
        splitPane1.setTopComponent(funcBrowser); // HERE!
        splitPane1.setBottomComponent(splitPane2);

        splitPane2.setTopComponent(numBrowser); // HERE!
        splitPane2.setBottomComponent(splitPane3);
        
        splitPane3.setTopComponent(strBrowser); // HERE!
        splitPane3.setBottomComponent(valBrowser);
        
        Dimension minimumSize = new Dimension(100, 50);
        boolBrowser.setMinimumSize(minimumSize);
        funcBrowser.setMinimumSize(minimumSize);
        numBrowser.setMinimumSize(minimumSize);
        strBrowser.setMinimumSize(minimumSize);
        valBrowser.setMinimumSize(minimumSize);
        
        splitPane.setDividerLocation(100); 
        splitPane.setPreferredSize(new Dimension(500, 300));

        //Add the split pane to this panel.
        add(splitPane);*/
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           tree.getLastSelectedPathComponent();

        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        
        if (DEBUG) {
            System.out.println(nodeInfo.toString());
        }
    }

    private class BookInfo {
        public String bookName;
        
        public BookInfo(String book) {
            bookName = book;
        }

        public String toString() {
            return bookName;
        }
    }

    private void createNodes(DefaultMutableTreeNode top) {
        //DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        // Where category is a sub-directory:

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForA()"));
        top.add(book);

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForAa()"));
        top.add(book);

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForAb()"));
        top.add(book);

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForAc()"));
        top.add(book);

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForB()"));
        top.add(book);

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForBa()"));
        top.add(book);

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForBb()"));
        top.add(book);

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForC()"));
        top.add(book);

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForCa()"));
        top.add(book);

        //Function:
        book = new DefaultMutableTreeNode(new BookInfo
            ("checkForCb()"));
        top.add(book);
    }
        
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("Function Browser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new StringBrowser());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

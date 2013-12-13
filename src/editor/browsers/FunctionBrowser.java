package editor.browsers;

/*
*   function browser
*           list of all functions organized by folder (JTree)
*           search box to search for functions with keywords in them
*           filter by return type (check boxes?)
*/

import javax.swing.Box;
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
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FunctionBrowser extends JPanel
                      implements TreeSelectionListener {
    //private JEditorPane editorPane;
    private JTree funcTree;
    private JTree boolTree;
    private JTree numTree;
    private JTree strTree;
    private JTree valTree;
    
    private static boolean DEBUG = false;

    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
    
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;

    public FunctionBrowser() {
        super(new GridLayout(1,0));

        // ==========================================================
        // =================== Function browser: ====================
        // ==========================================================
        
        //Create the nodes:
        DefaultMutableTreeNode funcRoot =
            new DefaultMutableTreeNode("All Functions");
        funcNodes(funcRoot);

        //Create a tree that allows one selection at a time.
        funcTree = new JTree(funcRoot);
        funcTree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        funcTree.addTreeSelectionListener(this);
        
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            funcTree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        // ==========================================================
        // =================== Booleans browser: ====================
        // ==========================================================
        
        // Custom tree node icons do not work at the moment.
        ImageIcon leafIcon = createImageIcon("../../../images/switch.gif");
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer = 
                new DefaultTreeCellRenderer();
            renderer.setLeafIcon(leafIcon);
            boolTree.setCellRenderer(renderer);
        }

        //Create the nodes:
        DefaultMutableTreeNode boolRoot =
            new DefaultMutableTreeNode("All Booleans");
        boolNodes(boolRoot);

        //Create a tree that allows one selection at a time.
        boolTree = new JTree(boolRoot);
        boolTree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        boolTree.addTreeSelectionListener(this);
        
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            boolTree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        // ==========================================================
        // ==================== Numbers browser: ====================
        // ==========================================================
        
        //Create the nodes:
        DefaultMutableTreeNode numRoot =
            new DefaultMutableTreeNode("All Numbers");
        numNodes(numRoot);

        //Create a tree that allows one selection at a time.
        numTree = new JTree(numRoot);
        numTree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        numTree.addTreeSelectionListener(this);
        
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            numTree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        // ==========================================================
        // ==================== Strings browser: ====================
        // ==========================================================
        
        //Create the nodes:
        DefaultMutableTreeNode strRoot =
            new DefaultMutableTreeNode("All Strings");
        strNodes(strRoot);

        //Create a tree that allows one selection at a time.
        strTree = new JTree(strRoot);
        strTree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        strTree.addTreeSelectionListener(this);
        
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            strTree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        // ==========================================================
        // ==================== Values browser: =====================
        // ==========================================================
        
        //Create the nodes:
        DefaultMutableTreeNode valRoot =
            new DefaultMutableTreeNode("All Values");
        valNodes(valRoot);

        //Create a tree that allows one selection at a time.
        valTree = new JTree(valRoot);
        valTree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        valTree.addTreeSelectionListener(this);
        
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            valTree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        // ==========================================================
        // =============== Splitting into browsers: =================
        // ==========================================================
        
        // The gap above the first browser:
        int y = 41;
        Dimension maximumS = new Dimension(0, y);
        Dimension minimumS = new Dimension(0, y);
        Dimension preferredS = new Dimension(0, y);
        add(new Box.Filler(minimumS, preferredS, maximumS));
        
        //Create the scroll pane and add the tree to it:
        JScrollPane funcBrowser = new JScrollPane(funcTree);
        JScrollPane boolBrowser = new JScrollPane(boolTree);
        JScrollPane numBrowser = new JScrollPane(numTree);
        JScrollPane strBrowser = new JScrollPane(strTree);
        JScrollPane valBrowser = new JScrollPane(valTree);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        splitPane.setTopComponent(funcBrowser); // HERE!
        splitPane.setBottomComponent(splitPane1);
        
        splitPane1.setTopComponent(boolBrowser); // HERE!
        splitPane1.setBottomComponent(splitPane2);

        splitPane2.setTopComponent(numBrowser); // HERE!
        splitPane2.setBottomComponent(splitPane3);
        
        splitPane3.setTopComponent(strBrowser); // HERE!
        splitPane3.setBottomComponent(valBrowser);
        
        Dimension minimumSize = new Dimension(100, 50);
        funcBrowser.setMinimumSize(minimumSize);
        boolBrowser.setMinimumSize(minimumSize);
        numBrowser.setMinimumSize(minimumSize);
        strBrowser.setMinimumSize(minimumSize);
        valBrowser.setMinimumSize(minimumSize);
        
        splitPane.setDividerLocation(100); 
        splitPane.setPreferredSize(new Dimension(500, 300));

        //Add the split pane to this panel.
        add(splitPane);
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FunctionBrowser.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           funcTree.getLastSelectedPathComponent();

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

    private void funcNodes(DefaultMutableTreeNode root) {
        //DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        // Where category is a sub-directory:

        book = new DefaultMutableTreeNode(new BookInfo
            ("Example()"));
        root.add(book);

        book = new DefaultMutableTreeNode(new BookInfo
            ("Nested Example()"));
        root.add(book);
    }
        
    private void boolNodes(DefaultMutableTreeNode root) {
        //DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        // Where category is a sub-directory:

        book = new DefaultMutableTreeNode(new BookInfo
            ("[unnamed] (Example())"));
        root.add(book);

        book = new DefaultMutableTreeNode(new BookInfo
            ("[unnamed] (Nested Example())"));
        root.add(book);
    }
        
    private void numNodes(DefaultMutableTreeNode root) {
        //DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        // Where category is a sub-directory:
        
        book = new DefaultMutableTreeNode(new BookInfo
            ("[empty]"));
        root.add(book);
    }
        
    private void strNodes(DefaultMutableTreeNode root) {
        //DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        // Where category is a sub-directory:
        
        book = new DefaultMutableTreeNode(new BookInfo
            ("testing (Example())"));
        root.add(book);
        
        book = new DefaultMutableTreeNode(new BookInfo
            ("testing (Nested Example())"));
        root.add(book);
    }
        
    private void valNodes(DefaultMutableTreeNode root) {
        //DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        // Where category is a sub-directory:
        
        book = new DefaultMutableTreeNode(new BookInfo
            ("[empty]"));
        root.add(book);
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
        frame.add(new FunctionBrowser());

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


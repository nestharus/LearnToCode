package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY_OR_MOVE;
import static javax.swing.TransferHandler.MOVE;
import javax.swing.UIManager;

/**
 *
 * @author Lisette
 */
public class Editor extends JFrame {

    DefaultListModel from = new DefaultListModel();
    JList dragFrom;

    public Editor() {
        super("Learn To Code");

        from.add(0, "Function");
        from.add(0, "Function Call");
        from.add(0, "If-statement");
        from.add(0, "Loop");
        from.add(0, "Variable Declaration");
        from.add(0, "Variable Assignment");

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
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
        p.add(jMenuBar1);

        add(p, BorderLayout.NORTH);

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        Dimension maximumS = new Dimension(0, 25);
        Dimension minimumS = new Dimension(0, 25);
        Dimension preferredS = new Dimension(0, 25);
        p.add(new Box.Filler(minimumS, preferredS, maximumS));

        dragFrom = new JList(from);
        dragFrom.setTransferHandler(new FromTransferHandler());
        dragFrom.setPrototypeCellValue("List Item WWWWWW");
        dragFrom.setDragEnabled(true);
        dragFrom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JLabel label = new JLabel("Drag from here:");
        label.setAlignmentX(0f);
        p.add(label);
        JScrollPane sp = new JScrollPane(dragFrom);
        sp.setAlignmentX(0f);
        p.add(sp);
        add(p, BorderLayout.WEST);

        DragDropList copyTo = new DragDropList();
        copyTo.setTransferHandler(new ToTransferHandler(TransferHandler.COPY));
        copyTo.setDropMode(DropMode.INSERT);

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(new Box.Filler(minimumS, preferredS, maximumS));

        label = new JLabel("Drop to COPY to here:");  //NEEDS TO BE CHANGED
        label.setAlignmentX(0f);
        p.add(label);

        sp = new JScrollPane(copyTo);
        sp.setAlignmentX(0f);
        p.add(sp);
        p.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        add(p, BorderLayout.CENTER);
        
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(new Box.Filler(minimumS, preferredS, maximumS));

        label = new JLabel("PANEL:");  //NEEDS TO BE CHANGED
        label.setAlignmentX(0f);
        p.add(label);

        //Example
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.setAlignmentX(0f);

        Dimension dimension = new Dimension(70, 50);
        panel.setMaximumSize(dimension);
        panel.setMinimumSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setBackground(Color.red);
        p.add(panel);        
        
        p.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        add(p, BorderLayout.EAST);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        getContentPane().setPreferredSize(new Dimension(1700, 690));
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        Editor test = new Editor();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        test.pack();
        test.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }

    class FromTransferHandler extends TransferHandler {

        public int getSourceActions(JComponent comp) {
            return COPY_OR_MOVE;
        }

        private int index = 0;

        public Transferable createTransferable(JComponent comp) {
            index = dragFrom.getSelectedIndex();
            if (index < 0 || index >= from.getSize()) {
                return null;
            }

            return new StringSelection((String) dragFrom.getSelectedValue());
        }

        public void exportDone(JComponent comp, Transferable trans, int action) {
            if (action != MOVE) {
                return;
            }

            from.removeElementAt(index);
        }
    }

    class ToTransferHandler extends TransferHandler {

        int action;

        public ToTransferHandler(int action) {
            this.action = action;
        }

        public boolean canImport(TransferHandler.TransferSupport support) {
            // only support drops (not clipboard paste)
            if (!support.isDrop()) {
                return false;
            }

            // only import Strings
            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }

            boolean actionSupported = (action & support.getSourceDropActions()) == action;
            if (actionSupported) {
                support.setDropAction(action);
                return true;
            }

            return false;
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            // if we can't handle the import, say so
            if (!canImport(support)) {
                return false;
            }

            // fetch the drop location
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();

            int index = dl.getIndex();

            // fetch the data and bail if this fails
            String data;
            try {
                data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            }

            JList list = (JList) support.getComponent();
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.insertElementAt(data, index);

            Rectangle rect = list.getCellBounds(index, index);
            list.scrollRectToVisible(rect);
            list.setSelectedIndex(index);
            list.requestFocusInWindow();

            return true;
        }
    }

    public class DragDropList extends JList {

        DefaultListModel model;

        public DragDropList() {
            super(new DefaultListModel());
            model = (DefaultListModel) getModel();
            setDragEnabled(true);
            setDropMode(DropMode.INSERT);  

            setTransferHandler(new MyListDropHandler(this));

            new MyDragListener(this);

            //model.addElement("a");
        }

    }

    class MyDragListener implements DragSourceListener, DragGestureListener {

        DragDropList list;

        DragSource ds = new DragSource();

        public MyDragListener(DragDropList list) {
            this.list = list;
            DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(list,
                    DnDConstants.ACTION_COPY_OR_MOVE, this);    
            //ACTION_MOVE does not allow for drag and drop on the same JList
            //and using ACTION_COPY_OR_MOVE or ACTION_COPY it is copying the index
            //not moving the actual string :(
        }

        public void dragGestureRecognized(DragGestureEvent dge) {
            StringSelection transferable = new StringSelection(Integer.toString(list.getSelectedIndex()));
            ds.startDrag(dge, DragSource.DefaultCopyDrop, transferable, this);
        }

        public void dragEnter(DragSourceDragEvent dsde) {
        }

        public void dragExit(DragSourceEvent dse) {
        }

        public void dragOver(DragSourceDragEvent dsde) {
        }

        public void dragDropEnd(DragSourceDropEvent dsde) {
            if (dsde.getDropSuccess()) {
                System.out.println("Succeeded");
            } else {
                System.out.println("Failed");
            }
        }

        public void dropActionChanged(DragSourceDragEvent dsde) {
        }
    }

    class MyListDropHandler extends TransferHandler {

        DragDropList list;

        public MyListDropHandler(DragDropList list) {
            this.list = list;
        }

        public boolean canImport(TransferHandler.TransferSupport support) {
            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            if (dl.getIndex() == -1) {
                return false;
            } else {
                return true;
            }
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            Transferable transferable = support.getTransferable();
            String indexString;
            try {
                indexString = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return false;
            }

            int index = Integer.parseInt(indexString);
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            int dropTargetIndex = dl.getIndex();

            System.out.println(dropTargetIndex + " : ");
            System.out.println("inserted");
            return true;
        }
    }

}

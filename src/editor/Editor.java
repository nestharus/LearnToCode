package editor;

import dragdrop.DragDropList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
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
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY_OR_MOVE;
import static javax.swing.TransferHandler.MOVE;

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
   
        Components.addMenu(p); //Adding Menu
        add(p, BorderLayout.NORTH);

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        Dimension maximumS = new Dimension(0, 25);
        Dimension minimumS = new Dimension(0, 25);
        Dimension preferredS = new Dimension(0, 25);
        p.add(new Box.Filler(minimumS, preferredS, maximumS)); //Just leaving a space between the menu and the labels/lists

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

        p.add(new Box.Filler(minimumS, preferredS, maximumS)); //Space between the first label and list

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

        p.add(new Box.Filler(minimumS, preferredS, maximumS)); //Space between the second label and list

        label = new JLabel("PANEL:");  //NEEDS TO BE CHANGED
        label.setAlignmentX(0f);
        p.add(label);

        //Example of adding components
        Components.addPanel(0f, p, 170, 100);
                
        p.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        add(p, BorderLayout.EAST);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        getContentPane().setPreferredSize(new Dimension(1700, 690));
    }

    public static void createAndShowGUI() {
        //Create and set up the window.
        Editor test = new Editor();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        test.pack();
        test.setVisible(true);
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
}
package dragdrop;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import javax.swing.DropMode;

/*
*   Stores a list of elements that can be dragged
*/
public class DragDropList extends JList {
    private DefaultListModel model;

    public DragDropList() {
        super(new DefaultListModel());
        model = (DefaultListModel)getModel();
        
        setDragEnabled(true);
        setDropMode(DropMode.INSERT);

        setTransferHandler(new ListDropHandler(this));

        new DragListener(this);
    }

}
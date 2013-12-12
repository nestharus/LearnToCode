package dragdrop;

import dragdrop.DragDropList;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JList;
import javax.swing.TransferHandler;

/*
*   Used to transfer a component from
*   the toolbar to a canvas
*/
class ListDropHandler extends TransferHandler {
    DragDropList list;

    public ListDropHandler(DragDropList list) {
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
        return true;
    }
}


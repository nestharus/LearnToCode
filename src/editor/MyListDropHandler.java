/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package editor;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 *
 * @author Lisette
 */
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
        return true;
    }
}


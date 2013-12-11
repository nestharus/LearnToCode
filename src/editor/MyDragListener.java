/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package editor;

import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

/**
 *
 * @author Lisette
 */
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

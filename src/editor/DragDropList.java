/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package editor;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;

/**
 *
 * @author Lisette
 */
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
package editor.toolbars;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;

import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.MOVE;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import javax.swing.DropMode;
import java.awt.Component;

import javax.swing.JToolBar;

//JSplitPane

/*
*   Contains list of tools that can be dragged to Canvas
*/
public class Toolbar extends JToolBar {
    public Toolbar() {
        
        setOrientation(JToolBar.TOP);
        
        //setDragEnabled(true);
        
        //getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        /*
        dragFrom.setTransferHandler(new FromTransferHandler());
        */
    }
}
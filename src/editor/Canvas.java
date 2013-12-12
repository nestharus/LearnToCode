package editor;

import editor.toolbars.Toolbar;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;

import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY;
import static javax.swing.TransferHandler.MOVE;

import static javax.swing.TransferHandler.COPY_OR_MOVE;

public class Canvas extends JList {
    public Canvas() {
        super(new DefaultListModel<Object>());
        
        setDragEnabled(true);
        
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        
        setTransferHandler(new TransferHandler() {
            private final DataFlavor localObjectFlavor = 
                    new ActivationDataFlavor (
                        Object[].class,
                        DataFlavor.javaJVMLocalObjectMimeType,
                        "List of elements"
                    );
            
            /*
            *   Bundles data up in preparation for transfer
            */
            @Override protected Transferable createTransferable(JComponent component) {
                return new DataHandler(((JList)component).getSelectedValuesList().toArray(), localObjectFlavor.getMimeType());
            } //Transferable

            /*
            *   What actions transfer works with
            */
            @Override public int getSourceActions(JComponent component) {
                return COPY_OR_MOVE;
            } //getSourceActions

            /*
            *   Called when transfer is done
            */
            @Override protected void exportDone(JComponent component, Transferable data, int action) {
                JList list = (JList)component;
                DefaultListModel listModel = (DefaultListModel)list.getModel();
                
                int[] indices = list.getSelectedIndices();
                
                for (int index = indices.length - 1; index >= 0; --index) {
                    listModel.removeElementAt(indices[index]);
                }
            } //exportDone

            /*
            *   Calls component mouse is currently over to see if it supports drop or not
            */
            @Override public boolean canImport(TransferHandler.TransferSupport info) {
                /*
                *   Can only take from the toolbar or the canvas
                */

                Class componentClass = info.getComponent().getClass();

                return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor) && (componentClass == Toolbar.class || componentClass == Canvas.class);
            } //canImport

            /*
            *   Called when data is dropped on to the component. Returns true if import
            *   was successful.
            */
            @Override public boolean importData(TransferHandler.TransferSupport info) {
                if(!canImport(info)) {
                    return false;
                }

                JList target = (JList)info.getComponent();
                JList.DropLocation dropLocation = (JList.DropLocation)info.getDropLocation();
                DefaultListModel listModel = (DefaultListModel)target.getModel();

                int index = dropLocation.getIndex();
                int max = listModel.getSize();

                if(index < 0 || index > max) {
                    index = max;
                }

                try {
                    Object[] elements = (Object[])info.getTransferable().getTransferData(localObjectFlavor);
                    
                    for (Object element : elements) {
                        listModel.add(index++, element);
                    }
                    
                    return true;
                } //try
                catch(Exception exception) {
                    exception.printStackTrace();
                } //catch

                return false;
            } //importData
        });
    } //Canvas
} //Canvas
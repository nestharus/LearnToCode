/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package editor;

import javax.swing.JFrame;

/**
 *
 * @author nes
 */
public final class Editor extends JFrame {
    public Editor() {
          setTitle("Editor");
          setSize(300, 200);
          setLocationRelativeTo(null);
          
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          setVisible(true);
    }
}

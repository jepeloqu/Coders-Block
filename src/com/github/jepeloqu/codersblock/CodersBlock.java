package com.github.jepeloqu.codersblock;



import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;

/**
 * CodersBlock --- entry point for Coders Block educational program
 * @author    Joe Peloquin
 */

public class CodersBlock extends JFrame {
            
   public CodersBlock() {
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      setSize(dim.width, dim.height);
      //setResizable(false);
      
      Screen screen = new Screen();
      add(screen);   
      
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setVisible(true);
   }

   public static void main(String[] args) {
      CodersBlock codersBlock = new CodersBlock();
   }   
}
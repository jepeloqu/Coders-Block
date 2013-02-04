package com.github.jepeloqu.horizontalmovement;

import javax.swing.JFrame;

/**
 * HorizontalMovement --- entry point for Coders Block - Horizontal Movement tutorial program
 * @author Joe
 */
public class HorizontalMovement extends JFrame {
   private Screen screen;
   
   public HorizontalMovement() {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(990, 510);                        // width/height one tile less than we draw on screen
      screen = new Screen();
      add(screen);
      setVisible(true);
   }
   
   public static void main(String[] arguments) {
      HorizontalMovement horizontalMovement = new HorizontalMovement();
   }  
}

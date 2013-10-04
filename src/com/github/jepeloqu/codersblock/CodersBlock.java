package com.github.jepeloqu.codersblock;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * CodersBlock --- entry point for Coders Block educational program
 * @author    Joe Peloquin
 */

public class CodersBlock extends JFrame implements ActionListener, KeyListener {
   private Screen screen;
   private Timer timer;
   
   private boolean leftPressed, rightPressed, spacePressed;
            
   public CodersBlock() {
      
      screen = new Screen();
      add(screen);  
      
      setFocusable(true);
      setResizable(false);
      setUndecorated(false);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      pack();
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
      setVisible(true);
      
      timer = new Timer(33, this);
      timer.start();
      addKeyListener(this);
   }

   public static void main(String[] args) {
      CodersBlock codersBlock = new CodersBlock();
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      screen.mainLoop(leftPressed, rightPressed, spacePressed);
   }

   @Override
   public void keyPressed(KeyEvent input) {
      if (input.getKeyCode() == KeyEvent.VK_D)
         rightPressed = true;
      else if (input.getKeyCode() == KeyEvent.VK_A)
         leftPressed = true;
      else if (input.getKeyCode() == KeyEvent.VK_SPACE)
         spacePressed = true;  
   }
    
   @Override
   public void keyReleased(KeyEvent input) {
      if (input.getKeyCode() == KeyEvent.VK_D)
         rightPressed = false;
      else if (input.getKeyCode() == KeyEvent.VK_A)
         leftPressed = false;
      else if (input.getKeyCode() == KeyEvent.VK_SPACE)
         spacePressed = false;
   }
    
   @Override
   public void keyTyped(KeyEvent input) {}
}
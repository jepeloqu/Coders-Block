package com.github.jepeloqu;



import javax.swing.JFrame;

/**
 * GameDisplay --- highest level structure in our game, holds the screen
 * @author    Joe Peloquin
 */

public class GameDisplay extends JFrame {
   GameScreen myScreen;
            
   public GameDisplay() {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(990, 510);
      myScreen = new GameScreen((int)this.getSize().getHeight());
      add(myScreen);   
      setUndecorated(true);
      setVisible(true);
   }

   public static void main(String[] args) {
      GameDisplay myGameDisplay = new GameDisplay();
   }   
}
package com.github.jepeloqu;



import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * Player --- Our player character
 * @author    Joe Peloquin
 */
public class Player extends Character {
    
   public Player(int xPosition, int yPosition) {
      super();
      x = xPosition;
      y = yPosition;
      
      jump = fall = false;
      stand = true;
      
      facingLeft = true;
      facingRight = false;
      
      updateImage();
      initializePhysicsValues();
      updateCollisionPoints();
   }
   
   /**
   * Initializes physics related fields
   */   
   private void initializePhysicsValues() {
      dx = 0;
      dy = 0;
      maxSpeed = 10;
      
      gravity = .5;
      groundDampingFactor = .80;
      airDampingFactor = .95;
      
      xGroundImpulse = 1;
      xAirImpulse = .2;
      yJumpImpulse = 10;
   }
   
  /**
   * Main loop executed every tick
   * 
   * @param level   current game level
   */ 
   @Override 
   public void mainLoop(GameLevel level) {
      processXMovement();
      checkAndResolveXCollision(level);
      if (!standing()){
         processYMovement();
         checkAndResolveYCollision(level);
      }
      if (standing()){
         checkFall(level);
      }
      applyDamping();
      applyGravity();
   }

   /**
   * Updates character to appropriate state based on current state and user input
   * 
   * @param leftPressed   true if the left arrow key is pressed
   * @param rightPressed  true if the right arrow key is pressed
   * @param spacePressed  true if the space bar is pressed
   */     
   public void updateState(boolean leftPressed, boolean rightPressed, boolean spacePressed) {
      //handle horizontal movement
      if(leftPressed || rightPressed) {
            if (leftPressed) {
               facingLeft = true;
               facingRight = false;
               updateImage();
               if (stand)
                  addImpulse(-xGroundImpulse, 0);
               else
                  addImpulse(-xAirImpulse, 0);
            }
            else if (rightPressed) {
               facingLeft = false;
               facingRight = true;
               updateImage();
               if (stand)
                  addImpulse(xGroundImpulse, 0);
               else
                  addImpulse(xAirImpulse, 0);
            }
      }
      
      //handle start of jump
      if (!jump && !fall && spacePressed) {
         jump = true;
         fall = false;
         stand = false;
         addImpulse(0, -yJumpImpulse);
      } 
   }
   
   private void updateImage() {
      if (facingLeft) {
         ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Player_Left.gif");
         characterImage = ii.getImage();
      }
      else if (facingRight) {
         ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Player_Right.gif");
         characterImage = ii.getImage();
      }
         
   }
}

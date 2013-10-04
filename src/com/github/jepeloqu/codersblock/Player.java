package com.github.jepeloqu.codersblock;



import java.io.File;
import javax.swing.ImageIcon;

/**
 * Player --- Our player character
 * @author    Joe Peloquin
 */
public class Player extends Character {
    
   public Player(int xPosition, int yPosition) {
      x = xPosition;
      y = yPosition;
      
      hitPoints = 10;
      
      jump = false;
      fall = false;
      stand = true;
      
      dead = false;
      
      facingLeft = true;
      facingRight = false;
      
      updateImage();
      initializePhysicsValues();
      updateHitBoxAndCollisionPoints();
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
   public void mainLoop(Level level, Enemy enemy, boolean leftPressed, boolean rightPressed, boolean spacePressed) {
      if(!dead)
         updateState(leftPressed, rightPressed, spacePressed);
      
      processXMovement();
      checkIfTouchingSpike(level);
      checkEnemyCollision(enemy);
      checkAndResolveXCollision(level);
      
      if (!standing()){
         processYMovement();
         checkIfTouchingSpike(level);
         checkEnemyCollision(enemy);
         checkAndResolveYCollision(level);
      }
      else if (standing())
         checkFall(level);
      
      applyGravity();
      applyDamping();
      updateImage();
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
   
   private void checkEnemyCollision(Enemy enemy) {
      
      if(hitBox.intersects(enemy.getHitBox()))
         takeDamage(1, System.currentTimeMillis());
   }
   
   private void takeDamage(int damage, long hitTime) {
      if((hitTime - lastHitTime) > 200) {
         hitPoints = hitPoints - damage;
         lastHitTime = hitTime;
         if(hitPoints <= 0) {
            hitPoints = 0;
            dead = true;
            updateImage();
         }
      }
   }
   
   private void updateImage() {
      ImageIcon ii;
      
      if (facingLeft) {
         if (!dead)
            ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Player_Left.gif");
         else
            ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Dead_Left.gif");
         characterImage = ii.getImage();
      }
      else if (facingRight) {
         if (!dead)
            ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Player_Right.gif");
         else
            ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Dead_Right.gif");
         characterImage = ii.getImage();
      } 
   }
}

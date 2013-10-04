package com.github.jepeloqu.codersblock;


import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

public class Enemy extends Character {
   private boolean playerSpotted;
   private boolean playerAbove;
   
   public Enemy(int xPosition, int yPosition) {
      super();
      x = xPosition;
      y = yPosition;
      
      jump = false;
      fall = false;
      stand = true;
      
      facingLeft = false;
      facingRight = true;
      
      playerSpotted = false;
      playerAbove = false;
      
      dead = false;
      
      updateImage();
      updateHitBoxAndCollisionPoints();
      
      initializePhysicsValues();
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
   * @param player  player object
   */ 
   public void mainLoop(Level level, Player player) {
      if(!dead) {
         checkIfPlayerSpotted(player);
         checkIfPlayerAbove(player);
         
         if(playerSpotted) {
            setXGroundImpulse(1.2);
            facePlayer(player);
            if(playerAbove)
               jump();
         }
         else
           setXGroundImpulse(1.0);
         
         walk();
         processXMovement();
         checkIfTouchingSpike(level);
         checkAndResolveXCollision(level);
      }
      
      if (!standing()) {
         processYMovement();
         checkIfTouchingSpike(level);
         checkAndResolveYCollision(level);
      }
      else if (standing()) {
         checkFall(level);
      }
      
      applyGravity();
      applyDamping();
      updateImage();
   }
   
   private void updateImage() {
      if (dead) {
         if (facingLeft) {
            ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Dead_Left.gif");
            characterImage = ii.getImage();
         }
         else if (facingRight) {
            ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Dead_Right.gif");
            characterImage = ii.getImage();
         }
      }
      else if (playerSpotted) {
         if (facingLeft) {
            ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "AngryEnemy_Left.gif");
            characterImage = ii.getImage();
         }
         else if (facingRight) {
            ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "AngryEnemy_Right.gif");
            characterImage = ii.getImage();
         }
      }
      else if (!playerSpotted) {
         if (facingLeft) {
            ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Enemy_Left.gif");
            characterImage = ii.getImage();
         }
         else if (facingRight) {
            ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Enemy_Right.gif");
            characterImage = ii.getImage();
         }
      }
   }
   
   private void facePlayer(Player player) {
      if(this.x < player.getX()) {
         facingLeft = false;
         facingRight = true;
      } 
      else if (this.x > player.getX()) {
         facingLeft = true;
         facingRight = false;
      }
   }
   
   private void checkIfPlayerSpotted(Player player) {
      if((Math.abs(this.x - player.getX()) <= 150) && (Math.abs(this.y - player.getY()) <= 150))
         if((this.x < player.getX()) && facingRight) {
            playerSpotted = true;
         } 
         else if((this.x > player.getX()) && facingLeft) {
            playerSpotted = true;
         } 
         else if (!(Math.abs(this.x - player.getX()) <= 30))
            playerSpotted = false;
   }
   
   private void checkIfPlayerAbove(Player player) {
      if(this.y > player.getY())
         playerAbove = true;
      else 
         playerAbove = false;
   }
   
   /**
   * Move in whatever direction currently facing
   */ 
   private void walk() {
      if (facingLeft) {
         updateImage();
         if (stand)
            addImpulse(-xGroundImpulse, 0);
         else
            addImpulse(-xAirImpulse, 0);
      }
      else if (facingRight) {
         updateImage();
         if (stand)
            addImpulse(xGroundImpulse, 0);
         else
            addImpulse(xAirImpulse, 0);
      }
   }
   
   /**
   * Checks for collisions in the horizontal axis and resolves them
   * Reverses direction on collision
   * 
   * @param level   current game level where collisions will be checked
   */ 
   @Override
   protected void checkAndResolveXCollision(Level level) {
      boolean leftCollision, rightCollision;
      
      leftCollision = rightCollision = false;
      
      //check left side collision
      while (level.level[Screen.worldCoordToTile(getBottomLeft().getY())][Screen.worldCoordToTile(getBottomLeft().getX())] == TileSet.GREEN_PLATFORM_TILE ||
             level.level[Screen.worldCoordToTile(getTopLeft().getY())][Screen.worldCoordToTile(getTopLeft().getX())] == TileSet.GREEN_PLATFORM_TILE) {
         leftCollision = true;
         processXCollision(leftCollision, rightCollision);
      }
      
      //check right side collision
      while (level.level[Screen.worldCoordToTile(getBottomRight().getY())][Screen.worldCoordToTile(getBottomRight().getX())] == TileSet.GREEN_PLATFORM_TILE ||
             level.level[Screen.worldCoordToTile(getTopRight().getY())][Screen.worldCoordToTile(getTopRight().getX())] == TileSet.GREEN_PLATFORM_TILE) {
         rightCollision = true;
         processXCollision(leftCollision, rightCollision);
      }
      
      
      if(leftCollision || rightCollision) {
         if (playerSpotted)
            jump();
         else
            reverseDirection(leftCollision, rightCollision);
      }
   }
   
   public void jump() {
      if(stand) {
         addImpulse(0, -yJumpImpulse);
         stand = false;
         fall = false;
         jump = true;
      }
   }
   
   public void reverseDirection(boolean leftCollision, boolean rightCollision) {
      if (leftCollision) {
         facingLeft = false;
         facingRight = true;
         dx = 0;
      }  
      else if (rightCollision) {
         facingLeft = true;
         facingRight = false;
         dx = 0;
      }
   }
}
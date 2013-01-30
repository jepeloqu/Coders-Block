package com.github.jepeloqu;


import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

public class Enemy extends Character {
   
   public Enemy(int xPosition, int yPosition) {
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
      walkRoutine();
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
   
   private void updateImage() {
      if (facingLeft) {
         ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Enemy_Left.gif");
         characterImage = ii.getImage();
      }
      else if (facingRight) {
         ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Enemy_Right.gif");
         characterImage = ii.getImage();
      }
   }
   
   /**
   * Move in whatever direction currently facing
   */ 
   private void walkRoutine() {
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
   protected void checkAndResolveXCollision(GameLevel level) {
      boolean leftCollision, rightCollision;
      
      leftCollision = rightCollision = false;
      
      //check left side collision
      while (level.level2[GameScreen.worldCoordToTile(getLeftBottom().getY())][GameScreen.worldCoordToTile(getLeftBottom().getX())] == TileSet.GREEN_PLATFORM_TILE ||
             level.level2[GameScreen.worldCoordToTile(getLeftTop().getY())][GameScreen.worldCoordToTile(getLeftTop().getX())] == TileSet.GREEN_PLATFORM_TILE) {
         leftCollision = true;
         processXCollision(leftCollision, rightCollision);
      }
      
      //check right side collision
      while (level.level2[GameScreen.worldCoordToTile(getRightBottom().getY())][GameScreen.worldCoordToTile(getRightBottom().getX())] == TileSet.GREEN_PLATFORM_TILE ||
             level.level2[GameScreen.worldCoordToTile(getRightTop().getY())][GameScreen.worldCoordToTile(getRightTop().getX())] == TileSet.GREEN_PLATFORM_TILE) {
         rightCollision = true;
         processXCollision(leftCollision, rightCollision);
      }
      
      //reverse direction
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
   
   /*************/
   /** GETTERS **/
   /*************/
   
   public Image getImage() {
      return characterImage;
   }
   
   public int getX() {
      return x;
   }
   
   public int getY() {
      return y;
   }
   
}
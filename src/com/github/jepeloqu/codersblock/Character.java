package com.github.jepeloqu.codersblock;


import java.awt.Image;
import java.awt.Rectangle;

/**
 * Character --- Defines non-static entities, inherited by player and all enemies
 * @author    Joe Peloquin
 */
public class Character {
   protected int x, y;
   protected double dx, dy, maxSpeed;
   protected double xGroundImpulse, xAirImpulse, yJumpImpulse;
   protected double groundDampingFactor, airDampingFactor, gravity;
   
   protected boolean jump, fall, stand; 
   protected boolean facingLeft, facingRight;
   
   protected CollisionPoint topLeft, topRight, bottomLeft, bottomRight;
   
   protected Rectangle hitBox;
   int hitPoints;
   long lastHitTime;
   boolean dead;
   
   protected Image characterImage;
   
   public Character() {
      
   }
   
   /**
   * Main loop executed every tick
   * 
   * @param level   current game level
   */ 
   protected void mainLoop(Level level) {
      
   }
   
   protected void addImpulse(double xImpulse, double yImpulse) {
      dx = dx + xImpulse;
      if (dx > maxSpeed)
         dx = maxSpeed;
      else if (dx < -maxSpeed)
         dx = -maxSpeed;
      dy = dy + yImpulse;
   }  
   
   protected void processXMovement() {
      x = x + (int)Math.round(dx);
      
      updateHitBoxAndCollisionPoints();
   }
   
   protected void processYMovement() {
      y = y + (int)Math.round(dy);
      
      updateHitBoxAndCollisionPoints();
   }
   
   protected void applyDamping() {
      if (stand)
         dx = dx * groundDampingFactor;
      else if (jump || fall)
         dx = dx * airDampingFactor;
      
      if(Math.abs(dx) < .01)
         dx = 0;
   }
   
   protected void applyGravity() {
      if (jump || fall)
         dy = dy + gravity;
   }  
   
   /**
   * Checks for collisions in the horizontal axis and resolves them
   * 
   * @param level   current game level where collisions will be checked
   */ 
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
      
      if (leftCollision || rightCollision)
         dx = 0;           
   }

   /**
   * Checks for collisions in the vertical axis and resolves them
   * 
   * @param level   current game level where collisions will be checked
   */ 
   protected void checkAndResolveYCollision(Level level) {
      boolean risingCollision, fallingCollision;
      
      risingCollision = fallingCollision = false;

      //check top collision
      while (level.level[Screen.worldCoordToTile(getTopLeft().getY())][Screen.worldCoordToTile(getTopLeft().getX())] == TileSet.GREEN_PLATFORM_TILE ||
             level.level[Screen.worldCoordToTile(getTopRight().getY())][Screen.worldCoordToTile(getTopRight().getX())] == TileSet.GREEN_PLATFORM_TILE) {
         risingCollision = true;

         processYCollision(risingCollision, fallingCollision);
      }

      //check bottom collision
      while (level.level[Screen.worldCoordToTile(getBottomLeft().getY())][Screen.worldCoordToTile(getBottomLeft().getX())] == TileSet.GREEN_PLATFORM_TILE ||
             level.level[Screen.worldCoordToTile(getBottomRight().getY())][Screen.worldCoordToTile(getBottomRight().getX())] == TileSet.GREEN_PLATFORM_TILE) {

         fallingCollision = true;
         processYCollision(risingCollision, fallingCollision);
      }
      //System.out.println("Check Passed");
      if (risingCollision)
         fall();
      else if (fallingCollision)
         onGround();
   }
   
   /**
   * Partially reverses a horizontal collision by moving character 1 pixel in opposite direction
   * 
   * @param leftCollision    true if collision is on left side
   * @param rightCollision   true if collision is on right side
   */  
   protected void processXCollision(boolean leftCollision, boolean rightCollision) {
      
      if (leftCollision)
         x = x + 1;
      else if (rightCollision)
         x = x - 1;
      
      updateHitBoxAndCollisionPoints();
   }
   
   /**
   * Partially reverses a vertical collision by moving character 1 pixel in opposite direction
   * 
   * @param risingCollision    true if collision is on left side
   * @param fallingCollision   true if collision is on right side
   */  
   protected void processYCollision(boolean risingCollision, boolean fallingCollision) {
      
      if (risingCollision)
         y = y + 1;
      else if (fallingCollision)
         y = y - 1;
      
      updateHitBoxAndCollisionPoints();
   }
   
   /**
   * Checks if the character is standing on a empty space, if so initiates fall
   * 
   * @param level   current game level where fall will be checked
   */ 
   protected void checkFall(Level level){
      boolean onAirLeft, onAirRight;
      
      onAirLeft = onAirRight = false;
      
      if (level.level[Screen.worldCoordToTile(getBottomLeft().getY() + 1)][Screen.worldCoordToTile(getBottomLeft().getX() + 1)] == TileSet.BLANK_TILE ||
          level.level[Screen.worldCoordToTile(getBottomLeft().getY() + 1)][Screen.worldCoordToTile(getBottomLeft().getX() + 1)] == TileSet.SPIKE_TILE)
         onAirLeft = true;
      
      if (level.level[Screen.worldCoordToTile(getBottomRight().getY() + 1)][Screen.worldCoordToTile(getBottomRight().getX() + 1)] == TileSet.BLANK_TILE ||
          level.level[Screen.worldCoordToTile(getBottomRight().getY() + 1)][Screen.worldCoordToTile(getBottomRight().getX() + 1)] == TileSet.SPIKE_TILE)
         onAirRight = true;
      
      if (onAirLeft && onAirRight)
         fall();
   }
  
   protected void fall() {
      if (!fall) {
         dy = 0;
      }

      jump = false;
      fall = true;
      stand = false;
   }
   
   protected void onGround() {
      dy = 0;
      
      jump = false;
      fall = false;
      stand = true;
      
      updateHitBoxAndCollisionPoints(); 
   }
   
   protected void updateHitBoxAndCollisionPoints() {
      hitBox = new Rectangle(x, y, 30, 30);
      
      topLeft = new CollisionPoint(x, y);
      topRight = new CollisionPoint(x + characterImage.getWidth(null) - 1, y);
      bottomLeft = new CollisionPoint(x, y + characterImage.getHeight(null) - 1);
      bottomRight = new CollisionPoint(x + characterImage.getWidth(null) - 1, y + characterImage.getHeight(null) - 1);
   }
   
      protected void checkIfTouchingSpike(Level level) {
      if(level.level[Screen.worldCoordToTile(getBottomLeft().getY())][Screen.worldCoordToTile(getBottomLeft().getX())] == TileSet.SPIKE_TILE ||
         level.level[Screen.worldCoordToTile(getBottomRight().getY())][Screen.worldCoordToTile(getBottomRight().getX())] == TileSet.SPIKE_TILE ||
         level.level[Screen.worldCoordToTile(getTopLeft().getY())][Screen.worldCoordToTile(getTopLeft().getX())] == TileSet.SPIKE_TILE ||
         level.level[Screen.worldCoordToTile(getTopRight().getY())][Screen.worldCoordToTile(getTopRight().getX())] == TileSet.SPIKE_TILE) {
         
         dead = true;
         hitPoints = 0;
      }
   }

   /*************/
   /** SETTERS **/
   /*************/
   protected void setXGroundImpulse(Double newXImpulse) {
      xGroundImpulse = newXImpulse;
   }
      
   /*************/
   /** GETTERS **/
   /*************/
   
   public CollisionPoint getTopLeft() {
      return topLeft;
   }
   
   public CollisionPoint getTopRight() {
      return topRight;
   }
   
   public CollisionPoint getBottomLeft() {
      return bottomLeft;
   }
   
   public CollisionPoint getBottomRight() {
      return bottomRight;
   }
   
   public Image getImage() {
      return characterImage;
   }
   
   public boolean jumping() {
      return jump;
   }
   
   public boolean falling() {
      return fall;
   }
   
   public boolean standing() {
      return stand;
   }
   
   public boolean facingLeft() {
      return facingLeft;
   }
   
   public boolean facingRight() {
      return facingRight;
   }
   
   public int getX() {
      return x;
   }
    
   public int getY() {
      return y;
   }
   
   public double getdx() {
      return dx;
   }
   
   public double getdy() {
      return dy;
   }
   
   public int getHitPoints() {
      return hitPoints;
   }
   
   public Rectangle getHitBox() {
      return hitBox;
   }
}
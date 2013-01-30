package com.github.jepeloqu;


import java.awt.Image;

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
   
   protected OrderedPair topLeft, topRight, bottomLeft, bottomRight;
   protected OrderedPair leftBottom, leftTop, rightBottom, rightTop;
   
   protected Image characterImage;
   
   public Character() {
      
   }
   
   /**
   * Main loop executed every tick
   * 
   * @param level   current game level
   */ 
   protected void mainLoop(GameLevel level) {
      
   }
   
   public void addImpulse(double xImpulse, double yImpulse) {
      dx = dx + xImpulse;
      if (dx > maxSpeed)
         dx = maxSpeed;
      else if (dx < -maxSpeed)
         dx = -maxSpeed;
      dy = dy + yImpulse;
   }  
   
   protected void processXMovement() {
      x = x + (int)Math.round(dx);
      
      updateCollisionPoints();
   }
   
   protected void processYMovement() {
      y = y + (int)Math.round(dy);
      
      updateCollisionPoints();
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
      
      if (leftCollision || rightCollision)
         dx = 0;           
   }

   /**
   * Checks for collisions in the vertical axis and resolves them
   * 
   * @param level   current game level where collisions will be checked
   */ 
   protected void checkAndResolveYCollision(GameLevel level) {
      boolean risingCollision, fallingCollision;
      
      risingCollision = fallingCollision = false;

      //check top collision
      while (level.level2[GameScreen.worldCoordToTile(getTopLeft().getY())][GameScreen.worldCoordToTile(getTopLeft().getX())] == TileSet.GREEN_PLATFORM_TILE ||
             level.level2[GameScreen.worldCoordToTile(getTopRight().getY())][GameScreen.worldCoordToTile(getTopRight().getX())] == TileSet.GREEN_PLATFORM_TILE) {
         risingCollision = true;

         processYCollision(risingCollision, fallingCollision);
      }

      //check bottom collision
      while (level.level2[GameScreen.worldCoordToTile(getBottomLeft().getY())][GameScreen.worldCoordToTile(getBottomLeft().getX())] == TileSet.GREEN_PLATFORM_TILE ||
             level.level2[GameScreen.worldCoordToTile(getBottomRight().getY())][GameScreen.worldCoordToTile(getBottomRight().getX())] == TileSet.GREEN_PLATFORM_TILE) {

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
      
      updateCollisionPoints();
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
      
      updateCollisionPoints();
   }
   
   /**
   * Checks if the character is standing on a empty space, if so initiates fall
   * 
   * @param level   current game level where fall will be checked
   */ 
   protected void checkFall(GameLevel level){
      boolean onAirLeft, onAirRight;
      
      onAirLeft = onAirRight = false;
      
      if (level.level2[GameScreen.worldCoordToTile(getBottomLeft().getY())][GameScreen.worldCoordToTile(getBottomLeft().getX())] == TileSet.BLANK_TILE)
         onAirLeft = true;
      
      if (level.level2[GameScreen.worldCoordToTile(getBottomRight().getY())][GameScreen.worldCoordToTile(getBottomRight().getX())] == TileSet.BLANK_TILE)
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
      y = y + 1;
      
      jump = false;
      fall = false;
      stand = true;
      
      updateCollisionPoints(); 
   }
   
   protected void updateCollisionPoints() {
      topLeft = new OrderedPair(x + 1, y);
      topRight = new OrderedPair(x + characterImage.getWidth(null) - 2, y);
      bottomLeft = new OrderedPair(x + 1, y + characterImage.getHeight(null) - 1);
      bottomRight = new OrderedPair(x + characterImage.getWidth(null) - 2, y + characterImage.getHeight(null) - 1);
      
      leftBottom = new OrderedPair(x, y + characterImage.getHeight(null) - 2);
      leftTop = new OrderedPair(x, y + 1);
      rightBottom = new OrderedPair(x + characterImage.getWidth(null) - 1, y + characterImage.getHeight(null) - 2);
      rightTop = new OrderedPair(x + characterImage.getWidth(null) - 1, y + 1);   
   }
   
   /*************/
   /** GETTERS **/
   /*************/
   
   public OrderedPair getTopLeft() {
      return topLeft;
   }
   
   public OrderedPair getTopRight() {
      return topRight;
   }
   
   public OrderedPair getBottomLeft() {
      return bottomLeft;
   }
   
   public OrderedPair getBottomRight() {
      return bottomRight;
   }
   
   public OrderedPair getLeftBottom() {
      return leftBottom;
   }
   
   public OrderedPair getLeftTop() {
      return leftTop;
   }
   
   public OrderedPair getRightBottom() {
      return rightBottom;
   }
   
   public OrderedPair getRightTop() {
      return rightTop;
   }
   
   public Image getImage() {
      return characterImage;
   }
   
   public int getCharHeight() {
      return characterImage.getHeight(null);
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
}
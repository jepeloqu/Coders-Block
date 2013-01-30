package com.github.jepeloqu;

public class Camera {
   int x, y;
   int xPixelOffset, yPixelOffset;
   int xTileOffset, yTileOffset;
   
   public Camera(Player character) {
      updateCamera(character);
   }
   
   public void updateCamera(Player character) {
      x = character.getX();
      y = character.getY();
      
      //Make sure camera is within camera range
      if (x > 496)
         x = 496;
      else if (x < 494)
         x = 494;
      
      if (y > 256)
         y = 256;
      else if (y < 254)
         y = 254;
      
      xPixelOffset = character.getX() - x;
      yPixelOffset = character.getY() - y;
      
      //Limit Pixel Offset to stay within level
      if (xPixelOffset < 0)
         xPixelOffset = 0;
      else if (xPixelOffset > 2040)
         xPixelOffset = 2040;
      
      if (yPixelOffset < 0)
         yPixelOffset = 0;
      else if (yPixelOffset > 540)
         yPixelOffset = 540;
      
      xTileOffset = GameScreen.worldCoordToTile(xPixelOffset);
      yTileOffset = GameScreen.worldCoordToTile(yPixelOffset);
   }
   
   /*************/
   /** GETTERS **/
   /*************/
   
   public int getX() {
      return x;
   }
   
   public int getY() {
      return y;
   }
   
   public int getXPixelOffset() {
      return xPixelOffset;
   }
   
   public int getYPixelOffset() {
      return yPixelOffset;
   }
      
   public int getXTileOffset() {
      return xTileOffset;
   }
      
   public int getYTileOffset() {
      return yTileOffset;
   }
}
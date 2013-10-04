package com.github.jepeloqu.codersblock;

public class Camera {
   int xStartingPoint, yStartingPoint;
   int xLimit, yLimit;
   int xPixelOffset, yPixelOffset;
   int xTileOffset, yTileOffset;
   
   public Camera(Player character) {
      xStartingPoint = 495;
      yStartingPoint = 255;
      updateCamera(character);
   }
   
   public void updateCamera(Player character) {
      
      xPixelOffset = character.getX() - xStartingPoint;
      yPixelOffset = character.getY() - yStartingPoint;
      
      //Limit Pixel Offset to stay within level
      if (xPixelOffset < 0)
         xPixelOffset = 0;
      else if (xPixelOffset > 2040)
         xPixelOffset = 2040;
      
      if (yPixelOffset < 0)
         yPixelOffset = 0;
      else if (yPixelOffset > 540)
         yPixelOffset = 540;
      
      xTileOffset = Screen.worldCoordToTile(xPixelOffset);
      yTileOffset = Screen.worldCoordToTile(yPixelOffset);
   }
   
   /*************/
   /** GETTERS **/
   /*************/
   
   public int getX() {
      return xStartingPoint;
   }
   
   public int getY() {
      return yStartingPoint;
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
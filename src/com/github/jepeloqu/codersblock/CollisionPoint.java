package com.github.jepeloqu.codersblock;

   public class CollisionPoint {
      private int x;
      private int y;
   
      public CollisionPoint(int x, int y){
         this.x = x;
         this.y = y;
      }
   
      public int getX(){
         return x;
      }
   
      public int getY(){
         return y;
      }
   }
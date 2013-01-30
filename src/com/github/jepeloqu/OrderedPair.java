package com.github.jepeloqu;



public class OrderedPair {
   private int x;
   private int y;
   
   public OrderedPair(int x, int y){
      this.x = x;
      this.y = y;
   }
   
   public void printOrderedPair() {
      System.out.println("X: " + x + " Y: " + y);
   }
   
   public int getX(){
      return x;
   }
   
   public int getY(){
      return y;
   }
   
   public void setX(int x) {
      this.x = x;
   }
   
   public void setY(int y) {
      this.y = y;
   }
}
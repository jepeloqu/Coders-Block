package com.github.jepeloqu.codersblock;



import java.awt.*;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Screen --- main area where the game is controlled
 * @author    Joe Peloquin
 */
public class Screen extends JPanel{
   private Player player; 
   private Enemy enemy;
   private Camera camera;
   private Image background;
   
   private Level level;
   private TileSet tileSet;
   
   private int screenHeight, screenWidth;
   private int tileHeight, tileWidth;
   private int playerXPosition, playerYPosition;
   private int enemyXPosition, enemyYPosition;
    
   public Screen() {
      initializeScreenVariables();    

      Dimension dim = new Dimension(screenWidth, screenHeight);
      this.setPreferredSize(dim);
      
      player = new Player(playerXPosition, playerYPosition); 
      enemy = new Enemy(enemyXPosition, enemyYPosition);
      camera = new Camera(player);
  
      ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "background.gif");
      background = ii.getImage();
      
      level = new Level();
      tileSet = new TileSet();
   }
   
   private void initializeScreenVariables() {
      
      screenWidth = 990;
      screenHeight = 510;
      
      tileHeight = 18;
      tileWidth = 34;
      
      playerXPosition = 550;
      playerYPosition = 400;
      
      enemyXPosition = 50;
      enemyYPosition = 400;
   }
   
   public void mainLoop(boolean leftPressed, boolean rightPressed, boolean spacePressed) {
      player.mainLoop(level, enemy, leftPressed, rightPressed, spacePressed);
      
      enemy.mainLoop(level, player);
      
      camera.updateCamera(player);
      
      repaint();
   }
    
   @Override
   public void paint(Graphics g) {
      super.paint(g);
        
      Graphics2D g2d = (Graphics2D) g;
      
      g2d.drawImage(background, 0, 0, this);

      for (int row = camera.getYTileOffset(); row < tileHeight + camera.getYTileOffset(); row++) {
         for (int col = camera.getXTileOffset(); col < tileWidth + camera.getXTileOffset(); col++) {
            if(level.level[row][col] == TileSet.GREEN_PLATFORM_TILE)
               g2d.drawImage(tileSet.getGreenPlatformTileImage(), tileCoordToWorld(col) - camera.getXPixelOffset(), tileCoordToWorld(row) - camera.getYPixelOffset(), this);
            else if (level.level[row][col] == TileSet.SPIKE_TILE)
               g2d.drawImage(tileSet.getSpikeTileImage(), tileCoordToWorld(col) - camera.getXPixelOffset(), tileCoordToWorld(row) - camera.getYPixelOffset(), this);
         }
      }

      g2d.drawImage(enemy.getImage(), enemy.getX() - camera.getXPixelOffset(), enemy.getY() - camera.getYPixelOffset(), this);
      g2d.drawImage(player.getImage(), player.getX() - camera.getXPixelOffset(), player.getY() - camera.getYPixelOffset(), this);

      g2d.setColor(Color.RED); 
      g2d.fillRect(50, 15, player.getHitPoints() * 30, 30);
      
      drawDebugData(g2d);
      
   }
   
   public void drawDebugData(Graphics2D g2d){
      /*
      g2d.drawString("X Character Position: " + Integer.toString(player.getX()), 5, 15);
      g2d.drawString("Y Character Position: " + Integer.toString(player.getY()), 5, 30);
      g2d.drawString("X Camera Position: " + Integer.toString(camera.getX()), 5, 45);
      g2d.drawString("Y Camera Position: " + Integer.toString(camera.getY()), 5, 60);
      g2d.drawString("X Pixel Offset: " + Integer.toString(camera.getXPixelOffset()), 5, 75);
      g2d.drawString("Y Pixel Offset: " + Integer.toString(camera.getYPixelOffset()), 5, 90);
      g2d.drawString("X Tile Offset: " + Integer.toString(camera.getXTileOffset()), 5, 105);
      g2d.drawString("Y Tile Offset: " + Integer.toString(camera.getYTileOffset()), 5, 120);
      */
      /*
      g2d.drawString("X Velocity: " + Double.toString(player.getdx()), 5, 15);
      g2d.drawString("Y Velocity: " + Double.toString(player.getdy()), 5, 30);
      if (player.jumping())
         g2d.drawString("Jumping", 5, 75);
      else if (player.standing())
         g2d.drawString("Standing", 5, 75);
      else if (player.falling())
         g2d.drawString("Falling", 5, 75);
      g2d.drawString("Tile X: " + worldCoordToTile(player.getX()) + " Tile Y: " + worldCoordToTile(player.getY()), 5, 90);
      */
   }
   
   public static int tileCoordToWorld(int tileCoord) {
      
      return tileCoord * 30;
   }
   
   public static int worldCoordToTile(int worldCoord) {

      return worldCoord / 30;
   }
   
}
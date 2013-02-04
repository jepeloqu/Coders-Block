package com.github.jepeloqu.codersblock;



import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.*;

/**
 * Screen --- main area where the game is controlled
 * @author    Joe Peloquin
 */
public class Screen extends JPanel implements ActionListener, KeyListener{
   private Timer timer;
   private Player player; 
   private Enemy enemy;
   private Camera camera;
   private Image background;
   
   private boolean leftPressed, rightPressed, spacePressed;
   
   private Level level;
   private TileSet tileSet;
   
   private int screenTileHeight;
   private int screenTileWidth;
    
   public Screen() {
      setFocusable(true);  
      setSize(990, 510);
      //setUndecorated(true);
      
      screenTileHeight = 18;
      screenTileWidth = 34;
        
      player = new Player(500, 200); 
      enemy = new Enemy(450, 50);
      camera = new Camera(player);
        
      ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "background.gif");
      background = ii.getImage();
      
      level = new Level();
      tileSet = new TileSet();
     
      timer = new Timer(33, this);
      timer.start();
      addKeyListener(this);
   }
    
   @Override
   public void paint(Graphics g) {
      super.paint(g);
        
      Graphics2D g2d = (Graphics2D) g;
      
      g2d.drawImage(background, 0, 0, this);

      for (int row = camera.getYTileOffset(); row < screenTileHeight + camera.getYTileOffset(); row++) {
         for (int col = camera.getXTileOffset(); col < screenTileWidth + camera.getXTileOffset(); col++) {
            if(level.level[row][col] == TileSet.GREEN_PLATFORM_TILE)
               g2d.drawImage(tileSet.getGreenPlatformTileImage(), tileCoordToWorld(col) - camera.getXPixelOffset(), tileCoordToWorld(row) - camera.getYPixelOffset(), this);
         }
      }

      g2d.drawImage(enemy.getImage(), enemy.getX() - camera.getXPixelOffset(), enemy.getY() - camera.getYPixelOffset(), this);
      g2d.drawImage(player.getImage(), player.getX() - camera.getXPixelOffset(), player.getY() - camera.getYPixelOffset(), this);

      g2d.drawString("C", camera.getXPixelOffset(), camera.getYPixelOffset()); 
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
      
      g2d.drawString("X Velocity: " + Double.toString(player.getdx()), 5, 15);
      g2d.drawString("Y Velocity: " + Double.toString(player.getdy()), 5, 30);
      if (player.jumping())
         g2d.drawString("Jumping", 5, 75);
      else if (player.standing())
         g2d.drawString("Standing", 5, 75);
      else if (player.falling())
         g2d.drawString("Falling", 5, 75);
      g2d.drawString("Tile X: " + worldCoordToTile(player.getX()) + " Tile Y: " + worldCoordToTile(player.getY()), 5, 90);
   
   }
    
   @Override
   public void actionPerformed(ActionEvent e) {
      player.updateState(leftPressed, rightPressed, spacePressed);
      player.mainLoop(level);
      
      enemy.mainLoop(level);
      
      camera.updateCamera(player);
      
      repaint();
   } 
   
   public static int tileCoordToWorld(int tileCoord) {
      
      return tileCoord * 30;
   }
   
   public static int worldCoordToTile(int worldCoord) {

      return worldCoord / 30;
   }
    
   @Override
   public void keyPressed(KeyEvent input) {
      if (input.getKeyCode() == KeyEvent.VK_D)
         rightPressed = true;
      else if (input.getKeyCode() == KeyEvent.VK_A)
         leftPressed = true;
      else if (input.getKeyCode() == KeyEvent.VK_SPACE)
         spacePressed = true;  
   }
    
   @Override
   public void keyReleased(KeyEvent input) {
      if (input.getKeyCode() == KeyEvent.VK_D)
         rightPressed = false;
      else if (input.getKeyCode() == KeyEvent.VK_A)
         leftPressed = false;
      else if (input.getKeyCode() == KeyEvent.VK_SPACE)
         spacePressed = false;
   }
    
   @Override
   public void keyTyped(KeyEvent input) {
        
   }
}
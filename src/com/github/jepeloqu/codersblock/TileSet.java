package com.github.jepeloqu.codersblock;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

public class TileSet {
   public static final int BLANK_TILE = 0;
   public static final int GREEN_PLATFORM_TILE = 1;
   public static final int SPIKE_TILE = 2;
   
   private Image greenPlatformTile;
   private Image spikeTile;
   
   public TileSet() {
      ImageIcon ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Platform.gif");
      greenPlatformTile = ii.getImage();
      
      ii = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "images" + File.separator + "Spike.gif");
      spikeTile = ii.getImage();
      
   }
   
   public Image getGreenPlatformTileImage() {     
      return greenPlatformTile;
   }
   
   public Image getSpikeTileImage() {
      return spikeTile;
   }
}

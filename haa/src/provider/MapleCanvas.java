package provider;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MapleCanvas {
  private File file;
  
  private int width;
  
  private int height;
  
  private BufferedImage image;
  
  public MapleCanvas(int width, int height, File fileIn) {
    this.width = width;
    this.height = height;
    this.file = fileIn;
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public BufferedImage getImage() {
    loadImageIfNecessary();
    return this.image;
  }
  
  private void loadImageIfNecessary() {
    if (this.image == null)
      try {
        this.image = ImageIO.read(this.file);
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }  
  }
}

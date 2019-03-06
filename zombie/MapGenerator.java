
package zombieland;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.io.File;
import javax.imageio.ImageIO;


public class MapGenerator{
    private BufferedImage backgroundImage;
    
    private int backgroundWidth, backgroundHeight;
    
    private double gameScale;
    
    public MapGenerator(String path, int x, int y, double gameScale){
       try {
           backgroundImage = ImageIO.read(new File(path));//change to your path of file 
       }catch(Exception ex) {//file did not load properly
           ex.printStackTrace();
       }
       backgroundWidth = x;
       backgroundHeight = y;
       this.gameScale = gameScale;
       
       tileBackground();
    }

       
    private void tileBackground() {
       if(backgroundImage.getWidth() < backgroundWidth || backgroundImage.getHeight() < backgroundHeight) {
        BufferedImage tiledImage = new BufferedImage(backgroundWidth, backgroundHeight, TYPE_INT_RGB);
        Graphics g = tiledImage.createGraphics();
        for (int row = 0; row < gameScale; row++) {
            for (int col = 0; col < gameScale; col++) {
                g.drawImage(backgroundImage, row*backgroundImage.getWidth(), col*backgroundImage.getHeight(), null);
            }
        }
        g.dispose();
        backgroundImage = tiledImage;
       }
    }
    
    public int getWidth(){
        return backgroundWidth;
       
    }
    
    public int getHeight(){
        return backgroundHeight;
    }
    public Image getImage(){
        return backgroundImage;
    }
    
    
}

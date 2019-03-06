
package zombieland;

import zombieland.Controller;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import java.util.Timer;

public class PowerUp {
    
private int numPower;
    
private Image FreezingBullet, poisonBullet, Shield, p1Shield, p2Shield;
private BufferedImage get;

    // Constructor with 3 different types of power up
    public PowerUp(String power1, String power2, String power3){
       try {
           poisonBullet = ImageIO.read(new File(power1));//change to your path of file 
           Shield = ImageIO.read(new File(power2));
           FreezingBullet =ImageIO.read(new File(power3));
           p1Shield = ImageIO.read(new File("resources/Shield1.png"));
           p2Shield = ImageIO.read(new File("resources/Shield2.png"));
           
       }
                       catch(Exception ex) {//file did not load properly
           ex.printStackTrace();
       }      
    }
    
   public Image poisonBullet(){
    return poisonBullet;
    }
    
    
    public Image Shield(){
        return Shield;
}

    public Image FreezingBullet(){
        return FreezingBullet;
    }
   
    
    public void GetPowerUp(int x, Player player){
            switch (x) {
            case 7:
                player.setBulletType(poisonBullet, 2);
                break;
            case 8:
                System.out.println("Shield is ON");
                if(player.getTankID() == 1) {
                    player.setShield(p1Shield);
                }   
                else if(player.getTankID() == 2) {
                    player.setShield(p2Shield);
                }
                break;
            case 9:
                player.setBulletType(FreezingBullet, 3);
                break;
            default:
               break;
        }
        
    }
    }


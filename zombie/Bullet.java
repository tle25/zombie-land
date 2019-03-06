
package zombieland;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet {
    private int bulletXDir, x;
    private int bulletYDir, y;
    private int angle;
    private boolean isFire = true;
    
    private Image img;
    
    private int bulletSize;
    private final Rectangle bullet;
    private int speed;
    private String newFeature;
    private int bulletType;
    private int count = 1;
    private int rad;
    
    private int target;

    public Bullet(Image weapon, int type, int posX, int posY, int z, int speed,int bulletSize, boolean Fire){
        
        img = weapon;

       this.x = posX-bulletSize/2;
       this.y = posY-bulletSize/2;
        angle = z; 
        isFire = Fire;
        this.speed = speed;
        
        bulletType = type;
        bullet = new Rectangle(0,0, bulletSize, bulletSize);
        this.bulletSize = bulletSize;
        
          
       // Set bullet direction and its velocity. 
       bulletXDir = (int)(Math.sin(angle * (Math.PI/180)) * -speed);
       bulletYDir = (int)(Math.cos(angle * (Math.PI/180)) * speed);
        
    }

    public int getType(){
        return bulletType;
    }
    
    //Return bullet location
    public Rectangle getBullet(){
        return bullet;
    }
    
    public int getBulletPosX(){
        return x;
    }

     public int getBulletPosY(){
        return y;
    }
     
     // Draw and update bullet move
   public void DrawBullet(Graphics2D g){  

     if(isFire){

         x += bulletXDir;
         y += bulletYDir;
         
   /*      
         for(int i = 0; i < enemies.getEnemy().size(); i++){
           int var = (int) Math.sqrt(Math.pow(x - enemies.getEnemy().get(i).getPosX(), 2)
               + Math.pow(y - enemies.getEnemy().get(i).getPosY(), 2));
           if(var == 100)
                target = (int)Math.toDegrees(Math.atan2(enemies.getEnemy().get(i).getPosY() - x,
               enemies.getEnemy().get(i).getPosX()- x));
               x +=  (int)(Math.sin((target) * (Math.PI/180)) *-speed);
               y +=  (int)(Math.sin((target) * (Math.PI/180)) *speed);
               break;
         }
     */    
         
         g.drawImage(img, x , y, bulletSize, bulletSize ,null);
         //Update bullet location
         bullet.setLocation(x, y);     
      }   
   }
    
   public boolean getFire(){
       return isFire;
   }

   public void setFire(boolean set){
       this.isFire = set;
   }
   
   public void setBoucingBullet(boolean ChangeBullet){
       
   }
   
   public void setImage(BufferedImage image){
       this.img = image;
   }

}


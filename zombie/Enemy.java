/*
  * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zombieland;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import static zombieland.Controller.enemies;
import static zombieland.Controller.player1;
import static zombieland.Controller.player2;
import static zombieland.Controller.walls;

public class Enemy {
    
    private boolean up, down, turnleft, turnright, fire;
    private double angle;
    private int posX, posY;
    
    private final int StayAtX , StayAtY;
    private int enemySize = 30;

  //  private BufferedImage image;
    private Image img, weapon;
    private int bulletType = 2;   
    private boolean ShieldIsOn = false;    
    private int speed = 2;
    private int radarRange = 400;
    
   private int health = 100;

   private long GenerationDelay = System.nanoTime();
   private int zombieProduction = 10000; // set perior 1 second per bullet
   
   private boolean Boss = false;
   private Timer tmr = new Timer(),
           timer = new Timer();
   
   private boolean freezing = false;
   private int var1, var2;
   
  
   
   
   
   
   private Rectangle enemy;

    // Constructor with image pahth and set init its location on frame
    public Enemy(String path, int x, int y, boolean typeOfEnemy){
      try {
           this.img = ImageIO.read(new File(path)); 
       }catch(Exception ex) {//file did not load properly
           ex.printStackTrace();
        }

       Boss = typeOfEnemy;
       posX = x;
       posY = y;
       StayAtX = x;
       StayAtY = y;
       
       
       if(Boss){
           health *= 4;
           enemySize *= 2;
           speed -= 1;
           radarRange = 600;
       }
       
       enemy = new Rectangle(0,0,enemySize,enemySize);

    }
    
    public boolean isBoss(){
        return Boss;
    }
    
    public void Hit(){
        health-= 10;
    } 
    
    public Rectangle enemy(){
       return enemy;
    }
    
    
    
    //Draw tank
    public void draw(Graphics2D g){

        g = (Graphics2D)g.create();
        move();
        
        // turn zombie back to player so that it can run away...
        if((player1.isTankMode() && var1 < radarRange/2) 
            || (player2.isTankMode()&& var2 < radarRange/2))
           g.rotate(Math.toRadians(angle+90), posX+enemySize/2, posY+enemySize/2);
        else
           g.rotate(Math.toRadians(angle-90), posX+enemySize/2, posY+enemySize/2);

        g.drawImage(img, posX, posY, enemySize, enemySize, null);
        enemy.setLocation(posX, posY);
        g.dispose();
   
}
 // Update zombie move   
private void move(){
        // get the distance between player and zombie
        var2 = (int) Math.sqrt(Math.pow(posX+enemySize/2 - player2.coordinateAtX()-20, 2)
               + Math.pow(posY+enemySize/2 - player2.coordinateAtY()-20, 2));
        
        var1 = (int) Math.sqrt(Math.pow(posX+enemySize/2 - player1.coordinateAtX()-20, 2)
               + Math.pow(posY+enemySize/2 - player1.coordinateAtY()-20, 2));
        
        

if(var1 < radarRange/2 || var2 < radarRange/2){
    if(var2 <= var1){
       angle = Math.toDegrees(Math.atan2(player2.coordinateAtY()+20 - posY-enemySize/2,
               player2.coordinateAtX()+20-posX-enemySize/2));
       if(!player2.isTankMode()){
         posX += (double)(Math.sin((angle-90) * (Math.PI/180)) *-speed);
         posY += (double)(Math.cos((angle-90) * (Math.PI/180)) *speed);
       }
       
       else{
         posX -= (double)(Math.sin((angle-90) * (Math.PI/180)) *-speed*2/3);
         posY -= (double)(Math.cos((angle-90) * (Math.PI/180)) *speed*2/3);
          }
       }
       
       
    else{
       angle = Math.toDegrees(Math.atan2(player1.coordinateAtY()+20 - posY-enemySize/2,
           player1.coordinateAtX()+20-posX - enemySize/2));
         if(!player1.isTankMode()){
         posX += (double)(Math.sin((angle-90) * (Math.PI/180)) *-speed);
         posY += (double)(Math.cos((angle-90) * (Math.PI/180)) *speed);
         }
       
         else{
         posX -= (double)(Math.sin((angle-90) * (Math.PI/180)) *-speed*2/3);
         posY -= (double)(Math.cos((angle-90) * (Math.PI/180)) *speed*2/3);
         }   
       }  

  }
        
   // Going back to its position 
 else{
       angle = Math.toDegrees(Math.atan2(StayAtY - posY, StayAtX-posX));
       int length =(int) Math.sqrt(Math.pow(StayAtX-posX, 2)+ Math.pow(StayAtY - posY, 2));
       if(length >= 10){
         posX += (double)(Math.sin((angle-90) * (Math.PI/180)) *-speed);
         posY += (double)(Math.cos((angle-90) * (Math.PI/180)) *speed);
       } 
       
     }

      
     // Generate every zombie every 8 second is bigZombie is alive
       long delay = (System.nanoTime()-GenerationDelay)/1000000;     
       if(delay > zombieProduction && Boss){
           int random = -50 + new Random().nextInt(50);
             enemies.getEnemy().add(new Enemy("resources/zombie1.png",
                posX+random, posY+random,false));
             
           // Set generation delay to original value so that delay will count 0 to 8 seconds
           GenerationDelay = System.nanoTime();
       }  
    }
    
    public int getPosX(){
        return posX;
    }
     public int getPosY(){
        return posY;
    }

    
    public boolean dead(){
        if(health <= 0)
            return true;
        else
            return false;
    }
    
public void freezeIt(){
    if(freezing){
        timer.cancel();
        timer = new Timer();
    }
    freezing = true;
    speed = 0;
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
        speed = 2;
        freezing = false;
        timer.cancel();
        timer = new Timer();
        }
    } , 1000);
    
    
} 

public void setSpeed(){
    speed = 1;
}

}
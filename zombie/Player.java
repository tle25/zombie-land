
package zombieland;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static zombieland.Controller.bullets1;
import static zombieland.Controller.bullets2;
import static zombieland.Controller.power;
import static zombieland.Controller.walls;

public class Player{
    private int count = 0;
    
    private boolean tankMode = false;
    
    private boolean up, down, turnleft, turnright, fire;
    private int degrees = 0;
    private int posX, posY, respawnPosX, respawnPosY;

  //  private BufferedImage image;
    private Image img, weapon, originForm, originWeapon;
    private int bulletType = 1;   
    private boolean ShieldIsOn = false;    
    private int player;   
    private int speed = 3;
    
    private int rotateSpeed = 3;
    
    private int life = 3;
    private int health = 400;

    private int tanksize = 40;
    
    private int dist;
    private int bulletSize = 5;

   private long fireTime = System.nanoTime();
   private int fireDelay = 150; // set perior 400 milisecond each fire
   
   private int tankID;
   
   private Rectangle Player;
   
   private Timer timer = new Timer();
   private Timer tmr = new Timer();
   
   private boolean powerBullet = false;
   private boolean bullet = false;
   
   private boolean leftHand= false;
   
   
 //  private int dt = (int) (Math.sqrt(Math.pow((tanksize/2),2) + Math.pow((tanksize/2),2)));
   private int db = (int) (Math.sqrt(Math.pow((10),2)+ Math.pow((10),2)));
   
   private int safeDistance = tanksize/2 + db;
    
    // Constructor with image pahth and set init its location on frame
    public Player(String path, String weapon, int x, int y, int tankID){
      try {
           this.img = ImageIO.read(new File(path)); 
           this.weapon = ImageIO.read(new File(weapon)); 
       }catch(Exception ex) {//file did not load properly
           ex.printStackTrace();
        }
        
      
      originWeapon = this.weapon;
      
       this.posX = x;
       this.posY = y;
       this.respawnPosX = x;
       this.respawnPosY = y;
       Player = new Rectangle(0,0,tanksize,tanksize);
       
       this.tankID = tankID;
       
       
    }
    
    
    //Draw tank
    public void draw(Graphics2D g){
   // rotation.rorate(Math.toRadian(angle, img.getWidth()/2, img.getHeight()/2,   );
   
   //Update tank move;
    update(); 
        g = (Graphics2D)g.create();
        if(health > 0){
            g.rotate(Math.toRadians(degrees), posX+tanksize/2, posY+tanksize/2);
            g.drawImage(img,posX, posY, tanksize, tanksize, null);
            
             // Update tank position
             Player.setLocation(posX, posY);
        } else if(health <= 0) {
            posX = respawnPosX;
            posY = respawnPosY;
            life--;
            health = 400;
        }
    g.dispose();
    }
    
    // Health condition
    public int getHealth(){
        return health;
    }
     
    // Set up tank rectangle 
    public Rectangle player(){
        return Player;
    }
    
    // decrease tank health if the tank is hit
    public void gotHit(){
        health -=1;
    }
    
    // Check if tank is destroyed?
    public boolean isDead(){
        if(health <= 0)
            return true;
        else
            return false;
    }
    
    // Check if player is lost
    public boolean isLost() {
        if(life <= 0)
            return true;
        else
            return false;
    }
            
    // return tank coordinate at X
    public int coordinateAtX(){
        return this.posX;
    }
    //return tank coordinate at Y
    public int coordinateAtY(){
        return this.posY;
    }
    
    //return its degree
    public int Degrees(){
        return this.degrees;
    }   
   
    //private void update(){   // This can be private function, It can be run, bur dont know why it shows error
    private void update(){             
          if(up){
             // If two tank collinizes each other, jump over 
                 if(Controller.player1.Player.intersects(Controller.player2.Player)){
                 posX -= (double)(Math.sin(degrees * (Math.PI/180)) *-speed);
                 posY -= (double)(Math.cos(degrees * (Math.PI/180)) *speed);
              }
             // Set tank moves up
              boolean done = true;     
              boolean move = false;
              // temp values posX and posY
              int dx = posX;
              int dy = posY;
                // Move tank up
                
                 posX += (double)(Math.sin(degrees * (Math.PI/180)) * -speed);
                 posY += (double)(Math.cos(degrees * (Math.PI/180)) *speed);
                 for(int i = 0; i < walls.walls.length && done; i++)
                    for(int j = 0; j < walls.walls[i].length; j++){
                        if((walls.walls[i][j]) > 0 ){
                            
       
               // Update distance between tank and walls
               dist = (int) Math.sqrt(Math.pow((posX+tanksize/2)- (i*walls.wallsize+db), 2)
               + Math.pow((posY+tanksize/2) - (j*walls.wallsize+db), 2));
    
                // check if it collision? if yes, assign it back to previous move.
                 if(dist < safeDistance){  
                    posX = dx;
                    posY= dy;
                    done = false;
                    break;
               }


                // Set tank collision with PowerUp Item
              if(walls.walls[i][j] >= 7){
                 // make it more easy to pick up
                 if(dist < safeDistance+10){
                   power.GetPowerUp(walls.walls[i][j], this);
                   walls.setWall(i, j, 0); 
                   done = false;
                   break;
                 }
                }            
                } 
                        }                
                       
          }
          
          
          if(down){
            
             // If two tank collinizes each other, jump over 
                 if(Controller.player1.Player.intersects(Controller.player2.Player)){
                 posX += (double)(Math.sin(degrees * (Math.PI/180)) *-speed);
                 posY += (double)(Math.cos(degrees * (Math.PI/180)) *speed);
              }
             // Set tank moves up
              boolean done = true;             
              // temp values posX and posY
              int dx = posX;
              int dy = posY;
               
                // Move tank up
                 posX -= (double)(Math.sin(degrees * (Math.PI/180)) * -speed);
                 posY -= (double)(Math.cos(degrees * (Math.PI/180)) *speed);
                for(int i = 0; i < walls.walls.length && done; i++)
                    for(int j = 0; j < walls.walls[i].length; j++){
                        if((walls.walls[i][j]) > 0){
                            
 
               // Update distance between tank and walls
               dist = (int) Math.sqrt(Math.pow((posX+tanksize/2)- (i*walls.wallsize+db), 2)
               + Math.pow((posY+tanksize/2) - (j*walls.wallsize+db), 2));
    
                // check if it collision? if yes, assign it back to previous move.
               if(dist <= safeDistance){  
                    posX = dx;
                    posY= dy;
                    done = false;
                    break;
               }                            
                        
                // Set tank collision with PowerUp Item
              if(walls.walls[i][j] >= 7){
                 if(dist < safeDistance+10){      
                  power.GetPowerUp(walls.walls[i][j], this);
                   walls.setWall(i, j, 0); 
                   done = false;
                   break;
                    } 
              }
                           
         } 
       }


          }
          // Tank rotates left
          if(turnleft)           
              degrees = degrees - rotateSpeed;
          // TTank rotates right
          if(turnright)
              degrees = degrees + rotateSpeed;
          
          if(fire){            
              long delay = (System.nanoTime() - fireTime) / 1000000;

            // set each time loading bullet by 250 milisecond
            // MAX 20 bullets each tank
            if(player == 1){
                if(delay > fireDelay){
              bullets1.add(new Bullet(weapon, bulletType, coordinateAtX()+tanksize/2,coordinateAtY()+tanksize/2, Degrees(), 5,bulletSize, true)); 
               
                fireTime = System.nanoTime();
                }        
                
            }
           
            if(player == 2){
              if(delay > fireDelay){
         //     if(!this.isTankMode()){
                      
                 if(leftHand)
                 {
                  bullets2.add(new Bullet(weapon, bulletType, coordinateAtX()+tanksize/2 +8,coordinateAtY()+tanksize/2 + 8, Degrees(),5,bulletSize, true));    
                 leftHand = false;
                 }
                 else{
                  bullets2.add(new Bullet(weapon, bulletType, coordinateAtX()+tanksize/2- 8,coordinateAtY()+tanksize/2 - 8, Degrees(),5,bulletSize, true)); 
                 leftHand = true;
                 }
         
             fireTime = System.nanoTime();
              }          
                
            }
          }
        
          
    }
      
      public void setUp(boolean set){
          up = set;
      }
       public void setDown(boolean set){
          down = set;
      }
        public void setleft(boolean set){
          turnleft = set;
      }
         public void setRight(boolean set){
          turnright = set;
      }
        public void setFire(boolean bool, int name){
        fire = bool;
        player = name;
    }
        
       
    // set TankMode
    public void setShield(Image image ){
       // Check if tank mode is on, then cancel all its task
        if(ShieldIsOn){
        img = originForm;
        timer.cancel();
        timer = new Timer();   
        }
        
        ShieldIsOn = true;
        originForm = img;
        this.img = image;
        bulletSize *= 2;
        tanksize = 50;
        setTankTankMode(true);

 // Set a new timertask, set tank mode to normal after 20 seconds
        timer.schedule(new TimerTask() {
          @Override 
          public void run(){
        img = originForm;
        tanksize = 40;
        bulletSize = 5;
        setTankTankMode(false);
        ShieldIsOn = false;
        timer.cancel();
        timer = new Timer();
        
        }
        },20000); // Set tank to normal mod after 20 seconds

    }

    public void setBulletType(Image newWeapon, int type){
    // Check if bullet power up is on, then cancel all its task    
      if(bullet){
            tmr.cancel();
            tmr.purge();
            tmr = new Timer();       
        }
        changeBullet(true);
        weapon = newWeapon;
        bulletType = type;
        
  /*       // Set time for each bullet type     
        int time;
       if(bulletType == 2){
            time = 20000;
        }else{
            time = 10000;       
                    }   */
 
     // Set timertask for bullet type   
     tmr.schedule(new TimerTask() {
          @Override 
          public void run(){
          weapon = originWeapon; 
          bulletType = 1;
          changeBullet(false);
          tmr.cancel();
          tmr.purge();
          tmr = new Timer();
        }
        },20000);
    }
    
    public void changeBullet(boolean bool){
        bullet = bool;
    }
    public boolean bullet(){
        return bullet;
    }
    
    public void setTankTankMode(boolean check){
        tankMode = check;
    }
    
    
    public boolean isTankMode(){
        return tankMode;
    }
   
    public int getTankID() {
        return tankID;
    }
    
    public int getLifeCount() {
        return life;
    }  
    
    public Image getImg() {
        return img;
    }
}  
      

 

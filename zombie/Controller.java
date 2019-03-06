
package zombieland;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

//Constructor
public class Controller extends JPanel implements ActionListener, KeyListener{
    
    private static final int BASE_WIDTH = 320;
    private static final int BASE_HEIGHT = 240;
    private static final double GAME_SCALE = 4;
    private static final int MAP_WIDTH = (int)(BASE_WIDTH * GAME_SCALE);
    private static final int MAP_HEIGHT = (int)(BASE_HEIGHT * GAME_SCALE);
    private static final int FRAME_WIDTH = 960;
    private static final int FRAME_HEIGHT = 540;
    private static final int MINIMAP_WIDTH = (int)(MAP_WIDTH *0.15);
    private static final int MINIMAP_HEIGHT = (int)(MAP_HEIGHT *0.15);
    
    private enum STATE { MENU, GAME, ENDGAME }
    private STATE state = STATE.MENU;
    private BufferedImage titleImg;
    private int counter = 0;   
    private int gameSpeed = 10; 
    private Timer timer; 

    public static Player player1, player2;
    public static ArrayList<Bullet> bullets1, bullets2;
    public static PowerUp power;
    public static Walls walls;
    public static Audio audio;
    
    
  //  private ArrayList<Enemy> enemies = new ArrayList<>();
    
    public static EnemyCreation enemies;
    
    private Rectangle bullet;   
    private MapGenerator Map; 
    
    private int dirX, dirY, degree;
    
    private BufferedImage bufferedImg, player1View, player2View;
    private Image miniMap;
    
    private Graphics2D g2;
    
    private int level;
      
    public Controller(){       
       
    // load the title image
       try {
           titleImg = ImageIO.read(new File("resources/Title.jpg"));//change to your path of file           
       }catch(IOException ex) {//file did not load properly
           ex.printStackTrace();
       }
        
        Map = new MapGenerator("resources/Background.bmp", MAP_WIDTH, MAP_HEIGHT, GAME_SCALE);
 
       // Create player2 tank object
       player1 = new Player("resources/player1.png","resources/Weapon.png" , 1130, 100, 1); 
   
       // Create player2 tank object
       player2 = new Player("resources/player2.png", "resources/Weapon.png" , 150, 100, 2); 
             
       bullets1 = new ArrayList<Bullet>(); // tank1's bullet
       bullets2 = new ArrayList<Bullet>(); // tank2's bullet
  
       //Create object sound
       audio = new Audio("resources/BgMusic.mid","resources/Explosion_small.wav","resources/Explosion_large.wav");
       
       // Wall object
       walls = new Walls("resources/Wall1.gif", "resources/Water.png");
       
       //Create enemy object
       enemies = new EnemyCreation();
       
       // Start with lvl 1
       level = 1;
        
       // Create PowerUp Object
        power = new PowerUp("resources/Bouncing.png", "resources/Tank1.jpg","resources/FreezingBullet.png");
        addKeyListener(this);       
        //This will focus on the key when a key is pressed
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);       
        // Timer will refresh the paintComponent every 10 milisecond. 
       timer = new Timer(gameSpeed, this);
       timer.start();
        
    }
 
    
    @Override
    public void paintComponent(Graphics g){ 
        super.paintComponent(g);
     
        // Starting Menu
        if(state == STATE.MENU) {
            g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
            g.drawImage(titleImg, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);
            g.setFont(new Font("SANS_SERIF", Font.BOLD, 30));
            if(counter % 50 <= 25 ) {
                g.setColor(Color.YELLOW);
            } else if(counter % 50 > 25 ) {
                g.setColor(Color.BLUE);
            }
            if (counter < 50) {
                counter++;
            } else {
                counter = 0;
            }
            g.drawString("Press Enter to Start", FRAME_WIDTH/2-140, FRAME_HEIGHT-FRAME_HEIGHT/5);
            return;
        }
        
        // Game Over
        if(state == STATE.ENDGAME) {
            if(player1.isLost() || player2.isLost()) {
                g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setFont(new Font("SANS_SERIF", Font.BOLD, 70));
                g.setColor(Color.RED);
                g.drawString("Game Over", FRAME_WIDTH/2-180, FRAME_HEIGHT/4);
            }
            else{
                 g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                 g.setFont(new Font("SANS_SERIF", Font.BOLD, 70));
                 g.setColor(Color.WHITE);
                 g.drawString("You Win", FRAME_WIDTH/2-180, FRAME_HEIGHT/4);
                 g.drawImage(player2.getImg(), FRAME_WIDTH/2-50, FRAME_HEIGHT/4+20, 100, 100, null);
                 g.drawImage(player2.getImg(), FRAME_WIDTH/2-50, FRAME_HEIGHT/4+20, 100, 100, null);
            }
            g.setFont(new Font("SANS_SERIF", Font.BOLD, 25));
            g.setColor(Color.RED);
            g.drawString("press ENTER to play again", FRAME_WIDTH/2-155, FRAME_HEIGHT-FRAME_HEIGHT/3);
            g.setColor(Color.BLUE);
            g.drawString("press ESC to exit", FRAME_WIDTH/2-100, FRAME_HEIGHT-FRAME_HEIGHT/3+40);
            return;
        }
        
        // create a buffer image to store the entire map
        bufferedImg = (BufferedImage)createImage(MAP_WIDTH, MAP_HEIGHT);
        g2 = bufferedImg.createGraphics();
  
        // Draw Background    
        g2.drawImage(Map.getImage(),0,0,Map.getWidth(), Map.getHeight(),null); 
        // draw walls 
        walls.draw(g2);
        
        
    // Set game level up
      if(enemies.getEnemy().isEmpty()){
        enemies.setLevel(level++);
        }
      
        for(int i = 0; i < enemies.getEnemy().size(); i++){
         enemies.getEnemy().get(i).draw(g2);
        }
     //   enemy.draw(g2);
        
        
        
        for(int i = 0; i < bullets1.size(); i++) {
            if(!bullets1.get(i).getFire()) 
            bullets1.remove(i);
        }
      
        for(int i = 0; i < bullets2.size(); i++) {
            if(!bullets2.get(i).getFire()) 
            bullets2.remove(i);
        }
       

        // Draw players     
        if(!player1.isLost()){
            player1.draw(g2);
        } 
        if(!player2.isLost()){
            player2.draw(g2);
        }


        // Draw bullets
        for(int i = 0; i < bullets1.size(); i++){
            bullets1.get(i).DrawBullet(g2);
        }
        for(int i = 0; i < bullets2.size(); i++){
            bullets2.get(i).DrawBullet(g2);
        }
 
        // Split Screen
        int player1x, player1y, player2x, player2y;

        if(player1.coordinateAtX() - FRAME_WIDTH/4 > 0) {
            player1x = player1.coordinateAtX() - FRAME_WIDTH/4;
        } else {
            player1x = 0;
        }

        if(player1.coordinateAtY() - FRAME_HEIGHT/2 > 0) {
            player1y = player1.coordinateAtY()- FRAME_HEIGHT/2;
        } else {
            player1y = 0;
        }

        if(player1x > MAP_WIDTH - FRAME_WIDTH/2) {
            player1x = MAP_WIDTH - FRAME_WIDTH/2;
        }

        if(player1y > MAP_HEIGHT - FRAME_HEIGHT) {
            player1y = MAP_HEIGHT - FRAME_HEIGHT;
        }

        if(player2.coordinateAtX() - FRAME_WIDTH/4 > 0) {
            player2x = player2.coordinateAtX() - FRAME_WIDTH/4;
        } else {
            player2x = 0;
        }

        if(player2.coordinateAtY() - FRAME_HEIGHT/2 > 0) {
            player2y = player2.coordinateAtY() - FRAME_HEIGHT/2;
        } else {
            player2y = 0;
        }

        if(player2x > MAP_WIDTH - FRAME_WIDTH/2) {
            player2x = MAP_WIDTH - FRAME_WIDTH/2;
        }

        if(player2y > MAP_HEIGHT - FRAME_HEIGHT) {
            player2y = MAP_HEIGHT - FRAME_HEIGHT;
        }

        player1View = bufferedImg.getSubimage(player1x, player1y, FRAME_WIDTH/2, FRAME_HEIGHT);
        player2View = bufferedImg.getSubimage(player2x, player2y, FRAME_WIDTH/2, FRAME_HEIGHT);
        g.drawImage(player1View, FRAME_WIDTH/2, 0, this);
        g.drawImage(player2View, 0, 0, this);
        
        // make a two-pixel gap between two screens
        g.fillRect(FRAME_WIDTH/2-1, 0, 2, FRAME_HEIGHT); 

        // Mini Map
        miniMap = createImage(MINIMAP_WIDTH, MINIMAP_HEIGHT);
        miniMap = bufferedImg.getScaledInstance(MINIMAP_WIDTH, MINIMAP_HEIGHT, Image.SCALE_DEFAULT);
        g.drawImage(miniMap, FRAME_WIDTH/2-MINIMAP_WIDTH/2, FRAME_HEIGHT-MINIMAP_HEIGHT-34, null);

        // Health Bar & Life Count - Player1
        g.setColor(Color.YELLOW);
        g.setFont(new Font("SANS_SERIF", Font.BOLD, 20));
        g.drawString("Player 1" , FRAME_WIDTH/2+MINIMAP_WIDTH/2+170, FRAME_HEIGHT-100); 
        g.drawString("Lives: ", FRAME_WIDTH/2+MINIMAP_WIDTH/2+290, FRAME_HEIGHT-100); 
        if(player1.getLifeCount() >= 1) {
            g.fillOval(FRAME_WIDTH/2+MINIMAP_WIDTH/2+290, FRAME_HEIGHT-95, 20, 20);
        } 
        if(player1.getLifeCount() >= 2) {
            g.fillOval(FRAME_WIDTH/2+MINIMAP_WIDTH/2+312, FRAME_HEIGHT-95, 20, 20);
        } 
        if(player1.getLifeCount() >= 3) {
            g.fillOval(FRAME_WIDTH/2+MINIMAP_WIDTH/2+334, FRAME_HEIGHT-95, 20, 20);
        }
        g.setColor(Color.WHITE);
        g.fillRect(FRAME_WIDTH/2+MINIMAP_WIDTH/2+170, FRAME_HEIGHT-95, 100, 20);
        if(player1.getHealth() > 100)
            g.setColor(Color.GREEN);
        else
            g.setColor(Color.RED);
        g.fillRect(FRAME_WIDTH/2+MINIMAP_WIDTH/2+170,FRAME_HEIGHT-95,player1.getHealth()/4,20); 

        // Health Bar & Life Count - Player2
        g.setColor(Color.BLUE);
        g.setFont(new Font("SANS_SERIF", Font.BOLD, 20));
        g.drawString("Player 2" , 30, FRAME_HEIGHT-100); 
        g.drawString("Lives: " , 150, FRAME_HEIGHT-100); 
        if(player2.getLifeCount() >= 1) {
            g.fillOval(150, FRAME_HEIGHT-95, 20, 20);
        } 
        if(player2.getLifeCount() >= 2) {
            g.fillOval(172, FRAME_HEIGHT-95, 20, 20);
        } 
        if(player2.getLifeCount() >= 3) {
            g.fillOval(194, FRAME_HEIGHT-95, 20, 20);
        }
        g.setColor(Color.WHITE);
        g.fillRect(30, FRAME_HEIGHT-95, 100, 20);
        if(player2.getHealth() > 100)
            g.setColor(Color.GREEN);
        else
            g.setColor(Color.RED);
        g.fillRect(30,FRAME_HEIGHT-95,player2.getHealth()/4,20); 

        // Reach End Game
        if(player1.isLost() || player2.isLost()){
            state = STATE.ENDGAME;
        }

            
        
        g.dispose();  
    }

    // This is where the game will be run
    // All the functions go here
    @Override
    public void actionPerformed(ActionEvent e) {
       timer.start();
       run();
     repaint();
    }
  
    
    
    // Beginning of run()
    public void run(){   
        if(state == STATE.MENU) {
            return; 
        }
        
        // Function for player 1       
        if(!player1.isLost()){
            // check collision between bullet from player1 to player2 and wall?
           for(int i = 0; i < bullets1.size(); i++){ 
               audio.smallExplosion();    //Play explosion each time shoot the bullet
               bullet = bullets1.get(i).getBullet();
               
            // Collision between Player1 and zombies   
            for(int count = 0 ; count < enemies.getEnemy().size(); count++){
               if(bullet.intersects(enemies.getEnemy().get(count).enemy())){
                   enemies.getEnemy().get(count).Hit();
                   
                   // Type 3 is freezing bullet
                   if(bullets1.get(i).getType() == 3){
                       enemies.getEnemy().get(count).freezeIt();
                   }
                   // Slow down zombie with type2 bullet(poison bullet)
                   else if (bullets1.get(i).getType() == 2)
                       enemies.getEnemy().get(count).setSpeed();

                   if(enemies.getEnemy().get(count).dead()){
                       enemies.getEnemy().remove(count);
                   }
         
                       
                   bullets1.get(i).setFire(false);
                   break;
               }
               
            }

                   for(int row = 0; row < walls.walls.length && bullets1.get(i).getFire(); row++){
                   for(int col = 0; col < walls.walls[row].length; col++){
                       // remove bullet when it his unbreable walls
                      if(bullet.intersects(row*walls.wallsize, col*walls.wallsize, walls.wallsize, walls.wallsize)
                              && walls.walls[row][col] ==  2) {
                               bullets1.get(i).setFire(false);
                               break;
                       }

                       //Destroying item if player does not want it
                     if (walls.walls[row][col] >= 7 && 
                      bullet.intersects(row*walls.wallsize, col*walls.wallsize, walls.wallsize, walls.wallsize)) { 
                            System.out.println("Item is destroyed!");
                             walls.setWall(row, col, 0);
                            bullets1.get(i).setFire(false);   
                             break;

                       }

                     // Display Puwerup items after breakable walls are detsroyed
                      if(walls.walls[row][col] == 1 || walls.walls[row][col] == 3 || 
                             walls.walls[row][col] == 4 || walls.walls[row][col] == 5) {
                            if(bullet.intersects(row*walls.wallsize, col*walls.wallsize, walls.wallsize, walls.wallsize))
                           {               
                               switch (walls.walls[row][col]) {
                                   case 1:  
                                       walls.setWall(row, col, 0);
                                       bullets1.get(i).setFire(false);
                                       break;
                                   case 3:   
                                       walls.setWall(row, col, 7);
                                       bullets1.get(i).setFire(false);
                                       break;
                                   case 4:
                                       walls.setWall(row, col, 8);
                                       bullets1.get(i).setFire(false);
                                       break;
                                   case 5:
                                       walls.setWall(row, col, 9);
                                       bullets1.get(i).setFire(false);
                                       break;
                                   default:
                                       break;                                       
                                    } 
                               break;
                              }
                      }
                   }          
               }              
           }        
        }   
        
        
        //********************************************************************************************   
        // Function for player2
        if(!player2.isLost()){
           // check collision between bullet from player2 to player1 and walls?
           for(int i = 0; i < bullets2.size(); i++){
               audio.smallExplosion();    //Play explosion each time shoot the bullet
               bullet = bullets2.get(i).getBullet();   //Initialize bullet
          
            for(int count = 0 ; count < enemies.getEnemy().size(); count++)
               if(bullet.intersects(enemies.getEnemy().get(count).enemy())){
                   enemies.getEnemy().get(count).Hit();
                   
                   
                   
                   if(bullets2.get(i).getType() == 3){
                       enemies.getEnemy().get(count).freezeIt();
                   }
                   // Slow zombie with type2 bullet speed
                   else if (bullets2.get(i).getType() == 2)
                       enemies.getEnemy().get(count).setSpeed();
                   
                   
                   
                   if(enemies.getEnemy().get(count).dead()){
                       enemies.getEnemy().remove(count);
                   }
                   
                   
                   bullets2.get(i).setFire(false);
                   break;
               }     

               // Check if a bullet hits walls?

                   for(int row = 0; row < walls.walls.length && bullets2.get(i).getFire(); row++){
                   for(int col = 0; col < walls.walls[row].length; col++){
                     // remove bullet when it his unbreable walls
                     if(bullet.intersects(row*walls.wallsize, col*walls.wallsize, walls.wallsize, walls.wallsize)
                              && walls.walls[row][col] ==  2) {
                               bullets2.get(i).setFire(false);
                               break;
                       }


                     //Destroy item(s) if player does not want it
                     if (walls.walls[row][col] >= 7 &&
                         bullet.intersects(row*walls.wallsize, col*walls.wallsize, walls.wallsize, walls.wallsize)) { 
                            System.out.println("Item is destroyed!");
                             walls.setWall(row, col, 0);
                            bullets2.get(i).setFire(false);   
                             break;   
                       }
                      // Display Puwerup items after breakable walls are detsroyed
                      if (walls.walls[row][col] == 1 || walls.walls[row][col] == 3 ||
                        walls.walls[row][col] == 4 || walls.walls[row][col] == 5){        
                               if(bullet.intersects(row*walls.wallsize, col*walls.wallsize, walls.wallsize, walls.wallsize))
                              {
                                   switch (walls.walls[row][col]) {
                                   case 1:
                                       walls.setWall(row, col, 0);
                                       bullets2.get(i).setFire(false);
                                       break;
                                   case 3:
                                       walls.setWall(row, col, 7);
                                       bullets2.get(i).setFire(false);
                                       break;
                                   case 4:
                                       walls.setWall(row, col, 8);
                                       bullets2.get(i).setFire(false);
                                       break;
                                   case 5:
                                       walls.setWall(row, col, 9);
                                       bullets2.get(i).setFire(false);
                                       break;
                                   default:
                                       break;
                               }
                                   break;
                           }
                       }
                   }
               }
           } 
        }

        
      for(int count = 0 ; count < enemies.getEnemy().size(); count++){
       if ((enemies.getEnemy().get(count).enemy().intersects(player1.player()) && player1.isTankMode())           
        || (enemies.getEnemy().get(count).enemy().intersects(player2.player()) && player2.isTankMode())){
              // If tank is roll over zombie , then zombie is killed, remove zombie
                if(!enemies.getEnemy().get(count).isBoss())
                    enemies.getEnemy().remove(count);
            break;
            }
      // If a player gets bit, subtract player's health
       if(enemies.getEnemy().get(count).enemy().intersects(player1.player()) && !player1.isTankMode()){
           player1.gotHit();
       }
       if(enemies.getEnemy().get(count).enemy().intersects(player2.player()) && !player2.isTankMode()){
       player2.gotHit();
       }
         }
   

    }
// End of run() 
    
    // 
    @Override
    public void keyPressed(KeyEvent e) {

        //Player1 keylisteners
        if(!player1.isLost() && !player2.isLost()){      
        if(e.getKeyCode() == KeyEvent.VK_UP ){
            player1.setUp(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN ){
             player1.setDown(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT ){
             player1.setleft(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT ){
             player1.setRight(true);
        }  
        if(e.getKeyCode() == KeyEvent.VK_P ){
             player1.setFire(true,1);
        }

    //****************************************************************
        //Player2 keyListener
        if(e.getKeyCode() == KeyEvent.VK_W ){
            player2.setUp(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_S ){
             player2.setDown(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_A ){
             player2.setleft(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_D ){
             player2.setRight(true);
        }  
        if(e.getKeyCode() == KeyEvent.VK_SPACE ){
             player2.setFire(true,2);
        }   
    }    

 
    // Starting Manu
    if(state == STATE.MENU && e.getKeyCode() == KeyEvent.VK_ENTER){  
        state = STATE.GAME;
    }   
        
    // Options after game is over
    // Exit
    if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
     System.exit(0);

    // Play Again
    if(state == STATE.ENDGAME && e.getKeyCode() == KeyEvent.VK_ENTER){  
       state = STATE.GAME;
       Map = new MapGenerator("resources/Background.bmp", MAP_WIDTH, MAP_HEIGHT, GAME_SCALE);
       player1 = new Player("resources/player1.png","resources/Weapon.gif" , 1130, 100, 1);  
   
       player2 = new Player("resources/player2.png", "resources/Weapon.gif" , 150, 100, 2); 
             
       bullets1 = new ArrayList<Bullet>();
       bullets2 = new ArrayList<Bullet>();
       
       walls = new Walls("resources/Wall1.gif", "resources/Water.png");
       power = new PowerUp("resources/Bouncing.png", "resources/Tank1.jpg","resources/FreezingBullet.png");
       
       level = 1;
       enemies = new EnemyCreation();
       repaint();
    }
 
    } 
    @Override
    public void keyReleased(KeyEvent e) {
     if(e.getKeyCode() == KeyEvent.VK_UP ){
         player1.setUp(false);
     }
     if(e.getKeyCode() == KeyEvent.VK_DOWN ){
         player1.setDown(false);
     }
     if(e.getKeyCode() == KeyEvent.VK_LEFT ){
         player1.setleft(false);
     }
     if(e.getKeyCode() == KeyEvent.VK_RIGHT ){
         player1.setRight(false);
     }
     if(e.getKeyCode() == KeyEvent.VK_P ){
       player1.setFire(false,1);
     }
     
     //Player2
     if(e.getKeyCode() == KeyEvent.VK_W ){
        player2.setUp(false);
     }
     if(e.getKeyCode() == KeyEvent.VK_S ){
         player2.setDown(false);
     }
     if(e.getKeyCode() == KeyEvent.VK_A ){
         player2.setleft(false);
     }
     if(e.getKeyCode() == KeyEvent.VK_D ){
         player2.setRight(false);
     }
     if(e.getKeyCode() == KeyEvent.VK_SPACE ){
         player2.setFire(false,2);
     }
      
}

    @Override
    public void keyTyped(KeyEvent e) {
    }

    //  Main Method   
    public static void main(String[] args) {     
        JFrame tankWar = new JFrame("Tank war");
        Controller controller = new Controller();
        tankWar.setBounds(300, 150, FRAME_WIDTH, FRAME_HEIGHT);
        tankWar.setResizable(false);
        tankWar.setVisible(true);
        tankWar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tankWar.add(controller);  
    }
    
}


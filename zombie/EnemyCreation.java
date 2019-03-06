/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zombieland;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import static zombieland.Controller.enemies;


public class EnemyCreation {
    
private Image img;
private ArrayList<Enemy> enemies = new ArrayList<>();
private int normalZombie = 5;
private int bigZombie = 1;

    
    
    public void setLevel(int x){
        
            int lvl = x;


            for(int i = normalZombie*lvl; i > 0; i--){
                int randomX = 300 + new Random().nextInt(600);
                int randomY = 300 + new Random().nextInt(400);
                enemies.add(new Enemy("resources/zombie1.png", randomX, randomY,false));
            }
            
            for(int i = bigZombie*lvl ; i > 0; i--){
            int randomX = 300 + new Random().nextInt(600);
            int randomY = 300 + new Random().nextInt(400);
            int r = -30 + new Random().nextInt(30);
            enemies.add(new Enemy("resources/zombie2.png", randomX, randomY,true));
            enemies.add(new Enemy("resources/zombie1.png", randomX + r, randomY + r,false));
            }

    }
    
    public ArrayList<Enemy> getEnemy(){
        return enemies;
    }
    
}


package zombieland;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Audio {
 
    private Clip bgMusic, smallExpl, largeExpl;
    public Audio(String music, String Small_Explosion, String Large_Explosion)
    {
  
        try {
            // Set background music
             File file = new File(music);
             
             
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                bgMusic = AudioSystem.getClip();  //get Sound resource         
                bgMusic.open(audio);              //load sound
                // Decrease background player
                ((FloatControl)bgMusic.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-20);
                bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
                

                
            // Load and open small explosion file
            File small_expl = new File(Small_Explosion);
            AudioInputStream expl_S = AudioSystem.getAudioInputStream(small_expl);
                smallExpl = AudioSystem.getClip();
                smallExpl.open(expl_S);              
                //set up volumn of explosion sound
                ((FloatControl) smallExpl.getControl(FloatControl.Type.MASTER_GAIN)).setValue(1);
                
       
            // Load and open large explosion file
            File large_expl = new File(Large_Explosion);
             AudioInputStream expl_L = AudioSystem.getAudioInputStream(large_expl);
                largeExpl = AudioSystem.getClip();
                largeExpl.open(expl_L);
                //set up volumn of explosion sound
                ((FloatControl)largeExpl.getControl(FloatControl.Type.MASTER_GAIN)).setValue(1);
               
            } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }  
            catch (LineUnavailableException ex) {
            ex.printStackTrace();
             }
    }
    // play small explosion
    public void smallExplosion(){
        smallExpl.setFramePosition(0);
        smallExpl.start();
    }
    
    // play large explosion
     public void largeExplosion(){
        largeExpl.setFramePosition(0);
        largeExpl.start();
    }

}


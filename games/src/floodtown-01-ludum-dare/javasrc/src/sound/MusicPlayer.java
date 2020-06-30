package sound;

import game.ResourceIO;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class MusicPlayer {

    public static void play(SoundEffect effect) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream audio;
            audio = ResourceIO.loadSound(getCorrespondingFile(effect));
            clip.open(audio);
            clip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCorrespondingFile(SoundEffect effect) {
        double r = Math.random();
        switch (effect) {
            case hover:
                return "/sounds/hover2.wav";
            case placing:
                if (r < 0.25) {
                    return "/sounds/placing.wav";
                } else if (r < 0.5) {
                    return "/sounds/placing1.wav";
                } else if (r < 0.75) {
                    return "/sounds/placing2.wav";
                }
                return "/sounds/placing3.wav";
            case waves:
                if (r < 0.2) {
                    return "/sounds/waves0.wav";
                } else if (r < 0.4) {
                    return "/sounds/waves1.wav";
                } else if (r < 0.6) {
                    return "/sounds/waves3.wav";
                } else if (r < 0.8) {
                    return "/sounds/waves5.wav";
                }
                return "/sounds/waves4.wav";
        }
        return "";
    }

}

package com.vengeance.game.main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[10];

    public Sound() {
        soundURL[0] = getClass().getResource("/resources/sounds/mainSound.wav");
        soundURL[1] = getClass().getResource("/resources/sounds/ItemPickUp.wav");
        soundURL[2] = getClass().getResource("/resources/sounds/playerHurt.wav");
        soundURL[3] = getClass().getResource("/resources/sounds/sword.wav");
        soundURL[4] = getClass().getResource("/resources/sounds/door.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

}

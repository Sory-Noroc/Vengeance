package com.vengeance.game.animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimationFactory {

     static ArrayList<Animation> animations = new ArrayList<>();
     static public Animation createAnimation(int spriteCount, int delay, BufferedImage... images) {
        Animation animation = new Animation(spriteCount, delay, images);
        animations.add(animation);
        return animation;
    }


    public static void updateAnimations() {
        for (int i = 0; i < animations.size(); i++) {
            animations.get(i).update();
        }
    }
}

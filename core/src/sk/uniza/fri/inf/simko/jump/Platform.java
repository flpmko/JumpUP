/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.inf.simko.jump;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author flpmko
 */
public class Platform extends GameObject {
    //private Rectangle bounds;
    private long lastSpawnTime; //cas od posledneho spawnu
    private boolean isHit; //ci s nou hrac uz kolidoval
    
    //default konstruktor, spawne platformu s random suradnicami
    public Platform() {
        super("plank.png", 64, 26, MathUtils.random(0, 500 - 64), MathUtils.random(1000, 2000));
        this.getBounds().height = 26 * 3;
        this.lastSpawnTime = TimeUtils.nanoTime();
        this.isHit = false;
    }
    
    //parametricky konstruktor, spawne platformu presne kde mu zadam
    public Platform(int x, int y) {
        super("plank.png", 64, 26, x, y);
        this.getBounds().height = 26 * 3;
        this.lastSpawnTime = TimeUtils.nanoTime();
        this.isHit = false;
    }

    public long getLastSpawnTime() {
        return this.lastSpawnTime;
    }

    public void setLastSpawnTime(long lastSpawnTime) {
        this.lastSpawnTime = lastSpawnTime;
    }

    public boolean getIsHit() {
        return this.isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }
}

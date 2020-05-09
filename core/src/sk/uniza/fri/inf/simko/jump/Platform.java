/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.inf.simko.jump;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author flpmko
 */
public class Platform extends GameObject {
    private Rectangle bounds;
    private long lastSpawnTime;
    private boolean isHit;
    
    public Platform() {
        super("plank.png", 64, 26, MathUtils.random(0, 500 - 64), MathUtils.random(1000, 2000));
        this.bounds = new Rectangle();
        this.bounds.height = 26 * 3;
        this.bounds.width = 64 * 2;
        this.bounds.x = this.getRectangle().x - 64 / 2;
        this.bounds.y = this.getRectangle().y - 26 / 2;
        this.lastSpawnTime = TimeUtils.nanoTime();
        this.isHit = false;
    }
    
    public Platform(int x, int y) {
        super("plank.png", 64, 26, x, y);
        this.bounds = new Rectangle();
        this.bounds.height = 26 * 3;
        this.bounds.width = 64 * 2;
        this.bounds.x = x;
        this.bounds.y = y;
    }
    
    public Rectangle getBounds() {
        return this.bounds;
    }
    
    public void setHeight(int height) {
        this.getRectangle().height = height;
    }
    
    public void setWidth(int width) {
        this.getRectangle().width = width;
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

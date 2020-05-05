/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.inf.simko.jump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author flpmko
 */
public class Player extends GameObject {

    private double gravity = 1;
    
    private float acceleration;
    private float time;
    private float dy;
    private float maxDY;
    private float lastY;
    
    private boolean isJumping;
    private boolean canJump;
    private boolean isFalling;

    private Rectangle rectForCoins;

    public Player() {
        super("man.png", 32, 1, 500 / 2 - 16, 30);
        this.acceleration = 5; //100
        this.time = 0;
        this.dy = 0;
        this.maxDY = 10;
        this.lastY = this.getRectangle().y;
        
        this.isJumping = false;
        this.canJump = true;
        this.isFalling = true;

        this.rectForCoins = new Rectangle();
        this.rectForCoins.height = 128;
        this.rectForCoins.width = 64;
        this.rectForCoins.x = 500 / 2 - 32;
        this.rectForCoins.y = 30;
    }

    public float getAcceleration() {
        return this.acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getDy() {
        return this.dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public boolean getCanJump() {
        return this.canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public boolean getIsFalling() {
        return this.isFalling;
    }

    public void setIsFalling(boolean isFalling) {
        this.isFalling = isFalling;
    }

    public double getGravity() {
        return this.gravity;
    }
    
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public boolean getIsJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public Rectangle getRectForCoins() {
        return this.rectForCoins;
    }

    public void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.getRectangle().x += 400 * Gdx.graphics.getDeltaTime();
            this.rectForCoins.x += 400 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.getRectangle().x -= 400 * Gdx.graphics.getDeltaTime();
            this.rectForCoins.x -= 400 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //this.canJump = true;
            this.jumping();
            //this.time = 0;
        }
    }

    public void jumping() {
        this.time += Gdx.graphics.getDeltaTime();
        

        if (this.canJump) {
            this.isJumping = true;
            this.dy -= this.acceleration;
            this.getRectangle().y -= 0.3;
            //this.dy;
            //this.rectForCoins.y += this.dy;

            if (this.time > 0.08) {
                this.canJump = false;
                this.isJumping = false;
                this.time = 0;
                //this.dy = 0;
            }
        }
    }

    public void falling() {
        this.dy += this.gravity;
        this.getRectangle().y -= this.dy;
        this.rectForCoins.y -= this.dy;
        this.lastY = this.getRectangle().y + this.dy;

        if (this.getRectangle().y < this.lastY) {
            this.isFalling = true;
        } else {
            this.isFalling = false;
        }
        if (this.dy > this.maxDY) {
            this.dy = this.maxDY;
        }
    }
}
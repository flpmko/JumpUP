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

    public static final double JUMP = 0.3;

    private double gravity = 1;
    private double jumpBuffer = 0.3;
    private double maxJumpTime = 0.08;

    private float acceleration;
    private float jumpTime;
    private float dy; //delta y, velkost zmeny
    private float maxDY;
    private float lastY;

    private boolean isJumping;
    private boolean canJump;
    private boolean isFalling;
    private boolean canMove = true;

    private Rectangle rectForCoins;

    public Player() {
        super("man.png", 32, 1, 500 / 2 - 16, 30);
        this.acceleration = 5;
        this.jumpTime = 0;
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

    public boolean getIsJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public Rectangle getRectForCoins() {
        return this.rectForCoins;
    }

    public double getJumpBuffer() {
        return this.jumpBuffer;
    }

    public void setJumpBuffer(double jumpBuffer) {
        this.jumpBuffer = jumpBuffer;
    }

    public double getMaxJumpTime() {
        return this.maxJumpTime;
    }

    public void setMaxJumpTime(double maxJumpTime) {
        this.maxJumpTime = maxJumpTime;
    }

    public boolean getCanMove() {
        return this.canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public float getLastY() {
        return this.lastY;
    }

    public void move() {
        if (this.canMove) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                this.getRectangle().x += 500 * Gdx.graphics.getDeltaTime();
                this.rectForCoins.x += 500 * Gdx.graphics.getDeltaTime();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                this.getRectangle().x -= 500 * Gdx.graphics.getDeltaTime();
                this.rectForCoins.x -= 500 * Gdx.graphics.getDeltaTime();
            }
        }
    }

    public void jumping() {
        this.jumpTime += Gdx.graphics.getDeltaTime();

        if (this.canJump) {
            this.isJumping = true;
            this.dy -= this.acceleration; //znizujem velkost zmeny v smere y o akceleraciu, akceleracia je vacsia ako gravitacia
            this.getRectangle().y -= this.jumpBuffer; //tym padom vo fallingu zacne ist hore, takze tu este od y odratavam konstantu
            this.rectForCoins.y -= this.jumpBuffer;

            if (this.jumpTime > this.maxJumpTime) { //ak cas skocu presiahne max povoleny cas, skakanie sa zakaze a cas sa zresetuje
                this.canJump = false;
                this.isJumping = false;
                this.jumpTime = 0;
            }
        }
    }

    public void falling() {
        this.dy += this.gravity; //zvysujem velkost zmeny v smere y o gravitaciu
        this.getRectangle().y -= this.dy; //znizujem y suradnicu o velkost zmeny
        this.rectForCoins.y -= this.dy;
        
        this.lastY = this.getRectangle().y + this.dy; //zapamatavam si predoslu polohu y

        if (this.getRectangle().y < this.lastY) { //porovnavam aktualny a predosli y, aby som zistil, ci padam
            this.isFalling = true;
        } else {
            this.isFalling = false;
        }
        if (this.dy > this.maxDY) { //zabranuje tomu, aby sa velkost zmeny v smere y stale zvacsovala
            this.dy = this.maxDY;
        }
    }
}

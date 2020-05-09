/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.inf.simko.jump;

/**
 *
 * @author flpmko
 */
public class Spring extends Platform {
    private float jumpTimeResetTimer;
    private final double springJumpTime = 0.2;
    
    public Spring() {
        super();
        this.setTexture("spring.png");
        this.jumpTimeResetTimer = 0;
    }

    public double getSpringJumpTime() {
        return this.springJumpTime;
    }

    public float getJumpTimeResetTimer() {
        return this.jumpTimeResetTimer;
    }

    public void setJumpTimeResetTimer(float jumpTimeResetTimer) {
        this.jumpTimeResetTimer = jumpTimeResetTimer;
    }
}

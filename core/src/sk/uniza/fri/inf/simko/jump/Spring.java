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
    private boolean isHit;
    private boolean isSprung;
    private final double springJumpTime = 0.2;
    
    public Spring() {
        super();
        this.setTexture("spring.png");
        this.isHit = false;
        this.isSprung = false;
    }

    public boolean getIsHit() {
        return this.isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }

    public double getSpringJumpTime() {
        return this.springJumpTime;
    }

    public boolean getIsSprung() {
        return this.isSprung;
    }

    public void setIsSprung(boolean isSprung) {
        this.isSprung = isSprung;
    }
}

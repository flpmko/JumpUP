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
    private float power;
    private float time;
    
    public Spring() {
        super();
        this.setTexture("spring.png");
        this.isHit = false;
        this.power = 3;
    }

    public boolean getIsHit() {
        return this.isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }

    public float getPower() {
        return this.power;
    }
}

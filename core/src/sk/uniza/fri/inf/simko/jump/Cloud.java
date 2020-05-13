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

public class Cloud extends Platform {
    private float hitTime; //cas od kolizie s danym cloudom
    
    public Cloud() {
        super();
        this.setTexture("cloud.png");
        this.hitTime = 0;
    }

    public float getHitTime() {
        return this.hitTime;
    }

    public void setHitTime(float hitTime) {
        this.hitTime = hitTime;
    }
}

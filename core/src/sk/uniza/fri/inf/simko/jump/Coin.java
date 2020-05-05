/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.inf.simko.jump;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author flpmko
 */
public class Coin extends GameObject {
    
    public Coin() {
        super("coin.png", 32, 32, MathUtils.random(0, 500 - 32), MathUtils.random(1000, 2000));
    }
}

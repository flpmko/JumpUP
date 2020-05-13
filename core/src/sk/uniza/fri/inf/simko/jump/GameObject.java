/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.inf.simko.jump;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author flpmko
 */
public abstract class GameObject {
    private Rectangle rectangle; //obsahuje x, y, w, h data o objekte, koliduje s inymi objektami
    private Texture texture; //graficka reprezentacia rectangle
    private Rectangle bounds; //pouziva sa pri spawnovani platforiem, 2xh a 2xw normalneho rectangle
    
    public GameObject(String texture, int width, int height, int posX, int posY) {
        this.texture = new Texture(texture);
        this.rectangle = new Rectangle();
        
        this.rectangle.height = height;
        this.rectangle.width = width;
        this.rectangle.x = posX;
        this.rectangle.y = posY;
        
        this.bounds = new Rectangle();
        this.bounds.height = height * 2;
        this.bounds.width = width * 2;
        this.bounds.x = posX - width / 2;
        this.bounds.y = posY - height / 2;
    }
    
    public Texture getTexture() {
        return this.texture;
    }
    
    public void setTexture(String texture) {
        this.texture = new Texture(texture);
    }
    
    public Rectangle getRectangle() {
        return this.rectangle;
    }
    
    public void setHeight(int height) {
        this.getRectangle().height = height;
    }
    
    public void setWidth(int width) {
        this.getRectangle().width = width;
    }
    
    public Rectangle getBounds() {
        return this.bounds;
    }
    
    public boolean objectCollision(GameObject object) {
        if (this.getRectangle().overlaps(object.getRectangle())) { //skumam, ci sa rectangle mojho objektu prekryva s rectanglom objektu v parametri
            return true;
        } else {
            return false;
        }
    }
    
}

package sk.uniza.fri.inf.simko.jump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

public class Game extends ApplicationAdapter {

    public static final float WIDTH = 500;
    public static final float HEIGTH = 1000;

    private SpriteBatch batch;
    private Sound jump;
    private Music music;
    private OrthographicCamera camera;

    private Player player;
    private Array<GameObject> platforms;

    private Platform lastSpawnedCloud;
    private Platform lastSpawnedSpring;
    private Platform lastSpawnedPlatform;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private int coinCounter;
    private long scoreCounter;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(2);

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 500, 1000);

        this.player = new Player();
        this.platforms = new Array<>();
        this.spawnGround();
        this.spawnStart();
        this.spawnPlatform(Platform.class);
        //this.spawnGlass();
        //this.spawnSpring();

        this.lastSpawnedPlatform = new Platform();
        this.lastSpawnedSpring = new Spring();
        this.lastSpawnedCloud = new Cloud();

        this.shapeRenderer = new ShapeRenderer();
        this.coinCounter = 0;
        this.scoreCounter = 0;

        Coin coin = new Coin();
        this.platforms.add(coin);
    }

    @Override
    public void render() {
        //vyfarbenie pozadia a update kamery
        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.camera.update();

        //vykreslenie objektov
        this.batch.setProjectionMatrix(this.camera.combined);

        this.shapeRenderer.begin(ShapeType.Filled);
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.rect(this.player.getRectangle().x, this.player.getRectangle().y, this.player.getRectangle().width, this.player.getRectangle().height);
        this.shapeRenderer.end();

        this.batch.begin();
        this.batch.draw(this.player.getTexture(), this.player.getRectangle().x - 16, this.player.getRectangle().y);
        for (GameObject go : this.platforms) {
            this.batch.draw(go.getTexture(), go.getRectangle().x, go.getRectangle().y);
        }
        this.font.draw(this.batch, "jumpUP", 200, 990);
        this.font.draw(this.batch, "score: " + this.scoreCounter, 10, 990);
        this.font.draw(this.batch, "coins: " + this.coinCounter, 390, 990);
        this.batch.end();

        //pohyb playera a platform
        
        this.player.move();
        this.player.jumping();
//        if(this.player.isJumpPressed()){
//            this.player.jumping();
//        }
        
        this.player.falling();
        this.movePlatforms();

        //kontrola hranic
        this.checkPlayerBounds();
        this.checkSpawnTime();

        //kontrola kolizie
        this.checkCollision();
        this.checkCoins();
        //this.checkScore();

        //System.out.println("canJump> " + this.player.getCanJump());
        //System.out.println("isFalling> " + this.player.getIsFalling());
        System.out.println("----isJumping>" + this.player.getIsJumping());
    }

    private void spawnPlatform(Class<?> platformType) {

        boolean shouldGenerate = true;
        boolean isOverlap = false;
        Platform platform = this.createSpawnPlatform(platformType);

        while (shouldGenerate) {
            isOverlap = false;

            //checkuje overlapping
            for (GameObject p : this.platforms) {
                if (platform.getBounds().overlaps(p.getBounds())) {
                    System.out.println("overlap");
                    isOverlap = true;
                    break;
                }
            }
            if (isOverlap) {
                shouldGenerate = true;
                platform = this.createSpawnPlatform(platformType);
            } else {
                shouldGenerate = false;
                System.out.println("-------resolved");
                this.platforms.add(platform);
            }
        }
    }

    private Platform createSpawnPlatform(Class<?> platformType) {
        Platform p = new Platform();
        try {
            p = (Platform)Class.forName(platformType.getName()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        if (p instanceof Cloud) {
            this.lastSpawnedCloud = p;
        } else if (p instanceof Spring) {
            this.lastSpawnedSpring = p;
        } else {
            this.lastSpawnedPlatform = p;
        }

        return p;
    }

    public void spawnStart() {
        for (int i = 0; i < 10; i++) {
            Platform p = new Platform(MathUtils.random(0, 500 - 64), 80 + 80 * i);
            this.platforms.add(p);
        }
    }

    public void spawnCoins() {

    }

    //spawne zem na zaciatku hry
    public void spawnGround() {
        Platform ground = new Platform(0, 0);
        ground.setTexture("ground.png");
        ground.setHeight(10);
        ground.setWidth(500);
        this.platforms.add(ground);

    }

    //automaticky pohyb platformami na zaklade pohybu hraca
    private void movePlatforms() {
        if (this.player.getRectangle().y > 200) { // !this.player.getCanJump() ||
            for (Iterator<GameObject> iter = this.platforms.iterator(); iter.hasNext();) {
                GameObject p = iter.next();
                p.getRectangle().y -= 100 * Gdx.graphics.getDeltaTime();
                p.getBounds().y -= 100 * Gdx.graphics.getDeltaTime();
                if (p.getRectangle().y + 64 < 0) {
                    iter.remove();
                }
            }
        }
    }

    private void checkCoins() {
        for (GameObject platform : this.platforms) {
            if (platform instanceof Coin) {
                if (platform.getRectangle().overlaps(this.player.getRectForCoins())) {
                    this.coinCounter++;
                    this.platforms.removeValue(platform, true);
                }
            }
        }
    }
    
    private void checkScore() {
        if (this.player.getIsJumping()) {
            this.scoreCounter += this.player.getRectForCoins().y;
        }
    }

    //na zaklade casu od posledneho spawnu rozhodne, ci spawnut dalsiu platformu
    private void checkSpawnTime() {
        if (this.player.getCanJump()) {
            if (TimeUtils.nanoTime() - this.lastSpawnedPlatform.getLastSpawnTime() > 1000000000 && this.platforms.size < 15) {
                this.spawnPlatform(Platform.class);
            }
            if (TimeUtils.nanoTime() - this.lastSpawnedCloud.getLastSpawnTime() > 1000000000 && this.platforms.size < 15) {
                this.spawnPlatform(Cloud.class);
            }
            if (TimeUtils.nanoTime() - this.lastSpawnedSpring.getLastSpawnTime() > 1000000000 && this.platforms.size < 15) {
                this.spawnPlatform(Spring.class);
            }
        }
    }

    //udrzuje playera v hraniciach okna
    private void checkPlayerBounds() {
        if (this.player.getRectangle().y > 1000 - 128) {
            this.player.getRectangle().y = 1000 - 128;
        }
        if (this.player.getRectangle().x < 0) {
            this.player.getRectangle().x = 500;
        }
        if (this.player.getRectangle().x > 500) {
            this.player.getRectangle().x = 0;
        }
        if (this.player.getRectangle().y < 0) {
            this.player.getRectangle().y = 0;
        }
    }

    //kontroluje koliziu hraca a platform
    private void checkCollision() {
        for (GameObject platform : this.platforms) {
            if (!(platform instanceof Coin)) {

                //kontroluje koliziu iba ked hrac pada
                if (this.player.getIsFalling() && this.player.objectCollision(platform)) {
                    this.player.getRectangle().y = platform.getRectangle().y + platform.getRectangle().height;
                    this.player.getRectForCoins().y = platform.getRectangle().y + platform.getRectangle().height;
                    if (platform instanceof Cloud) {
                        ((Cloud)platform).setIsHit(true);
                        ((Cloud)platform).setHitTime(((Cloud)platform).getHitTime() + Gdx.graphics.getDeltaTime());
                    }
                    this.player.setCanJump(true);
                    //System.out.println("kolizia");

                }
                if (platform instanceof Cloud) {
                    if (((Cloud)platform).getIsHit() && ((Cloud)platform).getHitTime() > 0.1) {
                        this.platforms.removeValue(platform, true);

                    }
                }
            }
        }
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.shapeRenderer.dispose();
        this.font.dispose();
    }
}

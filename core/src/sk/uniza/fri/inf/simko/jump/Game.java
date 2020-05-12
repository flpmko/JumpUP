package sk.uniza.fri.inf.simko.jump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

public class Game extends ApplicationAdapter {

    public static final float WIDTH = 500;
    public static final float HEIGTH = 1000;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Player player;
    private Array<GameObject> gameObject;

    private Platform lastSpawnedCloud;
    private Platform lastSpawnedSpring;
    private Platform lastSpawnedPlatform;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private int coinCounter;
    private long scoreCounter;

    private boolean arePlatformsMoving;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(2);

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 500, 1000);

        this.player = new Player();
        this.gameObject = new Array<>();
        this.spawnGround();
        this.spawnStart();
        this.spawnPlatform(Platform.class);

        this.lastSpawnedPlatform = new Platform();
        this.lastSpawnedSpring = new Spring();
        this.lastSpawnedCloud = new Cloud();

        this.shapeRenderer = new ShapeRenderer();
        this.coinCounter = 0;
        this.scoreCounter = 0;

        this.arePlatformsMoving = false;
    }

    @Override
    public void render(){
        //vyfarbenie pozadia a update kamery
        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.camera.update();

        //vykreslenie objektov
        this.batch.setProjectionMatrix(this.camera.combined);

        //vykreslovanie
        this.batch.begin();
        this.batch.draw(this.player.getTexture(), this.player.getRectangle().x - 16, this.player.getRectangle().y);
        for (GameObject go : this.gameObject) {
            this.batch.draw(go.getTexture(), go.getRectangle().x, go.getRectangle().y);
        }
        this.font.draw(this.batch, "jumpUP", 200, 990);
        this.font.draw(this.batch, "score: " + this.scoreCounter, 10, 990);
        this.font.draw(this.batch, "coins: " + this.coinCounter, 370, 990);
        if (this.player.getRectangle().y == 0) {
            try {
                this.gameOver(this.batch);
            } catch (FileNotFoundException ex) {
                System.err.println("File not found");
            }
        }
        this.batch.end();

        //pohyb playera a platform
        this.player.move();
        this.player.jumping();
        this.player.falling();
        this.movePlatforms();

        //kontrola hranic
        this.checkPlayerBounds();
        this.checkSpawnTime();

        //kontrola kolizie
        this.checkCollision();
        this.checkCoins();
        this.checkScore();
    }

    private void spawnPlatform(Class<?> platformType) {

        boolean shouldGenerate = true;
        boolean isOverlap = false;
        Platform platform = this.createSpawnPlatform(platformType);

        while (shouldGenerate) {
            isOverlap = false;

            //checkuje overlapping
            for (GameObject p : this.gameObject) {
                if (platform.getBounds().overlaps(p.getBounds())) {
                    //System.out.println("overlap");
                    isOverlap = true;
                    break;
                }
            }
            if (isOverlap) {
                shouldGenerate = true;
                platform = this.createSpawnPlatform(platformType);
            } else {
                shouldGenerate = false;
                //System.out.println("-------resolved");
                this.gameObject.add(platform);
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
        for (int i = 0; i < 20; i++) {
            Platform p = new Platform(MathUtils.random(0, 500 - 64), 100 + 100 * i);
            this.gameObject.add(p);
        }
    }

    public void spawnCoins() {
        Coin coin = new Coin();
        this.gameObject.add(coin);
        for (GameObject object : this.gameObject) {
            if (object.objectCollision(coin) && object != coin) {
                this.gameObject.removeValue(coin, true);
            }
        }
    }

    //spawne zem na zaciatku hry
    public void spawnGround() {
        Platform ground = new Platform(0, 0);
        ground.setTexture("ground.png");
        ground.setHeight(10);
        ground.setWidth(500);
        this.gameObject.add(ground);

    }

    //automaticky pohyb platformami na zaklade pohybu hraca
    private void movePlatforms() {
        if (this.player.getRectangle().y > 200) {
            for (Iterator<GameObject> iter = this.gameObject.iterator(); iter.hasNext();) {
                GameObject p = iter.next();
                p.getRectangle().y -= 100 * Gdx.graphics.getDeltaTime();
                p.getBounds().y -= 100 * Gdx.graphics.getDeltaTime();
                this.arePlatformsMoving = true;
                if (p.getRectangle().y + 64 < 0) {
                    iter.remove();
                }
            }
        } else {
            this.arePlatformsMoving = false;
        }
    }

    private void checkCoins() {
        for (GameObject object : this.gameObject) {
            if (object instanceof Coin) {
                if (object.getRectangle().overlaps(this.player.getRectForCoins())) {
                    this.coinCounter++;
                    this.gameObject.removeValue(object, true);
                }
            }
        }
    }

    private void checkScore() {
        if (this.player.getIsJumping() && this.arePlatformsMoving) {
            this.scoreCounter += this.player.getRectangle().y - this.player.getLastY();
        }
    }

    //na zaklade casu od posledneho spawnu rozhodne, ci spawnut dalsiu platformu
    private void checkSpawnTime() {
        if (this.player.getCanJump()) {
            if (TimeUtils.nanoTime() - this.lastSpawnedPlatform.getLastSpawnTime() > 1000000000 && this.gameObject.size < 30) {
                this.spawnPlatform(Platform.class);
                this.spawnCoins();
            }
            if (TimeUtils.nanoTime() - this.lastSpawnedCloud.getLastSpawnTime() > 1000000000 && this.gameObject.size < 30) {
                this.spawnPlatform(Cloud.class);
            }
            if (TimeUtils.nanoTime() - this.lastSpawnedSpring.getLastSpawnTime() > 1000000000 && this.gameObject.size < 30) {
                this.spawnPlatform(Spring.class);
            }
        }
    }

    //udrzuje playera v hraniciach okna
    private void checkPlayerBounds() {
        if (this.player.getRectangle().y > 1000 - 128) {
            this.player.getRectangle().y = 1000 - 128;
            this.player.getRectForCoins().y = 1000 - 128;
        }
        if (this.player.getRectangle().x < 0) {
            this.player.getRectangle().x = 500;
            this.player.getRectForCoins().x = 500;
        }
        if (this.player.getRectangle().x > 500) {
            this.player.getRectangle().x = 0;
            this.player.getRectForCoins().x = 0;
        }
        if (this.player.getRectangle().y < 0) {
            this.player.getRectangle().y = 0;
            this.player.getRectForCoins().y = 0;
        }
    }

    //kontroluje koliziu hraca a platform
    private void checkCollision() {
        for (GameObject platform : this.gameObject) {
            if (!(platform instanceof Coin)) {

                //kontroluje koliziu iba ked hrac pada
                if (this.player.getIsFalling() && this.player.objectCollision(platform)) {
                    
                    this.player.getRectangle().y = platform.getRectangle().y + platform.getRectangle().height;
                    this.player.getRectForCoins().y = platform.getRectangle().y + platform.getRectangle().height;
                    
                    this.setPlatformAsHit(platform);
                    this.player.setCanJump(true);
                }
                this.checkCloudTime(platform);
                this.checkSpringHit(platform);
            }
        }
    }

    private void setPlatformAsHit(GameObject platform) {
        
        if (platform instanceof Cloud) {
            ((Cloud)platform).setIsHit(true);
            ((Cloud)platform).setHitTime(((Cloud)platform).getHitTime() + Gdx.graphics.getDeltaTime());
        }
        if (platform instanceof Spring) {
            ((Spring)platform).setIsHit(true);
        }
        if (platform instanceof Platform) {
            ((Platform)platform).setIsHit(true);
        }
    }

    private void checkCloudTime(GameObject platform) {
        if (platform instanceof Cloud) {
            if (((Cloud)platform).getIsHit() && ((Cloud)platform).getHitTime() > 0.04) {
                this.gameObject.removeValue(platform, true);
            }
        }
    }

    private void checkSpringHit(GameObject platform) {
        if (platform instanceof Spring) {
            if (((Spring)platform).getIsHit()) {
                this.player.setMaxJumpTime(((Spring)platform).getSpringJumpTime());
                ((Spring)platform).setJumpTimeResetTimer(((Spring)platform).getJumpTimeResetTimer() + 1);
                if (((Spring)platform).getJumpTimeResetTimer() > 10) {
                    this.player.setMaxJumpTime(0.08);
                    ((Spring)platform).setIsHit(false);
                    ((Spring)platform).setJumpTimeResetTimer(0);
                }
            }
        }
    }

    private void gameOver(SpriteBatch batch) throws FileNotFoundException {
        this.player.setCanMove(false);
        this.font.getData().setScale(4);
        this.font.draw(batch, "GAME OVER", 70, 550);
        this.font.getData().setScale(2);
        
        Scanner scan = new Scanner(new FileReader("highscore.txt"));
        long highscore = scan.nextLong();
        
        if (this.scoreCounter > highscore) {
            highscore = this.scoreCounter;
        }
        
        File file = new File("highscore.txt");
        PrintWriter writer = new PrintWriter(file);
        writer.println(highscore);
        writer.close();
        
        if (this.scoreCounter == highscore) {
            this.font.draw(batch, "New highscore!", 150, 400);
            this.font.draw(batch, "" + this.scoreCounter, 220, 360);
        } else {
            this.font.draw(batch, "Highscore: " + highscore, 160, 400);
            this.font.draw(batch, "Your score: " + this.scoreCounter, 160, 350);
        }
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.shapeRenderer.dispose();
        this.font.dispose();
    }
}

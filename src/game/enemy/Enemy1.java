package game.enemy;

import game.*;
import game.bom.*;
import game.box.BoxStone;
import game.box.BoxWood;
import game.player.Player;
import game.player.PlayerDie;
import game.renderer.SingleImageRenderer;
import physics.BoxCollider;
import tklibs.AudioUtils;
import tklibs.SpriteUtils;

import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy1 extends GameObjectPhysics {
    public ArrayList<Integer> arrayListStatus=new ArrayList<>();
    int direction;
    FrameCounter moveCounter;
    FrameCounter randomCouter;
    Clip die;
    public Enemy1(){
        super();
        this.createRenderer();
        this.direction= Settings.LEFT;
        this.boxCollider=new BoxCollider(this.position,this.anchor,40,40);
        this.moveCounter=new FrameCounter(30);
        this.randomCouter=new FrameCounter(130);
    }

    private void createRenderer() {
        BufferedImage image;
        image= SpriteUtils.loadImage("assets/images/background/monster_left.png");
        this.renderer=new SingleImageRenderer(image);
    }

    @Override
    public void run() {
        super.run();
        this.move();
        this.random();
        this.enemyDie();
    }

    private void enemyDie() {

        ArrayList<BombBangRight> bombBangRights=GameObject.findIntersecteds(BombBangRight.class,this.boxCollider);
        ArrayList<BombBangLeft> bombBangLefts=GameObject.findIntersecteds(BombBangLeft.class,this.boxCollider);
        ArrayList<BombBangUp> bombBangUps=GameObject.findIntersecteds(BombBangUp.class,this.boxCollider);
        ArrayList<BombBangDown> bombBangDowns=GameObject.findIntersecteds(BombBangDown.class,this.boxCollider);

        if(bombBangRights.size()>=1||bombBangLefts.size()>=1||bombBangUps.size()>=1||bombBangDowns.size()>=1){
            this.die= AudioUtils.loadSound("assets/music/monster_die.wav");
            AudioUtils.replay(this.die);
            Player.enemyCount--;
            if(Player.enemyCount==0)System.out.println("wwin");
            this.destroy();
        }
    }

    private void random() {
        if(this.randomCouter.run()){
            this.direction=(int) Math.random()*4+1;
            this.randomCouter.reset();
        }
    }

    private void move() {
        if(this.moveCounter.run()) {
            float vx = 0, vy = 0;
            switch (this.direction) {
                case Settings.TOP: {
                    vy = -Settings.WAY_SIZE;
                    BufferedImage images ;
                    images= SpriteUtils.loadImage("assets/images/background/monster_up.png");
                    this.renderer = new SingleImageRenderer(images);
                    break;
                }
                case Settings.RIGHT: {
                    vx = Settings.WAY_SIZE;
                    BufferedImage images;
                    images = SpriteUtils.loadImage("assets/images/background/monster_right.png");
                    this.renderer = new SingleImageRenderer(images);
                    break;
                }
                case Settings.BOT: {
                    vy = Settings.WAY_SIZE;
                    BufferedImage images ;
                    images= SpriteUtils.loadImage("assets/images/background/monster_down.png");
                    this.renderer = new SingleImageRenderer(images);
                    break;
                }
                case Settings.LEFT: {
                    vx = -Settings.WAY_SIZE;
                    BufferedImage images ;
                    images= SpriteUtils.loadImage("assets/images/background/monster_left.png");
                    this.renderer = new SingleImageRenderer(images);
                    break;
                }
            }
            if(this.direction != Settings.STAY) {
                if(this.checkBox()==true) {
                    this.position.addThis(vx, vy);
                    this.moveCounter.reset();
                }
                else {
                        this.direction = (int) (Math.random() * 4) + 1;

                }
            }
        }
    }

    private boolean checkBox() {
        Vector2D aheadPosition = null;

        if (this.direction == Settings.TOP) {
            aheadPosition = this.position.add(0, -Settings.WAY_SIZE);
        } else if (this.direction == Settings.RIGHT) {
            aheadPosition = this.position.add(Settings.WAY_SIZE, 0);
        } else if (this.direction == Settings.LEFT) {
            aheadPosition = this.position.add(-Settings.WAY_SIZE, 0);
        } else if (this.direction == Settings.BOT) {
            aheadPosition = this.position.add(0, Settings.WAY_SIZE);
        }
        BoxWood boxWood= GameObject.findByPosition(BoxWood.class, aheadPosition);
        BoxStone boxStone=GameObject.findByPosition(BoxStone.class,aheadPosition);
        Bom bom=GameObject.findByPosition(Bom.class,aheadPosition);
        Enemy1 enemy1=new Enemy1();
        enemy1.position.set(this.position);
        BombBangDown bombBangDown=GameObject.findIntersected(BombBangDown.class,enemy1.boxCollider);
        BombBangUp bombBangUp=GameObject.findIntersected(BombBangUp.class,enemy1.boxCollider);
        BombBangLeft bombBangLeft=GameObject.findIntersected(BombBangLeft.class,enemy1.boxCollider);
        BombBangRight bombBangRight=GameObject.findIntersected(BombBangRight.class,enemy1.boxCollider);
        if(boxWood!=null
                ||boxStone!=null
                ||bom!=null
                ||bombBangDown!=null
                ||bombBangUp!=null
                ||bombBangLeft!=null
                ||bombBangRight!=null
                ) return false;
        return true;
    }
}

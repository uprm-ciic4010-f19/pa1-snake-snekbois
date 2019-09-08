package Game.Entities.Static;

import java.awt.Font;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;

    public int xCoord;
    public int yCoord;

    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
    }
    
    private void isGood(int i) {
    	//check if apple is rotten r fresh
    	//if the snake took more than 60 steps, apple rots, deducts points, removes one tail piece
    	int score = handler.getWorld().player.score;
    	if (i<60) {
    		score = (int) (score + Math.sqrt((2*score)+1));
		}
    }


}

package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public int lenght;
    public boolean justAte;
    private Handler handler;
    //Registers the speed of snake based on ticks per second
    //The higher the number, the slower the snake
    //The lower or closer number to 0 makes the snake go faster
    private int moverate = 10;
    public int xCoord;
    public int yCoord;

    public int moveCounter;
    public int score;
    public int appleTimer; //times steps left before apple rots

    public String direction;//is your first name one?
    public void directionRestriction(){
    	
    }

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        lenght= 1;
        appleTimer = 59;

    }

    public void tick(){
        moveCounter++;
        if(moveCounter>=moverate) {
            checkCollisionAndMove();
            moveCounter=0;
            if (appleTimer<=59 && appleTimer != 0) appleTimer--; //subtracts steps until apple goes bad
                        
        }
        // direction != restricts the snake from going on the opposite direction of the pressed key
        if((handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)||handler.getKeyManager().up) && direction!="Down"){
            direction="Up";
        }if((handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)||handler.getKeyManager().down) && direction!="Up"){
            direction="Down";
        }if((handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)||handler.getKeyManager().left) && direction!="Right") {
            direction="Left";
        }if((handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)||handler.getKeyManager().right) && direction!="Left"){
            direction="Right";
        }
        if(handler.getKeyManager().addTail) {
			Eat();
			//Triggers manually the tail and doens't spawn new apple
			handler.getWorld().appleOnBoard=true;
			//setJustAte(true);
        }
        //Button makes snake go slower 
        if(handler.getKeyManager().slow) {
        	moverate++;
        }
        //Button makes snake go faster
        if(handler.getKeyManager().fast) {
        	moverate--;
        }
        if (handler.getKeyManager().pbutt) {
        	State.setState(handler.getGame().pauseState); //sets pause state with escape key
		}
        if(!handler.getWorld().body.isEmpty())gameOver();

    }
    //Iterates through the LinkedList verifying the players x and y don't intersects with any parts of the tail
    public void gameOver() {
    	int currentPosition=0;
    	while(lenght-1 > currentPosition) {
    		if(this.xCoord==handler.getWorld().body.get(currentPosition).x && this.yCoord==handler.getWorld().body.get(currentPosition).y) {
    			State.setState(handler.getGame().gameOverState);
    		}
    		
    		currentPosition++;
    		
    	}
    }
        

    public void checkCollisionAndMove(){
        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        switch (direction){
            case "Left":
                if(xCoord==0){
                    kill();
                    //Snake moves to the last position on the right (59) making it go through the other side of the screen
                	xCoord=59;
                }else{
                    xCoord--;
                }
                break;
            case "Right":
                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    kill();
                  //Snake moves to the last position on the left (0) making it go through the other side of the screen
                    xCoord=0;
                }else{
                    xCoord++;
                }
                break;
            case "Up":
                if(yCoord==0){
                    kill();
                  //Snake moves to the last position on the downward (59) making it go through the other side of the screen
                    yCoord=59;
                }else{
                    yCoord--;
                }
                break;
            case "Down":
                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    kill();
                  //Snake moves to the last position on the upward (0) making it go through the other side of the screen
                    yCoord=0;
                }else{
                    yCoord++;
                }
                break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){
            Eat();
        }

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler));
        }

    }

    public void render(Graphics g,Boolean[][] playeLocation){
        Random r = new Random();
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				Color green = new Color(82,208,83); //custom color (eva 01 green)
				Color brown = new Color(210,105,30);
            	g.setColor(green); //set snake color to the custom color
                if(playeLocation[i][j]){ //separated methods to draw snake from apple
                    g.fillRect((i*handler.getWorld().GridPixelsize), 
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);                    		
                }
                if (handler.getWorld().appleLocation[i][j]) {
					if (appleTimer != 0) g.setColor(Color.red);
					else g.setColor(brown);
					g.fillRect((i*handler.getWorld().GridPixelsize), 
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
					
				}

            }
        }
        g.setColor(Color.WHITE);
        g.drawString("Score: "+Integer.toString(score), handler.getWidth()/2, 10);
        g.drawString(Integer.toString(appleTimer), 20, 10);
    }

    public void Eat(){
        //lenght++;
        if (appleTimer != 0) {
        	moverate += 1; //my student id ends in 0, 0+1=1, it's painfully slow this way, would be slower with my partner's id
        	score = (int) (score + Math.sqrt((2*score)+1)); // added scoring 
        	lenght++;
        	handler.getWorld().appleLocation[xCoord][yCoord]=false;
            handler.getWorld().appleOnBoard=false;
            Tail tail = null;
            
            switch (direction){
                case "Left":
                    if(handler.getWorld().body.isEmpty()){
                        if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                            tail = new Tail(this.xCoord+1,this.yCoord,handler);
                        }else{
                            if(this.yCoord!=0){
                                tail = new Tail(this.xCoord,this.yCoord-1,handler);
                            }else{
                                tail =new Tail(this.xCoord,this.yCoord+1,handler);
                            }
                        }
                    }else{
                        if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
                            tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                        }else{
                            if(handler.getWorld().body.getLast().y!=0){
                                tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                            }else{
                                tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                            }
                        }

                    }
                    break;
                case "Right":
                    if( handler.getWorld().body.isEmpty()){
                        if(this.xCoord!=0){
                            tail=new Tail(this.xCoord-1,this.yCoord,handler);
                        }else{
                            if(this.yCoord!=0){
                                tail=new Tail(this.xCoord,this.yCoord-1,handler);
                            }else{
                                tail=new Tail(this.xCoord,this.yCoord+1,handler);
                            }
                        }
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            if(handler.getWorld().body.getLast().y!=0){
                                tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                            }else{
                                tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                            }
                        }

                    }
                    break;
                case "Up":
                    if( handler.getWorld().body.isEmpty()){
                        if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                            tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                        }else{
                            if(this.xCoord!=0){
                                tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                            }else{
                                tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                            }
                        }
                    }else{
                        if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }else{
                            if(handler.getWorld().body.getLast().x!=0){
                                tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                            }else{
                                tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                            }
                        }

                    }
                    break;
                case "Down":
                    if( handler.getWorld().body.isEmpty()){
                        if(this.yCoord!=0){
                            tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                        }else{
                            if(this.xCoord!=0){
                                tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                            }else{
                                tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                            } System.out.println("Tu biscochito");
                        }
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }else{
                            if(handler.getWorld().body.getLast().x!=0){
                                tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                            }else{
                                tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                            }
                        }

                    }
                    break;
            }
            handler.getWorld().body.addLast(tail);
            handler.getWorld().playerLocation[tail.x][tail.y] = true;
        }
        else if (appleTimer == 0){
        	score = (int) (score - Math.sqrt((2*score)+1)); // bad apple score
        	if (score < 0) score = 0;
        	if (lenght != 1) {
        		lenght--;
        		handler.getWorld().playerLocation[handler.getWorld().body.getLast().x]
        				[handler.getWorld().body.getLast().y]=true;
        		kill();
            	handler.getWorld().body.removeLast(); // removes tail piece
            	
        	}
        	else {
        		lenght = 1;
        	}
        	
        }
        
        appleTimer = 59; //resets apple timer for next apple
        
    }

    public void kill(){
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                handler.getWorld().playerLocation[i][j]=false;
            	//this.xCoord = xCoord + 60;

            }
        }
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
    }
}

package Game.GameStates;

import java.awt.Color;
import java.awt.Graphics;

import Main.Handler;
import Resources.Images;
import UI.ClickListlener;
import UI.UIImageButton;
import UI.UIManager;

public class GameOverState extends State {
	private Handler handler;
	private UIManager uiManager;

	public GameOverState(Handler handler){
        super(handler);
        this.handler = handler;
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);


        uiManager.addObjects(new UIImageButton(handler.getWidth()/2-64, handler.getHeight()/2-256, 128, 64, Images.butstart, new ClickListlener() {
            @Override
            public void onClick() {
                handler.getMouseManager().setUimanager(null);
                handler.getGame().reStart();
                State.setState(handler.getGame().gameState);
            }
        }));

    }

    public  void tick() {
    	handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
    	
    	
    }

    public void render(Graphics g) {
    	g.setColor(Color.darkGray);
        g.fillRect(0,0,handler.getWidth(),handler.getHeight());
        // Create Variable with new image file and substitute title with the name of the variable
        g.drawImage(Images.gameOver,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);
    	
    	
    }

	

}

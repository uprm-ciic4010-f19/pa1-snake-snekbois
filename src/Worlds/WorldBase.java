package Worlds;

import Game.Entities.Dynamic.Player;
import Game.Entities.Dynamic.Tail;
import Game.Entities.Static.Apple;
import Main.Handler;

import java.awt.*;
import java.util.LinkedList;


/**
 * Created by AlexVR on 7/2/2018.
 */
public abstract class WorldBase {

    //How many pixels are from left to right
    //How many pixels are from top to bottom
    //Must be equal
    public int GridWidthHeightPixelCount;

    //automatically calculated, depends on previous input.
    //The size of each box, the size of each box will be GridPixelsize x GridPixelsize.
    public int GridPixelsize;

    public Player player;

    protected Handler handler;


    public Boolean appleOnBoard;
    protected Apple apple;
    public Boolean[][] appleLocation;


    public Boolean[][] playerLocation;

    public LinkedList<Tail> body = new LinkedList<>();


    public WorldBase(Handler handler){
        this.handler = handler;

        appleOnBoard = false;

    
    }
    
    public boolean check(int t) {
    	return apple.isGood(t);
    }
    public void tick(){



    }

    public void render(Graphics g){

        for (int i = 0; i <= 800; i = i + GridPixelsize) {
        	Color purple = new Color(118,88,152); //custom color (eva 01 purple)
            g.setColor(purple); //set grid color to custom color (same as background color in DisplayScreen)
            g.drawLine(0, i, handler.getWidth() , i);
            g.drawLine(i,0,i,handler.getHeight());

        }
        


    }

}

package com.brackeen.javagamebook.graphics;

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class AnimationTest2 {
	public static void main(String[] args){
		AnimationTest2 test = new AnimationTest2();
		test.run();
	}
	
	private ScreenManager screen;
	private static final long DEMO_TIME = 10000;
	private Image bgImage;
	private Animation anim;
	private static final DisplayMode POSSIBLE_MODES[] = {
			new DisplayMode(800, 600, 32, 0),
			new DisplayMode(800, 600, 24, 0),
			new DisplayMode(800, 600, 16, 0),
			new DisplayMode(640, 480, 32, 0),
			new DisplayMode(640, 480, 24, 0),
			new DisplayMode(640, 480, 16, 0),
	};
	
	public void loadImages(){
		bgImage = loadImage("image/background.jpg");
		Image player0 = loadImage("image/player0.png");
		Image player1 = loadImage("image/player1.png");
		Image player2 = loadImage("image/player2.png");
		
		//生成动画
		anim = new Animation();
		anim.addFrame(player0, 150);
		anim.addFrame(player1, 150);
		anim.addFrame(player2, 150);
		anim.addFrame(player0, 150);
		anim.addFrame(player1, 150);
		anim.addFrame(player2, 150);
	}
	
	public Image loadImage(String fileName){
		return new ImageIcon(fileName).getImage();
	}
	
	public void run(){
		screen = new ScreenManager();
		try{
			DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES);
			screen.setFullScreen(displayMode);
			loadImages();
			animationLoop();
		}
		finally{
			screen.restoreScreen();
		}
	}
	
	public void animationLoop(){
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		while(currTime - startTime < DEMO_TIME){
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			//更新动画
			anim.update(elapsedTime);
			//绘制与更新屏幕
			Graphics2D g = screen.getGraphics();
			draw(g);
			g.dispose();
			screen.update();
			
			try{
				Thread.sleep(20);
			}catch(InterruptedException ex){}
		}
	}
	
	public void draw(Graphics g){
		g.drawImage(bgImage, 0, 0, null);
		g.drawImage(anim.getImage(), 0, 0, null);
	}
}

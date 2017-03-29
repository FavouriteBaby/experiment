package pers.vinson.screen;
import java.awt.*;

import java.awt.DisplayMode;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class AnimationTest1 {
	public static void main(String[] args){
		DisplayMode displayMode;
		displayMode = new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		AnimationTest1 test = new AnimationTest1();
		test.run(displayMode);
	}
	
	private static final long DEMO_TIME = 5000;
	private SimpleScreenManager screen;
	private Image bgImage;
	private Animation anim;
	
	public void run(DisplayMode displayMode){
		screen = new SimpleScreenManager();
		try{
			screen.setFullScreen(displayMode, new JFrame());
			loadImages();
			animationLoop();
		}
		finally{
			screen.restoreScreen();
		}
	}
	
	public void loadImages(){
		//装入图像
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
	
	private Image loadImage(String fileName){
		return new ImageIcon(fileName).getImage();
	}
	
	public void animationLoop(){
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		while(currTime - startTime < DEMO_TIME){
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			//更新动画
			anim.update(elapsedTime);
			//绘制屏幕
			Graphics g = screen.getFullScreenWindow().getGraphics();
			draw(g);
			g.dispose();
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

package com.brackeen.javagamebook.test;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;

import com.brackeen.javagamebook.graphics.ScreenManager;

//测试使用的简单抽象类，子类要实现draw()方法
public abstract class GameCore {
	protected static final int FONT_SIZE = 24;
	private boolean isRunning;
	protected ScreenManager screen;
	private static final DisplayMode POSSIBLE_MODES[] = {
			new DisplayMode(800, 600, 32, 0),
			new DisplayMode(800, 600, 24, 0),
			new DisplayMode(800, 600, 16, 0),
			new DisplayMode(640, 480, 32, 0),
			new DisplayMode(640, 480, 24, 0),
			new DisplayMode(640, 480, 16, 0),
	};
	
	//告诉游戏循环该退出了
	public void stop(){
		isRunning = false;
	}
	
	//调用init()与gameLoop()方法
	public void run(){
		try{
			init();
			gameLoop();
		}
		finally{
			screen.restoreScreen();
		}
	}
	
	//设置全屏方式，将对象初始化
	public void init(){
		screen = new ScreenManager();
		DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES);
		screen.setFullScreen(displayMode);
		
		Window window = screen.getFullScreenWindow();
		window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
		window.setBackground(Color.blue);
		window.setForeground(Color.white);
		isRunning = true;
	}
	
	public Image loadImage(String fileName){
		return new ImageIcon(fileName).getImage();
	}
	
	//运行游戏循环，直到调用stop方法
	public void gameLoop(){
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		while(isRunning){
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			
			//更新
			update(elapsedTime);
			
			//绘制屏幕
			Graphics2D g = screen.getGraphics();
			draw(g);
			g.dispose();
			screen.update();
			
			try{
				Thread.sleep(20);
			}catch(InterruptedException ex){}
		}
	}
	
	public void update(long elapsedTime){}		//根据经过的时间量更新游戏/动画状态
	public abstract void draw(Graphics2D g);	//绘制到屏幕上，子类要覆盖这个方法
}

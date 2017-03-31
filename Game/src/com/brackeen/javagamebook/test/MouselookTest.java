package com.brackeen.javagamebook.test;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import com.brackeen.javagamebook.graphics.*;

/**
 * 简单鼠标观看特性测试程序。利用鼠标观看特性，用户可心按任意方向任意移动鼠标，鼠标遇到屏幕边沿时停止
 * 鼠标观看特性在鼠标移动时将鼠标居中，使其问题可以测量相对鼠标运动，使鼠标不会遇到屏幕边沿
 * @author peng
 *
 */
public class MouselookTest extends GameCore implements MouseMotionListener, KeyListener {
	public static void main(String[] args){
		new MouselookTest().run();
	}
	
	private Image bgImage;
	private Robot robot;
	private Point mouseLocation;
	private Point centerLocation;
	private Point imageLocation;
	private boolean relativeMouseMode;
	private boolean isRecentering;

	public void init(){
		super.init();
		mouseLocation = new Point();
		centerLocation = new Point();
		imageLocation = new Point();
		relativeMouseMode = true;
		isRecentering = false;
		try{
			robot = new Robot();
			recenterMouse();
			mouseLocation.x = centerLocation.x;
			mouseLocation.y = centerLocation.y;
		}catch(AWTException ex){
			System.out.println("couldn't create robot");
		}
		Window window = screen.getFullScreenWindow();
		window.addMouseMotionListener(this);
		window.addKeyListener(this);
		bgImage = loadImage("image/bg.jpg");
	}
	
	//用Robot类将鼠标放到屏幕中央。注意并不是所有平台都支持使用这个类
	private synchronized void recenterMouse(){
		Window window = screen.getFullScreenWindow();
		if (robot != null && window.isShowing()) {
			centerLocation.x = window.getWidth()/2;
			centerLocation.y = window.getHeight()/2;
			SwingUtilities.convertPointToScreen(centerLocation, window);
			isRecentering = true;
			robot.mouseMove(centerLocation.x, centerLocation.y);
		}
	}
	
	
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		int w = screen.getWidth();
		int h = screen.getHeight();
		
		//保证位置正确
		imageLocation.x %= w;
		imageLocation.y %= h;
		if(imageLocation.x < 0)
			imageLocation.x += w;
		if(imageLocation.y < 0)
			imageLocation.y += h;
		
		//在四个位置画图，盖住屏幕
		int x = imageLocation.x;
		int y = imageLocation.y;
		g.drawImage(bgImage, x, y, null);
		g.drawImage(bgImage, x-w, y, null);
		g.drawImage(bgImage, x, y-h, null);
		g.drawImage(bgImage, x-w, y-h, null);
		
		//画指令
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawString("Press space to change mouse modes.", 5, FONT_SIZE);
		g.drawString("press escape to exit.", 5, FONT_SIZE*2);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			stop();
		else if(e.getKeyCode() == KeyEvent.VK_SPACE)
			//改变相对鼠标方式
			relativeMouseMode = !relativeMouseMode;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseMoved(e);
	}

	@Override
	public synchronized void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//这个事件用于鼠标居中
		if(isRecentering && centerLocation.x == e.getX() && centerLocation.y == e.getY())
			isRecentering = false;
		else{
			int dx = e.getX() - mouseLocation.x;
			int dy = e.getY() - mouseLocation.y;
			imageLocation.x += dx;
			imageLocation.y += dy;
			//鼠标居中
			if(relativeMouseMode)
				recenterMouse();
		}
		mouseLocation.x = e.getX();
		mouseLocation.y = e.getY();
	}

}

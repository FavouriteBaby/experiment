package com.brackeen.javagamebook.test;
import com.brackeen.javagamebook.graphics.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

/**
 * 简单鼠标测试。在鼠标所在位置画一个“HelloWorld”消息。单击时，它变成“trail mode”，画出最近10个鼠标位置（如有）
 * @author peng
 *
 */
public class MouseTest extends GameCore implements KeyListener, MouseMotionListener, 
	MouseListener, MouseWheelListener{
	public static void main(String[] args){
		new MouseTest().run();
	}
	
	private LinkedList trailList;
	private static final int TRAIL_SIZE = 10;
	private boolean trailMode;
	private int colorIndex;
	private static final Color[] COLORS = {
			Color.white, Color.black, Color.yellow, Color.magenta
	};
	
	public void init(){
		super.init();
		trailList = new LinkedList();
		
		Window window = screen.getFullScreenWindow();
		window.addMouseListener(this);
		window.addMouseMotionListener(this);
		window.addMouseWheelListener(this);
		window.addKeyListener(this);
	}
	
	@Override
	public synchronized void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		int count = trailList.size();
		if(count > 1 && !trailMode)
			count = 1;
		
		Window window = screen.getFullScreenWindow();
		
		//画背景
		g.setColor(window.getBackground());
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		
		//画指令
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(window.getForeground());
		g.drawString("MouseTest. Press Escape to exit.", 5, FONT_SIZE);
		
		//画鼠标尾巴
		for(int i = 0; i < count; ++i){
			Point p = (Point)trailList.get(i);
			g.drawString("Hello World", p.x, p.y);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		colorIndex = (colorIndex + e.getWheelRotation()) % COLORS.length;
		if(colorIndex < 0)
			colorIndex += COLORS.length;
		Window window = screen.getFullScreenWindow();
		window.setForeground(COLORS[colorIndex]);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseMoved(arg0);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseMoved(arg0);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		trailMode = !trailMode;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseMoved(arg0);
	}

	@Override
	public synchronized void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Point p = new Point(arg0.getX(), arg0.getY());
		trailList.addFirst(p);
		while(trailList.size() > TRAIL_SIZE)
			trailList.removeLast();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
			stop();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

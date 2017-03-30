package com.brackeen.javagamebook.test;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.awt.event.KeyEvent;

import com.brackeen.javagamebook.graphics.*;

public class KeyTest extends GameCore implements KeyListener {
	public static void main(String[] args){
		new KeyTest().run();
	}
	
	private LinkedList messages = new LinkedList();
	
	public void init(){
		super.init();
		Window window = screen.getFullScreenWindow();
		window.addKeyListener(this);
		//可以输入TAB键和其他通常用于移动焦点的键
		window.setFocusTraversalKeysEnabled(false);
		//将这个对象注册为窗口的键监听器
		addMessage("KeyInputTest.Press Escape to exit");
	}
	
	//将消息加进消息清单
	public synchronized void addMessage(String message){
		messages.add(message);
		if(messages.size() >= screen.getHeight() / FONT_SIZE)
			messages.remove(0);
	}
	
	public void keyPressed(KeyEvent e){
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_ESCAPE)
			stop();
		else{
			addMessage("Pressed: " + KeyEvent.getKeyText(keyCode));
			//保证不对其他事项处理键
			e.consume();
		}
	}
	
	public void keyReleased(KeyEvent e){
		int keyCode = e.getKeyCode();
		addMessage("Released: " + KeyEvent.getKeyText(keyCode));
		e.consume();
	}
	
	public void keyTyped(KeyEvent e){
		e.consume();
	}
	
	
	
	
	public void draw(Graphics2D g){
		Window window = screen.getFullScreenWindow();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//绘制背景
		g.setColor(window.getBackground());
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		
		//绘制消息
		g.setColor(window.getForeground());
		int y = FONT_SIZE;
		for(int i = 0; i < messages.size(); ++i){
			g.drawString((String)messages.get(i), 5, y);
			y += FONT_SIZE;
		}
	}
	
}

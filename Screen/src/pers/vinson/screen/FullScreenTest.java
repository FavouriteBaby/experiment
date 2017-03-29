package pers.vinson.screen;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;

public class FullScreenTest extends JFrame {
	public static void main(String[] args){
		DisplayMode displayMode;
		if(args.length == 3){
			displayMode = new DisplayMode(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), DisplayMode.REFRESH_RATE_UNKNOWN);
		}
		else
			displayMode = new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		FullScreenTest test = new FullScreenTest();
		test.run(displayMode);
	}
	
	private static final long DEMO_TIME = 5000;
	
	public void run(DisplayMode displayMode){
		setBackground(Color.blue);
		setForeground(Color.white);
		setFont(new Font("Dialog", 0, 24));
		
		SimpleScreenManager screen = new SimpleScreenManager();
		try{
			Thread.sleep(DEMO_TIME);;
		}catch(InterruptedException ex){}
		finally{
			screen.restoreScreen();
		}
	}
	
	public void paint(Graphics g){
		g.drawString("Hello World", 20, 50);
	}
}

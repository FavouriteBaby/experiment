package pers.vinson.screen;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ImageTest extends JFrame {
	public static void main(String[] args){
		DisplayMode displayMode; 
		displayMode = new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		ImageTest test = new ImageTest();
		test.run(displayMode);
	}
	
	
	private static final int FONT_SIZE = 24;
	private static final long DEMO_TIME = 10000;
	private boolean imagesLoaded;
	private SimpleScreenManager screen;
	private Image bgImage;
	private Image opaqueImage;
	private Image transparentImage;
	private Image translucentImage;
	private Image antiAliasedImage;
	
	public void run(DisplayMode displayMode){
		setBackground(Color.blue);
		setForeground(Color.white);
		setFont(new Font("dialog", Font.PLAIN, FONT_SIZE));
		imagesLoaded = false;
		
		screen = new SimpleScreenManager();
		try{
			screen.setFullScreen(displayMode, this);
			loadImages();
			try{
				Thread.sleep(DEMO_TIME);
			}catch(InterruptedException ex){}
		}
		finally{
			screen.restoreScreen();
		}
	}
	
	public void loadImages(){
		bgImage = loadImage("image/background.jpg");
		opaqueImage = loadImage("image/opaque.png");
		transparentImage = loadImage("image/opaque.png");
		translucentImage = loadImage("image/opaque.png");
		antiAliasedImage = loadImage("image/opaque.png");
		imagesLoaded = true;
		System.out.println("repaint");
		repaint();
	}
	
	private Image loadImage(String fileName){
		return new ImageIcon(fileName).getImage();
	}
	
	public void paint(Graphics g){
		if(g instanceof Graphics2D){
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		if(imagesLoaded){
			g.drawImage(bgImage, 0, 0, null);
			drawImage(g, opaqueImage, 0, 0, "Opaque");
			drawImage(g, transparentImage, 320, 0, "TransparentImage");
			drawImage(g, translucentImage, 0, 300, "TranslucentImage");
			drawImage(g, antiAliasedImage, 320, 300, "AntiAliasedImage");
		}
	}
	
	public void drawImage(Graphics g, Image image, int x, int y, String caption){
		g.drawImage(image, x, y, null);
		g.drawString(caption, x+5, y+FONT_SIZE+image.getHeight(null));
	}
}

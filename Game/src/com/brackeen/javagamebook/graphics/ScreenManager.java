package com.brackeen.javagamebook.graphics;

import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

public class ScreenManager {
	private GraphicsDevice device;
	
	
	//生成新ScreenManager对象
	public ScreenManager(){
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
	}
	
	//对系统缺省设备返回一系列兼容显示方式
	public DisplayMode[] getCompatibleDisplayModes(){
		return device.getDisplayModes();
	}

	//返回方法清单中第一个兼容显示方式，如果没有兼容显示方式，则返回null
	public DisplayMode findFirstCompatibleMode(DisplayMode modes[]){
		DisplayMode goodModes[] = device.getDisplayModes();
		for(int i = 0; i < modes.length; ++i){
			for(int j = 0; j < goodModes.length; ++j){
				if(displayModesMatch(modes[i], goodModes[j]))
					return modes[i];
			}
		}
		return null;
	}
	
	//返回当前显示方式
	public DisplayMode getCurrentDisplayMode(){
		return device.getDisplayMode();
	}
	
	//确定两个显示方式是否匹配。两个显示方式匹配指具有相同分辨率、位深度和刷新率。
	//如果一个显示方式的位深度为DisplayMode.BIT_DEPTH_MULTI，则忽略位深度
	//如果一个显示方式的刷新率为DisplayMode.REFRESH_RATE_UNKNOWN，则忽略刷新率
	public boolean displayModesMatch(DisplayMode mode1, DisplayMode mode2){
		if(mode1.getWidth() != mode2.getWidth() || mode1.getHeight() != mode2.getHeight())
			return false;
		if(mode1.getBitDepth() != mode2.getBitDepth() && mode1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
				&& mode2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI)
			return false;
		if(mode1.getRefreshRate() != mode2.getRefreshRate() && mode1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
				&& mode2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN)
			return false;
		
		return true;
	}
	
	//进入全屏方式和改变显示方式。如果指定显示方式为null或与这个设备不兼容，或这个系统中无法改变显示方式，则使用当前显示方式
	//显示所用BufferStrategy有两个缓存区
	public void setFullScreen(DisplayMode displayMode){
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setIgnoreRepaint(true);
		frame.setResizable(false);
		device.setFullScreenWindow(frame);
		if(displayMode != null && device.isDisplayChangeSupported()){
			try{
				device.setDisplayMode(displayMode);
			}catch(IllegalArgumentException ex){}
			frame.setSize(displayMode.getWidth(), displayMode.getHeight());
		}
		try{
			EventQueue.invokeAndWait(new Runnable(){
				public void run(){
					frame.createBufferStrategy(2);	//根据所要的缓存数生成BufferStrategy
				}
			});
		}catch(InterruptedException ex){}
		catch(InvocationTargetException ex){}
	}
	
	/**
	 * 取得显示的图形描述表，ScreenManager使用双缓存，因此应用程序要调用update()方法，显示绘制的任何图形
	 * 应用程序要处置图形图像
	 */
	public Graphics2D getGraphics(){
		Window window = device.getFullScreenWindow();
		if(window != null){
			BufferStrategy strategy = window.getBufferStrategy();	//获得BufferStrategy的引用
			return (Graphics2D)strategy.getDrawGraphics();			//getDrawGraphics()可以取得绘图缓存区的图形描述表
		}
		return null;
	}
	
	//更新显示
	public void update(){
		Window window = device.getFullScreenWindow();
		if(window != null){
			BufferStrategy strategy = window.getBufferStrategy();
			if(!strategy.contentsLost())
				strategy.show();	//显示绘图缓存区
		}
		Toolkit.getDefaultToolkit().sync();	//保证与窗口系统同步
	}
	
	/**
	 * 返回全屏方式中当前使用的窗口。如果设备不在全屏方式中，则返回null
	 */
	public JFrame getFullScreenWindow(){
		return (JFrame)device.getFullScreenWindow();
	}
	
	/**
	 * 返回全屏方式中当前使用的窗口宽度，如果设备不在全屏方式中，则返回0
	 */
	public int getWidth(){
		Window window = device.getFullScreenWindow();
		if(window != null)
			return window.getWidth();
		return 0;
	}
	
	/**
	 * 返回全屏方式中当前使用的窗口高度，如果设备不在全屏方式中，则返回0
	 */
	public int getHeight(){
		Window window = device.getFullScreenWindow();
		if(window != null)
			return window.getHeight();
		return 0;
	}
	
	//恢复屏幕的显示方式
	public void restoreScreen(){
		Window window = device.getFullScreenWindow();
		if(window != null)
			window.dispose();
		device.setFullScreenWindow(null);
	}
	
	//生成与当前显示器兼容的图像
	public BufferedImage createCompatibleImage(int w, int h, int transparancy){
		Window window = device.getFullScreenWindow();
		if(window != null){
			GraphicsConfiguration gc = window.getGraphicsConfiguration();
			return gc.createCompatibleImage(w, h, transparancy);
		}
		return null;
	}
}

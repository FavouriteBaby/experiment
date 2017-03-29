/**
 * 将显示切换到全屏方式
 */
package pers.vinson.screen;

import java.awt.*;
import javax.swing.JFrame;

/**
 * 生成新的SimpleScreenManager类管理并初始化
 * @author peng
 *
 */
public class SimpleScreenManager {
	private GraphicsDevice device;	//GraphicsDevice对象用来改变显示方式和检查显示属性
	
	public SimpleScreenManager(){
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();		
	}
	
	/**
	 *进入全屏方式和改变显示方式 
	 * @param displayMode
	 * @param window
	 */
	public void setFullScreen(DisplayMode displayMode, JFrame window){
		window.setUndecorated(true);	//启用或禁用Frame的装饰。此处使窗口失去边框和标题栏的装饰
		window.setResizable(false);
		device.setFullScreenWindow(window);
		
		if(displayMode != null && device.isDisplayChangeSupported()){
			try{
				device.setDisplayMode(displayMode);
			}catch(IllegalArgumentException ex){}
		}
	}
	
	//将当前使用的窗口返回全屏方式
	public Window getFullScreenWindow(){
		return device.getFullScreenWindow();
	}
	
	//恢复全屏使用方式
	public void restoreScreen(){
		Window window = device.getFullScreenWindow();
		if(window != null)
			window.dispose();
		device.setFullScreenWindow(null);
	}
}

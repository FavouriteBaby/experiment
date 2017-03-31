package com.brackeen.javagamebook.graphics;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

//NullRepaintManager是不进行重画的RepaintManager，用于应用程序手巧进行所有绘制
public class NullRepaintManager extends RepaintManager {
	//安装NullRepaintManager
	public static void install(){
		RepaintManager repaintManager = new NullRepaintManager();
		repaintManager.setDoubleBufferingEnabled(false);
		RepaintManager.setCurrentManager(repaintManager);
	}
	
	public void addDirtyRegion(JComponent c, int x, int y, int w, int h){}	
	public void addInvalidComponent(JComponent c){}
	public void markCompletelyDirty(JComponent c){}
	public void paintDirtyRegoins(){}
}

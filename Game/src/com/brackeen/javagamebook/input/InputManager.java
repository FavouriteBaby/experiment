package com.brackeen.javagamebook.input;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener,
	MouseWheelListener {
	//隐藏光标
	public static final Cursor INVISIBLE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
			Toolkit.getDefaultToolkit().getImage(""), new Point(0, 0), "invisible");
	
	//鼠标代码
	public static final int MOUSE_MOVE_LEFT  = 0;
	public static final int MOUSE_MOVE_RIGHT = 1;
	public static final int MOUSE_MOVE_UP 	 = 2;
	public static final int MOUSE_MOVE_DOWN  = 3;
	public static final int MOUSE_WHEEL_UP 	 = 4;
	public static final int MOUSE_WHEEL_DOWN = 5;
	public static final int MOUSE_BUTTON_1 	 = 6;
	public static final int MOUSE_BUTTON_2 	 = 7;
	public static final int MOUSE_BUTTON_3 	 = 8;
	public static final int NUM_MOUSE_CODES  = 9;
	
	//编码在java.awt.KeyEvent中定义，大多数虚拟键码的值小于600
	private static final int NUM_KEY_CODES = 600;
	private GameAction[] keyActions = new GameAction[NUM_KEY_CODES];
	private GameAction[] mouseActions = new GameAction[NUM_KEY_CODES];
	
	private Point mouseLocation;	//鼠标位置
	private Point centerLocation;	//中心位置
	private Component comp;
	private Robot robot;			//移动鼠标的类
	private boolean isRecentering;	//是否在中间位置
	
	//生成新的InputManager，监听指定组件的输入
	public InputManager(Component comp){
		this.comp = comp;
		mouseLocation = new Point();
		centerLocation = new Point();
		//注册键与鼠标监听器
		comp.addKeyListener(this);
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
		comp.addMouseWheelListener(this);
		//允许tab键和其他焦点遍历键的输入
		comp.setFocusTraversalKeysEnabled(false);
	}
	
	//在这个InputManager的输入组件上设置光标
	public void setCursor(Cursor cursor){
		comp.setCursor(cursor);
	}
	
	//将相对鼠标方式设置为开/关，对相对鼠标方式，鼠标锁在屏幕中央，只测量鼠标移动变化，而在正常方式中，鼠标在屏幕上自由移动
	public void setRelativeMouseMode(boolean mode){
		if(mode == isRelativeMouseMode()){
			return;
		}
		if(mode){
			try{
				robot = new Robot();
				recenterMouse();
			}catch(AWTException ex){
				robot = null;
			}
		}
		else{
			robot = null;
		}
	}
		
	//返回相对鼠标方式是否打开
	public boolean isRelativeMouseMode(){
		return (robot != null);
	}
	
	//将GameAction贴图特定键。键码在java.awt.KeyEvent中定义。如果键已经贴图GameAction，则新GameAction将其覆盖
	public void mapToKey(GameAction gameAction, int keyCode){
		keyActions[keyCode] = gameAction;
	}
	
	//将GameAction贴图特定鼠标，鼠标码在InputManager中定义，如果鼠标已经贴图GameAction，则新GameAction将其覆盖
	public void mapToMouse(GameAction gameAction, int mouseCode){
		mouseActions[mouseCode] = gameAction;
	}
	
	//对这个GameAction清除所有贴图的键与鼠标操作
	public void clearMap(GameAction gameAction){
		for(int i = 0; i < keyActions.length; ++i)
			if(keyActions[i] == gameAction)
				keyActions[i] = null;
		
		for(int i = 0; i < mouseActions.length; ++i)
			if(mouseActions[i] == gameAction)
				mouseActions[i] = null;
		
		gameAction.reset();
	}
	
	//取得这个GameAction的键与鼠标操作贴图清单，清单中每个项目是个字符串
	public List getMaps(GameAction gameCode){
		ArrayList list = new ArrayList();
		for(int i = 0; i < keyActions.length; ++i)
			if(keyActions[i] == gameCode)
				list.add(getKeyName(i));
		for(int i = 0; i < mouseActions.length; ++i)
			if(mouseActions[i] == gameCode)
				list.add(getMouseName(i));
		return list;
	}
	
	//复位所有GameActions，使其像没有按下一样
	public void resetAllGameActions(){
		for(int i = 0; i < keyActions.length; ++i)
			if(keyActions[i] != null)
				keyActions[i].reset();
		for(int i = 0; i < mouseActions.length; ++i)
			if(mouseActions[i] != null)
				mouseActions[i].reset();
	}
	
	//取得键码名
	public static String getKeyName(int keyCode){
		return KeyEvent.getKeyText(keyCode);
	}
	
	//取得鼠标代码名
	public static String getMouseName(int mouseCode){
		switch(mouseCode){
		case MOUSE_MOVE_LEFT:return "Mouse Left";
		case MOUSE_MOVE_RIGHT:return "Mouse Right";
		case MOUSE_MOVE_UP:return "Mouse Up";
		case MOUSE_MOVE_DOWN:return "Mouse Down";
		case MOUSE_WHEEL_UP:return "Mouse Wheel Up";
		case MOUSE_WHEEL_DOWN:return "Mouse Wheel Down";
		case MOUSE_BUTTON_1:return "Mouse Button 1";
		case MOUSE_BUTTON_2:return "Mouse Button 2";
		case MOUSE_BUTTON_3:return "Mouse Button 3";
		default:return "Unknown mouse code " + mouseCode;
		}
	}
	
	//取得鼠标的位置
	public int getMouseX(){
		return mouseLocation.x;
	}
	public int getMouseY(){
		return mouseLocation.y;
	}
	
	//用Robot类将鼠标放到屏幕中央，注意并不是所有平台都支持这个类
	private synchronized void recenterMouse(){
		if(robot != null && comp.isShowing()){
			centerLocation.x = comp.getWidth()/2;
			centerLocation.y = comp.getHeight()/2;
			SwingUtilities.convertPointFromScreen(centerLocation, comp);
			isRecentering = true;
			robot.mouseMove(centerLocation.x, centerLocation.y);
		}
	}
	
	private GameAction getKeyAction(KeyEvent e){
		int keyCode = e.getKeyCode();
		if(keyCode < keyActions.length)
			return keyActions[keyCode];
		return null;
	}
	
	//取得这个MouseEvent所指定鼠标键的鼠标代码
	public static int getMouseButtonCode(MouseEvent e){
		switch(e.getButton()){
		case MouseEvent.BUTTON1:
			return MOUSE_BUTTON_1;
		case MouseEvent.BUTTON2:
			return MOUSE_BUTTON_2;
		case MouseEvent.BUTTON3:
			return MOUSE_BUTTON_3;
		default:
			return -1;
		}
	}
	
	private GameAction getMouseButtonAction(MouseEvent e){
		int mouseCode = getMouseButtonCode(e);
		if(mouseCode != -1)
			return mouseActions[mouseCode];
		return null;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		mouseHelper(MOUSE_WHEEL_UP, MOUSE_WHEEL_DOWN, e.getWheelRotation());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(centerLocation.x == e.getX() && centerLocation.y == e.getY())
			isRecentering = false;
		else{
			int dx = e.getX() - mouseLocation.x;
			int dy = e.getY() - mouseLocation.y;
			mouseHelper(MOUSE_MOVE_LEFT, MOUSE_MOVE_RIGHT, dx);
			mouseHelper(MOUSE_MOVE_UP, MOUSE_MOVE_DOWN, dy);
			if(isRelativeMouseMode()){
				recenterMouse();
			}
		}
		mouseLocation.x = e.getX();
		mouseLocation.y = e.getY();
	}
	
	private void mouseHelper(int codeNeg, int codePos, int amount){
		GameAction gameAction;
		if(amount < 0)
			gameAction = mouseActions[codeNeg];
		else
			gameAction = mouseActions[codePos];
		if(gameAction != null){
			gameAction.press(Math.abs(amount));
			gameAction.release();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseMoved(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		GameAction gameAction = getMouseButtonAction(e);
		if(gameAction != null)
			gameAction.press();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		GameAction gameAction = getMouseButtonAction(e);
		if(gameAction != null)
			gameAction.release();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		GameAction gameAction = getKeyAction(e);
		if(gameAction != null)
			gameAction.press();
		e.consume();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		GameAction gameAction = getKeyAction(e);
		if(gameAction != null)
			gameAction.release();
		e.consume();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		e.consume();
	}
	
}

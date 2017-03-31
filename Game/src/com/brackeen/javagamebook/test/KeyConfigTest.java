package com.brackeen.javagamebook.test;
import com.brackeen.javagamebook.graphics.*;
import com.brackeen.javagamebook.input.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.*;
import javax.swing.border.Border;

import java.util.List;
import java.util.ArrayList;

public class KeyConfigTest extends MenuTest {
	public static void main(String[] args){
		new KeyConfigTest().run();
	}
	
	private static final String INSTRUCTIONS = "<html>Click an action's input box to change it's key." +
	"<br>An action can have at most three keys associated with it." + 
			"<br>Press Backspace to clear an action's keys.";
	private JPanel dialog;
	private JButton okButton;
	private List inputs;
	
	public void init(){
		super.init();
		inputs = new ArrayList();
		//生成GameActions与贴图键清单
		JPanel configPanel = new JPanel(new GridLayout(5, 2, 2, 2));
		addActionConfig(configPanel, moveLeft);
		addActionConfig(configPanel, moveRight);
		addActionConfig(configPanel, jump);
		addActionConfig(configPanel, pause);
		addActionConfig(configPanel, exit);
		
		//生成包含OK按钮的面板
		JPanel bottomPanel = new JPanel(new FlowLayout());
		okButton = new JButton("OK");
		okButton.setFocusable(false);
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		
		//生成包含指令的面板
		JPanel topPanel = new JPanel(new FlowLayout());
		topPanel.add(new JLabel(INSTRUCTIONS));
		
		//生成指令对话框
		Border border = BorderFactory.createLineBorder(Color.black);
		
		//生成配置对话框
		dialog = new JPanel(new BorderLayout());
		dialog.add(topPanel, BorderLayout.NORTH);
		dialog.add(configPanel, BorderLayout.CENTER);
		dialog.add(bottomPanel, BorderLayout.SOUTH);
		dialog.setBorder(border);
		dialog.setVisible(false);
		dialog.setSize(dialog.getPreferredSize());
		
		//居中对话框
		dialog.setLocation((screen.getWidth() - dialog.getWidth())/2,
				(screen.getHeight() - dialog.getHeight())/2);
		
		//将对话框加进屏幕分层窗格的横态对话框层
		screen.getFullScreenWindow().getLayeredPane().add(dialog, JLayeredPane.MODAL_LAYER);
	}
	
	//增加追念GameAction名与InputComponent的标题，用于改变贴图键
	private void addActionConfig(JPanel configPanel, GameAction action){
		JLabel label = new JLabel(action.getName(), JLabel.RIGHT);
		InputComponent input = new InputComponent(action);
	}
	
	public void actionPerformed(ActionEvent e){
		super.actionPerformed(e);
		if(e.getSource() == okButton)
			configAction.tap();
	}
	
	public void checkSystemInput(){
		super.checkSystemInput();
		if(configAction.isPressed()){
			boolean show = !dialog.isVisible();
			dialog.setVisible(show);
			setPaused(show);
		}
	}
	
	//复位每个InputComponent中显示的文本
	private void resetInputs(){
		for(int i = 0; i < inputs.size(); ++i)
			((InputComponent)inputs.get(i)).setText();
	}
	
	//InputComponent类显示贴图特定操作的键，使用户可以改变贴图键。用户单击选择InputComponent，然后可以按任何键或鼠标键改变贴图值
	class InputComponent extends JTextField{
		private GameAction action;
		public InputComponent(GameAction action){
			this.action = action;
			setText();
			enableEvents(KeyEvent.KEY_EVENT_MASK | MouseEvent.MOUSE_EVENT_MASK | 
					MouseEvent.MOUSE_MOTION_EVENT_MASK | MouseEvent.MOUSE_WHEEL_EVENT_MASK);
		}
		
		//将这个InputComponent的显示文本设置为贴图键名
		private void setText(){
			String text = "";
			List list = inputManager.getMaps(action);
			if(list.size() > 0){
				for(int i = 0; i < list.size(); ++i)
					text += (String)list.get(i) + ",";
				text = text.substring(0, text.length()-2);
			}
			//保证不死锁
			synchronized(getTreeLock()){
				setText(text);
			}
		}
		
		//将这个InputComponent的GameAction贴图指定键或鼠标操作
		private void mapGameAction(int code, boolean isMouseMap){
			if(inputManager.getMaps(action).size() >= 3)
				inputManager.clearMap(action);
			if(isMouseMap)
				inputManager.mapToMouse(action, code);
			else
				inputManager.mapToKey(action, code);
			resetInputs();
			screen.getFullScreenWindow().requestFocus();
		}
		
		protected void processKeyEvent(KeyEvent e){
			if(e.getID() == e.KEY_PRESSED){
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && inputManager.getMaps(action).size() > 0){
					inputManager.clearMap(action);
					setText("");
					screen.getFullScreenWindow().requestFocus();
				}
				else{
					mapGameAction(e.getKeyCode(), false);
				}
			}
			e.consume();
		}
		
		protected void processMouseEvent(MouseEvent e){
			if(e.getID() == e.MOUSE_PRESSED){
				if(hasFocus()){
					int code = InputManager.getMouseButtonCode(e);
					mapGameAction(code, true);
				}
				else
					requestFocus();
			}
			e.consume();
		}
		
		protected void processMouseMotionEvent(MouseEvent e){
			e.consume();
		}
		
		protected void processMouseWheelEvent(MouseWheelEvent e){
			if(hasFocus()){
				int code = InputManager.MOUSE_WHEEL_DOWN;
				if(e.getWheelRotation() < 0)
					code = InputManager.MOUSE_WHEEL_UP;
				mapGameAction(code, true);
			}
			e.consume();
		}
	}
}

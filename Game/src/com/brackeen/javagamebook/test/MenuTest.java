package com.brackeen.javagamebook.test;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import com.brackeen.javagamebook.graphics.*;
import com.brackeen.javagamebook.input.GameAction;

public class MenuTest extends InputManagerTest implements ActionListener {
	public static void main(String[] args){
		new MenuTest().run();
	}
	
	protected GameAction configAction;
	private JButton playButton;
	private JButton configButton;
	private JButton quitButton;
	private JButton pauseButton;
	private JPanel playButtonSpace;
	
	public void init(){
		super.init();
		//让Swing组件不画自己
		NullRepaintManager.install();
		//生成配置的GameAction
		configAction = new GameAction("config");
		//生成按钮
		quitButton = createButton("quit", "Quit");
		playButton = createButton("play", "Continue");
		pauseButton = createButton("pause", "Pause");
		configButton = createButton("config", "Change Settings");
		//生成播放/暂停按钮的空间
		playButtonSpace = new JPanel();
		playButtonSpace.setOpaque(false);
		playButtonSpace.add(pauseButton);
		JFrame frame = super.screen.getFullScreenWindow();
		Container contentPane = frame.getContentPane();
		//保证内容窗格透明
		if(contentPane instanceof JComponent){
			((JComponent)contentPane).setOpaque(false);
		}
		
		//将组件加进屏幕的内容窗格
		contentPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPane.add(playButtonSpace);
		contentPane.add(configButton);
		contentPane.add(quitButton);
		//显式布置组件
		frame.validate();
	}
	
	public void draw(Graphics2D g){
		super.draw(g);
		JFrame frame = super.screen.getFullScreenWindow();
		frame.getLayeredPane().paintComponents(g);
	}
	
	//在暂停状态改变时改变播放/暂停按钮
	public void setPaused(boolean p){
		super.setPaused(p);
		playButtonSpace.removeAll();
		if(isPaused())
			playButtonSpace.add(playButton);
		else
			playButtonSpace.add(pauseButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
		if(src == quitButton)
			super.exit.tap();
		else if(src == configButton)
			configAction.tap();
		else if(src == playButton || src == pauseButton)
			super.pause.tap();
	}
	
	//生成Swing JButton。
	public JButton createButton(String name, String toolTip){
		//装入图像
		String imagePath = "image/" + name + ".png";
		ImageIcon iconRollover = new ImageIcon(imagePath);
		int w = iconRollover.getIconWidth();
		int h = iconRollover.getIconHeight();
		//取得按钮的光标
		Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
		//生成半透明缺省图像
		Image image = screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		
		Graphics2D g = (Graphics2D)image.getGraphics();
		Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g.setComposite(alpha);
		g.drawImage(iconRollover.getImage(), 0, 0, null);
		g.dispose();
		ImageIcon iconDefault = new ImageIcon(image);
		
		//生成压下图像
		image = screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		g.drawImage(iconRollover.getImage(), 2, 2, null);
		g.dispose();
		ImageIcon iconPressed = new ImageIcon(image);
		
		//生成按钮
		JButton button = new JButton();
		button.addActionListener(this);
		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setToolTipText(toolTip);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.setCursor(cursor);
		button.setIcon(iconDefault);
		button.setRolloverIcon(iconRollover);
		button.setPressedIcon(iconPressed);
		return button;
	}
}

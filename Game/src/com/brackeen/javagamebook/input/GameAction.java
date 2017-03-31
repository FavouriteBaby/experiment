package com.brackeen.javagamebook.input;

//GameAction类抽象用户启动的操作，如跳动、移动。GameAction可以用InputManager贴图键与鼠标
public class GameAction {
	public static final int NORMAL = 0;	//正常行为，按下键时，isPressed()方法返回真值
	//初击行为，isPressed()方法只在初击后返回真值，然后要放开和再次按下时才返回真值
	public static final int DETECT_INITAL_PRESS_ONLY = 1;
	private static final int STATE_RELEASED = 0;
	private static final int STATE_PRESSED = 1;
	private static final int STATE_WAITING_FOR_RELEASE = 2;
	
	private String name;
	private int behavior;
	private int amount;
	private int state;
	
	//生成新的GameAction，具有正常行为
	public GameAction(String name){
		this(name, NORMAL);
	}
	
	public GameAction(String name, int behavior){
		this.name = name;
		this.behavior = behavior;
		reset();
	}
	
	public String getName(){
		return this.name;
	}
	
	//按击GameAction，等于press()和release()
	public synchronized void tap(){
		press();
		release();
	}
	
	//键按下
	public synchronized void press(){
		press(1);
	}
	
	//键按下次数，或鼠标移动指定距离
	public synchronized void press(int amount){
		if(state != STATE_WAITING_FOR_RELEASE){
			this.amount += amount;
			state = STATE_PRESSED;
		}
	}
	
	//键放开
	public synchronized void release(){
		state = STATE_RELEASED;
	}
	
	//表示上次检查以来键是否按下
	public synchronized boolean isPressed(){
		return (getAmount() != 0);
	}
	
	//上次检查以来键按下的次数，或鼠标移动的距离
	public synchronized int getAmount(){
		int retVal = amount;
		if(retVal != 0){
			if(state == STATE_RELEASED)
				amount = 0;
			else if(behavior == DETECT_INITAL_PRESS_ONLY){
				state = STATE_WAITING_FOR_RELEASE;
				amount = 0;
			}
		}
		return retVal;
	}
	
	//复位GameAction，使其像没有按过一样
	public void reset(){
		state = STATE_RELEASED;
		amount = 0;
	}
}

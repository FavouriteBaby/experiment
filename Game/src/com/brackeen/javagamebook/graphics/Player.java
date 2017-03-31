package com.brackeen.javagamebook.graphics;

//扩展Sprite类，增加了重力特性和跳动功能
public class Player extends Sprite{
	public static final int STATE_NORMAL = 0;
	public static final int STATE_JUMPING = 1;
	public static final float SPEED = 0.3f;
	public static final float GRAVITY = 0.002f;
	private float floorY;		//地面位置
	private int state;
	
	public Player(Animation anim){
		super(anim);
		state = STATE_NORMAL;
	}
	
	//取得Player状态
	public int getState(){
		return state;
	}
	
	//设置Player状态
	public void setState(int state){
		this.state = state;
	}
	
	//设置地面位置，游戏者在此起跳和落地
	public void setFloorY(float floorY){
		this.floorY = floorY;
		setY(floorY);
	}
	
	//让Player跳动
	public void jump(){
		setVelocityY(-1);
		state = STATE_JUMPING;
	}
	
	//更新游戏者位置与动画，并在游戏者落地后将其状态设置为NORMAL
	public void update(long elapsedTime){
		if(getState() == STATE_JUMPING)
			setVelocityY(getVelocityY() + GRAVITY * elapsedTime);
		//移动游戏者
		super.update(elapsedTime);
		//检查游戏者是否着地
		if(getState() == STATE_JUMPING && getY() >= floorY){
			setVelocityY(0);
			setY(floorY);
			setState(STATE_NORMAL);
		}
	}
}

package com.brackeen.javagamebook.graphics;

import java.awt.Image;

public class Sprite {
	private Animation anim;
	
	//位置（像素）
	private float x;
	private float y;
	
	//速度（像素/毫秒）
	private float dx;
	private float dy;
	
	//用指定Animation生成新的Sprite对象
	public Sprite(Animation anim){
		this.anim = anim;
	}
	
	//根据速度更新Sprite的Animation及其位置
	public void update(long elapsedTime){
		x += dx * elapsedTime;
		y += dy * elapsedTime;
		anim.update(elapsedTime);
	}
	
	//取得Sprite的当前位置
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	
	//设置Sprite的当前位置
	public void setX(float x){
		this.x = x;
	}
	public void setY(float y){
		this.y = y;
	}
	
	//根据当前图像取得Sprite的宽度
	public float getWidth(){
		return anim.getImage().getWidth(null);
	}
	public float getHeight(){
		return anim.getImage().getHeight(null);
	}
	
	//取得Sprite的速度
	public float getVelocityX(){
		return dx;
	}
	public float getVelocityY(){
		return dy;
	}
	
	//设置Sprite的速度
	public void setVelocityX(float dx){
		this.dx = dx;
	}
	public void setVelocityY(float dy){
		this.dy = dy;
	}
	
	//取得Sprite的当前图像
	public Image getImage(){
		return anim.getImage();
	}
}

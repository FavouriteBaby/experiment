package pers.vinson.screen;

import java.awt.Image;
import java.util.ArrayList;

//Animation类管理一系列图像（帧）和每个帧显示的时间
public class Animation {
	private ArrayList frames;
	private int currFrameIndex;
	private long animTime;
	private long totalDuration;
	
	//生成新的空白动画
	public Animation(){
		frames = new ArrayList();
		totalDuration = 0;
		start();
	}
	
	//将指定显示时间的图像加进动画中
	//duration:图片的显示时间
	public synchronized void addFrame(Image image, long duration){
		totalDuration += duration;		//动画总时间延长
		frames.add(new AnimFrame(image, totalDuration));
	}
	
	//从头开始重新启动这个动画
	public synchronized void start(){
		animTime = 0;
		currFrameIndex = 0;
	}
	
	//必要时更新这个动画的当前像素（帧）
	public synchronized void update(long elapsedTime){
		if(frames.size() > 1){
			animTime += elapsedTime;
			if(animTime >= totalDuration){
				//保证动画完成时，动画时间重新开始，使动画循环
				animTime = animTime % totalDuration;
				currFrameIndex = 0;
			}
			while(animTime > getFrame(currFrameIndex).endTime){
				currFrameIndex++;
			}
		}
	}
	
	//取得这个Animation的当前图像，如果没有图像，返回null
	public synchronized Image getImage(){
		if(frames.size() == 0)
			return null;
		else
			return getFrame(currFrameIndex).image;
	}
	
	//取得第i帧，包括动画时间和图像
	private AnimFrame getFrame(int i){
		return (AnimFrame)frames.get(i);
	}
	
	private class AnimFrame{
		Image image;
		long endTime;
		public AnimFrame(Image image, long endTime){
			this.image = image;
			this.endTime = endTime;
		}
	}
}

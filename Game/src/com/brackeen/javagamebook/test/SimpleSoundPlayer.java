package com.brackeen.javagamebook.test;
import java.io.*;
import javax.sound.sampled.*;

public class SimpleSoundPlayer {
	public static void main(String[] args){
		SimpleSoundPlayer sound = new SimpleSoundPlayer("sound/star.wav");
		InputStream stream = new ByteArrayInputStream(sound.getSamples());
		sound.play(stream);
		System.exit(0);
	}
	
	private AudioFormat format;
	private byte[] samples;
	
	//从文件打开声音
	public SimpleSoundPlayer(String fileName){
		try{
			//打开声频输入流
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(fileName));
			format = stream.getFormat();
			//取得声频样本
			samples = getSamples(stream);
		}catch(UnsupportedAudioFileException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	//取得声音样本的字节数组
	public byte[] getSamples(){
		return samples;
	}
	
	//从AudioInputStream取得样本的字节数组
	public byte[] getSamples(AudioInputStream audioStream){
		//取得要读取的字节数
		int length = (int)(audioStream.getFrameLength() * format.getFrameSize());
		
		//读取整个流
		byte[] samples = new byte[length];
		DataInputStream is = new DataInputStream(audioStream);
		try{
			is.readFully(samples);
		}catch(IOException ex){
			ex.printStackTrace();
		}
		return samples;
	}
	
	//播放流。这个方法受阴，要播放完声音后才返回
	public void play(InputStream source){
		//用短的100ms缓冲区实时改变声音流
		int bufferSize = format.getFrameSize() * Math.round(format.getSampleRate() / 10);
		byte[] buffer = new byte[bufferSize];
		
		//生成要播放的Line
		SourceDataLine line;
		try{
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(format, bufferSize);
		}catch(LineUnavailableException ex){
			ex.printStackTrace();
			return;
		}
		
		line.start();
		try{
			int numBytesRead = 0;
			while(numBytesRead != -1){
				numBytesRead = source.read(buffer, 0, buffer.length);
				if(numBytesRead != -1)
					line.write(buffer, 0, numBytesRead);
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
		line.drain();
		line.close();
	}
}

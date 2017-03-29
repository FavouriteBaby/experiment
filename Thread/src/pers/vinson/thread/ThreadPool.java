package pers.vinson.thread;

import java.util.LinkedList;

public class ThreadPool extends ThreadGroup {
	private boolean isAlive;
	private LinkedList taskQueue;	//双向链表
	private int threadID;
	private static int threadPoolID;
	
	
	
	/**
	 * 创建新的线程池
	 * @param numThreads 池中的线程数
	 */
	public ThreadPool(int numThreads){
		super("ThreadPool-" + (threadPoolID++));
		setDaemon(true);	//改变守护进行的状态
		isAlive = true;
		taskQueue = new LinkedList();
		for(int i = 0; i < numThreads; ++i){
			new PooledThread().start();
		}
	}
	
	/**
	 * 请求新任务。这个方法立即返回，任务在池中下一闲机线程中执行<p>任务按收到的顺序开始执行
	 * @param task 要运行的任务，null表示不采取操作
	 * 如果这个线程池已经关闭，则抛出IllegalStateException
	 */
	public synchronized void runTask(Runnable task){
		if(!isAlive){
			throw new IllegalStateException();
		}
		if(task != null){
			taskQueue.add(task);
			notify();
		}
	}
	
	protected synchronized Runnable getTask() throws InterruptedException{
		while(taskQueue.size() == 0){
			if(!isAlive){
				return null;
			}
			wait();
		}
		return (Runnable)taskQueue.removeFirst();
	}
	
	/**
	 * 关闭这个线程池并立即返回。所有线程停止，不执行任何等待的任务。关闭一个线程池之后，这个线程池上不能再运行任何任务
	 */
	public synchronized void close(){
		if(isAlive){
			isAlive = false;
			taskQueue.clear();
			interrupt();
		}
	}
	
	/**
	 * 这个线程池等待所有运行线程完成，执行任何等待的任务
	 */
	public void join(){
		//告诉所有等待线程，这个线程池不再活动
		synchronized(this){
			isAlive = false;
			notifyAll();
		}
		
		//等待所有线程完成
		Thread[] threads = new Thread[activeCount()];
		int count = enumerate(threads);
		for(int i = 0; i < count; ++i){
			try{
				threads[i].join();
			}catch(InterruptedException ex){}
		}
	}
	
	/**
	 * PooledThread是线程池绥中的线程，用于运行任务(Runnables)
	 * @author peng
	 *
	 */
	private class PooledThread extends Thread{
		public PooledThread(){
			super(ThreadPool.this, "PooledThread-" + (threadID++));
		}
		
		public void run(){
			while(!isInterrupted()){
				//取得要运行的任务
				Runnable task = null;
				try{
					task = getTask();
				}catch(InterruptedException ex){}
				
				//如果getTask()返回null或中断，则关闭这个线程并返回
				if(task == null){
					return;
				}
				try{
					task.run();
				}catch(Throwable t){
					uncaughtException(this, t);
				}
			}
		}
	}
}

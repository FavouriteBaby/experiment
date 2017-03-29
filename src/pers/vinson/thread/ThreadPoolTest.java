package pers.vinson.thread;

public class ThreadPoolTest {
	public static void main(String[] args){
		if(args.length != 2){
			System.out.println("Tests the ThreadPool task");
			System.out.println("Usage: java ThreadPoolTest numTasks numThreads");
			System.out.println(" numTasks - integer: number of task to run.");
			System.out.println(" numThreads - integer: number of threads " + "in the thread pool.");
			return;
		}
		
		int numTasks = Integer.parseInt(args[0]);
		int numThreads = Integer.parseInt(args[1]);
		
		//生成线程池
		ThreadPool threadPool = new ThreadPool(numThreads);
		
		//运行例子任务
		for(int i = 0; i < numThreads; ++i){
			threadPool.runTask(createTask(i));
		}
		
		//关闭池并等待完成所有任务
		threadPool.join();
	}
	
	/**
	 * 生成简单可运行任务，打印一个ID，等待500毫秒，然后再打印这个ID
	 * @param taskID
	 * @return
	 */
	private static Runnable createTask(final int taskID){
		return new Runnable(){
			public void run(){
				System.out.println("Task " + taskID + ": start");
				//模拟长时间任务
				try{
					Thread.sleep(500);
				}catch(InterruptedException ex){}
				System.out.println("Task " + taskID + ": end");
			}
		};
	}
}

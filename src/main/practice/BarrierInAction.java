package src.main.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BarrierInAction {

	public static void main(String[] args) {
		class Friend implements Callable<String> {
			CyclicBarrier barrier;
			Friend(CyclicBarrier barrier) {
				this.barrier = barrier;
			}
			
			@Override
			public String call() throws Exception {
				try {
					Random rand = new Random();
					
					Thread.sleep(rand.nextInt(20) * 1000);
					System.out.println("I arrived at cafe...waiting for others");
					barrier.await();
					System.out.println("Lets go");
					return "ok";
				} catch(InterruptedException e) {
					System.out.println("Interrupted");
				}
				return "nok";
			}
			
		}
		CyclicBarrier barrier = new CyclicBarrier(4,() -> {System.out.println("khul jaa sim sim");});
		ExecutorService service = Executors.newFixedThreadPool(4);
		List<Future<String>> friends = new ArrayList<Future<String>>();
		for(int i=0; i < 4; i++) {
			friends.add(service.submit(new Friend(barrier)));
		}
		
		friends.forEach(friend -> {
			try {
				friend.get(20000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				System.out.println("Timed out");
				friend.cancel(true);
			}
		});
		
		service.shutdown();
	}

}

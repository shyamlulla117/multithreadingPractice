package src.main.practice;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class SquareForkJoinPool {
	public static void main(String[] args) {
		int[] inputArray = new int[100];
		for(int i=0;i<100;i++) {
			inputArray[i] = i;
		}
		long time = System.currentTimeMillis();
		SquareComputation task = new SquareComputation(inputArray,0,99);
		
		ForkJoinPool pool = new ForkJoinPool(2);
		pool.invoke(task);
		for(int i=0; i< 100; i++) {
			System.out.print(inputArray[i] + " ");
		}
		
		long time2 = System.currentTimeMillis();
		System.out.println();
		System.out.println(time2-time);
	}
	
	
}

class SquareComputation extends RecursiveAction {
	int threshold = 4;
	int[] squares;
	int start;
	int end;
	
	public SquareComputation(int[] inputArray, int start, int end) {
		squares = inputArray;
		this.start = start;
		this.end = end;
	}
	
	@Override
	protected void compute() {
		if(end - start <= threshold) {
			for(int i = start; i<= end; i++) {
				squares[i] = squares[i] * squares[i];
			}
		} else {
			invokeAll(new SquareComputation(squares, start, (end+start)/2),
					new SquareComputation(squares, 1 + (end+start)/2, end));
		}
		
	}
	
}

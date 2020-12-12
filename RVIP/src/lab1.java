import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class lab1 {
    public static int summ1=0;
    public static long summ2=0;
    public static void main(String args[]) {
        int[][] matrix = new int[100][100];
        final Random random = new Random();

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                matrix[i][j] = random.nextInt(1)+1;
            }
        }


        // Обыччное решение
        long m = System.currentTimeMillis();
        int multi = 1;
        int sum = 0;
        for (int[] i : matrix) {
            for (int j : i) {
                multi *= j;
            }
            sum += multi;
            multi = 1;
        }
        double t1=System.currentTimeMillis() - m;
        System.out.println("Ordinary Sum = "+ sum + " Time = " + t1);


        //Паралельное решение ThreadPoolExecutor
        long n = System.currentTimeMillis();
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), threadFactory, rejectionHandler);
        for (int[] i:matrix) {
            executorPool.execute(new MyThread(i));
        }
        double t2=System.currentTimeMillis() - n;
        System.out.println("\nThreadPoolExecutor Sum = "+ summ1 + " Time = " + t2);


        //Паралельное решение ForkJoinPoll
        long b = System.currentTimeMillis();
        new ForkJoinPool().invoke(new ValueSumCounter(matrix));
        double t3=System.currentTimeMillis() - b;
        System.out.println("\nForkJoinPoll Sum = "+ summ2 + " Time = " + t3);

    }

    public static class MyThread extends Thread {

        private int arg;

        public MyThread(int[] arg) {
            int multi = 1;
            for (int i = 0; i < 10; i++){
                multi *= arg[i];
            }
            summ1+=multi;
        }
    }

    public static class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(r.toString() + " is rejected");
        }

    }



    public static class ValueSumCounter extends RecursiveTask<Long>{
        private int[][] matrix;
        private int[] matrixA;

        public ValueSumCounter(int[][] matrix) {
            this.matrix = matrix;
        }

        public ValueSumCounter(int[] matrixA) {
            this.matrixA = matrixA;
        }

        @Override
        protected Long compute() {
            long sum = 1;
            List<ValueSumCounter> subTasks = new LinkedList<>();


            if (matrix != null) {
                for (int[] child : matrix) {
                    ValueSumCounter task = new ValueSumCounter(child);
                    task.fork(); // запустим асинхронно
                    subTasks.add(task);
                }
            }


            for(ValueSumCounter task : subTasks) {
                sum += task.join(); // дождёмся выполнения задачи и прибавим результат
            }

            if (matrixA != null) {
                for (int i : matrixA) {
                    sum *= i;
                }
            }

            summ2=sum-1;
            return sum;
        }

    }
}


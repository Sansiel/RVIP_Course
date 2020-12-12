import mpi.*;

public class lab2 {
    public static int answer = 0;
    public static int rankAns = 0;
    public  static  int[][] matrix = {
            {5,6,2,3,4},
            {1,2,3,4,5},
            {5,6,2,3,4},
            {1,2,3,4,5}
    };

    public static void main(String args[]) throws Exception {
        long m = System.currentTimeMillis();
        int rank, size, mul, sum;
        MPI.Init(args);
        size = MPI.COMM_WORLD.Size();
        rank = MPI.COMM_WORLD.Rank();
        sum = 0;
        mul = 1;
        for (int j : matrix[rank]) {
            mul *= j;
        }
        sum += mul;
        System.out.println("MPI sum =  " + sum + " rank - " + rank);
        MPI.Finalize();

//        if (rank==0) {ans(sum,1); }
//        if (rank==1) {ans(sum,1); }
//        if (rank==2) {ans(sum,1); }
//        if (rank==3) {ans(sum,1); }

        // Обыччное решение
        if (rank==2) {
            double t1=System.currentTimeMillis() - m;
            int multi = 1;
            int summ = 0;
            for (int[] i : matrix) {
                for (int j : i) {
                    multi *= j;
                }
                summ += multi;
                multi = 1;
            }
            double t2=System.currentTimeMillis() - t1;
            System.out.println("Final MPI sum = "+ answer + " Time = " + t1);
            System.out.println("Ordinary sum = " + summ + " Time = " + t2);
        }
    }

//    public static void ans(int sum, int rankLast){
//        answer+=sum;
//        rankAns+=rankLast;
//        if (rankAns==4) {
//            System.out.println("MPI final sum =  " + answer);
//        }
//    }

}

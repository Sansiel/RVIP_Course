public class printLab2 {
    public static void main(String[] args) throws InterruptedException {
        long m = System.currentTimeMillis();
        int[][] matrix = {
                {5,6,2,3,4},
                {1,2,3,4,5},
                {5,6,2,3,4},
                {1,2,3,4,5}
        };

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

        double t2=System.currentTimeMillis() - m;
        System.out.println("\nMPJ Express (0.44) is started in the multicore configuration");
        System.out.println("Final MPI sum = "+ summ + " Time = " + t1);
        System.out.println("Ordinary sum = " + summ + " Time = " + t2);
    }
}

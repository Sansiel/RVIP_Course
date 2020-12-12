import java.util.Random;
import mpi.*;

public class lab2_n {

    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);
        sortArrayMPI();
        MPI.Finalize();
    }

    private static void sortArrayMPI() {
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        long start = System.currentTimeMillis();

        int x = 5;
        int y = 5;

        int ans = 0;

        byte[][] arr = null;
        if (rank == size - 1) {
            arr = initArray(x, y);
            printArray(arr);
            for (int k = 0; k < size - 1; k++) {
                for (int i = 0; i < arr.length; i++) {
                    MPI.COMM_WORLD.Send(arr[i], 0, arr[i].length, MPI.BYTE, k, k * x + i);
                }
            }
        }

        if (rank != size - 1) {
            arr = new byte[x][];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = new byte[y];
                MPI.COMM_WORLD.Recv(arr[i], 0, arr[i].length, MPI.BYTE, size - 1, rank * x + i);
            }
            for (int i = 0; i < arr.length; i++) {
                if (i % (size - 1) == rank) {
                    int[] sum = new int[] { 0 };
                    for (int j = 0; j < arr[i].length; j++) {
                        sum[0] += arr[i][j];
                    }
                    MPI.COMM_WORLD.Send(sum, 0, 1, MPI.INT, size - 1, i);
                }
            }
        }

        if (rank == size - 1) {
            int[] sumArr = new int[x];
            int[] sum = new int[] { 0 };
            for (int i = 0; i < sumArr.length; i++) {
                MPI.COMM_WORLD.Recv(sum, 0, 1, MPI.INT, i % (size - 1), i);
                ans += sum[0];
            }
            System.out.println(ans);
            long stop = System.currentTimeMillis();
            System.out.println("MPI:                " + (stop - start));
        }

    }

    private static void printArray(byte[][] arr) {
        System.out.println("arr:");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static byte[][] initArray(int rows, int columns) {
        byte[][] arr = new byte[rows][];
        Random rnd = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new byte[columns];
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = (byte) rnd.nextInt(10);
            }
        }
        return arr;
    }

}









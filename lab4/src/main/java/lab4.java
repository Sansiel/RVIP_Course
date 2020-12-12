import org.apache.ignite.*;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class lab4 {

    public static int[][] generateArray(int x, int y) {
        int[][] arr = new int[x][y];
        Random rnd = new Random();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = rnd.nextInt(10);
            }
        }
        return arr;
    }


    public static void main(String[] args) throws IgniteException {
        // Preparing IgniteConfiguration using Java APIs
        IgniteConfiguration cfg = new IgniteConfiguration();

        // The node will be started as a client node.
        cfg.setClientMode(true);

        // Classes of custom Java logic will be transferred over the wire from this app.
        cfg.setPeerClassLoadingEnabled(true);

        // Setting up an IP Finder to ensure the client can locate the servers.
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

        // Starting the node
        Ignite ignite = Ignition.start(cfg);



        int x = 5;
        int y = 1000;
        int[][] arr = generateArray(x, y);

        int ans=0;

        Collection<IgniteCallable<Integer>> calls = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            calls.add(findSum(arr[i]));
        }

        long start = System.currentTimeMillis();
        Collection<Integer> res = ignite.compute().call(calls);
        long stop = System.currentTimeMillis();

        for (int i = 0; i < x; i++) {
            ans = (int) res.toArray()[i];
        }




        System.out.println("Matrix:\n");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
        System.out.println("Result: " +ans + " Time: " + (stop - start));

        ignite.close();
    }

    static IgniteCallable<Integer> findSum(int[] array) {
        return (IgniteCallable<Integer>) () -> {
            int sum = 0;
            for (int i = 0; i < array.length; i++) {
                sum+=array[i];
            }
            return sum;
        };
    }
}
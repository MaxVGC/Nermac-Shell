/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Random;

/**
 *
 * @author carlo
 */
public class Sistem {

    int mb = 1024 * 1024;
    int gb = 1024 * 1024 * 1024;

    public static void log(Object message) {
//        System.out.println(message);
    }

    static boolean isPrime(int n) {
        if (n <= 2) {
            return n == 2;
        }
        if (n % 2 == 0) {
            return false;
        }
        for (int i = 3, end = (int) Math.sqrt(n); i <= end; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int calcCPU(long cpuStartTime, long elapsedStartTime, int cpuCount) {
        long end = System.nanoTime();
        long totalAvailCPUTime = cpuCount * (end - elapsedStartTime);
        long totalUsedCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - cpuStartTime;
        //log("Total CPU Time:" + totalUsedCPUTime + " ns.");
        //log("Total Avail CPU Time:" + totalAvailCPUTime + " ns.");
        float per = ((float) totalUsedCPUTime * 100) / (float) totalAvailCPUTime;
        log(per);
        return (int) per;
    }

    public String Ram() {
        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        long physicalMemorySize = os.getTotalPhysicalMemorySize();
//        System.out.println("total physical memory : " + physicalMemorySize / mb + "MB ");
        long physicalfreeMemorySize = os.getFreePhysicalMemorySize();
//        System.out.println("total free physical memory : " + physicalfreeMemorySize / mb + "MB");
        return (100-(((physicalfreeMemorySize/mb)*100)/(physicalMemorySize / mb)))+"%";
    }

    public String Disk() {
        File diskPartition = new File("C:");
        File diskPartition1 = new File("X:");
        long totalCapacity = diskPartition.getTotalSpace() / gb;
        long totalCapacity1 = diskPartition1.getTotalSpace() / gb;
        double freePartitionSpace = diskPartition.getFreeSpace() / gb;
        double freePartitionSpace1 = diskPartition1.getFreeSpace() / gb;
//        System.out.println("Total C partition size : " + totalCapacity + "GB");
//        System.out.println("Total X partition size : " + totalCapacity1 + "GB");
//        System.out.println("Free Space in drive C: : " + freePartitionSpace + "GB");
//        System.out.println("Free Space in drive X:  : " + freePartitionSpace1 + "GB");
        return (100-(((diskPartition.getFreeSpace() / gb)*100)/(diskPartition.getTotalSpace() / gb)))+"%";
    }

    public String Cpu() {
        long start = System.nanoTime();
        // log(start);
        //number of available processors;
        int cpuCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        Random random = new Random(start);
        int seed = Math.abs(random.nextInt());
//        log("\n \n CPU USAGE DETAILS \n\n");
//        log("Starting Test with " + cpuCount + " CPUs and random number:" + seed);
        int primes = 10000;
        //
        long startCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        start = System.nanoTime();
        while (primes != 0) {
            if (isPrime(seed)) {
                primes--;
            }
            seed++;

        }
        float cpuPercent = calcCPU(startCPUTime, start, cpuCount);
//        log("CPU USAGE : " + cpuPercent + " % ");
        return cpuPercent+"%";
    }
}

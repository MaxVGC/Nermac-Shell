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
        float per = ((float) totalUsedCPUTime * 100) / (float) totalAvailCPUTime;
        return (int) per;
    }

    public String Ram() {
        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        long physicalMemorySize = os.getTotalPhysicalMemorySize();
        long physicalfreeMemorySize = os.getFreePhysicalMemorySize();
        return (100 - (((physicalfreeMemorySize / mb) * 100) / (physicalMemorySize / mb))) + "%";
    }

    public String Disk() {
        File diskPartition = new File("C:");
        return (100 - (((diskPartition.getFreeSpace() / gb) * 100) / (diskPartition.getTotalSpace() / gb))) + "%";
    }

    public String Cpu() {
        long start = System.nanoTime();
        int cpuCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        Random random = new Random(start);
        int seed = Math.abs(random.nextInt());
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
        int cpuPercent = calcCPU(startCPUTime, start, cpuCount);
        return cpuPercent + "%";
    }
}

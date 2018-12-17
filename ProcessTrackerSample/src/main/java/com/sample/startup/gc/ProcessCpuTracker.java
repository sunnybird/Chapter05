package com.sample.startup.gc;


import android.annotation.SuppressLint;


public class ProcessCpuTracker {
    private static final String TAG = "ProcessCpuTracker";

    // /proc/self/stat
    private static final int PROCESS_STATS_STATUS = 2;
    private static final int PROCESS_STATS_MINOR_FAULTS = 9;
    private static final int PROCESS_STATS_MAJOR_FAULTS = 11;
    private static final int PROCESS_STATS_UTIME = 13;
    private static final int PROCESS_STATS_STIME = 14;

    // /proc/self/sched
    private static final String NR_VOLUNTARY_SWITCHES = "nr_voluntary_switches";
    private static final String NR_INVOLUNTARY_SWITCHES = "nr_involuntary_switches";
    private static final String SE_IOWAIT_COUNT = "se.statistics.iowait_count";
    private static final String SE_IOWAIT_SUM = "se.statistics.iowait_sum";

    // /proc/stat
    private static final int SYSTEM_STATS_USER_TIME = 2;
    private static final int SYSTEM_STATS_NICE_TIME = 3;
    private static final int SYSTEM_STATS_SYS_TIME = 4;
    private static final int SYSTEM_STATS_IDLE_TIME = 5;
    private static final int SYSTEM_STATS_IOWAIT_TIME = 6;
    private static final int SYSTEM_STATS_IRQ_TIME = 7;
    private static final int SYSTEM_STATS_SOFT_IRQ_TIME = 8;

    // /proc/loadavg
    private static final int LOAD_AVERAGE_1_MIN = 0;
    private static final int LOAD_AVERAGE_5_MIN = 1;
    private static final int LOAD_AVERAGE_15_MIN = 2;

    // How long a CPU jiffy is in milliseconds.
    private final long mJiffyMillis;
    private int mCurrentProcID;


    public ProcessCpuTracker(int id) {
        long jiffyHz = Sysconf.getScClkTck();
        mJiffyMillis = 1000 / jiffyHz;
        mCurrentProcID = id;
    }

    public void update() {
         printCpu();
        getCpuCore();
    }




    @SuppressLint("SimpleDateFormat")
    final public String printCurrentState(long now) {

        return "";
    }


 private void printCpu() {
        try {
            File fileCpu = new File("/proc/stat");
            FileReader fileReader = new FileReader(fileCpu);
            BufferedReader br = new BufferedReader(fileReader);
            String cpuInfo = br.readLine();
            Log.d(TAG, "cpuInfo =" + cpuInfo);
            String[] infos = cpuInfo.split("  ")[1].split(" ");

            long user = Long.parseLong(infos[0]);
            long nice = Long.parseLong(infos[1]);
            long system = Long.parseLong(infos[2]);
            long idle = Long.parseLong(infos[3]);
            long iowait = Long.parseLong(infos[4]);
            long irq = Long.parseLong(infos[5]);
            long softirq = Long.parseLong(infos[6]);
            long stealstolen = Long.parseLong(infos[7]);
            long guest = Long.parseLong(infos[8]);

            long totalCPUTime = user + nice + system + idle + iowait + irq + softirq + stealstolen + guest;

            String precentCpu =
                              String.format("%.2f", (float)user / totalCPUTime) + "%user "
                            + String.format("%.2f", (float)system / totalCPUTime) + "%kernel "
                            + String.format("%.2f", (float)iowait / totalCPUTime) + "%iowait "
                            + String.format("%.2f", (float)irq / totalCPUTime) + "%irq "
                            + String.format("%.2f", (float)softirq / totalCPUTime) + "%softirq ";

            Log.d(TAG,"cpu info =" + precentCpu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCpuCore(){

        try {
            File fileCpu = new File("/proc/cpuinfo");
            FileReader fileReader = new FileReader(fileCpu);
            BufferedReader br = new BufferedReader(fileReader);
            String cpuInfo = br.readLine();
            Log.d(TAG, "cpu =" + cpuInfo);
        }catch (Exception e){


        }
       return 0;
    }

}

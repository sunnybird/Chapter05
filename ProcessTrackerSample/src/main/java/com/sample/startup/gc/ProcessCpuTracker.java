package com.sample.startup.gc;


import android.annotation.SuppressLint;
<<<<<<< HEAD
import android.os.Process;
import android.util.Log;
=======
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
>>>>>>> ae80e8c974bb4bec1120546a8a108bfd91edd737

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * https://linux.die.net/man/5/proc
 */
public class ProcessCpuTracker {
    private static final String TAG = "ProcessCpuTracker";

    // /proc/self/stat
    private static final int PROCESS_STATS_STATUS = 2;
    private static final int PROCESS_STATS_MINOR_FAULTS = 9;
    private static final int PROCESS_STATS_MAJOR_FAULTS = 11;
    private static final int PROCESS_STATS_UTIME = 13;
    private static final int PROCESS_STATS_STIME = 14;
    private static final int PROCESS_STATS_CUTIME = 15;
    private static final int PROCESS_STATS_CSTIME = 16;

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

    private long sysCpuTotalTime;
    private long processCpuTotalTime;


    public ProcessCpuTracker(int id) {
        long jiffyHz = Sysconf.getScClkTck();
        mJiffyMillis = 1000 / jiffyHz;
        mCurrentProcID = id;
    }

    public void update() {
<<<<<<< HEAD
=======
        printCpu(0,0);
        getCpuCore();
        getloadavg();
    }
>>>>>>> ae80e8c974bb4bec1120546a8a108bfd91edd737

        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("System TOTAL: ");
        String sysCpuInfo = printSysCpuStat();
        stringBuilder.append(sysCpuInfo);
        stringBuilder.append("\n");

        stringBuilder.append("CPU Core: ");
        int cpucore = getCpuCore();
        stringBuilder.append(cpucore);
        stringBuilder.append("\n");

        stringBuilder.append("Load Average: ");
        String loadavg = getloadavg();
        stringBuilder.append(loadavg);
        stringBuilder.append("\n\n");

        stringBuilder.append("Process: ");
        String porcessName = getProcessName();
        stringBuilder.append(porcessName);
        stringBuilder.append("\n");

        String processCpuInfo = printProcessCpuStat(mCurrentProcID, sysCpuTotalTime);
        stringBuilder.append(processCpuInfo);
        stringBuilder.append("\n\n");

<<<<<<< HEAD
        stringBuilder.append("Threads: ");
        stringBuilder.append("\n");
        String threadCpuInfo = printThreasCpuStst(mCurrentProcID, processCpuTotalTime);
        stringBuilder.append(threadCpuInfo);
        String res = stringBuilder.toString();

        Log.d(TAG,"info = "+ res);
    }


=======
>>>>>>> ae80e8c974bb4bec1120546a8a108bfd91edd737
    @SuppressLint("SimpleDateFormat")
    final public String printCurrentState(long now) {

        return "";
    }


<<<<<<< HEAD
    private String printSysCpuStat() {
        String result = "";

        try {
            String filePath = "/proc/stat";
=======
    private void printCpu(int pid ,int tid) {
        try {

            String filePath = "/proc/{}/{}/stat";
>>>>>>> ae80e8c974bb4bec1120546a8a108bfd91edd737
            File fileCpu = new File(filePath);
            FileReader fileReader = new FileReader(fileCpu);
            BufferedReader br = new BufferedReader(fileReader);
            String cpuInfo = br.readLine();
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
                    String.format("%.2f", (float) user / totalCPUTime) + "% user +"
                            + String.format("%.2f", (float) system / totalCPUTime) + "% kernel + "
                            + String.format("%.2f", (float) iowait / totalCPUTime) + "% iowait + "
                            + String.format("%.2f", (float) irq / totalCPUTime) + "% irq + "
                            + String.format("%.2f", (float) softirq / totalCPUTime) + "% softirq + "
                            + String.format("%.2f", (float) idle / totalCPUTime) + "% idle "
                    ;

            result = precentCpu;

            sysCpuTotalTime = totalCPUTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String printProcessCpuStat(long pid, long cpuTotalTime) {
        String result = "";

        try {
            String filePath = "/proc/" + pid + "/stat";
            File fileCpu = new File(filePath);
            FileReader fileReader = new FileReader(fileCpu);
            BufferedReader br = new BufferedReader(fileReader);
            String cpuInfo = br.readLine();
            String[] infos = cpuInfo.split(" ");

            long utime = Long.parseLong(infos[PROCESS_STATS_UTIME - 1]);
            long stime = Long.parseLong(infos[PROCESS_STATS_STIME - 1]);
            long cstime = Long.parseLong(infos[PROCESS_STATS_CSTIME - 1]);
            long cutime = Long.parseLong(infos[PROCESS_STATS_CUTIME - 1]);

            long major_faults = Long.parseLong(infos[PROCESS_STATS_MAJOR_FAULTS - 1]);
            long minor_faults = Long.parseLong(infos[PROCESS_STATS_MINOR_FAULTS - 1]);
            String status = infos[PROCESS_STATS_STATUS - 1] + infos[PROCESS_STATS_STATUS];
            long processTotalTime = utime + stime + cstime + cutime;
            double processPrecent = processTotalTime * 1.00 / cpuTotalTime;
            NumberFormat format = NumberFormat.getPercentInstance();
            format.setMinimumFractionDigits(2);

            String precentCpu =
<<<<<<< HEAD
                    format.format(processPrecent) + " " +
                            mCurrentProcID + "/" + status + ": " +
                            String.format("%.2f", (float) utime / processTotalTime) + "% user + "
                            + String.format("%.2f", (float) stime / processTotalTime) + "% kernel + "
                            + String.format("faults：%d", major_faults + minor_faults);


            result = precentCpu;
            processCpuTotalTime = processTotalTime;

=======
                    String.format("%.2f", (float) user / totalCPUTime) + "%user "
                            + String.format("%.2f", (float) system / totalCPUTime) + "%kernel "
                            + String.format("%.2f", (float) iowait / totalCPUTime) + "%iowait "
                            + String.format("%.2f", (float) irq / totalCPUTime) + "%irq "
                            + String.format("%.2f", (float) softirq / totalCPUTime) + "%softirq ";

            Log.d(TAG, "cpu info =" + precentCpu);
>>>>>>> ae80e8c974bb4bec1120546a8a108bfd91edd737
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String printThreasCpuStst(long pid, long processTotalTime) {

        StringBuilder stringBuilder = new StringBuilder();

        String filePath = "/proc/" + pid + "/task";
        File fileTask = new File(filePath);
        File[] files = fileTask.listFiles();
        for (File file : files) {

            try {
                String fileStatPath = file.getAbsolutePath() + "/stat";
                File fileCpu = new File(fileStatPath);
                FileReader fileReader = new FileReader(fileCpu);
                BufferedReader br = new BufferedReader(fileReader);
                String cpuInfo = br.readLine();
                String[] infos = cpuInfo.split(" ");

                long utime = Long.parseLong(infos[PROCESS_STATS_UTIME - 1]);
                long stime = Long.parseLong(infos[PROCESS_STATS_STIME - 1]);
                long cstime = Long.parseLong(infos[PROCESS_STATS_CSTIME - 1]);
                long cutime = Long.parseLong(infos[PROCESS_STATS_CUTIME - 1]);

                long major_faults = Long.parseLong(infos[PROCESS_STATS_MAJOR_FAULTS - 1]);
                long minor_faults = Long.parseLong(infos[PROCESS_STATS_MINOR_FAULTS - 1]);
                String status = infos[PROCESS_STATS_STATUS - 1] + infos[PROCESS_STATS_STATUS];
                long threadTotalTime = utime + stime + cstime + cutime;

                double processPrecent = threadTotalTime * 1.00 / processTotalTime;
                NumberFormat format = NumberFormat.getPercentInstance();
                format.setMinimumFractionDigits(2);

                if (threadTotalTime > 0) {
                    String precentCpu =
                            format.format(processPrecent) + " " +
                                    pid + "/" + status + ": " +
                                    String.format("%.2f", (float) utime / threadTotalTime) + "% user + "
                                    + String.format("%.2f", (float) stime / threadTotalTime) + "% kernel "
                                    + String.format("faults：%d", major_faults + minor_faults);


//                    Log.d(TAG, "thread cpu info =" + precentCpu);

                    stringBuilder.append(precentCpu);
                    stringBuilder.append("\n");
                }

            } catch (Exception e) {

            }
        }


        return stringBuilder.toString();
    }

<<<<<<< HEAD
    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

=======
>>>>>>> ae80e8c974bb4bec1120546a8a108bfd91edd737
    private int getCpuCore() {
        int core = 0;
        try {
            File fileCpu = new File("/proc/cpuinfo");
            FileReader fileReader = new FileReader(fileCpu);
            BufferedReader br = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }

            String cupinfo = stringBuilder.toString().trim();
            Pattern pattern = Pattern.compile("processor\t: [0-9]");
            Matcher matcher = pattern.matcher(cupinfo);
            while (matcher.find()) {
                core++;
            }

            Log.d(TAG, "cpu core  =" + core);

        } catch (Exception e) {


        }
        return core;
    }

    private String getloadavg() {

        String loadavg = "";

        try {
            File fileCpu = new File("/proc/loadavg");
            FileReader fileReader = new FileReader(fileCpu);
            BufferedReader br = new BufferedReader(fileReader);
            loadavg = br.readLine();
        } catch (Exception e) {


        }
        return loadavg;
    }

}

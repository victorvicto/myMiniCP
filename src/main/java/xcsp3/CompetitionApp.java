package xcsp3;
import minicp.search.SearchStatistics;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class CompetitionApp {
    public static long randomSeed = 1811631;
    public static long timeLimit = -1;
    public static int memLimit = -1;
    public static int nbcore = -1;
    public static String tmpdir = "";
    public static String dir = "";
    public static String benchname = null;
    public static boolean statusPrinted = false;
    public static String status = "UNKNOWN";
    public static String currentSol = "";

    // Time management
    public static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    public static Long t0 = threadMXBean.getCurrentThreadCpuTime();

    public static void main(String[] args) {
        parseArgs(args, 0);
        String version = "2018-04-11";

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                if(!statusPrinted) printStatus();
            }
        });

        printComment("version: " + version);
        printComment("seed: " + randomSeed);
        printComment("timeout: " + timeLimit);
        printComment("memlimit: " + memLimit);
        printComment("nbcore: " + nbcore);

        Random rand = new Random();
        rand.setSeed(randomSeed);

        try {
            runSolver();
        }
        catch(Exception e) {
            printDiagnostic("EXCEPTION", e.getMessage());
            printComment(mkString(e.getStackTrace(), StackTraceElement::toString,"\n"));
            if(!statusPrinted)
                printStatus();
        }
    }

    public static void runSolver() throws Exception {
        try {
            XCSP3 xcsp3 = new XCSP3(benchname);
            SearchStatistics ss = xcsp3.solve((solution, value) -> updateSol(solution, value, value != Integer.MAX_VALUE),
                    (ss2) -> (!xcsp3.isCOP() && ss2.nSolutions >= 1) || (timeLimit != -1 && threadMXBean.getCurrentThreadCpuTime() - t0 >= timeLimit * 1000000));


            if(!currentSol.isEmpty()) {
                if(ss.completed && xcsp3.isCOP())
                    status = "OPTIMUM FOUND";
            }
            else if(ss.completed) {
                status = "UNSATISFIABLE";
            }
            else
                printDiagnostic("NO_SOL_FOUND");
            printStatus();
        }
        catch (NotImplementedException e) {
            status = "UNSUPPORTED";
            printComment("Caught NotImplementedException");
            printComment("\t"+mkString(e.getStackTrace(), StackTraceElement::toString,"\n\t"));
            printStatus();
        }
        catch (InconsistencyException e) {
            status = "UNSATISFIABLE";
            printStatus();
        }
    }
    public static void updateSol(String sol, int obj, boolean cop) {
        currentSol = sol;
        if(status.equals("UNKNOWN")) status = "SATISFIABLE";
        if(cop)
            System.out.println("o "+obj);
    }

    public static void printSolution() {
        if(!currentSol.equals("")) {
            System.out.println("s "+status);
            System.out.println(prefixLines(currentSol, "v "));
        }
    }

    public static void printStatus() {
        if(status.equals("OPTIMUM FOUND") || status.equals("SATISFIABLE"))
            printSolution();
        else
            System.out.println("s " + status);
        statusPrinted = true;
    }

    public static void printComment(String comment) {
        System.out.println(prefixLines(comment, "c "));
    }

    public static void printDiagnostic(String arg) {
        System.out.println(prefixLines(arg, "d "));
    }

    public static void printDiagnostic(String arg, String value) {
        System.out.println(prefixLines(arg + " " + value, "d "));
    }

    public static void parseArgs(String[] args, int pos) {
        if(pos >= args.length)
            return;

        switch (args[pos]) {
            case "--randomseed":
                randomSeed = Long.parseLong(args[pos+1]);
                parseArgs(args, pos+2);
                break;
            case "--timelimit":
                timeLimit = Integer.parseInt(args[pos+1]);
                parseArgs(args, pos+2);
                break;
            case "--memlimit":
                memLimit = Integer.parseInt(args[pos+1]);
                parseArgs(args, pos+2);
                break;
            case "--nbcore":
                nbcore = Integer.parseInt(args[pos+1]);
                parseArgs(args, pos+2);
                break;
            case "--tmpdir":
                tmpdir = args[pos+1];
                parseArgs(args, pos+2);
                break;
            case "--dir":
                dir = args[pos+1];
                parseArgs(args, pos+2);
                break;
            default:
                if(args[pos].startsWith("--"))
                    throw new IllegalArgumentException("Unknown arg "+args[pos]);

                benchname = args[pos];
                parseArgs(args, pos+1);
        }
    }

    private static String prefixLines(String text, String prefix) {
        return prefix + mkString(text.split("\\r?\\n"), "\n"+prefix);
    }

    private static <E> String mkString(List<E> list, Function<E,String> stringify, String delimiter) {
        int i = 0;
        StringBuilder s = new StringBuilder();
        for (E e : list) {
            if (i != 0) { s.append(delimiter); }
            s.append(stringify.apply(e));
            i++;
        }
        return s.toString();
    }

    private static String mkString(List<String> list, String delimiter) {
        return mkString(list, x -> x, delimiter);
    }

    private static String mkString(String[] list, String delimiter) {
        return mkString(Arrays.asList(list), delimiter);
    }

    private static <E> String mkString(E[] list, Function<E,String> stringify, String delimiter) {
        return mkString(Arrays.asList(list), stringify, delimiter);
    }
}
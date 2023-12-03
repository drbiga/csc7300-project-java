package fm_index;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public class Driver {
    
    public static void main(String[] args) {
//        runBanana();
//        System.out.println("=======================================");
//        System.out.println("RUNNING DNA");
//        runDna();
//        System.out.println("=======================================");
//        System.out.println("RUNNING SOURCES");
//        runSources();
        System.out.println("=======================================");
        System.out.println("RUNNING ENGLISH");
        runEnglish();
    }

    public static void runBanana() {
        String sequence = "banana";

        String[] queries = {
//                "nana",
                "ana",
//                "ba",
        };

        int[] blockSizes = {1};
        for (int blockSize : blockSizes) {
            System.out.println(String.format("Running for block size %d", blockSize));
            runBlockSize(sequence, blockSize, queries);
        }
    }

    public static void runDna() {
        try {
            Scanner in = new Scanner(new File("data/dnasample1M.txt"));
            String sequence = in.nextLine();

            String[] queries = {
//                    "GTA",
//                    "ATGCGA",
                    "GATCACTGATG",
            };

            int[] blockSizes = {100, 1000, 10000, 100000};
            for (int blockSize : blockSizes) {
                System.out.println(String.format("Running for block size %d", blockSize));
                runBlockSize(sequence, blockSize, queries);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void runSources() {
        try {
            Scanner in = new Scanner(new File("data/sourcessample1M.txt"));
            String sequence = in.nextLine();

            String[] queries = {
//                    "something that is not there",
//                    "int main",
                    "struct",
            };

            int[] blockSizes = {100, 1000, 10000, 100000};
            for (int blockSize : blockSizes) {
                System.out.println(String.format("Running for block size %d", blockSize));
                runBlockSize(sequence, blockSize, queries);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void runEnglish() {
        try {
            Scanner in = new Scanner(new File("data/englishsample1M.txt"), "windows-1252");
            String sequence = in.nextLine();
//            System.out.println(sequence);0
            String[] queries = {
//                    "something that maybe is not there",
//                    "something",
//                    "the fox jumps over the lazy dog",
//                    "cat",
//                    "playing cards",
                    "placed it in the hands" // extracted from text file
//                    "placed" // extracted from text file
//                    "Graphics" // extracted from the text file
//                    "number of graphics" // extracted from the text file
            };

            int[] blockSizes = {100, 1000, 10000, 100000};
            for (int blockSize : blockSizes) {
                System.out.println(String.format("Running for block size %d", blockSize));
                runBlockSize(sequence, blockSize, queries);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void runBlockSize(String text, int blockSize, String[] queries) {
        long start = System.currentTimeMillis();
        FMIndex fmi = new FMIndex(text, blockSize);
        long end = System.currentTimeMillis();

        System.out.println(String.format("\tIndex Build Time: %s", end - start));
        System.out.println("Meg used="+(Runtime.getRuntime().totalMemory()-
                Runtime.getRuntime().freeMemory())/(1000*1000)+"M");

        for (String query: queries) {
            System.out.println(String.format("\tRunning query: %s", query));
            runQuery(fmi, query);
        }
    }

    public static void runQuery(FMIndex fmi, String query) {
        // Search for a substring in the sequence
        long start = System.nanoTime();
        int[] results = fmi.search(query);
        long end = System.nanoTime();
        System.out.println(String.format("\t\tQuery Time: %s", end - start));

        // Print the range of the result
        System.out.println(String.format("\t\tNumber of matches: %d", results[1] - results[0] + 1));
        System.out.println(String.format("\t\tRange %d..%d", results[0], results[1]));
    }
}

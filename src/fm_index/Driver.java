package fm_index;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Driver {
    
    public static void main(String[] args) {
        // Testing Block class
//        boolean[] bitvector = {false, true, false, false, false, true, false};
//        int blockSize = 5;
//        Block b = new Block(bitvector, blockSize);
//        System.out.println("Checkpoints vector:");
//        b.printCheckpoints();
//        int index = 4;
//        System.out.println(String.format("Number of zeros until index %d: %d", index, b.getCount(index, false)));
        try {
//             Open file and extract sequence
            Scanner in = new Scanner(new File("data/dna.50MB.nonl.txt"));
            String sequence = in.nextLine();
//            String sequence = "banana";

            int blockSize = 500000;
            // Initialize FM-Index with sequence
            FMIndex fmi = new FMIndex(sequence, blockSize);

            Scanner userIn = new Scanner(System.in);
            System.out.println("Enter the search query");
            String query = userIn.next();

            // Search for a substring in the sequence
            int[] results = fmi.search(query);

            // Print the range of the result
            System.out.println("The result range is: " + results[0] + ".." + results[1]);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

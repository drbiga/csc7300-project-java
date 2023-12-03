package fm_index;



public class Block {

    private boolean bitvector[];
    private int blockSize;
    private boolean useSimpleScan;

    // Number of "zeros" up until the corresponding block
    // The number of "ones" is the complement of that,
    // meaning it can be computed using
    // blockSize*index - checkpoints[index]
    // which is the total number of positions up until the
    // checkpoint minus the number of occurrences of the
    // "zero" character.
    private int checkpoints[];

    public Block(boolean[] bitvector, int blockSize) {
        this.bitvector = bitvector;
        this.blockSize = blockSize;

        if (blockSize > bitvector.length || blockSize == -1) {
            this.useSimpleScan = true;
        } else {
            int numberBlocks = bitvector.length / blockSize;
//        System.out.println(String.format("Number of blocks: %d", numberBlocks));
            this.checkpoints = new int[numberBlocks];

            // We are not going to keep the counts for the last
            // section of the bitvector, meaning for indexes that
            // are greater than numberBlocks*blockSize, we will
            // have to compute the counts starting from the last
            // position of the checkpoints vector.
            int zeroCount = 0;
            for (int i = 0; i < bitvector.length; i++) {
//            System.out.println(String.format("Analysing position %d", i));
                if (bitvector[i] == false) {
                    zeroCount += 1;
//                System.out.println("\tIncrementing zero count");
                }
                // If we have analyzed (i+1) positions of the bit vector,
                // we store the counts in the checkpoints array
                if ((i+1) % blockSize == 0) {
                    int checkpointIndex = i / blockSize;
                    checkpoints[checkpointIndex] = zeroCount;
//                System.out.println(String.format("\tSaved zero count %d to position %d of checkpoints vector", zeroCount, checkpointIndex));
                }
            }
        }
    }

    public int getCount(int index, boolean encoding) {
//        System.out.println("Block.getCount()");
        if (this.useSimpleScan) {
//            System.out.println("Block.getCount() -> useSimpleScan");
            int count = 0;
            for (int i = 0; i < index; i++) {
                if (this.bitvector[i] == encoding) {
                    count += 1;
                }
            }
            return count;
        }
//        System.out.println("Block.getCount() -> not useSimpleScan");
        int checkpointIndex = index / blockSize - 1;
//        System.out.println(String.format("Checkpoint index is %d", checkpointIndex));

        // Naive approach: get the checkpoint of the last
        // multiple and go from there. Not using the "nearest"
        // checkpoint for now. That would be computed by calculating
        // the distances from both the previous and the next checkpoints
        // to the index that is being queried.
        int checkpointCount = 0;
        // We also have to check if the queried index is before the first
        // checkpoint to avoid array indexing problems. For example,
        // the checkpoint index for query index = 1 when block size > 2
        // would be -1, because 1 / 2 - 1 is going to result to -1.
        if (checkpointIndex >= 0) {
            checkpointCount = this.checkpoints[checkpointIndex];
        }
        // If we are querying the "zero" count, then we are done
        // If not, we have to transform this to the "one" count.
        if (encoding == true) {
            // Total number of positions in the bitvector up until the checkpoint
            // minus the number of occurrences of "zero".
            checkpointCount = (checkpointIndex+1)*this.blockSize - checkpointCount;
        }
//        System.out.println(String.format("Checkpoint count: %d", checkpointCount));
        int startingIndex = (checkpointIndex+1)*this.blockSize;
//        System.out.println(String.format("Starting index: %d", startingIndex));
        for (int i = startingIndex; i < index; i++) {
//            System.out.println(String.format("Evaluating index %d", i));
            if (this.bitvector[i] == encoding) {
//                System.out.println("Adding 1 to checkpoint count");
                checkpointCount += 1;
            }
        }

        return checkpointCount;
    }

    public void printCheckpoints() {
        for (int i = 0; i < this.checkpoints.length; i++) {
            if (i < this.checkpoints.length) {
                System.out.print(this.checkpoints[i] + ", ");
            }
        }
        System.out.println();
    }
}

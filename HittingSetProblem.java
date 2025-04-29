import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;

public class HittingSetProblem {

    /**
     * 1st VARIANT
     * - Divide & Conquer Approach
     */
    public int[] algorithm1(int[][] currentSets, int n, int c, int k){
        if(currentSets.length == 0) return new int[0];
        if(k == 0) return null;

        // Select random set
        int randomIndex = (int)(Math.random() * currentSets.length);
        int[] pickedSet = shuffleArray(currentSets[randomIndex]);

        // For each element of pickedSet, attempt to find result by removing it
        for(int i = 0; i < pickedSet.length; i++){
            int element = pickedSet[i];
            if(element == 0) continue; // skip 0 (indicates that set's length < c)

            // Reduce problem by removing sets that contain element
            int[][] reducedProblem = reduceSets(currentSets, element);
            
            // Recursive call on reduced problem
            int[] solution = algorithm1(reducedProblem, n, c, k - 1);
            
            if(solution != null){ // if solution is found
                // hitting set is the set: element we reduced the problem with + elements returned from previous recursive call
                int[] hittingSet = new int[solution.length + 1];
                hittingSet[0] = element;

                // copy solution.length elements of solution starting from index=0, into hitting set, starting from its index=1
                System.arraycopy(solution, 0, hittingSet, 1, solution.length);

                return hittingSet;
            }
        }

        return null;
    }

    /**
     * 2nd VARIANT
     * - Similar to 1st variant
     * - Instead of randomly selecting element for reducing the problem size, we select a "critical element"
     * - Critical Element: element which appears in most sets
     * - If more than one elements have same number of occurances: select the one with smallest index 
     */
    public int[] algorithm2(int[][] currentSets, int n, int c, int k){
        if(currentSets.length == 0) return new int[0];
        if(k == 0) return null;
        
        // Select random set
        int randomIndex = (int)(Math.random() * currentSets.length);
        int[] pickedSet = currentSets[randomIndex];

        // Get criticallity value for each element of pickedSet
        int[] criticallities = getCriticallities(currentSets, n);

        // Sort pickedSet based on criticallity of each element (descending order)
        for (int i = 0; i < pickedSet.length - 1; i++) {
            for (int j = i + 1; j < pickedSet.length; j++) {
                int a = pickedSet[i];
                int b = pickedSet[j];

                if (a == 0 || b == 0) continue; // ignore 0 (indicates that pickedSet.length < c)

                int critA = criticallities[a];
                int critB = criticallities[b];

                if (critB > critA || (critB == critA && b < a)) {
                    // swap elements in pickedSet
                    int temp = pickedSet[i];
                    pickedSet[i] = pickedSet[j];
                    pickedSet[j] = temp;
                }
            }
        }

        // For each element of pickedSet, attempt to find result by removing it
        for(int i = 0; i < pickedSet.length; i++){
            int element = pickedSet[i];
            if(element == 0) continue; // skip 0 (indicates that set's length < c)

            // Reduce problem by removing sets that contain element
            int[][] reducedProblem = reduceSets(currentSets, element);
            
            // Recursive call on reduced problem
            int[] solution = algorithm2(reducedProblem, n, c, k - 1);
            
            if(solution != null){ // if solution is found
                // hitting set is the set: element we reduced the problem with + elements returned from previous recursive call
                int[] hittingSet = new int[solution.length + 1];
                hittingSet[0] = element;

                // copy solution.length elements of solution starting from index=0, into hitting set, starting from its index=1
                System.arraycopy(solution, 0, hittingSet, 1, solution.length);

                return hittingSet;
            }
        }

        return null;
    }

    /**
     * 3rd VARIANT
     * - Similar to 1st variant
     * - Instead of randomly selecting subset for reducing the problem size, we select the subset with least 
     *   number of elements 
     * - If more than one subsets have same number of occurances: select the one with smallest index 
     */
    public int[] algorithm3(int[][] currentSets, int n, int c, int k){
        if(currentSets.length == 0) return new int[0];
        if(k == 0) return null;

        // Select subset B with least amount of elements inside
        int setIndex = findSetWithLeastElements(currentSets, c);
        int[] pickedSet = shuffleArray(currentSets[setIndex]);

        // For each element of pickedSet, attempt to find result by removing it
        for(int i = 0; i < pickedSet.length; i++){
            int element = pickedSet[i];
            if(element == 0) continue;

            // Reduce problem by removing sets that contain element
            int[][] reducedProblem = reduceSets(currentSets, element);
            
            // Recursive call on reduced problem
            int[] solution = algorithm3(reducedProblem, n, c, k - 1);
            
            if(solution != null){ // if solution is found
                // hitting set is the set: element we reduced the problem with + elements returned from previous recursive call
                int[] hittingSet = new int[solution.length + 1];
                hittingSet[0] = element;

                // copy solution.length elements of solution starting from index=0, into hitting set, starting from its index=1
                System.arraycopy(solution, 0, hittingSet, 1, solution.length);

                return hittingSet;
            }
        }

        return null;
    }

    /**
     * 4th VARIANT
     * - Similar to 1st variant
     * - Select subset with least number of elements and the process of finding a hitting set begins with the
     *   most critical element first.
     */
    public int[] algorithm4(int[][] currentSets, int n, int c, int k){
        if(currentSets.length == 0) return new int[0];
        if(k == 0) return null;
    
        // Find set with least number of elements
        int setIndex = findSetWithLeastElements(currentSets, c);
        int[] pickedSet = currentSets[setIndex];

        // Get criticallity value for each element of pickedSet
        int[] criticallities = getCriticallities(currentSets, n);

        // Sort pickedSet based on criticallity of each element (descending order)
        for (int i = 0; i < pickedSet.length - 1; i++) {
            for (int j = i + 1; j < pickedSet.length; j++) {
                int a = pickedSet[i];
                int b = pickedSet[j];

                if (a == 0 || b == 0) continue; // ignore 0 (indicates that pickedSet.length < c)

                int critA = criticallities[a];
                int critB = criticallities[b];

                if (critB > critA || (critB == critA && b < a)) {
                    // swap elements in pickedSet
                    int temp = pickedSet[i];
                    pickedSet[i] = pickedSet[j];
                    pickedSet[j] = temp;
                }
            }
        }

        // For each element of pickedSet, attempt to find result by removing it
        for(int i = 0; i < pickedSet.length; i++){
            int element = pickedSet[i];
            if(element == 0) continue; // skip 0 (indicates that set's length < c)

            // Reduce problem by removing sets that contain element
            int[][] reducedProblem = reduceSets(currentSets, element);
            
            // Recursive call on reduced problem
            int[] solution = algorithm4(reducedProblem, n, c, k - 1);
            
            if(solution != null){ // if solution is found
                // hitting set is the set: element we reduced the problem with + elements returned from previous recursive call
                int[] hittingSet = new int[solution.length + 1];
                hittingSet[0] = element;

                // copy solution.length elements of solution starting from index=0, into hitting set, starting from its index=1
                System.arraycopy(solution, 0, hittingSet, 1, solution.length);

                return hittingSet;
            }
        }

        return null;
    }


    /* HELPER METHODS BELOW */

    // Helper method to reduce problem
    private int[][] reduceSets(int[][] currentSets, int element) {
        // Count how many sets DON'T contain element
        int count = 0;
        for(int i = 0; i < currentSets.length; i++){
            boolean found = false;
            for(int j = 0; j < currentSets[i].length; j++){
                if(currentSets[i][j] == element){
                    found = true;
                    break;
                }
            }
            if(!found) count++;
        }
        
        int[][] reduced = new int[count][currentSets[0].length]; // new array to represent sets, having removed sets that contain element
        int setIndex = 0; // index to keep track of sets that dont contain element
        for(int i = 0; i < currentSets.length; i++){
            boolean found = false;
            for(int j = 0; j < currentSets[i].length; j++){
                if(currentSets[i][j] == element){
                    found = true;
                    break;
                }
            }

            // If we didn't encounter element in the set Bi
            if(!found){
                // Copy all elements of set Bi into reduced
                System.arraycopy(currentSets[i], 0, reduced[setIndex], 0, currentSets[i].length);
                setIndex++;
            }
        }

        return reduced;
    }

    // Helper method to get criticallity values for alle elements of pickedSet
    private int[] getCriticallities(int[][] currentSets, int n){
        int[] criticallities = new int[n + 1];

        for (int[] subset : currentSets){
            for(int element : subset){
                if(element != 0) criticallities[element]++;
            }
        }

        return criticallities;
    }

    // Helper method to find subset B with least number of elements
    private int findSetWithLeastElements(int[][] currentSets, int c){
        int minCount = c;
        int minIndex = 0;
        for(int i = 0; i < currentSets.length; i++){
            int count = 0;
            for(int j = 0; j < currentSets[i].length; j++){
                int element = currentSets[i][j];
                if(element != 0){
                    count++;
                }
            }

            if(count < minCount){
                minCount = count;
                minIndex = i;
            }
        }

        return minIndex;
    }

    // Helper method to shuffle array
    private static int[] shuffleArray(int[] array) {
        Random rand = new Random();
        int[] result = array.clone();

        for (int i = result.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = result[i];
            result[i] = result[j];
            result[j] = temp;
        }

        return result;
    }

    // Helper method to validate hitting set
    private boolean isValidHittingSet(int[] hittingSet, int[][] sets) {
        for (int i = 0; i < sets.length; i++) {
            boolean covered = false;
            for (int j = 0; j < sets[i].length; j++) {
                int val = sets[i][j];
                if (val == 0) continue;

                // Check if value in Bi subset is in hitting set
                for (int h = 0; h < hittingSet.length; h++) {
                    if (val == hittingSet[h]) {
                        covered = true;
                        break;
                    }
                }

                // If at least one is found -> break and check next Bi subset
                if (covered) break;
            }

            // If we check all values inside Bi subset, and none are found in hitting set -> INVALID
            if (!covered) return false;
        }
        return true;
    }

    // Helper method that reads the file and returns data read
    private Object[] loadData(String filename) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] line1 = reader.readLine().trim().split("\\s+");
            int n = Integer.parseInt(line1[0]);
            int m = Integer.parseInt(line1[1]);
            int c = Integer.parseInt(line1[2]);
            int k = Integer.parseInt(line1[3]);

            int[][] sets = new int[m][c];
            for(int i = 0; i < m; i++){
                String line[] = reader.readLine().trim().split("\\s+");
                for(int j = 0; j < line.length; j++){
                    sets[i][j] = Integer.parseInt(line[j]);
                }
                // if B set's length is < than c, it automatically gets filled with 0s
            }

            reader.close();
            return new Object[]{n, m, c, k, sets};
        }
    }

    @SuppressWarnings("unused")
    private Object[] generateRandomData(int n, int m, int c, int k) {
        int[][] sets = new int[m][c];
 
        // Generate random sets
        for (int i = 0; i < m; i++) {
            // For each set, decide how many elements it will have (between 1 and c)
            int setSize = 1 + (int)(Math.random() * c);

            // Use a boolean array to track which elements are already in the set
            boolean[] used = new boolean[n+1];

            // Fill the set with random elements
            for (int j = 0; j < setSize; j++) {
                int element;
                do {
                    // Generate elements from 1 to n (not 0)
                    element = 1 + (int)(Math.random() * n);
                } while (used[element]); // Ensure no duplicates in the same set

                sets[i][j] = element;
                used[element] = true;
            }

            // Sort the elements in the set for better readability
            Arrays.sort(sets[i], 0, setSize);
        }
 
        return new Object[]{sets, n, m, c, k};
    }

    /* EXPERIMENT MAIN METHOD */
    public static void main(String[] args) {
        try {
            HittingSetProblem hsp = new HittingSetProblem();
    
            Object[] data = hsp.loadData("script-6-input.txt");
            int n = (int) data[0];
            int m = (int) data[1];
            int c = (int) data[2];
            int k = (int) data[3];
            int[][] sets = (int[][]) data[4];
    
            String[] algorithms = {
                "Algorithm 4",
                "Algorithm 3",
                "Algorithm 2",
                "Algorithm 1"
            };
    
            boolean[] runAlgo = {true, true, true, true};
    
            PrintWriter writer = new PrintWriter(new FileWriter("./results/experiment2_test_results.txt"));
            writer.println("Hitting Set Experiment Results");
            writer.println("=======================================");
            writer.printf("%-5s%-12s%-15s%-12s%-40s%s%n", "k", "repetition", "Algorithm", "Time(ms)", "Hitting Set", "Validity");
    
            
            int totalTime = 0;
            for (int a = 0; a < 4; a++) {
                totalTime = 0;
                if (!runAlgo[a]) {
                    System.out.println("[" + algorithms[a] + "] skipped (timed out in previous k)");
                    continue;
                }

                for (int rep = 1; rep <= 3; rep++) {
                    long start = System.nanoTime();
                    int[] result = null;

                    if (a == 0) {
                        result = hsp.algorithm4(sets, n, c, k);
                    } else if (a == 1) {
                        result = hsp.algorithm3(sets, n, c, k);
                    } else if (a == 2){
                        result = hsp.algorithm2(sets, n, c, k);
                    } else if (a == 3){
                        result = hsp.algorithm1(sets, n, c, k);
                    }

                    long elapsed = (System.nanoTime() - start) / 1_000_000;
                    totalTime += elapsed;

                    if (elapsed > 3600000) {
                        System.out.println("[" + algorithms[a] + "] k=" + k + " exceeded 1 hour on repetition " + rep);
                        writer.println(k + "\t" + algorithms[a] + "\t>3600000\tTimeout (1 hour)");
                        runAlgo[a] = false;
                        break;
                    } else{
                        System.out.println("[" + algorithms[a] + "] k=" + k + " on rep=" + rep + " ran for " + elapsed + "ms.");
                        boolean isValid = (result != null) && hsp.isValidHittingSet(result, sets);
                        String resultString = (result != null) ? Arrays.toString(result) : "null";
                        String validity = isValid ? "Valid" : "Invalid";
                        writer.printf("%-5d%-12d%-15s%-12d%s %s%n", k, rep, algorithms[a], elapsed, resultString, validity);
                    }
                }
                System.out.println("Average Time: " + (totalTime/3) + "ms");
                System.out.println();
            }

            writer.close();
            System.out.println("\nResults saved.");
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* NORMAL MAIN METHOD */
    // public static void main(String[] args) {
    //     try {
    //         boolean experimentMode = false;

    //         HittingSetProblem hsp = new HittingSetProblem();

    //         int n, m, c, k;
    //         int[][] sets;

    //         if(experimentMode){
    //             n = 6;
    //             m = 8;
    //             c = 3;
    //             k = 3;
                
    //             // Generate random test data
    //             Object[] data = hsp.generateRandomData(n, m, c, k);
    //             sets = (int[][])data[0];
                
    //             System.out.println("[EXPERIMENT MODE] Generated random data:");
    //             System.out.println("n:" + n + " m:" + m + " c:" + c + " k:" + k);
    //         } else{
    //             // Regular Mode - read values from file
    //             Object[] data = hsp.loadData("./inputs/tutorial-ok.dat");
    //             n = (int)data[0];
    //             m = (int)data[1];
    //             c = (int)data[2];
    //             k = (int)data[3];
    //             sets = (int[][])data[4];

    //             System.out.println("[CORRECTNESS MODE] Loaded Data:");
    //             System.out.println("n:" + n + " m:" + m + " c:" + c + " k:" + k);
    //         }

    //         String[] algorithms = {
    //             "Algorithm 1",
    //             "Algorithm 2",
    //             "Algorithm 3",
    //             "Algorithm 4"
    //         };
            
    //         int[][] results = new int[4][];
    //         long[] times = new long[4];

    //         for (int i = 0; i < 4; i++) {
    //             long start = System.nanoTime();
            
    //             switch (i) {
    //                 case 0:
    //                     results[i] = hsp.algorithm1(sets, n, c, k);
    //                     break;
    //                 case 1:
    //                     results[i] = hsp.algorithm2(sets, n, c, k);
    //                     break;
    //                 case 2:
    //                     results[i] = hsp.algorithm3(sets, n, c, k);
    //                     break;
    //                 case 3:
    //                     // results[i] = hsp.algorithm4(sets, n, c, k);
    //                     break;
    //             }
            
    //             long end = System.nanoTime();
    //             times[i] = (end - start) / 1_000_000;
            
    //             System.out.println(">>> " + algorithms[i]);
    //             if (results[i] != null) {
    //                 Arrays.sort(results[i]);
    //                 System.out.print("Hitting Set: ");
    //                 for (int val : results[i]) {
    //                     System.out.print(val + " ");
    //                 }
    //                 System.out.println("\nValid: " + hsp.isValidHittingSet(results[i], sets));
    //             } else {
    //                 System.out.println("No hitting set of size ≤ k found.");
    //             }
            
    //             System.out.println("Execution Time: " + times[i] + " ms");
    //             System.out.println("----------------------------------------\n");
    //         }
            
    
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }



}
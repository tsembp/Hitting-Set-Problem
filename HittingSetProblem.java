import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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
        int[] pickedSet = currentSets[randomIndex];

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
        int[] criticallityArray = getCriticallities(currentSets, pickedSet);

        // Sort pickedSet based on criticallity of each element (descending order)
        for (int i = 0; i < pickedSet.length - 1; i++) {
            for (int j = i + 1; j < pickedSet.length; j++) {
                int a = pickedSet[i];
                int b = pickedSet[j];

                if (a == 0 || b == 0) continue; // ignore 0 (indicates that pickedSet.length < c)

                int critA = criticallityArray[i];
                int critB = criticallityArray[j];

                if (critB > critA || (critB == critA && b < a)) {
                    // swap elements in pickedSet
                    int temp = pickedSet[i];
                    pickedSet[i] = pickedSet[j];
                    pickedSet[j] = temp;

                    // swap parallel criticallity values
                    int tmpCrit = criticallityArray[i];
                    criticallityArray[i] = criticallityArray[j];
                    criticallityArray[j] = tmpCrit;
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
        int[] pickedSet = currentSets[setIndex];

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
        int[] criticallityArray = getCriticallities(currentSets, pickedSet);

        // Sort pickedSet based on criticallity of each element (descending order)
        for (int i = 0; i < pickedSet.length - 1; i++) {
            for (int j = i + 1; j < pickedSet.length; j++) {
                int a = pickedSet[i];
                int b = pickedSet[j];

                if (a == 0 || b == 0) continue; // ignore 0 (indicates that pickedSet.length < c)

                int critA = criticallityArray[i];
                int critB = criticallityArray[j];

                if (critB > critA || (critB == critA && b < a)) {
                    // swap elements in pickedSet
                    int temp = pickedSet[i];
                    pickedSet[i] = pickedSet[j];
                    pickedSet[j] = temp;

                    // swap parallel criticallity values
                    int tmpCrit = criticallityArray[i];
                    criticallityArray[i] = criticallityArray[j];
                    criticallityArray[j] = tmpCrit;
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
    private int[] getCriticallities(int[][] currentSets, int[] pickedSet){
        int[] criticallityArray = new int[pickedSet.length];

        for(int i = 0; i < pickedSet.length; i++){ // for each element of pickedSet
            int element = pickedSet[i];
            if(element == 0) continue; // skip 0 (indicates pickedSet.length < c)

            for(int j = 0; j < currentSets.length; j++){ // for each set Bi
                boolean found = false;
                for(int p = 0; p < currentSets[j].length; p++){ // for each element of Bi
                    if(currentSets[j][p] == element){ // if that element is equal to pickedSet's element
                        found = true;
                    }
                }

                if (found) {
                    criticallityArray[i]++;
                }
            }
        }

        return criticallityArray;
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

    // Helper method to generate random B sets based on given parameters
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
        
        // Ensure the generated instance has a hitting set of size ≤ k
        // This is a simplistic approach - real instances might need more sophisticated generation
        
        return new Object[]{sets, n, m, c, k};
    }

    // Helper method that reads the file and returns data read
    private Object[] loadData(String filename) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(filename));
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

    
    public static void main(String[] args) {
        try {
            boolean experimentMode = false;

            HittingSetProblem hsp = new HittingSetProblem();

            int n, m, c, k;
            int[][] sets;

            if(experimentMode){
                n = 6;
                m = 8;
                c = 3;
                k = 3;
                
                // Generate random test data
                Object[] data = hsp.generateRandomData(n, m, c, k);
                sets = (int[][])data[0];
                
                System.out.println("[EXPERIMENT MODE] Generated random data:");
                System.out.println("n:" + n + " m:" + m + " c:" + c + " k:" + k);
            } else{
                // Regular Mode - read values from file
                Object[] data = hsp.loadData("sets.dat");
                n = (int)data[0];
                m = (int)data[1];
                c = (int)data[2];
                k = (int)data[3];
                sets = (int[][])data[4];

                System.out.println("[CORRECTNESS MODE] Loaded Data:");
                System.out.println("n:" + n + " m:" + m + " c:" + c + " k:" + k);
            }

            // Print the sets
            for(int i = 0; i < m; i++) {
                System.out.print("B[" + i + "] = {");
                for(int j = 0; j < sets[i].length; j++) {
                    if (sets[i][j] != 0) {
                        System.out.print(sets[i][j] + ", ");
                    }
                }
                System.out.println("}");
            }
            System.out.println("");

            String[] algorithms = {
                "Algorithm 1",
                "Algorithm 2",
                "Algorithm 3",
                "Algorithm 4"
            };
            
            int[][] results = new int[4][];
            long[] times = new long[4];
            
            for (int i = 0; i < 4; i++) {
                long start = System.nanoTime();
            
                switch (i) {
                    case 0:
                        results[i] = hsp.algorithm1(sets, n, c, k);
                        break;
                    case 1:
                        results[i] = hsp.algorithm2(sets, n, c, k);
                        break;
                    case 2:
                        results[i] = hsp.algorithm3(sets, n, c, k);
                        break;
                    case 3:
                        results[i] = hsp.algorithm4(sets, n, c, k);
                        break;
                }
            
                long end = System.nanoTime();
                times[i] = (end - start) / 1_000_000;
            
                System.out.println(">>> " + algorithms[i]);
                if (results[i] != null) {
                    Arrays.sort(results[i]);
                    System.out.print("Hitting Set: ");
                    for (int val : results[i]) {
                        System.out.print(val + " ");
                    }
                    System.out.println("\nValid: " + hsp.isValidHittingSet(results[i], sets));
                } else {
                    System.out.println("No hitting set of size ≤ k found.");
                }
            
                System.out.println("Execution Time: " + times[i] + " ms");
                System.out.println("----------------------------------------\n");
            }
            
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
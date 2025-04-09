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
            if(element == 0) continue;

            // Get sets that remain after removing those that contain element
            int[][] reducedProblem = reduceSets(currentSets, element);
            
            // Recursive call on reduced problem (without element)
            int[] solution = algorithm1(reducedProblem, n, c, k - 1);
            
            if(solution != null){ // if solution is found
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

        // Reduce problem by the critical element
        int criticalElement = findCriticalElement(currentSets, n);
        
        // Get sets that remain after removing those that contain the critical element
        int[][] reducedProblem = reduceSets(currentSets, criticalElement);

        // Recursive call on reduced problem (without element)
        int[] solution = algorithm2(reducedProblem, n, c, k - 1);

        if(solution != null){ // if solution is found
            int[] hittingSet = new int[solution.length + 1];
            hittingSet[0] = criticalElement;
            // copy solution.length elements of solution starting from index=0, into hitting set, starting from its index=1
            System.arraycopy(solution, 0, hittingSet, 1, solution.length);
            return hittingSet;
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

            // Get sets that remain after removing those that contain element
            int[][] reducedProblem = reduceSets(currentSets, element);
            
            // Recursive call on reduced problem (without element)
            int[] solution = algorithm3(reducedProblem, n, c, k - 1);
            
            if(solution != null){ // if solution is found
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
     *   most critical element.
     */
    public int[] algorithm4(int[][] currentSets, int n, int c, int k){
        if(currentSets.length == 0) return new int[0];
        if(k == 0) return null;
    
        // Find set with least number of elements
        int setIndex = findSetWithLeastElements(currentSets, c);
        int[] pickedSet = currentSets[setIndex];
    
        // Calculate criticallity of each element [1..n]
        int[] criticallityAray = new int[n + 1];
        for (int i = 0; i < currentSets.length; i++) {
            boolean[] seen = new boolean[n + 1];
            for (int j = 0; j < currentSets[i].length; j++) {
                int el = currentSets[i][j];
                if (el == 0) continue;
                if (!seen[el]) {
                    criticallityAray[el]++;
                    seen[el] = true;
                }
            }
        }
    
        // Sort elements of pickedSet based on their criticallity
        int[] sortedPickedSet = new int[pickedSet.length];
        System.arraycopy(pickedSet, 0, sortedPickedSet, 0, pickedSet.length); // copy whole pickedSet into sortedPickedSet array

        for (int i = 0; i < sortedPickedSet.length - 1; i++) {
            for (int j = i + 1; j < sortedPickedSet.length; j++) {
                int a = sortedPickedSet[i];
                int b = sortedPickedSet[j];
                if (a == 0 || b == 0) continue;
    
                if (criticallityAray[b] > criticallityAray[a] || 
                   (criticallityAray[b] == criticallityAray[a] && b < a)) {
                    // swap
                    int temp = sortedPickedSet[i];
                    sortedPickedSet[i] = sortedPickedSet[j];
                    sortedPickedSet[j] = temp;
                }
            }
        }
    
        // Recursive call on each element based on criticallity (most critical first)
        for (int i = 0; i < sortedPickedSet.length; i++) {
            int element = sortedPickedSet[i];
            if (element == 0) continue;
    
            int[][] reduced = reduceSets(currentSets, element);
            int[] solution = algorithm4(reduced, n, c, k - 1);
    
            if (solution != null) {
                int[] hittingSet = new int[solution.length + 1];
                hittingSet[0] = element;
                System.arraycopy(solution, 0, hittingSet, 1, solution.length);
                return hittingSet;
            }
        }
    
        return null;
    }


    /* HELPER METHODS BELOW */

    /**
     * Helper funtion  that removes all sets that contain `element`
     * 
     * @param currentSets   // sets that we have now
     * @param element       // element to indicate which sets to be removes
     * @return              // new sets with those that contain `element` removed
     */
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

        // Create new array to represent sets, having removed sets that contain element
        int[][] reduced = new int[count][currentSets[0].length];
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
                for(int j = 0; j < currentSets[i].length; j++){
                    reduced[setIndex][j] = currentSets[i][j];
                }
                setIndex++;
            }
        }

        return reduced;
    }

    /**
     * Helper method that returns the critical element
     * Critical Element: Element that appears in mosts subsets B
     * @param currentSets
     * @return Critical Element
     */
    private int findCriticalElement(int[][] currentSets, int n) {
        int[] occurances = new int[n + 1]; // occurances[i]: number of sets that contain element i
    
        for (int i = 0; i < currentSets.length; i++) {
            boolean[] seenInThisSet = new boolean[n + 1];
            for (int j = 0; j < currentSets[i].length; j++) {
                int element = currentSets[i][j];
                if (element == 0) continue;
                if (!seenInThisSet[element]) {
                    occurances[element]++;
                    seenInThisSet[element] = true;
                }
            }
        }
    
        // Find element with highest count, breaking ties by smallest index
        int max = 0;
        int maxElement = 0;
        for (int i = 1; i < occurances.length; i++) {
            if (occurances[i] > max) {
                max = occurances[i];
                maxElement = i;
            }
        }
    
        return maxElement;
    }

    /**
     * Helper method that returns the set with least number of elements
     * @param currentSets
     * @return set index with least number of elements
     */
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
            if(count > minCount){
                minCount = count;
                minIndex = i;
            }
        }

        return minIndex;
    }

    /**
     * Helper method that reads contents of sets.dat and returns data read
     * @param filename
     * @return Data read for sets
     * @throws IOException
     */
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
            HittingSetProblem hsp = new HittingSetProblem();
            
            // Retrieve data read from file
            Object[] data = hsp.loadData("sets.dat");
            int n = (int)data[0];
            int m = (int)data[1];
            int c = (int)data[2];
            int k = (int)data[3];
            int[][] sets = (int[][])data[4];

            System.out.println("[DATA] Loaded Data:");
            System.out.println("n:" + n + " m:" + m + " c:" + c + " k:" + k);
    
            for(int i = 0; i < m; i++){
                System.out.print("B[" + i + "] = {");
                for(int j = 0; j < c; j++){
                    System.out.print(sets[i][j] + ", ");
                }
                System.out.println("}");
            }

            // Test Algorithm 1
            long start = System.nanoTime();
            int[] result1 = hsp.algorithm1(sets, n, c, k);
            long end = System.nanoTime();
    
            System.out.println("\nAlgorithm 1 result:");
            if (result1 != null) {
                for (int val : result1) {
                    System.out.print(val + " ");
                }
            } else {
                System.out.println("No hitting set of size ≤ k found.");
            }
            System.out.println("\nTime: " + (end - start)/1_000_000 + " ms");
            
            // Test Algorithm 2
            start = System.nanoTime();
            int[] result2 = hsp.algorithm2(sets, n, c, k);
            end = System.nanoTime();
    
            System.out.println("\nAlgorithm 2 result:");
            if (result2 != null) {
                for (int val : result2) {
                    System.out.print(val + " ");
                }
            } else {
                System.out.println("No hitting set of size ≤ k found.");
            }
            System.out.println("\nTime: " + (end - start)/1_000_000 + " ms");
            
            // Test Algorithm 3
            start = System.nanoTime();
            int[] result3 = hsp.algorithm3(sets, n, c, k);
            end = System.nanoTime();
    
            System.out.println("\nAlgorithm 3 result:");
            if (result3 != null) {
                for (int val : result3) {
                    System.out.print(val + " ");
                }
            } else {
                System.out.println("No hitting set of size ≤ k found.");
            }
            System.out.println("\nTime: " + (end - start)/1_000_000 + " ms");

            // Test Algorithm 4
            start = System.nanoTime();
            int[] result4 = hsp.algorithm4(sets, n, c, k);
            end = System.nanoTime();
    
            System.out.println("\nAlgorithm 4 result:");
            if (result4 != null) {
                for (int val : result4) {
                    System.out.print(val + " ");
                }
            } else {
                System.out.println("No hitting set of size ≤ k found.");
            }
            System.out.println("\nTime: " + (end - start)/1_000_000 + " ms");
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
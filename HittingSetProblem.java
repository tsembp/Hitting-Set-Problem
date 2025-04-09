import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HittingSetProblem {

    /**
     * n: size of universe set (A)
     * m: number of subsets (B)
     * c: max number of elements for each subset B
     * k: desired hitting set size (at most)
     */
    int n, m, c, k;
    boolean isTesting;
    int[][] sets;

    /**
     * 1st VARIANT
     * - Divide & Conquer Approach
     */
    public int[] algorithm1(int[][] currentSets, int k){
        if(currentSets.length == 0) return new int[0];
        if(k == 0) return null;

        // Select 1st set always
        int[] pickedSet = currentSets[0];

        // For each element of pickedSet, attempt to find result by removing it
        for(int i = 0; i < pickedSet.length; i++){
            int element = pickedSet[i];
            if(element == 0) continue;

            // Get sets that remain after removing those that contain element
            int[][] reducedProblem = reduceSets(currentSets, element);
            
            // Recursive call on reduced problem (without element)
            int[] solution = algorithm1(reducedProblem, k - 1);
            
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
    public void algorithm2(){
        
    }

    /**
     * 3rd VARIANT
     * - Similar to 1st variant
     * - Instead of randomly selecting subset for reducing the problem size, we select the subset with least 
     *   number of elements 
     * - If more than one subsets have same number of occurances: select the one with smallest index 
     */
    public void algorithm3(){
        
    }

    /**
     * 4th VARIANT
     * - Similar to 1st variant
     * - Select subset with least number of elements and the process of finding a hitting set begins with the
     *   most critical element.
     */
    public void algorithm4(){

    }

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

    public static void main(String[] args) {
        try {
            HittingSetProblem hsp = new HittingSetProblem();
            hsp.loadData("sets.dat");

            long start = System.nanoTime();
            int[] result = hsp.algorithm1(hsp.sets, hsp.k);
            long end = System.nanoTime();
    
            System.out.println("Algorithm 1 result:");
            if (result != null) {
                for (int val : result) {
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

    public void loadData(String filename) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String[] line1 = reader.readLine().trim().split("\\s+");
        n = Integer.parseInt(line1[0]);
        m = Integer.parseInt(line1[1]);
        c = Integer.parseInt(line1[2]);
        k = Integer.parseInt(line1[3]);

        sets = new int[m][c];
        for(int i = 0; i < m; i++){
            String line[] = reader.readLine().trim().split("\\s+");
            for(int j = 0; j < line.length; j++){
                sets[i][j] = Integer.parseInt(line[j]);
            }
            // if B set's length is < than c, it automatically gets filled with 0s
        }

        System.out.println("[DATA] Loaded Data:");
        System.out.println("n:" + n + " m:" + m + " c:" + c + " k:" + k);

        for(int i = 0; i < m; i++){
            System.out.print("B[" + i + "] = {");
            for(int j = 0; j < c; j++){
                System.out.print(sets[i][j] + ", ");
            }
            System.out.println("}");
        }

        reader.close();
    }

}
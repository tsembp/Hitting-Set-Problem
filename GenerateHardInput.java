import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GenerateHardInput {
    public static void main(String[] args) {
        int n = 200;  // Universe size (elements 1-200)
        int m = 1500; // Number of sets
        int c = 5;    // Elements per set
        int k = 20;   // Target hitting set size

        // Hidden hitting set (elements 1-20)
        List<Integer> hiddenSet = new ArrayList<>();
        for (int i = 1; i <= k; i++) hiddenSet.add(i);

        // Frequent noise elements (appear in 40% of sets but are NOT part of the solution)
        List<Integer> frequentNoise = Arrays.asList(21, 22, 23, 24, 25);

        try (FileWriter writer = new FileWriter("script-input-2.txt")) {
            writer.write(n + " " + m + " " + c + " " + k + "\n");

            Random rand = new Random();
            for (int i = 0; i < m; i++) {
                Set<Integer> set = new HashSet<>();

                // Add 1-2 elements from the hidden hitting set (required for solution)
                int hiddenCount = rand.nextInt(2) + 1; // 1 or 2
                Collections.shuffle(hiddenSet);
                for (int j = 0; j < hiddenCount && set.size() < c; j++) {
                    set.add(hiddenSet.get(j));
                }

                // Add frequent noise elements (appear in 40% of sets)
                while (set.size() < c) {
                    if (rand.nextDouble() < 0.4 && !frequentNoise.isEmpty()) {
                        int noise = frequentNoise.get(rand.nextInt(frequentNoise.size()));
                        set.add(noise);
                    } else {
                        // Add unique elements (rarely appear elsewhere)
                        int unique = 26 + rand.nextInt(n - 25); // 26-200
                        set.add(unique);
                    }
                }

                // Write the set (sorted for readability)
                List<Integer> sorted = new ArrayList<>(set);
                Collections.sort(sorted);
                for (int num : sorted) writer.write(num + " ");
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
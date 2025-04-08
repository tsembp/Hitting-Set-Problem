# 🧠 Hitting Set Problem – Algorithms & Complexity Lab Project

## 📚 About the Project

This repository contains the full implementation and experimental evaluation of four variations of the **Divide and Conquer** algorithm for solving the **Hitting Set Problem**, as part of the Algorithms & Complexity course lab.

A **Hitting Set** is a subset of elements that intersects with all subsets of a given collection. This problem has significant applications in computational complexity, databases, and AI search techniques.

## 🚀 Implemented Algorithms

This repo explores and compares the following four algorithmic variations:

1. **Basic Divide and Conquer**  
   - Picks a random element from a randomly selected subset and recurses.

2. **Critical Element Heuristic**  
   - Selects the element that appears in the most subsets (i.e. most "critical").  
   - Tiebreaker: element with the smallest index.

3. **Minimum-Size Subset Heuristic**  
   - Picks the subset with the fewest elements.  
   - Tiebreaker: subset with the smallest index.

4. **Combined Heuristic**  
   - Chooses the smallest subset, and then selects the most critical element within it.

Each algorithm is implemented in Java in its own method, and the program is structured to support both correctness verification (from file input) and randomized benchmarking (with synthetic data generation).

## 🧪 Experimental Evaluation

The experimental part of the project evaluates the performance and behavior of each algorithm under two scenarios:

- **Scenario 1 – Max Hitting Set Size Detection**  
  Runs each algorithm with increasing `k` values to find the largest hitting set each can compute within a 1-hour limit. Results are visualized in a line chart showing execution time vs. `k`.

- **Scenario 2 – Execution Time Distribution**  
  Benchmarks the algorithms on a specially generated case with a fixed `k`, to compare consistency and average execution time. Results are shown in a box-and-whisker plot.

All experiments were run on a personal machine with:
- OS: Windows 11 Home
- CPU: AMD Ryzen 7 8840HS @ 3.30 GHz  
- RAM: 16GB

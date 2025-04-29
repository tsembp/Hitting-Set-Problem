#!/usr/bin/env python3
import random
import math

# ─── TOUGH, REALISTIC INSTANCE PARAMETERS ─────────────────────────────────────
n            = 500       # universe size (elements 1..n)
m            = 1200      # number of subsets
c            = 12        # Java “row width”
k            = 20        # hidden hitting-set size
min_size     = 4         # allow sets as small as 4 elems
max_hidden   = 3         # include up to 3 of the true H in each B_i
output_file  = "script-6-input.txt"
# ────────────────────────────────────────────────────────────────────────────────

def generate_instance(n, m, c, k, min_size, max_hidden):
    # 1) pick a hidden hitting set H
    H = set(random.sample(range(1, n+1), k))
    H_list = list(H)  # convert to list for random.sample

    subsets = []
    for _ in range(m):
        # 2a) choose size s ∈ [min_size…c]
        s = random.randint(min_size, c)

        # 2b) pick r ∈ [1…max_hidden] hidden elements (but ≤ s)
        r = random.randint(1, min(max_hidden, s))
        hidden_in_B = random.sample(H_list, r)

        # 2c) fill the rest of B_i with non-H elements
        rest_count = s - r
        non_H = [x for x in range(1, n+1) if x not in H]
        rest = random.sample(non_H, rest_count)

        B = sorted(hidden_in_B + rest)

        subsets.append(B)

    return H, subsets

def write_instance(filename, n, m, c, k, subsets):
    with open(filename, "w") as f:
        f.write(f"{n} {m} {c} {k}\n")
        for B in subsets:
            f.write(" ".join(map(str, B)) + "\n")

if __name__ == "__main__":
    H, subsets = generate_instance(n, m, c, k, min_size, max_hidden)
    write_instance(output_file, n, m, c, k, subsets)

    print(f"Wrote {m} subsets (sizes ∈ [{min_size}…{c}], up to {max_hidden} hits each)")
    print(f"Hidden hitting set H (size {k}):")
    print(sorted(H))

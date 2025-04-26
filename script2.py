import random
import math
import os

n = 300
m = 1000
c = 4
k = 12

# 1. Plant a random hitting set H (choose higher-value elements to favor tie-break)
universe = list(range(1, n+1))
H = random.sample(universe, k)

# 2. Create a flat list of m*c slots and distribute elements as uniformly as possible
total_slots = m * c
base_count = total_slots // n
extra = total_slots % n

# Assign frequencies: first 'extra' elements get (base_count+1), rest get base_count
freqs = {}
for i, el in enumerate(universe):
    freqs[el] = base_count + (1 if i < extra else 0)

# Build flat list of elements according to freqs
flat = []
for el, cnt in freqs.items():
    flat.extend([el] * cnt)

# Shuffle and split into subsets
random.shuffle(flat)
subsets = [flat[i*c:(i+1)*c] for i in range(m)]

# 3. Ensure every subset contains at least one element from H.
#    If not, replace a random position with a random H element.
for idx, subset in enumerate(subsets):
    if not any(el in H for el in subset):
        pos = random.randrange(c)
        subsets[idx][pos] = random.choice(H)

# 4. Write to 'instance.txt'
# Ensure the /inputs directory exists
os.makedirs("inputs", exist_ok=True)

# Write to 'inputs/experiment2-input.txt'
with open("inputs/experiment2-input.txt", "w") as f:
    f.write(f"{n} {m} {c} {k}\n")
    for subset in subsets:
        f.write(" ".join(map(str, subset)) + "\n")

# 5. Echo planted hitting set
print("Planted hitting set (size k):", sorted(H))
print(f"Wrote 'instance.txt' with uniform distribution adversarial to heuristic.")

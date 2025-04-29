import random

# ————————————————————————— Configuration —————————————————————————
n = 300            # universe size
m = 11000          # total subsets (tuned so Alg 1 ≈5 min)
c = 6              # max subset size
k = 10             # hitting-set size
small_count = 10   # only 10 tiny sets, so Alg 3/4 breeze through them
seed = 42          # for reproducibility

random.seed(seed)

# 1) pick your hidden H of size k
H = random.sample(range(1, n+1), k)

# 2) build a little pool of non-H
others = [x for x in range(1, n+1) if x not in H]

# 3) hand-craft a few tiny sets (size=2) so Alg 3/4 always find them
small_sets = []
for x in random.sample(others, small_count):
    h = random.choice(H)
    s = [h, x]
    random.shuffle(s)
    small_sets.append(s)

# 4) the rest are “big” sets of size c drawn from a large non-H pool
big_sets = []
for _ in range(m - small_count):
    h = random.choice(H)
    pad = random.sample(others, c-1)
    s = [h] + pad
    random.shuffle(s)
    big_sets.append(s)

# 5) write it out in the format your Java code expects
with open("script-4-input.txt", "w") as f:
    f.write(f"{n} {m} {c} {k}\n")
    for s in small_sets + big_sets:
        f.write(" ".join(map(str, s)) + "\n")

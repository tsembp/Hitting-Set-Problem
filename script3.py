import random

def generate_sample_instance(filename="script-3-input.txt"):
    n = 50      # Number of elements
    m = 100     # Number of subsets
    c = 8       # Maximum subset size
    k = 20      # Size of the hitting set

    universe = list(range(1, n + 1))
    
    def get_biased_subset_size():
        if random.random() < 0.8:
            return random.randint(6, 8)
        else:
            return random.randint(3, 4)

    subsets = []
    for _ in range(m):
        subset_size = get_biased_subset_size()
        subset = random.sample(universe, subset_size)
        subsets.append(subset)

    with open(filename, "w") as f:
        f.write(f"{n} {m} {c} {k}\n")
        for subset in subsets:
            f.write(" ".join(map(str, subset)) + "\n")

    print(f"Generated sample instance: n={n}, m={m}, c={c}, k={k}, saved to {filename}")

if __name__ == "__main__":
    generate_sample_instance()

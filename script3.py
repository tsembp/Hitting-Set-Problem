import random

def generate_hard_instance(filename="script-3-input.txt"):
    n = 56      
    k = 8       
    c = 7       
    m = 15000   

    universe = list(range(1, n+1))

    # pick H ⊆ A of size k (the true hitting set)
    H = random.sample(universe, k)

    # let D = A \ H
    D = [x for x in universe if x not in H]
    assert len(D) == k * (c - 1)

    # build each subset so it has between 1 and c elements,
    # with exactly 1 from H and the rest from D
    subsets = []
    for _ in range(m):
        if random.random() < 0.75:
            size = c
        else:
            size = random.randint(1, c-1)
        hit  = random.choice(H)
        if size > 1:
            others = random.sample(D, size - 1)
        else:
            others = []
        s = [hit] + others
        random.shuffle(s)
        subsets.append(s)

    with open(filename, "w") as f:
        f.write(f"{n} {m} {c} {k}\n")
        for s in subsets:
            f.write(" ".join(map(str, s)) + "\n")

if __name__ == "__main__":
    generate_hard_instance()
    print("Generated with some subsets smaller than c.")

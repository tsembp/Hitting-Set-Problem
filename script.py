import random

def generate_hard_hitting_set_instance(n, m, s, k, seed=None):
    """
    Generates a Hitting Set instance with universe A = {1, 2, ..., n}.
    
    Args:
        n (int): Universe size (elements are 1, 2, ..., n).
        m (int): Number of subsets.
        s (int): Size of each subset.
        k (int): Size of the planted hitting set.
        seed (int): Optional seed for reproducibility.
    
    Returns:
        tuple: (planted_hitting_set, list_of_subsets)
    """
    if seed is not None:
        random.seed(seed)
    
    universe = list(range(1, n + 1))  # Elements are 1, 2, ..., n
    planted_hitting_set = random.sample(universe, k)
    non_hitting_elements = [x for x in universe if x not in planted_hitting_set]
    
    subsets = []
    for _ in range(m):
        # Ensure at least 1 element from the planted hitting set
        num_from_hitting = random.randint(1, min(s, k))
        hitting_elements = random.sample(planted_hitting_set, num_from_hitting)
        remaining_size = s - num_from_hitting
        
        # Fill the rest with random elements from the entire universe
        if remaining_size > 0:
            other_elements = random.choices(universe, k=remaining_size)
            subset = hitting_elements + other_elements
        else:
            subset = hitting_elements
        
        # Remove duplicates and shuffle
        subset = list(set(subset))
        random.shuffle(subset)
        subsets.append(subset)
    
    return planted_hitting_set, subsets

def save_instance_to_file(subsets, filename):
    with open(filename, 'w') as f:
        for subset in subsets:
            f.write(' '.join(map(str, subset)) + '\n')

# Example Usage
n = 100  # Universe = {1, 2, ..., 100}
m = 1000 # Number of subsets
s = 6    # Subset size
k = 20   # Target hitting set size

planted_set, subsets = generate_hard_hitting_set_instance(n, m, s, k, seed=42)
save_instance_to_file(subsets, "input.txt")

print(f"Planted hitting set (for verification): {sorted(planted_set)}")
print(f"First 3 subsets:")
for subset in subsets[:3]:
    print(subset)
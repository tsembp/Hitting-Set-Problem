import random

# === Configuration ===
n = 42  # Universe size
m = 500  # Number of subsets
min_size = 8  # Minimum subset size - increased to penalize small subset algorithms
max_size = 12  # Maximum subset size
seed = 42  # Random seed for reproducibility
output_file = "sets.dat"
max_k = 15  # Reduced maximum hitting set size to try

# Initialize random seed
random.seed(seed)

# Build universe
universe = list(range(1, n + 1))

# Create a specific hitting set of size     5
# Using a fixed set makes it easier to control the frequency distribution
hidden_hitting_set = random.sample(universe, 11)
hitting_set_size = len(hidden_hitting_set)   # == 11

print(f"Hidden hitting set (size {hitting_set_size}):", hidden_hitting_set)

# Generate the subsets
subsets = []

# Track element frequencies deliberately to help algorithm 2
element_frequencies = {e: 0 for e in range(1, n + 1)}

# Define deceptive elements (to create confusion for critical element evaluation)
deceptive_elements = [3, 10, 21, 24, 30]

# First, make the hitting set elements have high frequency
# This will make algorithm 2 perform better
for element in deceptive_elements:
    # Create many subsets with this element
    num_subsets = random.randint(40, 50)

    for _ in range(num_subsets):
        # Create medium to large subsets (not small) to penalize small subset algorithms
        size = random.randint(7, max_size)

        # Create a subset with this hitting element
        remaining_size = size - 1
        available_elements = [e for e in universe if e != element]
        other_elements = random.sample(available_elements, remaining_size)

        # Form the complete subset
        subset = [element] + other_elements
        random.shuffle(subset)

        subsets.append(subset)
        for e in subset:
            element_frequencies[e] += 1

for element in hidden_hitting_set:
    num_subsets = random.randint(5, 10)

# Create many small subsets that DON'T contain hitting set elements
# This creates "false leads" for algorithms 3 and 4
num_misleading = 20
for _ in range(num_misleading):
    # Small subsets to attract the small subset algorithms
    size = random.randint(min_size, 9)

    # Ensure no hitting set elements are used
    available = [e for e in universe if e not in hidden_hitting_set]
    subset = random.sample(available, size)

    subsets.append(subset)
    for e in subset:
        element_frequencies[e] += 1

# Add complexity to critical element evaluation by creating subsets with
# hitting elements mixed with deceptive elements
num_challenging = 20
for element in hidden_hitting_set:
    for _ in range(4):  # 4 challenging subsets per hitting element
        # Use medium size that won't be prioritized by small subset algorithms
        size = random.randint(min_size + 1, max_size)

        # Add the hitting element plus 2-3 deceptive elements
        subset_elements = [element]
        num_decoys = random.randint(2, 3)
        decoys = random.sample(
            deceptive_elements, min(num_decoys, len(deceptive_elements))
        )
        subset_elements.extend(decoys)

        # Fill the rest with random elements
        remaining_size = size - len(subset_elements)
        available = [e for e in universe if e not in subset_elements]
        other_elements = random.sample(available, remaining_size)
        subset_elements.extend(other_elements)

        random.shuffle(subset_elements)
        subsets.append(subset_elements)

        # Update frequencies
        for e in subset_elements:
            element_frequencies[e] += 1

# Create subsets with deceptive elements that have no hitting elements
# These will complicate frequency analysis without affecting small subset prioritization
for _ in range(20):  # Add 20 deceptive subsets
    # Use medium-large size to avoid being prioritized by small subset algorithms
    size = random.randint(min_size + 1, max_size)

    # Include 2-3 deceptive elements to boost their frequencies
    num_decoys = random.randint(2, 3)
    decoys = random.sample(deceptive_elements, min(num_decoys, len(deceptive_elements)))

    # Fill the rest with random elements (no hitting elements)
    available = [e for e in universe if e not in decoys and e not in hidden_hitting_set]
    other_elements = random.sample(available, size - len(decoys))

    subset_elements = decoys + other_elements
    random.shuffle(subset_elements)

    subsets.append(subset_elements)

    # Update frequencies
    for e in subset_elements:
        element_frequencies[e] += 1

# Fill the remaining subsets
while len(subsets) < m:
    size = random.randint(min_size, max_size)

    # 40% chance of including a hitting set element (adjusted from 70%)
    if random.random() < 0.40:
        # Choose a hitting set element
        hitting_element = random.choice(hidden_hitting_set)
        subset_elements = [hitting_element]

        # Add other random elements
        remaining_size = size - 1
        available_elements = [e for e in universe if e != hitting_element]
        other_elements = random.sample(available_elements, remaining_size)
        subset_elements.extend(other_elements)
    else:
        # Create a subset with no hitting set elements
        non_hitting_elements = [e for e in universe if e not in hidden_hitting_set]
        subset_elements = random.sample(non_hitting_elements, size)

    random.shuffle(subset_elements)
    subsets.append(subset_elements)

    # Update frequencies
    for e in subset_elements:
        element_frequencies[e] += 1

# Limit to exactly m subsets
subsets = subsets[:m]

# Shuffle the subsets
random.shuffle(subsets)

# Write sets.dat
with open(output_file, "w") as f:
    # Header: n m c maxK
    max_subset_size = max([len(s) for s in subsets])
    f.write(f"{n} {m} {max_subset_size} {max_k}\n")

    # Write subsets
    for subset in subsets:
        f.write(" ".join(map(str, subset)) + "\n")


# Verify our hitting set actually works
def is_hitting_set(subsets, candidate_set):
    """Check if the candidate set hits all subsets"""
    for subset in subsets:
        hit = False
        for element in candidate_set:
            if element in subset:
                hit = True
                break
        if not hit:
            return False
    return True


# Verify the hidden hitting set works
if is_hitting_set(subsets, hidden_hitting_set):
    print("\nVERIFIED: The hidden hitting set is valid")
else:
    print("\nERROR: Something went wrong - the hidden hitting set is not valid!")

# Calculate distribution of subset sizes
sizes = [len(subset) for subset in subsets]
size_counts = {}
for size in sizes:
    size_counts[size] = size_counts.get(size, 0) + 1

# Sort sizes for nice output
sorted_sizes = sorted(size_counts.keys())

# Summary
print(f"Generated {m} subsets in '{output_file}':")
for size in sorted_sizes:
    print(f"  {size_counts[size]} subsets of size {size}")

# Top 10 most frequent elements
top_elements = sorted(element_frequencies.items(), key=lambda x: x[1], reverse=True)[
    :10
]
print("\nTop 10 most frequent elements:")
for element, freq in top_elements:
    in_hitting_set = "✓" if element in hidden_hitting_set else "✗"
    print(f"  Element {element}: appears in {freq} subsets {in_hitting_set}")

# Top 5 most frequent deceptive elements (to show their competition with hitting elements)
deceptive_freqs = [(e, element_frequencies[e]) for e in deceptive_elements]
deceptive_freqs.sort(key=lambda x: x[1], reverse=True)
print(
    "\nDeceptive element frequencies (these add complexity to critical element evaluation):"
)
for element, freq in deceptive_freqs:
    print(f"  Element {element}: appears in {freq} subsets")

# Count subsets with hitting set elements
subsets_with_hitting_elements = 0
for subset in subsets:
    for element in hidden_hitting_set:
        if element in subset:
            subsets_with_hitting_elements += 1
            break

print(
    f"\nSubsets containing at least one hitting set element: {subsets_with_hitting_elements}/{m} "
    + f"({subsets_with_hitting_elements/m*100:.1f}%)"
)
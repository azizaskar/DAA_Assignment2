import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("results/buildheap.csv")
labels = {"n_inserts": "n × insert(x)", "buildHeap": "Floyd buildHeap"}

fig, axes = plt.subplots(1, 2, figsize=(12, 4.5))
for ax, col, unit in zip(axes, ["time_ms", "comparisons"], ["time (ms)", "comparisons (count)"]):
    for m, g in df.groupby("method"):
        ax.plot(g.n, g[col], marker="o", label=labels.get(m, m))
    ax.set_xscale("log")
    ax.set_yscale("log")
    ax.set_xlabel("n (elements)")
    ax.set_ylabel(unit)
    ax.set_title(f"Bonus B: {col} vs n")
    ax.legend()
    ax.grid(True, alpha=0.3)

fig.savefig("results/plots/buildheap.png", dpi=150, bbox_inches="tight")
print("saved")
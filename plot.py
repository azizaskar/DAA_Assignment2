import os
import pandas as pd
import matplotlib.pyplot as plt

os.makedirs("results/plots", exist_ok=True)
df = pd.read_csv("results/results.csv")

for wl, g in df.groupby("workload"):
    # 1) Time vs n
    fig, ax = plt.subplots(figsize=(7, 4.5))
    for (s, v), h in g.groupby(["structure", "variant"]):
        label = s if v == "-" else f"{s} ({v})"
        ax.plot(h.n, h.time_ms, marker="o", label=label)
    ax.set_xscale("log"); ax.set_yscale("log")
    ax.set_xlabel("n (elements)"); ax.set_ylabel("time (ms)")
    ax.set_title(f"{wl}: time vs n"); ax.legend(); ax.grid(True, alpha=0.3)
    fig.savefig(f"results/plots/{wl}_time.png", dpi=150, bbox_inches="tight")
    plt.close(fig)

    # 2) Steps / moves / comparisons vs n
    fig, axes = plt.subplots(1, 3, figsize=(15, 4.5))
    for ax, col in zip(axes, ["steps", "moves", "comparisons"]):
        for (s, v), h in g.groupby(["structure", "variant"]):
            label = s if v == "-" else f"{s} ({v})"
            ax.plot(h.n, h[col], marker="o", label=label)
        ax.set_xscale("log"); ax.set_yscale("symlog")
        ax.set_xlabel("n (elements)"); ax.set_ylabel(f"{col} (count)")
        ax.set_title(f"{wl}: {col} vs n"); ax.legend(); ax.grid(True, alpha=0.3)
    fig.savefig(f"results/plots/{wl}_ops.png", dpi=150, bbox_inches="tight")
    plt.close(fig)
print("plots saved")
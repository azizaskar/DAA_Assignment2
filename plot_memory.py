import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("results/memory.csv")
fig, ax = plt.subplots(figsize=(7, 4.5))
for s, g in df.groupby("structure"):
    ax.plot(g.n, g.mb, marker="o", label=s)
ax.set_xscale("log"); ax.set_yscale("log")
ax.set_xlabel("n (elements)"); ax.set_ylabel("memory (MB)")
ax.set_title("Memory footprint vs n"); ax.legend(); ax.grid(True, alpha=0.3)
fig.savefig("results/plots/memory.png", dpi=150, bbox_inches="tight")
print("saved")
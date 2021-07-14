import pandas as pd
import matplotlib.pyplot as plt

import git_analysis as git_data

for col in git_data.df.columns:
    if col != "Author":
        plt.pie(git_data.df[col], labels=git_data.df["Author"],autopct="%.2f")
        plt.title(col)
        plt.show()
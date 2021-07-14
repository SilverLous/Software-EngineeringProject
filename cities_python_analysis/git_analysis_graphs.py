import pandas as pd
import matplotlib.pyplot as plt

import git_analysis as git_data

data = git_data.df.drop(3)
for col in data.columns:
    if col != "Author":
        plt.pie(data[col], labels=data["Author"], autopct="%.2f")
        plt.title(col)
        plt.show()

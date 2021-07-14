import pandas as pd
import matplotlib.pyplot as plt

import git_analysis as git_data

data_surv = git_data.df_surv.drop(3)
data_ins = git_data.df_ins.drop(3)
length = len(data_surv.columns)
for col in data_surv.columns:
    if col != "Author":
        plt.pie(data_surv[col], labels=data_surv["Author"], autopct="%.2f")
        plt.title(col)
        plt.show()
plt.bar(data_surv["Author"], data_surv["loc"]/data_surv["coms"])
plt.title("Surviving lines per commit")
plt.xlabel("Author")
plt.ylabel("Commits")
plt.show()

plt.bar(data_surv["Author"], data_surv["loc"]/data_ins["loc"])
plt.title("Prozent überlebender Lines")
plt.xlabel("Author")
plt.ylabel("Commits")
plt.show()

#import gitfame
import pandas as pd

#print(gitfame.main(["--excl=citites.json,worldcities.csv", "-wt", "../"]))

# git-fame --excl='cities_python_analysis/*|dokumentation/*|src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt|src/main/kotlin/de/hbrs/team7/se1_starter_repo/ParkhausServiceSession.kt'

"""
Total commits: 251
Total ctimes: 1457
Total files: 96
Total loc: 7132
| Author         |   loc |   coms |   fils |  distribution   |
|:---------------|------:|-------:|-------:|:----------------|
| Thomas Gerlach |  5250 |     99 |     45 | 73.6/39.4/46.9  |
| Lukas Gerlach  |  1139 |     79 |     17 | 16.0/31.5/17.7  |
| Alexander      |   679 |     65 |     26 | 9.5/25.9/27.1   |
| Eileen Hanz    |    64 |      8 |      8 | 0.9/ 3.2/ 8.3   |
"""

git_data = {
  "Author": ["Thomas Gerlach", "Lukas Gerlach", "Alexander", "Eileen Hanz"],
  "loc": [5250, 1139, 679, 64],
  "coms": [99, 79, 65, 8],
  "fils": [45, 17, 26, 8]
}

df = pd.DataFrame(git_data)
df.set_index(["Author"])


# https://vm-2d21.inf.h-brs.de/mk_se1_ss21_Team_7/mk_se1_ss21_Team_7/-/commit/ac034d47020d3addde002be68ee34d3ea615e363
# https://vm-2d21.inf.h-brs.de/mk_se1_ss21_Team_7/mk_se1_ss21_Team_7/-/commit/8778058029002c89c727c6d1f0d9aa70cc1715e1

städte_lines = 1610

df.loc[0, "loc"] = df.loc[0, "loc"] - (städte_lines * 2)


# https://vm-2d21.inf.h-brs.de/mk_se1_ss21_Team_7/mk_se1_ss21_Team_7/-/commit/321069e9050213f11e8c5aca1edf05bf147ed605

svg_code = 887

df.loc[0, "loc"] = df.loc[0, "loc"] - svg_code

print(df)

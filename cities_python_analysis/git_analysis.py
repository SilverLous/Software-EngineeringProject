#import gitfame
import pandas as pd

#print(gitfame.main(["--excl=citites.json,worldcities.csv", "-wt", "../"]))

# git-fame --excl='cities_python_analysis/*|dokumentation/*|src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt|src/main/kotlin/de/hbrs/team7/se1_starter_repo/ParkhausServiceSession.kt'

# better one

# git-fame --since=2021-04-30 --incl ".kt|.java|.jsp" --excl='src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt'

"""
Total commits: 319
Total ctimes: 1699
Total files: 85
Total loc: 4908
| Author         |   loc |   coms |   fils |  distribution   |
|:---------------|------:|-------:|-------:|:----------------|
| Thomas Gerlach |  2708 |    120 |     34 | 55.2/37.6/40.0  |
| Lukas Gerlach  |  1387 |     97 |     22 | 28.3/30.4/25.9  |
| Alexander      |   760 |     94 |     24 | 15.5/29.5/28.2  |
| Eileen Hanz    |    53 |      8 |      5 | 1.1/ 2.5/ 5.9   |
"""

git_data = {
  "Author": ["Thomas Gerlach", "Lukas Gerlach", "Alexander", "Eileen Hanz"],
  "loc": [2708, 1387, 760, 53],
  "coms": [120, 97, 94, 8],
  "fils": [45, 17, 26, 8]
}

df = pd.DataFrame(git_data)
df.set_index(["Author"])


# https://vm-2d21.inf.h-brs.de/mk_se1_ss21_Team_7/mk_se1_ss21_Team_7/-/commit/ac034d47020d3addde002be68ee34d3ea615e363
# https://vm-2d21.inf.h-brs.de/mk_se1_ss21_Team_7/mk_se1_ss21_Team_7/-/commit/8778058029002c89c727c6d1f0d9aa70cc1715e1

#städte_lines = 1610

#df.loc[0, "loc"] = df.loc[0, "loc"] - (städte_lines * 1)


# https://vm-2d21.inf.h-brs.de/mk_se1_ss21_Team_7/mk_se1_ss21_Team_7/-/commit/321069e9050213f11e8c5aca1edf05bf147ed605

svg_code = 887

df.loc[0, "loc"] = df.loc[0, "loc"] - svg_code

# dotsvg_code = 2158

# df.loc[0, "loc"] = df.loc[0, "loc"] - dotsvg_code

df["codedist"] = df["loc"] / df["loc"].sum() * 100

df["commitdist"] = df["coms"] / df["coms"].sum() * 100

print(df)

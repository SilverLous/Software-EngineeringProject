#import gitfame
import pandas as pd

#print(gitfame.main(["--excl=citites.json,worldcities.csv", "-wt", "../"]))

# git-fame --excl='cities_python_analysis/*|dokumentation/*|src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt|src/main/kotlin/de/hbrs/team7/se1_starter_repo/ParkhausServiceSession.kt'

# better one


# git-fame --since=2021-04-30 --incl ".kt|.java|.jsp" --excl='src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt'

"""
Total commits: 373
Total ctimes: 1993
Total files: 91
Total loc: 5272
| Author         |   loc |   coms |   fils |  distribution   |
|:---------------|------:|-------:|-------:|:----------------|
| Thomas Gerlach |  2688 |    139 |     37 | 51.0/37.3/40.7  |
| Lukas Gerlach  |  1666 |    102 |     27 | 31.6/27.3/29.7  |
| Alexander      |   905 |    124 |     24 | 17.2/33.2/26.4  |
| Eileen Hanz    |    13 |      8 |      3 | 0.2/ 2.1/ 3.3   |
"""

git_surviving_data = {
  "Author": ["Thomas Gerlach", "Lukas Gerlach", "Alexander", "Eileen Hanz"],
  "loc": [2688, 1666, 905, 13],
  "coms": [139, 102, 124, 8],
  "fils": [37, 27, 24, 3]
}


# git-fame --loc=ins --excl='src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt|cities_python_analysis|.*'

"""
Total commits: 376
Total ctimes: 986
Total files: 285
Total loc: 46464
| Author         |   loc |   coms |   fils |  distribution   |
|:---------------|------:|-------:|-------:|:----------------|
| Thomas Gerlach | 39084 |    142 |    122 | 84.1/37.8/42.8  |
| Lukas Gerlach  |  4389 |    102 |     75 | 9.4/27.1/26.3   |
| Alexander      |  2755 |    124 |     71 | 5.9/33.0/24.9   |
| Eileen Hanz    |   236 |      8 |     17 | 0.5/ 2.1/ 6.0   |
"""

git_ins_data = {
  "Author": ["Thomas Gerlach", "Lukas Gerlach", "Alexander", "Eileen Hanz"],
  "loc": [39084, 4389, 2755, 236],
  "coms": [142, 102, 124, 8],
  "fils": [122, 75, 71, 17]
}

df_surv = pd.DataFrame(git_surviving_data)
df_surv.set_index(["Author"])

df_ins = pd.DataFrame(git_ins_data)
df_ins.set_index(["Author"])

# https://vm-2d21.inf.h-brs.de/mk_se1_ss21_Team_7/mk_se1_ss21_Team_7/-/commit/ac034d47020d3addde002be68ee34d3ea615e363
# https://vm-2d21.inf.h-brs.de/mk_se1_ss21_Team_7/mk_se1_ss21_Team_7/-/commit/8778058029002c89c727c6d1f0d9aa70cc1715e1

städte_lines = 1610

df_ins.loc[0, "loc"] = df_ins.loc[0, "loc"] - (städte_lines * 2)


# https://vm-2d21.inf.h-brs.de/mk_se1_ss21_Team_7/mk_se1_ss21_Team_7/-/commit/321069e9050213f11e8c5aca1edf05bf147ed605

svg_code = 887

df_surv.loc[0, "loc"] = df_surv.loc[0, "loc"] - svg_code

# copy is two insertions and one deletion
df_ins.loc[0, "loc"] = df_ins.loc[0, "loc"] - (svg_code * 2)



dotsvg_code = 2158

df_ins.loc[0, "loc"] = df_ins.loc[0, "loc"] - dotsvg_code


worldcities_csv = 26570

df_ins.loc[0, "loc"] = df_ins.loc[0, "loc"] - worldcities_csv


df_surv["codedist"] = df_surv["loc"] / df_surv["loc"].sum() * 100

df_surv["commitdist"] = df_surv["coms"] / df_surv["coms"].sum() * 100


df_ins["codedist"] = df_ins["loc"] / df_ins["loc"].sum() * 100

df_ins["commitdist"] = df_ins["coms"] / df_ins["coms"].sum() * 100

print(df_surv)
print(df_ins)
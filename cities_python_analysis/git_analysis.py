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

git_surviving_data = {
  "Author": ["Thomas Gerlach", "Lukas Gerlach", "Alexander", "Eileen Hanz"],
  "loc": [2800, 1368, 935, 13],
  "coms": [123, 98, 114, 8],
  "fils": [34, 20, 24, 3]
}


# git-fame --loc=ins --excl='src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt|cities_python_analysis|.*'

"""
Total commits: 346
Total ctimes: 895
Total files: 261
Total loc: 45366
| Author         |   loc |   coms |   fils |  distribution   |
|:---------------|------:|-------:|-------:|:----------------|
| Thomas Gerlach | 38784 |    126 |    115 | 85.5/36.4/44.1  |
| Lukas Gerlach  |  3831 |     98 |     64 | 8.4/28.3/24.5   |
| Alexander      |  2515 |    114 |     65 | 5.5/32.9/24.9   |
| Eileen Hanz    |   236 |      8 |     17 | 0.5/ 2.3/ 6.5   |
"""

git_ins_data = {
  "Author": ["Thomas Gerlach", "Lukas Gerlach", "Alexander", "Eileen Hanz"],
  "loc": [38784, 3831, 2515, 236],
  "coms": [126, 98, 114, 8],
  "fils": [115, 64, 65, 17]
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
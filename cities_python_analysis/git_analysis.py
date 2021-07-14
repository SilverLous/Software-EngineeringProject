#import gitfame
import pandas as pd

#print(gitfame.main(["--excl=citites.json,worldcities.csv", "-wt", "../"]))

# git-fame --excl='cities_python_analysis/*|dokumentation/*|src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt|src/main/kotlin/de/hbrs/team7/se1_starter_repo/ParkhausServiceSession.kt'

# better one


# git-fame --since=2021-04-30 --incl ".kt|.java|.jsp" --excl='src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt'

"""
Total commits: 362
Total ctimes: 1942
Total files: 90
Total loc: 5210
| Author         |   loc |   coms |   fils |  distribution   |
|:---------------|------:|-------:|-------:|:----------------|
| Thomas Gerlach |  2640 |    133 |     36 | 50.7/36.7/40.0  |
| Lukas Gerlach  |  1661 |    101 |     27 | 31.9/27.9/30.0  |
| Alexander      |   896 |    120 |     24 | 17.2/33.1/26.7  |
| Eileen Hanz    |    13 |      8 |      3 | 0.2/ 2.2/ 3.3   |
"""

git_surviving_data = {
  "Author": ["Thomas Gerlach", "Lukas Gerlach", "Alexander", "Eileen Hanz"],
  "loc": [2640, 1661, 896, 13],
  "coms": [133, 101, 120, 8],
  "fils": [36, 27, 24, 3]
}


# git-fame --loc=ins --excl='src/main/kotlin/de/hbrs/team7/se1_starter_repo/services/ParkhausServiceGlobal.kt|cities_python_analysis|.*'

"""
Total commits: 365
Total ctimes: 954
Total files: 279
Total loc: 46260
| Author         |   loc |   coms |   fils |  distribution   |
|:---------------|------:|-------:|-------:|:----------------|
| Thomas Gerlach | 38970 |    136 |    119 | 84.2/37.3/42.7  |
| Lukas Gerlach  |  4369 |    101 |     74 | 9.4/27.7/26.5   |
| Alexander      |  2685 |    120 |     69 | 5.8/32.9/24.7   |
| Eileen Hanz    |   236 |      8 |     17 | 0.5/ 2.2/ 6.1   |
"""

git_ins_data = {
  "Author": ["Thomas Gerlach", "Lukas Gerlach", "Alexander", "Eileen Hanz"],
  "loc": [38970, 4369, 2685, 236],
  "coms": [136, 101, 120, 8],
  "fils": [119, 74, 69, 17]
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
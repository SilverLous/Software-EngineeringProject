import json

import numpy as np
import pandas as pd
from matplotlib import pyplot as plt
from scipy.cluster.vq import kmeans2
from sklearn.cluster import KMeans

cities_df = pd.read_csv("worldcities.csv")
de_df = cities_df[cities_df.iso2 == "DE"]

kmeans = KMeans(init="random", n_clusters=5)

# df['label'] = kmeans.fit_predict(de_df[['mse']])

centroid, label = kmeans2(de_df.population.values, 6, minit='points', iter=10)

counts = np.bincount(label)
print(centroid)
print(counts)

sorted_centroids = sorted(range(len(centroid)), key=lambda k: centroid[k])

de_df["labels"] = label

json_df = de_df.copy()
json_df["price_tag"] = list(map(lambda l: sorted_centroids.index(l), de_df["labels"]))
json_df = json_df[["city", "lat", "lng", "admin_name", "population", "id", "price_tag"]]
json_df.rename(columns={'city': 'ct', 'admin_name': 'an',
                        'population': 'po', 'price_tag': 'pt'}, inplace=True)

result = json_df.to_json(orient="records")
parsed = json.loads(result)

print(set(de_df.admin_name.values))

# with open("cities.json", "w", encoding='utf-8') as outfile:
    # json.dump(parsed, outfile, separators=(',', ':'), ensure_ascii=False)

plt.figure(figsize=(20, 4))

cluster = 0
for l in counts:
    points = de_df[label == sorted_centroids[cluster]].population.values
    plt.plot(points, np.zeros_like(points) + 1, 'o', alpha=0.5,
             label='cluster ' + str(cluster + 1))
    cluster += 1

plt.plot(centroid[:], np.zeros_like(centroid[:]), 'k*', label='centroids')

plt.legend(shadow=True)

plt.show()
# https://stackoverflow.com/questions/50113595/how-to-get-k-means-cluster-for-1d-data

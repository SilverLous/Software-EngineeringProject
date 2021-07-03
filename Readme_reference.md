Homelab Monitoring
======
Inhaltsverzeichnis
======

[[_TOC_]]

# Ziel/Idee
- Anwenden des Wissen über Prometheus InfluxDB und Grafana
- Überwachung, ob das Docker-Cluster normal läuft
  - Überwachung der Aufrufe auf die Nextcloud (mögliche brute force Angriffe?)
- Herausfinden, wie viel die NAS an Schreiblast hat
  - Überwachung der verbauten Festplatten (Schreibfehler/Temperatur)
- Monitoring der Wetterdaten und Umweltverschmutzung
- Überwachung der Fritzbox zum Prüfen der Netzwerkauslastung

# Voraussetzungen
- Docker
- Fritzbox
- HS110 Steckdose

Dieses Projekt ist Modular aufgebaut, fehlende Komponenten könnte man im Dashboard einfach löschen.

# Quickstart

Docker-Compose Dateien: [projekt/monitoring-ws20-docker-configs](./monitoring-ws20-docker-configs)

Grafana JSON: [projekt/Grafana_Boards](./Grafana_Boards)


# Umsetzung

Da ein Teil dieses Projektes einen Docker-Swarm Cluster überwacht, sind alle Compose Files für Docker-Swarm geschrieben. Diese kann man übersetzen, in dem man die deploy Konfiguration löscht und alle Traefik Konfigurationen von deploy.labels in container.labels verschiebt.

## Grafana Installation
<br>

Docker-Compose File:
[projekt/monitoring-ws20-docker-configs/grafana/docker-compose.yml](./monitoring-ws20-docker-configs/grafana/docker-compose.yml)



## Influx Projekte
<br>


### Wetter

![Wetter Überblick](./präsentation/Wetter-Überblick_light.png?raw=true "CPU Usermode Vergleich")

Telegraf Konfiguration:
[projekt/monitoring-ws20-docker-configs/telegraf/config/telegraf.d/inputs.http.conf](./monitoring-ws20-docker-configs/telegraf/config/telegraf.d/inputs.http.conf)

Grafana Dashboard:
[projekt/Grafana_Boards/Main Dashboard-1610363334874.json](./Grafana_Boards/Main Dashboard-1610363334874.json)

---
### Unraid/NAS 

![Unraid Überblick](./präsentation/Unraid-Überblick.png?raw=true)

Vorraussetzung: SMART Tools Package "smartmontools" (via apt in Docker Container) installiert.
z.B. über CLI: 
``` bash
sh -c "apt update && apt install -y --install-recommends=no smartmontools; exec telegraf"
```

Verwendete Telegraf Konfiguration:
[projekt/Additionale Konfigurationen/unraid_standalone_telegraf.conf](./Additionale Konfigurationen/unraid_standalone_telegraf.conf)

Grafana Dashboard:
[projekt/Grafana_Boards/Main Dashboard-1610363334874.json](./Grafana_Boards/Main Dashboard-1610363334874.json)

---
### HS110


![Stromverbrauch Überblick](./präsentation/stromverbrauch-Überblick.png?raw=true)

Vorraussetzung: Besitz einer TP-Link HS110 Steckdose: 
[Preisvergleich](https://www.idealo.de/preisvergleich/OffersOfProduct/4943093_-wlan-smart-plug-hs110-tp-link.html)

Scrapper Compose File (Momentan nur eine Steckdose pro Container):
[projekt/monitoring-ws20-docker-configs/hs110-influx-scrapper/docker-compose.yml](./monitoring-ws20-docker-configs/hs110-influx-scrapper/docker-compose.yml)

Für eigene Anpassungen der Projektordner:
[projekt/strommesser/go](.projekt/strommesser/go)

Grafana Dashboard:
[projekt/Grafana_Boards/Main Dashboard-1610363334874.json](./Grafana_Boards/Main Dashboard-1610363334874.json)

---
### FritzBox

![Fritzbox Überblick](./präsentation/Fritzbox-überblick.png?raw=true)


Der FritzBox Scrapper benötigt das tool collectD, dieses wird mit einem Docker Container verwaltet.
Zur Konfiguration nutzt man die im Ordner beigefügte [collectd.conf](.//monitoring-ws20-docker-configs/collectD-FritzBox/collectd.conf) Datei


Compose File:
[projekt/monitoring-ws20-docker-configs/collectD-FritzBox/docker-compose.yml](./monitoring-ws20-docker-configs/collectD-FritzBox/docker-compose.yml)


Telegraf Konfiguration:
[projekt/monitoring-ws20-docker-configs/telegraf/config/telegraf.d/inputs.socket_listener.conf](./monitoring-ws20-docker-configs/telegraf/config/telegraf.d/inputs.socket_listener.conf)


## Prometheus Projekte
<br>

Komplette Prometheus Konfiguration  [projekt/monitoring-ws20-docker-configs/prometheus/prometheus.yml](./monitoring-ws20-docker-configs/prometheus/prometheus.yml)

(auszug) : 
``` yml 
  - job_name: 'node-exporter'
    dns_sd_configs:
    - names:
      - 'tasks.node-exporter'
      type: 'A'
      port: 9100
```
Diese Konfiguration verwendet das Docker eigene DNS System. Dies hat den Vorteil, dass es mehrere Deployments auf einmal ansprechen kann.

---
### Traefik

![Traefik Überblick](./präsentation/Traefik-Überblick.png?raw=true) 

Installationsanleitung: https://medium.com/@containeroo/traefik-2-0-docker-a-simple-step-by-step-guide-e0be0c17cfa5


Zusatzkonfiguration der traefik.yml
``` yml 
metrics:
  prometheus: {}
```

Relevanter Auszug aus [prometheus.yml](./monitoring-ws20-docker-configs/prometheus/prometheus.yml)

``` yml
  - job_name: 'traefik'
    dns_sd_configs:
    - names:
      - 'tasks.traefik'
      type: 'A'
      port: 8080
```

---
### Docker-Swarm

![Docker-Swarm Überblick](./präsentation/Docker-Swarm-Überblick.png?raw=true) 

Verwendung von Node Exporter und cAdvisor auf allen Nodes für die Überwachung

Node Exporter
[projekt/monitoring-ws20-docker-configs/node-exporter/docker-compose.yml](./monitoring-ws20-docker-configs/node-exporter/docker-compose.yml)

Relevanter Auszug aus [prometheus.yml](./monitoring-ws20-docker-configs/prometheus/prometheus.yml)

``` yml
  - job_name: 'node-exporter'
    dns_sd_configs:
    - names:
      - 'tasks.node-exporter'
      type: 'A'
      port: 9100

```


cAdvisor
[projekt/monitoring-ws20-docker-configs/cAdvisor/docker-compose.yml](./monitoring-ws20-docker-configs/cAdvisor/docker-compose.yml)


Relevanter Auszug aus [prometheus.yml](./monitoring-ws20-docker-configs/prometheus/prometheus.yml)
``` yml
  - job_name: 'cadvisor'
    dns_sd_configs:
    - names:
      - 'tasks.cadvisor-amd64'
      type: 'A'
      port: 8080
    - names:
      - 'tasks.cadvisor-arm'
      type: 'A'
      port: 8080
```


# Troubleshooting
- Grafana erzwingt eine nicht benötigte Quelle
  - Im JSON Code den nicht benötigen "__inputs" index löschen


<%@ page import="jakarta.inject.Inject" %>
<%@ page import="de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
    <meta name="author" content="Team89 (C) 2021">
    <meta name="description" content="Software Engineering 1 (SE1)">
    <meta name="license" content="The MIT License (MIT)">
    <meta name="theme-color" content="#239BD1"/>
    <meta property="og:title" content="Software Engineering 1 Project(SE1)">
    <meta property="og:description" content="Bachelor Course Software Engineering 1 (SE1), Hochschule Bonn-Rhein-Sieg.">
    <link rel="shortcut icon" href="https://kaul.inf.h-brs.de/favicon.ico" />
    <title>Tomcat Parkhaus</title>
    <script src="https://kaul.inf.h-brs.de/ccmjs/mkaul-components/parkhaus/versions/ccm.parkhaus-10.5.8.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/vue@2/dist/vue.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>




    <style>
        * {
            font-family: sans-serif, Helvetica, Arial;
        }
        .grey-background {
            background-color: lightgrey;
        }
        .center {
            text-align: center;
        }
        .box {
            border: thin solid black;
            margin: 0.5rem 0;
            padding: 1rem;
        }
        .lightblue {
            background-color: #d0ebf6;
        }
        .white {
            background-color: white;
        }
        .lightyellow {
            background-color: lightgoldenrodyellow;
        }
        .lightgreen {
            background-color: #ccfdcc;
        }
    </style>
</head>
<body>
<%!
    @Inject
    private ParkhausServiceSession parkhausServiceSession;
%>
<div class="box center grey-background">
    <h1>Parkhaus Team 7 in ${parkhausServiceSession.parkhaus.stadtname}, ${parkhausServiceSession.parkhaus.getUebersetztesBundesland()}, Preisklasse: ${parkhausServiceSession.parkhaus.getPreisklasse()}</h1>
</div>
<div class="box center lightgreen">
    <h2> <span style="color: navy; ">Unsere weiteren Standorte: </span></h2>
    <h1> ${parkhausServiceSession.zeigeHTMLParkhausListe()}</h1>
</div>
<div class="box lightblue">
    <h2><a href="kasse.jsp">Kasse</a></h2>
    <h2><a href="manager.jsp">Manager</a></h2>
</div>
    <div class="box lightyellow">
        <div class="row">
            <div class="col">
                <h1>Parkhaus Etage 1</h1>
                <ccm-parkhaus-10-5-8 server_url="./level1-servlet"
                                     hide_table="false"
                                     simulation.delay="6"
                                     simulation_speed = "1"
                                     name="Etage1"
                                     license_max="15"
                                     client_categories='["any","Family"]'
                                     vehicle_types='["PKW","SUV", "PICKUP", "ZWEIRAD", "TRIKE", "QUAD"]'
                                     price_factor='{"SUV":2,"Family":0.5}'
                                     json_format="true"
                                     max="11"
                                     extra_buttons='["Average","Sum","Total Users","Cars",{"extra_class":"undo start","extra_inner":"Undo","extra_popup_title":"Undo and Restart"},{"extra_class":"redo start","extra_inner":"Redo","extra_popup_title":"Redo and Restart"}]'
                                     extra_charts='["EinnahmenUeberAutotyp","TagesEinnahmen","WochenEinnahmen"]'>
                </ccm-parkhaus-10-5-8>
            </div>
            <div class="col-md-auto">
                <div class="white" id = "preiseebeneeins">
                </div>
            </div>
        </div>
    </div>
</div>
<div class="box lightgreen">
    <h1>Parkhaus Etage 2</h1>
    <ccm-parkhaus-10-5-8 server_url="./level2-servlet"
                         name="Etage2"
                         license_max="14"
                         simulation.delay="6"
                         simulation_speed = "1"
                         client_categories='["any","Business"]'
                         vehicle_types='["PKW","SUV","UNSICHTBARES_BOOTMOBIL"]'
                         price_factor='{"SUV":2,"Business":1}'
                         random_start="2"
                         json_format="true"
                         debug="true"
                         max="12"
                         extra_buttons='["Average","Sum"]'>
    </ccm-parkhaus-10-5-8>

</div>



<div id="main">
    <h1> Vue.JS API Call Example</h1>
    <div v-if="loaded">
        <p>{{plotData}}</p>
    </div>
    <p v-else="loaded">Your Plot is being generated</p>

</div>



<div class="click-map" onClick="style.pointerEvents='none'"></div>
<iframe id="googleMaps" title="Google Maps"
        src="https://maps.google.com/maps?q=${parkhausServiceSession.parkhaus.lat},${parkhausServiceSession.parkhaus.lng}+(Ihr%20Parkhaus%20in%20${parkhausServiceSession.parkhaus.stadtname})&z=15&output=embed"

        width="100%" height="500" frameborder="0" style="border:0" scrolling="no"
        allowfullscreen></iframe>
</body>

<script>
    function wechsleStadt(elmnt , stadID ) {

        axios.get('/team7Parkhaus/level1-servlet?cmd=wechsleParkhaus&stadt=' + stadID)
            .then(function (response) {
                // alert(response.body)
                location.reload(true);
            })
    }

    function ladePreistabelle() {

        axios.get('/team7Parkhaus/level1-servlet?cmd=preistabelle')
            .then(function (response) {

                    var preisdiv = document.getElementById('preiseebeneeins')
                    var dataDTO = response.data;
                    console.log(dataDTO);
                    var tabelle = "<table class=\"table table-bordered table-striped\" caption=\"Eine Liste der Preise für dieses Parkhaus\">";
                    tabelle += "<thead> <tr> ";

                tabelle += "<th scope=\"col\">Fahrzeugtyp</th>";
                tabelle += "<th scope=\"col\">Preis in €</th>";

                    tabelle += "</tr> </thead> <tbody >";
                    for (let i = 0; i < dataDTO.fahrzeugKlassen.length; i++) {
                        tabelle += "<tr> <td>" + dataDTO.fahrzeugKlassen[i] + "</td>";
                        tabelle += "<td>" + Math.round(dataDTO.preise[i] * 100) / 100 + "</td> </tr>";
                    }
                    tabelle += "<td colspan=\"2\">Basispreis: " + dataDTO.festpreisString + "</td>";
                    tabelle += "</tbody>";
                    preisdiv.innerHTML = tabelle;
                }
            )
    }

    const vm  = new Vue({
        el: '#main',
        data: {loaded: false, plotData: null},

       mounted() {

            axios.get('/team7Parkhaus/level1-servlet?cmd=chart')
                .then(function (response) {
                    vm.loaded = true;
                    vm.plotData = response.data;
                })
        }
    });

    document.addEventListener("DOMContentLoaded", function(event)
    {
        console.log(ladePreistabelle())
    })


</script>
</html>
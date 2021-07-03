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
    <script src="https://kaul.inf.h-brs.de/ccmjs/mkaul-components/parkhaus/versions/ccm.parkhaus-10.5.1.js"></script>

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
    <h1>Parkhaus Team 7 ${parkhausServiceSession.parkhaus.stadtname}, ${parkhausServiceSession.parkhaus.bundesland} </h1>
</div>
<div>
${parkhausServiceSession.zeigeHTMLParkhausListe()}
</div>
<div class="box lightblue">
    <h2><a href="kasse.jsp">Kasse</a></h2>
    <h2><a href="manager.jsp">Manager</a></h2>
</div>
<div class="box lightyellow">
    <h1>Parkhaus Etage 1</h1>
    <ccm-parkhaus-10-5-1 server_url="./level1-servlet"
                         hide_table="false"
                         simulation.delay="6"
                         name="Etage1"
                         license_max="15"
                         client_categories='["any","Family"]'
                         vehicle_types='["PKW","SUV"]'
                         price_factor='{"SUV":2,"Family":0.5}'
                         json_format="true"
                         max="11"
                         extra_buttons='["Average","Sum","Total Users","Cars",{"extra_class":"undo start","extra_inner":"Undo","extra_popup_title":"Undo and Restart"},{"extra_class":"redo start","extra_inner":"Redo","extra_popup_title":"Redo and Restart"}]'
                         extra_charts='["EinnahmenUeberAutotyp","TagesEinnahmen","WochenEinnahmen"]'>
    </ccm-parkhaus-10-5-1>
</div>
<div class="box lightgreen">
    <h1>Parkhaus Etage 2</h1>
    <ccm-parkhaus-10-5-1 server_url="./level2-servlet"
                         name="Etage2"
                         license_max="14"
                         client_categories='["any","Business"]'
                         vehicle_types='["PKW","SUV","UNSICHTBARES_BOOTMOBIL"]'
                         price_factor='{"SUV":2,"Business":1}'
                         random_start="2"
                         json_format="true"
                         debug="true"
                         max="12"
                         extra_buttons='["Average","Sum"]'>
    </ccm-parkhaus-10-5-1>

</div>



<div id="main">
    <h1> Vue.JS API Call Example</h1>
    <div v-if="loaded">
        <p>{{plotData}}</p>
    </div>
    <p v-else="loaded">Your Plot is being generated</p>

</div>

</body>

<script>
    function wechsleStadt(elmnt , stadID ) {

        axios.get('/team7Parkhaus/level1-servlet?cmd=wechsleParkhaus&stadt=' + stadID)
            .then(function (response) {
                // alert(response.body)
                window.location.reload();
            })
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



</script>
</html>
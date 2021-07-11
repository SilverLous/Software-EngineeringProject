<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.inject.Inject" %>
<%@ page import="de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession" %>
<!DOCTYPE html>
<html lang="de">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
        <title>Parkhaus Kasse</title>

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
    <%!
        @Inject
        private ParkhausServiceSession parkhausServiceSession;
    %>
    <body>
        <h1><%= "Parkhaus Kasse" %></h1>
        <div id="Kassenanzeige" class="box lightyellow" >
            <select id="ebenenNummerForm" name="ebenenNummer" class="form-select" aria-label="Default select example" form="bezahlForm">
                <option selected value="1">Ebene 1</option>
                ${parkhausServiceSession.generiereKassenForm()}
            </select>
            <form action = "PayServlet" method = "GET" id="bezahlForm">
                <input hidden name="kasse">
                <br />
                Parkplatznummer: <input type = "number" name = "parkplatzNummer" />
                <input type = "submit" value = "Submit" />
            </form>
            <a href="index.jsp" class="btn btn-primary my-2">Zurück zum Parkhaus</a>
        </div>
    </body>
</html>
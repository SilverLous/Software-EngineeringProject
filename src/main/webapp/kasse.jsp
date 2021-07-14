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

        <link rel="stylesheet" type="text/css" href="css/main.css" />


    </head>
    <%!
        @Inject
        private ParkhausServiceSession parkhausServiceSession;
    %>
    <body>


        <div class="container">
            <div class="row">
                <div class="col">
                    <h1><%= "Parkhaus Kasse" %></h1>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <div id="Kassenanzeige" class="box lightyellow" >
                        <form action = "PayServlet" method = "GET" id="bezahlForm">
                        <select id="ebenenNummerForm" name="ebenenNummer" class="form-select" aria-label="Default select example" form="bezahlForm">
                            ${parkhausServiceSession.generiereKassenForm()}
                        </select>
                            <br />
                            Parkplatznummer: <input id="Parkplatznummer" type = "number" min="1"  name = "parkplatzNummer" />
                            <button type="submit" class="btn btn-primary">Submit</button>
                        </form>

                        <div id="preisanzeige">
                        </div>

                        <div id="ladeSpinner" class="spinner-border text-primary visually-hidden" role="status">

                        </div>

                        <a href="index.jsp" class="btn btn-primary my-2">Zurück zum Parkhaus</a>
                    </div>
                </div>
            </div>
        </div>


    </body>

    <script>

        const form = document.getElementById('bezahlForm');
        const preisAnzeige = document.getElementById('preisanzeige');
        const PreisSpinner = document.getElementById("ladeSpinner");


        function suchePreis(event) {


            const EbenenName = document.getElementById('ebenenNummerForm').value;
            const PlatzNummer = document.getElementById('Parkplatznummer').value;
            event.preventDefault();
            if (!EbenenName || !PlatzNummer) { return }
            // console.log(event)
            PreisSpinner.classList.toggle("visually-hidden");
            axios.get('/team7Parkhaus/PayServlet?ebenenNummer=' + EbenenName + '&parkplatzNummer=' + PlatzNummer)
                .then(function (response) {
                    // alert(response.body)
                    PreisSpinner.classList.toggle("visually-hidden");
                    preisAnzeige.innerHTML = response.data;

                })

        }

        form.addEventListener('submit', suchePreis)

    </script>
</html>



<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.inject.Inject" %>
<%@ page import="de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession" %>
<!DOCTYPE html>
<html lang="de">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
        <title>Parkhaus Kasse</title>

        <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
        <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
    </head>
    <%!
        @Inject
        private ParkhausServiceSession parkhausServiceSession;
    %>
    <body>
        <h1><%= "Parkhaus Kasse" %></h1>
        <form action="pay-servlet" method="get">
            <input type="number" name="amount">
            <input type="submit">
        </form>
        <div id="Kassenanzeige">
            ${parkhausServiceSession.generiereKassenAusgabe(0,2)}
        </div>
        <a href="index.jsp">zurück zum Parkhaus</a>
    </body>
</html>
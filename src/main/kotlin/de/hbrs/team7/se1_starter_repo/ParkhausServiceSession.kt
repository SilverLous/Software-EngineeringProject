package de.hbrs.team7.se1_starter_repo

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.SessionScoped
import jakarta.inject.Inject
import jakarta.inject.Named
import java.io.Serializable


/*

BIG WARNING DURING LANG FEATURES ALL VALUES MUST BE OPEN!!!

 */
@Named
@SessionScoped
open class ParkhausServiceSession : Serializable {

    // must be this way to ensure it is loaded and the injector has time to do its job
    @Inject
    open var parkhausServiceGlobal: ParkhausServiceGlobal? = null

    open lateinit var city: String
        protected set


    open var sessionCars: Int = 0
        protected set

    open fun addCar() {
        sessionCars++;
        //
        parkhausServiceGlobal!!.addCar()
    }


    // this is the constructor for own functionality (called per new browser connection)
    @PostConstruct
    open fun sessionInit() {
        city = cities.random()
        print("Hello from $city Service new User ")
    }

    open var cities: List<String> = listOf(
        "Berlin",
        "Hamburg",
        "Munich",
        "Cologne",
        "Frankfurt",
        "Bremen",
        "Stuttgart",
        "Düsseldorf",
        "Dortmund",
        "Essen",
        "Dresden",
        "Leipzig",
        "Hannover",
        "Nuremberg",
        "Duisburg",
        "Bochum",
        "Wuppertal",
        "Bielefeld",
        "Bonn",
        "Münster",
        "Karlsruhe",
        "Mannheim",
        "Augsburg",
        "Wiesbaden",
        "Gelsenkirchen",
        "Mönchengladbach",
        "Kiel",
        "Braunschweig",
        "Aachen",
        "Chemnitz",
        "Halle",
        "Magdeburg",
        "Freiburg im Breisgau",
        "Krefeld",
        "Lübeck",
        "Oberhausen",
        "Rostock",
        "Erfurt",
        "Kassel",
        "Hagen",
        "Mainz",
        "Saarbrücken",
        "Hamm",
        "Potsdam",
        "Ludwigshafen",
        "Mülheim",
        "Oldenburg",
        "Osnabrück",
        "Leverkusen",
        "Heidelberg",
        "Solingen",
        "Darmstadt",
        "Herne",
        "Neuss",
        "Regensburg",
        "Paderborn",
        "Ingolstadt",
        "Fürth",
        "Würzburg",
        "Ulm",
        "Heilbronn",
        "Pforzheim",
        "Wolfsburg",
        "Göttingen",
        "Bottrop",
        "Reutlingen",
        "Koblenz",
        "Bremerhaven",
        "Recklinghausen",
        "Erlangen",
        "Bergisch Gladbach",
        "Remscheid",
        "Jena",
        "Trier",
        "Moers",
        "Salzgitter",
        "Siegen",
        "Hildesheim",
        "Gütersloh",
        "Cottbus",
        "Kaiserslautern",
        "Schwerin",
        "Ludwigsburg",
        "Esslingen",
        "Gladbeck",
        "Hilden",
        "Elmshorn",
        "Pinneberg",
        "Kornwestheim",
        "Weingarten",
        "Wolfratshausen",
        "Eppelheim",
        "Asperg",
        "Steinbach am Taunus",
        "Weißenthurm",
        "Eichwalde",
        "Witten",
        "Hanau",
        "Gera",
        "Iserlohn",
        "Düren",
        "Flensburg",
        "Tübingen",
        "Zwickau",
        "Gießen",
        "Ratingen",
        "Lünen",
        "Konstanz",
        "Villingen-Schwenningen",
        "Marl",
        "Worms",
        "Velbert",
        "Minden",
        "Dessau-Roßlau",
        "Neumünster",
        "Norderstedt",
        "Bamberg",
        "Delmenhorst",
        "Viersen",
        "Marburg",
        "Wilhelmshaven",
        "Rheine",
        "Troisdorf",
        "Bayreuth",
        "Lüneburg",
        "Dorsten",
        "Castrop-Rauxel",
        "Detmold",
        "Arnsberg",
        "Lüdenscheid",
        "Landshut",
        "Brandenburg",
        "Bocholt",
        "Aschaffenburg",
        "Celle",
        "Kempten",
        "Fulda",
        "Dinslaken",
        "Aalen",
        "Lippstadt",
        "Herford",
        "Kerpen",
        "Sindelfingen",
        "Weimar",
        "Plauen",
        "Neuwied",
        "Dormagen",
        "Rosenheim",
        "Neubrandenburg",
        "Grevenbroich",
        "Herten",
        "Bergheim",
        "Coburg",
        "Friedrichshafen",
        "Schwäbisch Gmünd",
        "Garbsen",
        "Hürth",
        "Stralsund",
        "Wesel",
        "Greifswald",
        "Offenburg",
        "Langenfeld",
        "Neu-Ulm",
        "Unna",
        "Göppingen",
        "Euskirchen",
        "Frankfurt (Oder)",
        "Hameln",
        "Sankt Augustin",
        "Stolberg",
        "Görlitz",
        "Eschweiler",
        "Meerbusch",
        "Waiblingen",
        "Schweinfurt",
        "Baden-Baden",
        "Langenhagen",
        "Hattingen",
        "Bad Homburg",
        "Pulheim",
        "Lingen",
        "Bad Salzuflen",
        "Nordhorn",
        "Wetzlar",
        "Frechen",
        "Neustadt",
        "Passau",
        "Ahlen",
        "Wolfenbüttel",
        "Ibbenbüren",
        "Kleve",
        "Bad Kreuznach",
        "Speyer",
        "Böblingen",
        "Willich",
        "Gummersbach",
        "Ravensburg",
        "Goslar",
        "Rastatt",
        "Lörrach",
        "Peine",
        "Emden",
        "Erftstadt",
        "Heidenheim",
        "Bergkamen",
        "Leonberg",
        "Frankenthal",
        "Bad Oeynhausen",
        "Freising",
        "Rheda-Wiedenbrück",
        "Bornheim",
        "Dachau",
        "Gronau",
        "Cuxhaven",
        "Alsdorf",
        "Straubing",
        "Soest",
        "Stade",
        "Herzogenrath",
        "Fellbach",
        "Oberursel",
        "Landau",
        "Schwerte",
        "Neunkirchen",
        "Filderstadt",
        "Dülmen",
        "Hof",
        "Melle",
        "Gotha",
        "Bünde",
        "Wittenberg",
        "Weinheim",
        "Erkrath",
        "Rodgau",
        "Albstadt",
        "Brühl",
        "Bruchsal",
        "Kaufbeuren",
        "Falkensee",
        "Oranienburg",
        "Kaarst",
        "Bietigheim-Bissingen",
        "Memmingen",
        "Neustadt am Rübenberge",
        "Lehrte",
        "Kamen",
        "Erkelenz",
        "Wismar",
        "Siegburg",
        "Nettetal",
        "Gifhorn",
        "Dreieich",
        "Borken",
        "Amberg",
        "Eisenach",
        "Laatzen",
        "Heinsberg",
        "Homburg",
        "Ansbach",
        "Germering",
        "Aurich",
        "Nordhausen",
        "Nürtingen",
        "Wunstorf",
        "Schwabach",
        "Königswinter",
        "Freiberg",
        "Kirchheim unter Teck",
        "Leinfelden-Echterdingen",
        "Bensheim",
        "Lemgo",
        "Eberswalde",
        "Pirmasens",
        "Ostfildern",
        "Schwäbisch Hall",
        "Weißenfels",
        "Freital",
        "Buxtehude",
        "Hückelhoven",
        "Halberstadt",
        "Maintal",
        "Neumarkt",
        "Hofheim",
        "Löhne",
        "Schorndorf",
        "Ettlingen",
        "Völklingen",
        "Würselen",
        "Mettmann",
        "Stendal",
        "Ahaus",
        "Niederkassel",
        "Ilmenau",
        "Neu Isenburg",
        "Langen",
        "Pirna",
        "Bitterfeld",
        "Bautzen",
        "Fürstenfeldbruck",
        "Backnang",
        "Kamp-Lintfort",
        "Greven",
        "Papenburg",
        "Wesseling",
        "Beckum",
        "Königs Wusterhausen",
        "Warendorf",
        "Suhl",
        "Erding",
        "Kehl",
        "Emsdetten",
        "Mühlhausen",
        "Coesfeld",
        "Sankt Ingbert",
        "Tuttlingen",
        "Limburg",
        "Porta Westfalica",
        "Ingelheim",
        "Sinsheim",
        "Mörfelden-Walldorf",
        "Dietzenbach",
        "Meppen",
        "Lage",
        "Cloppenburg",
        "Saarlouis",
        "Radebeul",
        "Bad Vilbel",
        "Wermelskirchen",
        "Winsen",
        "Datteln",
        "Kempen",
        "Seelze",
        "Leer",
        "Crailsheim",
        "Merseburg",
        "Zweibrücken",
        "Balingen",
        "Hemer",
        "Barsinghausen",
        "Wedel",
        "Ahrensburg",
        "Steinfurt",
        "Geldern",
        "Viernheim",
        "Goch",
        "Deggendorf",
        "Uelzen",
        "Korschenbroich",
        "Rheinfelden (Baden)",
        "Biberach",
        "Bad Nauheim",
        "Itzehoe",
        "Jülich",
        "Lampertheim",
        "Bernburg",
        "Wernigerode",
        "Forchheim",
        "Naumburg",
        "Vechta",
        "Altenburg",
        "Hoyerswerda",
        "Georgsmarienhütte",
        "Fürstenwalde",
        "Achim",
        "Oer-Erkenschwick",
        "Delbrück",
        "Herrenberg",
        "Gevelsberg",
        "Haan",
        "Radolfzell am Bodensee",
        "Weil am Rhein",
        "Kreuztal",
        "Rheinberg",
        "Geesthacht",
        "Werl",
        "Bramsche",
        "Schönebeck",
        "Burgdorf",
        "Einbeck",
        "Neuruppin",
        "Unterschleißheim",
        "Lohmar",
        "Ennepetal",
        "Riesa",
        "Andernach",
        "Osterholz-Scharmbeck",
        "Taunusstein",
        "Gaggenau",
        "Bad Hersfeld",
        "Friedberg",
        "Schwedt (Oder)",
        "Saalfeld",
        "Meschede",
        "Derne",
        "Neuburg",
        "Merzig",
        "Kelkheim (Taunus)",
        "Waltrop",
        "Tönisvorst",
        "Schwelm",
        "Vaihingen an der Enz",
        "Bretten",
        "Steinheim am Main",
        "Friedberg",
        "Rietberg",
        "Rendsburg",
        "Güstrow",
        "Landsberg",
        "Oelde",
        "Königsbrunn",
        "Rösrath",
        "Winnenden",
        "Northeim",
        "Bühl",
        "Springe",
        "Schwandorf",
        "Höxter",
        "Meißen",
        "Zeitz",
        "Leichlingen",
        "Griesheim",
        "Emmendingen",
        "Olching",
        "Idar-Oberstein",
        "Reinbek",
        "Wegberg",
        "Geislingen an der Steige",
        "Baunatal",
        "Wetter (Ruhr)",
        "Grimma",
        "Kevelaer",
        "Leimen",
        "Bad Neuenahr-Ahrweiler",
        "Baesweiler",
        "Sundern",
        "Neustadt",
        "Mechernich",
        "Arnstadt",
        "Wiesloch",
        "Neckarsulm",
        "Geilenkirchen",
        "Rheinbach",
        "Overath",
        "Aschersleben",
        "Heiligenhaus",
        "Schloß Holte-Stukenbrock",
        "Wangen im Allgäu",
        "Hennigsdorf",
        "Lohne",
        "Teltow",
        "Strausberg",
        "Lauf",
        "Hamminkeln",
        "Obertshausen",
        "Weiterstadt",
        "Hohen Neuendorf",
        "Mühlacker",
        "Heppenheim",
        "Nordenham",
        "Zirndorf",
        "Butzbach",
        "Selm",
        "Sangerhausen",
        "Ehingen an der Donau",
        "Bad Honnef am Rhein",
        "Ludwigsfelde",
        "Bingen am Rhein",
        "Schleswig",
        "Geretsried",
        "Lindau",
        "Pfaffenhofen",
        "Kulmbach",
        "Helmstedt",
        "Sankt Wendel",
        "Friedrichsdorf",
        "Achern",
        "Lübbecke",
        "Roth",
        "Verl",
        "Groß-Gerau",
        "Zittau",
        "Pfungstadt",
        "Rinteln",
        "Ditzingen",
        "Lennestadt",
        "Rottweil",
        "Wiehl",
        "Plettenberg",
        "Rudolstadt",
        "Staßfurt",
        "Brilon",
        "Markkleeberg",
        "Harsewinkel",
        "Meckenheim",
        "Horb am Neckar",
        "Norden",
        "Salzkotten",
        "Petershagen",
        "Sprockhövel",
        "Bad Oldesloe",
        "Idstein",
        "Delitzsch",
        "Eisenhüttenstadt",
        "Übach-Palenberg",
        "Warstein",
        "Ronnenberg",
        "Espelkamp",
        "Olpe",
        "Schmallenberg",
        "Lüdinghausen",
        "Öhringen",
        "Hannoversch Münden",
        "Meiningen",
        "Attendorn",
        "Syke",
        "Waldshut-Tiengen",
        "Waldkraiburg",
        "Rathenow",
        "Limbach-Oberfrohna",
        "Senftenberg",
        "Bad Soden am Taunus",
        "Varel",
        "Quedlinburg",
        "Sonneberg",
        "Döbeln",
        "Husum",
        "Calw",
        "Haren",
        "Bad Mergentheim",
        "Starnberg",
        "Bedburg",
        "Mosbach",
        "Korbach",
        "Herdecke",
        "Salzwedel",
        "Freudenstadt",
        "Jüchen",
        "Dillenburg",
        "Sehnde",
        "Herzogenaurach",
        "Gelnhausen",
        "Netphen",
        "Puchheim",
        "Eschborn",
        "Senden",
        "Warburg",
        "Walsrode",
        "Gersthofen",
        "Wertheim",
        "Überlingen",
        "Neusäß",
        "Leutkirch im Allgäu",
        "Eckernförde",
        "Westerstede",
        "Lengerich",
        "Weilheim",
        "Glauchau",
        "Vreden",
        "Bad Kissingen",
        "Kaltenkirchen",
        "Donaueschingen",
        "Metzingen",
        "Laupheim",
        "Nagold",
        "Karben",
        "Stadthagen",
        "Burg",
        "Friesoythe",
        "Radevormwald",
        "Flörsheim",
        "Apolda",
        "Gardelegen",
        "Schwetzingen",
        "Hockenheim",
        "Heide",
        "Spremberg",
        "Bad Harzburg",
        "Waldkirch",
        "Kitzingen",
        "Eislingen",
        "Büdingen",
        "Eppingen",
        "Sonthofen",
        "Elsdorf",
        "Xanten",
        "Seligenstadt",
        "Halle",
        "Osterode",
        "Quickborn",
        "Germersheim",
        "Stadtallendorf",
        "Versmold",
        "Zerbst",
        "Bad Rappenau",
        "Wülfrath",
        "Aichach",
        "Coswig",
        "Sondershausen",
        "Geseke",
        "Schramberg",
        "Traunreut",
        "Waghäusel",
        "Soltau",
        "Groß-Umstadt",
        "Reichenbach/Vogtland",
        "Schenefeld",
        "Wipperfürth",
        "Rees",
        "Fröndenberg",
        "Bad Schwartau",
        "Bruchköbel",
        "Werdau",
        "Günzburg",
        "Mühldorf",
        "Luckenwalde",
        "Enger",
        "Dillingen",
        "Traunstein",
        "Herborn",
        "Schifferstadt",
        "Gerlingen",
        "Mössingen",
        "Blieskastel",
        "Nidderau",
        "Bad Salzungen",
        "Duderstadt",
        "Nördlingen",
        "Korntal-Münchingen",
        "Schortens",
        "Stadtlohn",
        "Meinerzhagen",
        "Großburgwedel",
        "Bad Waldsee",
        "Annaberg-Buchholz",
        "Wittmund",
        "Zülpich",
        "Oberkirch",
        "Donauwörth",
        "Greiz",
        "Hörstel",
        "Leinefelde",
        "Lichtenfels",
        "Dingolfing",
        "Neustrelitz",
        "Torgau",
        "Holzminden",
        "Bad Krozingen",
        "Wildeshausen",
        "Telgte",
        "Uetersen",
        "Giengen an der Brenz",
        "Glinde",
        "Neviges",
        "Ennigerloh",
        "Heusenstamm",
        "Blankenburg",
        "Schopfheim",
        "Schmalkalden",
        "Eschwege",
        "Waldbröl",
        "Ochtrup",
        "Sulzbach-Rosenberg",
        "Sarstedt",
        "Mölln",
        "Marsberg",
        "Oschersleben",
        "Weil der Stadt",
        "Burghausen",
        "Hechingen",
        "Hemmingen",
        "Rhede",
        "Haiger",
        "Borna",
        "Bad Aibling",
        "Seesen",
        "Bückeburg",
        "Bad Berleburg",
        "Zossen",
        "Mayen",
        "Tettnang",
        "Müllheim",
        "Kolbermoor",
        "Vellmar",
        "Bad Tölz",
        "Bad Pyrmont",
        "Wittlich",
        "Haldensleben",
        "Bergneustadt",
        "Lebach",
        "Kronberg",
        "Püttlingen",
        "Pfullingen",
        "Sömmerda",
        "Dillingen",
        "Bad Driburg",
        "Prenzlau",
        "Oberasbach",
        "Kleinsachsenheim",
        "Radeberg",
        "Alzey",
        "Moosburg",
        "Alfeld",
        "Crimmitschau",
        "Burscheid",
        "Renningen",
        "Alzenau in Unterfranken",
        "Konz",
        "Schneverdingen",
        "Marktoberdorf",
        "Wassenberg",
        "Bad Reichenhall",
        "Auerbach",
        "Vlotho",
        "Bad Dürkheim",
        "Hochheim am Main",
        "Weißenburg",
        "Bremervörde",
        "Hochelheim",
        "Lahnstein",
        "Großenhain",
        "Garching bei München",
        "Schwalmstadt",
        "Forst",
        "Schkeuditz",
        "Hessisch Oldendorf",
        "Werdohl",
        "Wörth am Rhein",
        "Bad Segeberg",
        "Parchim",
        "Bexbach",
        "Heidenau",
        "Freilassing",
        "Freudenberg",
        "Sinzig",
        "Kelsterbach",
        "Nauen",
        "Ginsheim-Gustavsburg",
        "Illertissen",
        "Frankenberg",
        "Buchen in Odenwald",
        "Schwarzenbek",
        "Bad Säckingen",
        "Oerlinghausen",
        "Saulgau",
        "Bendorf",
        "Bad Münder am Deister",
        "Wendlingen am Neckar",
        "Bad Langensalza",
        "Raunheim",
        "Thale",
        "Marktredwitz",
        "Remagen",
        "Bobingen",
        "Trossingen",
        "Sulzbach",
        "Sigmaringen",
        "Nidda",
        "Gescher",
        "Bad Münstereifel",
        "Eutin",
        "Heilbad Heiligenstadt",
        "Horn-Bad Meinberg",
        "Stockach",
        "Wittenberge",
        "Schrobenhausen",
        "Guben",
        "Königstein im Taunus",
        "Altena",
        "Kamenz",
        "Damme",
        "Bad Wildungen",
        "Penzberg",
        "Marienberg",
        "Freiberg am Neckar",
        "Bargteheide",
        "Kronach",
        "Cham",
        "Babenhausen",
        "Schwarzenberg",
        "Preetz",
        "Diepholz",
        "Reinheim",
        "Kelheim",
        "Marbach am Neckar",
        "Bürstadt",
        "Gunzenhausen",
        "Aue",
        "Illingen",
        "Zeulenroda",
        "Hünfeld",
        "Kirchhain",
        "Brackenheim",
        "Finsterwalde",
        "Bad Lippspringe",
        "Wurzen",
        "Weißwasser",
        "Brakel",
        "Kierspe",
        "Dieburg",
        "Michelstadt",
        "Straelen",
        "Halver",
        "Walldorf",
        "Erwitte",
        "Clausthal-Zellerfeld",
        "Bad Wörishofen",
        "Pocking",
        "Taucha",
        "Lübbenau",
        "Ebersbach an der Fils",
        "Alsfeld",
        "Schlüchtern",
        "Bassum",
        "Weener",
        "Füssen",
        "Eilenburg",
        "Breisach am Rhein",
        "Markranstädt",
        "Wadern",
        "Neustadt in Holstein",
        "Templin",
        "Bad Neustadt",
        "Königslutter am Elm",
        "Plochingen",
        "Erlensee",
        "Drensteinfurt",
        "Bad Bentheim",
        "Künzelsau",
        "Arolsen",
        "Schriesheim",
        "Rahden",
        "Boppard",
        "Neustadt bei Coburg",
        "Ober-Ramstadt",
        "Mainburg",
        "Hofgeismar",
        "Langenau",
        "Hohenstein-Ernstthal",
        "Hückeswagen",
        "Brake",
        "Markgröningen",
        "Blomberg",
        "Selb",
        "Ribnitz-Damgarten",
        "Witzenhausen",
        "Mindelheim",
        "Bitburg",
        "Neutraubling",
        "Gehrden",
        "Landsberg",
        "Ratzeburg",
        "Hilchenbach",
        "Uhingen",
        "Neu-Anspach",
        "Bad Bramstedt",
        "Mittweida",
        "Meerane",
        "Fritzlar",
        "Rotenburg an der Fulda",
        "Pattensen",
        "Spenge",
        "Löbau",
        "Dorfen",
        "Lauchhammer",
        "Usingen",
        "Bad Wurzach",
        "Stein bei Nürnberg",
        "Lauda-Königshofen",
        "Ottweiler",
        "Jever",
        "Eberbach",
        "Olsberg",
        "Oschatz",
        "Sandersdorf",
        "Neustadt an der Donau",
        "Bad Camberg",
        "Langenselbold",
        "Schneeberg",
        "Wilsdruff",
        "Gernsbach",
        "Münsingen",
        "Immenstadt im Allgäu",
        "Tornesch",
        "Sassenberg",
        "Dippoldiswalde",
        "Hettstedt",
        "Markdorf",
        "Uslar",
        "Schwabmünchen",
        "Frankenberg",
        "Quakenbrück",
        "Wittstock",
        "Murrhardt",
        "Eppstein",
        "Isny im Allgäu",
        "Abensberg",
        "Grünstadt",
        "Lübben",
        "Vöhringen",
        "Leuna",
        "Grafing bei München",
        "Bebra",
        "Montabaur",
        "Holzgerlingen",
        "Schmölln",
        "Kalkar",
        "Eggenfelden",
        "Genthin",
        "Zeven",
        "Wanzleben",
        "Aßlar",
        "Philippsburg",
        "Erbach",
        "Melsungen",
        "Haßfurt",
        "Lich",
        "Eichstätt",
        "Neckargemünd",
        "Lauterbach",
        "Spaichingen",
        "Angermünde",
        "Hilpoltstein",
        "Biedenkopf",
        "Grünberg",
        "Bergen",
        "Burglengenfeld",
        "Weißenhorn",
        "Erbach",
        "Bad Laasphe",
        "Lorsch",
        "Bergen",
        "Höchstadt an der Aisch",
        "Bad Soden-Salmünster",
        "Krumbach",
        "Ettenheim",
        "Pfullendorf",
        "Landau an der Isar",
        "Löningen",
        "Schüttorf",
        "Zehdenick",
        "Buchloe",
        "Bad Dürrheim",
        "Wehr",
        "Wernau",
        "Tauberbischofsheim",
        "Plattling",
        "Besigheim",
        "Pegnitz",
        "Rödental",
        "Wasserburg am Inn",
        "Bad Salzdetfurth",
        "Neustadt",
        "Dinklage",
        "Zella-Mehlis",
        "Velen",
        "Wiesmoor",
        "Sendenhorst",
        "Herbrechtingen",
        "Östringen",
        "Beverungen",
        "Weilburg",
        "Wolfhagen",
        "Sankt Georgen im Schwarzwald",
        "Schleiden",
        "Waltershausen",
        "Olfen",
        "Herzberg am Harz",
        "Treuchtlingen",
        "Hersbruck",
        "Röthenbach an der Pegnitz",
        "Haselünne",
        "Schongau",
        "Sulingen",
        "Pfarrkirchen",
        "Möckern",
        "Hemsbach",
        "Weinsberg",
        "Steinheim",
        "Wächtersbach",
        "Linnich",
        "Hadamar",
        "Steinheim am der Murr",
        "Brunsbüttel",
        "Neuenburg am Rhein",
        "Bad Urach",
        "Winterberg",
        "Hungen",
        "Blaubeuren",
        "Fehmarnsund",
        "Lauenburg",
        "Anklam",
        "Rosbach vor der Höhe",
        "Twistringen",
        "Frohburg",
        "Feuchtwangen",
        "Bad Windsheim",
        "Ebersberg",
        "Erkner",
        "Lindenberg im Allgäu",
        "Beelitz",
        "Velten",
        "Sulz am Neckar",
        "Hessisch Lichtenau",
        "Preußisch Oldendorf",
        "Bad Freienwalde",
        "Gladenbach",
        "Pößneck",
        "Titisee-Neustadt",
        "Ludwigslust",
        "Jüterbog",
        "Hagenow",
        "Gaildorf",
        "Wünnenberg",
        "Vilsbiburg",
        "Burladingen",
        "Perleberg",
        "Wolgast",
        "Neuenrade",
        "Neustadt",
        "Roding",
        "Zwönitz",
        "Lauffen am Neckar",
        "Ladenburg",
        "Harrislee",
        "Leingarten",
        "Oestrich-Winkel",
        "Bad Fallingbostel",
        "Lichtenstein",
        "Hildburghausen",
        "Pritzwalk",
        "Geisenheim",
        "Dinkelsbühl",
        "Diez",
        "Bad Dürrenberg",
        "Drolshagen",
        "Miesbach",
        "Maxhütte-Haidhof",
        "Osterhofen",
        "Laichingen",
        "Büdelsdorf",
        "Bopfingen",
        "Coswig",
        "Mülheim-Kärlich",
        "Monschau",
        "Hauzenberg",
        "Wolmirstedt",
        "Gräfenhainichen",
        "Billerbeck",
        "Schwaigern",
        "Bad Nenndorf",
        "Walldürn",
        "Schöningen",
        "Langelsheim",
        "Stollberg",
        "Werther",
        "Rutesheim",
        "Glückstadt",
        "Wittingen",
        "Rheinau",
        "Balve",
        "Rothenburg ob der Tauber",
        "Marktheidenfeld",
        "Ochsenfurt",
        "Geisenfeld",
        "Welzheim",
        "Bad Schwalbach",
        "Trostberg an der Alz",
        "Wildau",
        "Herbolzheim",
        "Eisenberg",
        "Friedrichsthal",
        "Lauingen",
        "Braunfels",
        "Lorch",
        "Gengenbach",
        "Belzig",
        "Regen",
        "Osterwieck",
        "Betzdorf",
        "Flöha",
        "Hammelburg",
        "Bischofswerda",
        "Burgstädt",
        "Olbernhau",
        "Nieder-Olm",
        "Schleusingen",
        "Rüthen",
        "Altensteig",
        "Süßen",
        "Barmstedt",
        "Bad Iburg",
        "Donzdorf",
        "Boizenburg",
        "Engen",
        "Isselburg",
        "Langenzenn",
        "Lollar",
        "Erlenbach am Main",
        "Braunsbedra",
        "Haigerloch",
        "Meßstetten",
        "Bleicherode",
        "Demmin",
        "Felsberg",
        "Tangerhütte",
        "Riedlingen",
        "Lichtenau",
        "Nossen",
        "Gernsheim",
        "Waldkirchen",
        "Weilheim an der Teck",
        "Querfurt",
        "Altötting",
        "Gommern",
        "Wilkau-Haßlau",
        "Grevesmühlen",
        "Bad Lauterberg",
        "Staffelstein",
        "Oelsnitz",
        "Bogen",
        "Tangermünde",
        "Werlte",
        "Pasewalk",
        "Bad Frankenhausen",
        "Steinau an der Straße",
        "Neuenstadt am Kocher",
        "Münchberg",
        "Kenzingen",
        "Aulendorf",
        "Neuenhaus",
        "Aich",
        "Bad Ems",
        "Burgau",
        "Meuselwitz",
        "Wildberg",
        "Blumberg",
        "Bad Orb",
        "Bad Wildbad",
        "Rehburg-Loccum",
        "Simbach am Inn",
        "Wahlstedt",
        "Niebüll",
        "Heubach",
        "Schotten",
        "Rüdesheim am Rhein",
        "Klötze",
        "Kurort Steinbach-Hallenberg",
        "Nideggen",
        "Mengen",
        "Oldenburg in Holstein",
        "Ohrdruf",
        "Töging am Inn",
        "Bad König",
        "Bad Breisig",
        "Bad Gandersheim",
        "Osthofen",
        "Großröhrsdorf",
        "Brandis",
        "Bockenem",
        "Beilngries",
        "Gudensberg",
        "Bad Liebenzell",
        "Höhr-Grenzhausen",
        "Schlitz",
        "Heilsbronn",
        "Osterburg",
        "Kemberg",
        "Grimmen",
        "Eisenberg",
        "Neustadt",
        "Oberstdorf",
        "Heiligenhafen",
        "Hohenmölsen",
        "Visselhövede",
        "Dassel",
        "Laubach",
        "Ilsenburg",
        "Brand-Erbisdorf",
        "Zwiesel",
        "Luckau",
        "Zschopau",
        "Trebbin",
        "Reinfeld",
        "Altlandsberg",
        "Niesky",
        "Fürstenau",
        "Lügde",
        "Rehau",
        "Sebnitz",
        "Obernkirchen",
        "Lüchow",
        "Neu Bleckede",
        "Saßnitz",
        "Runkel",
        "Miltenberg",
        "Rauenberg",
        "Zwenkau",
        "Wertingen",
        "Wunsiedel",
        "Ichenhausen",
        "Laufenburg (Baden)",
        "Kandel",
        "Kyritz",
        "Vallendar",
        "Bad Bevensen",
        "Hemau",
        "Zörbig",
        "Torgelow",
        "Westerland",
        "Tecklenburg",
        "Mendig",
        "Bad Liebenwerda",
        "Mittenwalde",
        "Furth im Wald",
        "Furtwangen im Schwarzwald",
        "Neuhaus am Rennweg",
        "Griesbach",
        "Elsfleth",
        "Waldheim",
        "Neuötting",
        "Ortenberg",
        "Hallstadt",
        "Plön",
        "Borgholzhausen",
        "Elze",
        "Freystadt",
        "Doberlug-Kirchhain",
        "Nittenau",
        "Waldenbuch",
        "Breckerfeld",
        "Herzberg",
        "Werneuchen",
        "Obernburg am Main",
        "Ballenstedt",
        "Volkach",
        "Ochsenhausen",
        "Florstadt",
        "Bernsbach",
        "Schleiz",
        "Naunhof",
        "Rain",
        "Hermsdorf",
        "Auerbach",
        "Penig",
        "Stadtilm",
        "Bad Schussenried",
        "Landstuhl",
        "Bad Lauchstädt",
        "Bad Bergzabern",
        "Nierstein",
        "Barth",
        "Hemmoor",
        "Altenerding",
        "Großbottwar",
        "Mansfeld",
        "Kappeln",
        "Bad Sooden-Allendorf",
        "Tirschenreuth",
        "Kuppenheim",
        "Mücheln",
        "Berching",
        "Calbe",
        "Hainichen",
        "Barntrup",
        "Staufenberg",
        "Neuenkirchen",
        "Kirchen",
        "Weida",
        "Bersenbrück",
        "Ueckermünde",
        "Kirn",
        "Perl",
        "Großräschen",
        "Klingenthal",
        "Teterow",
        "Premnitz",
        "Oppenheim",
        "Lützen",
        "Schieder-Schwalenberg",
        "Simmern",
        "Lauta",
        "Wissen",
        "Borgentreich",
        "Kellinghusen",
        "Colditz",
        "Staufen im Breisgau",
        "Helmbrechts",
        "Lößnitz",
        "Meßkirch",
        "Wörrstadt",
        "Viechtach",
        "Neuenbürg",
        "Vohburg an der Donau",
        "Baiersdorf",
        "Kirchberg",
        "Barby",
        "Zwingenberg",
        "Oranienbaum",
        "Neunburg vorm Wald",
        "Bönnigheim",
        "Dornstetten",
        "Grafenau",
        "Ostseebad Kühlungsborn",
        "Kandern",
        "Lugau",
        "Leisnig",
        "Rottenburg an der Laaber",
        "Knittlingen",
        "Ransbach-Baumbach",
        "Zell am Harmersbach",
        "Könnern",
        "Falkenstein",
        "Dannenberg",
        "Bismark",
        "Bad Schmiedeberg",
        "Möckmühl",
        "Oberkochen",
        "Neustadt",
        "Willebadessen",
        "Arnstein",
        "Bad Herrenalb",
        "Vetschau",
        "Teuchern",
        "Beeskow",
        "Daun",
        "Bad Lausick",
        "Medebach",
        "Oederan",
        "Clausthal",
        "Kirchheimbolanden",
        "Herrieden",
        "Schweich",
        "Treuen",
        "Ramstein-Miesenbach",
        "Elsterwerda",
        "Rheinsberg",
        "Bad Düben",
        "Altomünster",
        "Neresheim",
        "Berchtesgaden",
        "Bützow",
        "Altenberg",
        "Bad Liebenstein",
        "Hüfingen",
        "Sontra",
        "Naila",
        "Calau",
        "Allstedt",
        "Gerolstein",
        "Harzgerode",
        "Eisfeld",
        "Saarburg",
        "Löffingen",
        "Pulsnitz",
        "Bad Wimpfen",
        "Roßwein",
        "Lengefeld",
        "Kremmen",
        "Markneukirchen",
        "Münnerstadt",
        "Kahla",
        "Hardegsen",
        "Groitzsch",
        "Kötzting",
        "Teublitz",
        "Renchen",
        "Bad Sachsa",
        "Meldorf",
        "Königsee",
        "Albbruck",
        "Vohenstrauß",
        "Birkenfeld",
        "Haslach im Kinzigtal",
        "Homberg",
        "Malchin",
        "Friedrichroda",
        "Radeburg",
        "Otterndorf",
        "Gundelsheim",
        "Treuenbrietzen",
        "Gedern",
        "Eibenstock",
        "Bernkastel-Kues",
        "Leipheim",
        "Weikersheim",
        "Oebisfelde",
        "Neugersdorf",
        "Gröditz",
        "Dettelbach",
        "Ebern",
        "Parsberg",
        "Elzach",
        "Nortorf",
        "Scheßlitz",
        "Wriezen",
        "Immenhausen",
        "Gau-Algesheim",
        "Freyung",
        "Heringen",
        "Lengenfeld",
        "Gerolzhofen",
        "Annweiler am Trifels",
        "Dingelstädt",
        "Greding",
        "Gerbstedt",
        "Rodalben",
        "Ettersburg",
        "Hartha",
        "Schwarzenbach an der Saale",
        "Ebermannstadt",
        "Neukirchen",
        "Stadtroda",
        "Neustadt-Glewe",
        "Buttstädt",
        "Dietenheim",
        "Marne",
        "Edenkoben",
        "Polch",
        "Hecklingen",
        "Moringen",
        "Reichelsheim",
        "Geithain",
        "Bonndorf im Schwarzwald",
        "Warnemünde",
        "Schelklingen",
        "Böhlen",
        "Artern",
        "Höchstädt an der Donau",
        "Geiselhöring",
        "Römhild",
        "Rodenberg",
        "Müncheberg",
        "Volkmarsen",
        "Jerichow",
        "Herdorf",
        "Maulbronn",
        "Altenkirchen",
        "Waldeck",
        "Waldsassen",
        "Waldmünchen",
        "Heitersheim",
        "Malchow",
        "Luftkurort Arendsee",
        "Glashütte",
        "Mitterteich",
        "Annaburg",
        "Güglingen",
        "Boxberg",
        "Bad Brückenau",
        "Ilshofen",
        "Hermeskeil",
        "Horstmar",
        "Sobernheim",
        "Arnstedt",
        "Zierenberg",
        "Neuenstein",
        "Neuffen",
        "Osterburken",
        "Uffenheim",
        "Thalheim",
        "Burgkunstadt",
        "Rodewisch",
        "Thannhausen",
        "Bad Blankenburg",
        "Havelberg",
        "Großalmerode",
        "Bad Marienberg",
        "Linz am Rhein",
        "Friedland",
        "Blankenhain",
        "Zell im Wiesental",
        "Beerfelden",
        "Rodach",
        "Laage",
        "Rosenfeld",
        "Kölleda",
        "Lübz",
        "Klingenberg am Main",
        "Trochtelfingen",
        "Falkenberg",
        "Beilstein",
        "Gammertingen",
        "Meersburg",
        "Großbreitenbach",
        "Pegau",
        "Alpirsbach",
        "Grafenwöhr",
        "Wittenburg",
        "Dohna",
        "Neumarkt-Sankt Veit",
        "Hachenburg",
        "Norderney",
        "Neustadt",
        "Geisingen",
        "Oberlungwitz",
        "Glücksburg",
        "Rötha",
        "Spangenberg",
        "Nabburg",
        "Bad Hönningen",
        "Lütjenburg",
        "Vilseck",
        "Nieheim",
        "Treffurt",
        "Wassertrüdingen",
        "Dornhan",
        "Braunlage",
        "Windsbach",
        "Riedenburg",
        "Bad Königshofen im Grabfeld",
        "Geislingen",
        "Moorbad Lobenstein",
        "Kastellaun",
        "Schlüsselfeld",
        "Mügeln",
        "Herrnhut",
        "Stadtoldendorf",
        "Bredstedt",
        "Wemding",
        "Westerburg",
        "Haiterbach",
        "Münzenberg",
        "Rochlitz",
        "Wolfach",
        "Leun",
        "Hausach",
        "Bräunlingen",
        "Gransee",
        "Kaltennordheim",
        "Waibstadt",
        "Biesenthal",
        "Tittmoning",
        "Dierdorf",
        "Hagenbach",
        "Wirges",
        "Fürstenberg",
        "Grebenstein",
        "Weidenberg",
        "Neubulach",
        "Schwarzheide",
        "Wittichenau",
        "Schrozberg",
        "Großschirma",
        "Bodenwerder",
        "Traben-Trarbach",
        "Wasungen",
        "Grabow",
        "Hirschau",
        "Munderkingen",
        "Großschönau",
        "Ruhla",
        "Leutershausen",
        "Prüm",
        "Stolpen",
        "Gadebusch",
        "Unkel",
        "Abenberg",
        "Mellrichstadt",
        "Ellrich",
        "Schöppenstedt",
        "Harburg",
        "Kemnath",
        "Ingelfingen",
        "Drebkau",
        "Cochem",
        "Otterberg",
        "Seelow",
        "Jestetten",
        "Gersfeld",
        "Tharandt",
        "Niedenstein",
        "Rockenhausen",
        "Pfreimd",
        "Eltmann",
        "Neudenau",
        "Thum",
        "Kusel",
        "Altentreptow",
        "Battenberg",
        "Heimsheim",
        "Stühlingen",
        "Lindenfels",
        "Mahlberg",
        "Bederkesa",
        "Velburg",
        "Pottenstein",
        "Borkum",
        "Oettingen in Bayern",
        "Vacha",
        "Ronneburg",
        "Wahrenbrück",
        "Arzberg",
        "Amöneburg",
        "Röbel",
        "Hofheim in Unterfranken",
        "Forchtenberg",
        "Külsheim",
        "Monheim",
        "Erbendorf",
        "Schwaan",
        "Freren",
        "Spalt",
        "Oberviechtach",
        "Hollfeld",
        "Naumburg",
        "Saal"
    )
        protected set

}


package de.hbrs.team7.se1_starter_repo.entities;

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


/**
 *
 *
 * @author Thomas Gerlach
 */
public class ParkhausEbenenTest {

    static final String EBENENNAMEN = "TestBundesland";
    static final int MAXPLÄTZE = 12;
    static final int ÖFFNUNGSZEIT = 6;
    static final int LADENSCHLUSS = 24;
    static final int VERZÖGERUNG = 200;
    static final int SIMULATIONSGESCHWINDIGKEIT = 1;
    static final int ZEITVERSCHUB = 0;
    static final HashMap<String, Double> FAHRZEUGPREISE = new HashMap<String, Double>();

    @Test
    @DisplayName("Teste Statischen Fabrikator")
    public void testFabrikator() {

        FAHRZEUGPREISE.put("PKW", 1.0);
        FAHRZEUGPREISE.put("Pickup", 1.2);
        FAHRZEUGPREISE.put("SUV", 1.5);

        ParkhausEbeneConfigDTO testConfig = new ParkhausEbeneConfigDTO(
                EBENENNAMEN, MAXPLÄTZE, ÖFFNUNGSZEIT,
                LADENSCHLUSS, VERZÖGERUNG, ZEITVERSCHUB, SIMULATIONSGESCHWINDIGKEIT, FAHRZEUGPREISE, null);


        ParkhausEbene pe = ParkhausEbene.Companion.ausEbenenConfig(testConfig);

        Assertions.assertEquals(EBENENNAMEN, pe.getName());
        Assertions.assertEquals(MAXPLÄTZE, pe.getMaxPlätze());
        Assertions.assertEquals(ÖFFNUNGSZEIT, pe.getöffnungszeit());
        Assertions.assertEquals(LADENSCHLUSS, pe.getLadenschluss());
        Assertions.assertEquals(VERZÖGERUNG, pe.getVerzögerung());
        Assertions.assertEquals(SIMULATIONSGESCHWINDIGKEIT, pe.getSimulationsGeschwindigkeit());
        Assertions.assertNull(pe.getParkhaus());

    }

}

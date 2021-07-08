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

    static final String EBENEN_NAMEN = "TestBundesland";
    static final int MAX_PLAETZE = 12;
    static final int OEFFNUNGSZEIT = 6;
    static final int LADENSCHLUSS = 24;
    static final int VERZOEGERUNG = 200;
    static final int SIMULATIONS_GESCHWINDIGKEIT = 1;
    static final HashMap<String, Double> FAHRZEUG_PREISE = new HashMap<>();

    @Test
    @DisplayName("Teste Statischen Fabrikator")
    public void testFabrikator() {

        FAHRZEUG_PREISE.put("PKW", 1.0);
        FAHRZEUG_PREISE.put("Pickup", 1.2);
        FAHRZEUG_PREISE.put("SUV", 1.5);

        ParkhausEbeneConfigDTO testConfig = new ParkhausEbeneConfigDTO(EBENEN_NAMEN, MAX_PLAETZE, OEFFNUNGSZEIT,
                LADENSCHLUSS, VERZOEGERUNG, SIMULATIONS_GESCHWINDIGKEIT, FAHRZEUG_PREISE, null);


        ParkhausEbene pe = ParkhausEbene.Companion.ausEbenenConfig(testConfig);

        Assertions.assertEquals(EBENEN_NAMEN, pe.getName());
        Assertions.assertEquals(MAX_PLAETZE, pe.getMaxPlätze());
        Assertions.assertEquals(OEFFNUNGSZEIT, pe.getöffnungszeit());
        Assertions.assertEquals(LADENSCHLUSS, pe.getLadenschluss());
        Assertions.assertEquals(VERZOEGERUNG, pe.getVerzögerung());
        Assertions.assertEquals(SIMULATIONS_GESCHWINDIGKEIT, pe.getSimulationsGeschwindigkeit());
        Assertions.assertNull(pe.getParkhaus());

    }

}

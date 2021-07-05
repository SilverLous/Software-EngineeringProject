package de.hbrs.team7.se1_starter_repo.entities;

import de.hbrs.team7.se1_starter_repo.dto.CitiesDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParkhausTest {

    static final String TESTBUNDESLAND = "TestBundesland";
    static final String TESTSTADTNAME = "TestStadt";
    static final Double TESTLAT = 15.;
    static final Double TESTLONG = 15.;
    static final Double TESTPOP = 150.;
    static final Integer TESTPREIS = 1;

    CitiesDTO testStadt = new CitiesDTO(TESTBUNDESLAND, TESTSTADTNAME, 42,
            TESTLAT, TESTLONG, TESTPOP, TESTPREIS);

    @Test
    @DisplayName("Teste Statischen Fabrikator")
    public void testFabrikator() {

        Parkhaus pa = Parkhaus.Companion.fromCitiesDTO(testStadt);

        Assertions.assertEquals(TESTBUNDESLAND, pa.getBundesland());
        Assertions.assertEquals(TESTSTADTNAME, pa.getStadtname());
        Assertions.assertEquals(TESTLAT, pa.getLat());
        Assertions.assertEquals(TESTLONG, pa.getLng());
        Assertions.assertEquals(TESTPOP, pa.getPopulation());
        Assertions.assertEquals(TESTPREIS, pa.getPreisklasse());

    }

}

package de.hbrs.team7.se1_starter_repo.entities;

import de.hbrs.team7.se1_starter_repo.dto.CitiesDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParkhausTest {

    final String testBundesland = "TestBundesland";
    final String testStadtName = "TestStadt";
    final Double testlat = 15.;
    final Double testlong = 15.;
    final Double testPop = 150.;
    final Integer testPreis = 1;

    CitiesDTO testStadt = new CitiesDTO(testBundesland, testStadtName, 42,
            testlat, testlong, testPop, testPreis);

    @Test
    @DisplayName("Teste Statischen Fabrikator")
    public void TestFabrikator() {

        Parkhaus pa = Parkhaus.Companion.fromCitiesDTO(testStadt);

        Assertions.assertEquals(testBundesland, pa.getBundesland());
        Assertions.assertEquals(testStadtName, pa.getStadtname());
        Assertions.assertEquals(testlat, pa.getLat());
        Assertions.assertEquals(testlong, pa.getLng());
        Assertions.assertEquals(testPop, pa.getPopulation());
        Assertions.assertEquals(testPreis, pa.getPreisklasse());

    }

}

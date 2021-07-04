package de.hbrs.team7.se1_starter_repo.entities;

import de.hbrs.team7.se1_starter_repo.dto.CitiesDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParkhausTest {

    static final String TestBundesland = "TestBundesland";
    static final String TestStadtName = "TestStadt";
    static final Double Testlat = 15.;
    static final Double Testlong = 15.;
    static final Double TestPop = 150.;
    static final Integer TestPreis = 1;

    CitiesDTO testStadt = new CitiesDTO(TestBundesland, TestStadtName, 42,
            Testlat, Testlong, TestPop, TestPreis);

    @Test
    @DisplayName("Teste Statischen Fabrikator")
    public void testFabrikator() {

        Parkhaus pa = Parkhaus.Companion.fromCitiesDTO(testStadt);

        Assertions.assertEquals(TestBundesland, pa.getBundesland());
        Assertions.assertEquals(TestStadtName, pa.getStadtname());
        Assertions.assertEquals(Testlat, pa.getLat());
        Assertions.assertEquals(Testlong, pa.getLng());
        Assertions.assertEquals(TestPop, pa.getPopulation());
        Assertions.assertEquals(TestPreis, pa.getPreisklasse());

    }

}

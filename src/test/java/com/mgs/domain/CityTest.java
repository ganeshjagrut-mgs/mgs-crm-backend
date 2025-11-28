package com.mgs.domain;

import static com.mgs.domain.CityTestSamples.*;
import static com.mgs.domain.StateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(City.class);
        City city1 = getCitySample1();
        City city2 = new City();
        assertThat(city1).isNotEqualTo(city2);

        city2.setId(city1.getId());
        assertThat(city1).isEqualTo(city2);

        city2 = getCitySample2();
        assertThat(city1).isNotEqualTo(city2);
    }

    @Test
    void stateTest() {
        City city = getCityRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        city.setState(stateBack);
        assertThat(city.getState()).isEqualTo(stateBack);

        city.state(null);
        assertThat(city.getState()).isNull();
    }
}

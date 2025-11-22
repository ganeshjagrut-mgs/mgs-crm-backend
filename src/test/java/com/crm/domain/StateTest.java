package com.crm.domain;

import static com.crm.domain.CountryTestSamples.*;
import static com.crm.domain.StateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(State.class);
        State state1 = getStateSample1();
        State state2 = new State();
        assertThat(state1).isNotEqualTo(state2);

        state2.setId(state1.getId());
        assertThat(state1).isEqualTo(state2);

        state2 = getStateSample2();
        assertThat(state1).isNotEqualTo(state2);
    }

    @Test
    void countryTest() throws Exception {
        State state = getStateRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        state.setCountry(countryBack);
        assertThat(state.getCountry()).isEqualTo(countryBack);

        state.country(null);
        assertThat(state.getCountry()).isNull();
    }
}

package biz.digissance.graalvmdemo.http;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@Disabled
@WebMvcTest(RegisterController.class)
class PersonControllerTest {

    @MockBean
    private JpaPersonRepository jpaPersonRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreatePerson() throws Exception {
        mockMvc.perform(get("/persons"))
                .andExpectAll(status().isOk());
    }
}
package br.com.devdojo;

import br.com.devdojo.model.Studant;
import br.com.devdojo.repository.StudantRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudanteEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private StudantRepository studantRepository;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class Config {
        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder().basicAuthorization("toyo", "devdojo");
        }
    }

    @Test
    public void listStudantWhenUsernameAndPasswordAreIncorrectShouldREturnStatusCode401() {
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/studants/", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void getStudantByIdWhenUsernameAndPasswordAreIncorrectShouldREturnStatusCode401() {
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/studants/1", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void listStudantWhenUsernameAndPasswordAreCorrectShouldREturnStatusCode200() {
        List<Studant> studants = asList(new Studant(1L, "Legolas", "legolas@lotr.com"),
                new Studant(2L, "Aragorn", "aragorn@lotr.com"));
        BDDMockito.when(studantRepository.findAll()).thenReturn(studants);

        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/studants/", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudantByIdWhenUsernameAndPasswordAreCorrectShouldREturnStatusCode200() {
        Studant studant = new Studant(1L, "Legolas", "legolas@lotr.com");
        BDDMockito.when(studantRepository.findOne(studant.getId())).thenReturn(studant);
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/studants/{id}", String.class, studant.getId());
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudantByIdWhenUsernameAndPasswordAreCorrectAndStudantDoesNotExistShouldREturnStatusCode404() {
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/studants/{id}", String.class, -1);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

}

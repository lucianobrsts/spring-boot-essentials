package br.com.devdojo;

import br.com.devdojo.model.Studant;
import br.com.devdojo.repository.StudantRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.DELETE;

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

    @Before
    public void setup() {
        Studant studant = new Studant(1L, "Legolas", "legolas@lotr.com");
        BDDMockito.when(studantRepository.findOne(studant.getId())).thenReturn(studant);
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
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/studants/{id}", String.class, 1L);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudantByIdWhenUsernameAndPasswordAreCorrectAndStudantDoesNotExistShouldREturnStatusCode404() {
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/studants/{id}", String.class, -1);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteWhenUserhasroleAdminAdnStudantExistsShouldReturnStatusCode200() {
        BDDMockito.doNothing().when(studantRepository).delete(1L);
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/studants/{id}", DELETE, null, String.class, 1L);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @WithMockUser(username = "xx", password = "xx", roles = {"USER", "ADMIN"})
    public void deleteWhenUserhasroleAdminAdnStudantDoesNotExistsShouldReturnStatusCode404() throws Exception {
        BDDMockito.doNothing().when(studantRepository).delete(1L);
//        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/studants/{id}", DELETE, null, String.class, -1L);
//        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/studants/{id}", -1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    @WithMockUser(username = "xx", password = "xx", roles = {"USER"})
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception {
        BDDMockito.doNothing().when(studantRepository).delete(1L);
//        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/studants/{id}", DELETE, null, String.class, -1L);
//        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/studants/{id}", -1L))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void createWhenNameIsNullShouldReturnStatusCod400BadRequest() throws Exception {
        Studant studant = new Studant(3L, null, "sam@lotr.com");
        BDDMockito.when(studantRepository.save(studant)).thenReturn(studant);
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/admin/studants/", studant, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(response.getBody()).contains("fieldMessage", "O campo nome do estudante é obrigatório");
    }

    @Test
    public void createShouldPersistDataAndReturnStatusCode201() throws Exception {
        Studant studant = new Studant(3L, "Sam", "sam@lotr.com");
        BDDMockito.when(studantRepository.save(studant)).thenReturn(studant);
        ResponseEntity<Studant> response = restTemplate.postForEntity("/v1/admin/studants/", studant, Studant.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }

}

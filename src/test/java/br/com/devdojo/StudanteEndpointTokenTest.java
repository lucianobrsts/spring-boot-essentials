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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.HttpMethod.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudanteEndpointTokenTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private StudantRepository studantRepository;

    @Autowired
    private MockMvc mockMvc;

    private HttpEntity<Void> protectedHeader;
    private HttpEntity<Void> adminHeader;
    private HttpEntity<Void> wrongHeader;

    @Before
    public void configProtectedHeaders() {
        String str = "{\"username\":\"oda\",\"password\":\"devdojo\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.protectedHeader = new HttpEntity<>(headers);
    }

    @Before
    public void configAdminHeaders() {
        String str = "{\"username\":\"toyo\",\"password\":\"devdojo\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }

    @Before
    public void configWrongHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "11111");
        this.wrongHeader =new HttpEntity<>(headers);
    }

    @Before
    public void setup() {
        Studant studant = new Studant(1L, "Legolas", "legolas@lotr.com");
        BDDMockito.when(studantRepository.findOne(studant.getId())).thenReturn(studant);
    }

    @Test
    public void listStudantWhenTokenIsIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/studants/", GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getStudantByIdWhenTokenIsIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/studants/1", GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listStudantWhenTokenIsCorrectShouldREturnStatusCode200() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/studants/1", GET, protectedHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudantByIdWhenTokenIsCorrectShouldREturnStatusCode200() {
        ResponseEntity<Studant> response = restTemplate.exchange("/v1/protected/studants/1", GET, protectedHeader, Studant.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudantByIdWhenToqkenIsCorrectAndStudantDoesNotExistShouldREturnStatusCode404() {
        ResponseEntity<Studant> response = restTemplate.exchange("/v1/protected/studants/-1", GET, protectedHeader, Studant.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteWhenUserhasroleAdminAdnStudantExistsShouldReturnStatusCode200() {
        BDDMockito.doNothing().when(studantRepository).delete(1L);
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/studants/1", DELETE, adminHeader, String.class);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteWhenUserhasroleAdminAdnStudantDoesNotExistsShouldReturnStatusCode404() throws Exception {
        String token = adminHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(studantRepository).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/studants/{id}", -1L).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception {
        String token = protectedHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(studantRepository).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/studants/{id}", 1L).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void createWhenNameIsNullShouldReturnStatusCod400BadRequest() throws Exception {
        Studant studant = new Studant(3L, null, "sam@lotr.com");
        BDDMockito.when(studantRepository.save(studant)).thenReturn(studant);
        ResponseEntity<String> response = restTemplate.exchange("/v1/admin/studants/", POST,  new HttpEntity<>(studant, adminHeader.getHeaders()), String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(response.getBody()).contains("fieldMessage", "O campo nome do estudante é obrigatório");
    }

    @Test
    public void createShouldPersistDataAndReturnStatusCode201() throws Exception {
        Studant studant = new Studant(3L, "Sam", "sam@lotr.com");
        BDDMockito.when(studantRepository.save(studant)).thenReturn(studant);
        ResponseEntity<Studant> response = restTemplate.exchange("/v1/admin/studants/", POST,  new HttpEntity<>(studant, adminHeader.getHeaders()), Studant.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }

}

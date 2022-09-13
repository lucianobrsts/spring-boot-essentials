package br.com.devdojo.javaclient;

import br.com.devdojo.handler.RestResponseExceptionHandler;
import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Studant;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class JavaClientDAO {

    private RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/protected/studants")
            .basicAuthorization("toyo", "devdojo")
            .errorHandler(new RestResponseExceptionHandler())
            .build();

    private RestTemplate restTemplateAdmin = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/admin/studants")
            .basicAuthorization("toyo", "devdojo")
            .errorHandler(new RestResponseExceptionHandler())
            .build();

    public Studant findById(long id) {
        return restTemplate.getForObject("/{id}", Studant.class, id);
//        ResponseEntity<Studant> forEntity = restTemplate.getForEntity("/{id}", Studant.class, 1);
    }

    public List<Studant> listAll() {
        ResponseEntity<PageableResponse<Studant>> exchange = restTemplate.exchange("/", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Studant>>() {
                });
        return exchange.getBody().getContent();
    }

    public Studant save(Studant studant) {
        ResponseEntity<Studant> exchangePost = restTemplateAdmin.exchange("/", HttpMethod.POST, new HttpEntity<>(studant, createJsonHeader()), Studant.class);

        return exchangePost.getBody();
    }

    public void update(Studant studant) {
        restTemplateAdmin.put("/", studant);
    }

    public void delete(long id) {
        restTemplateAdmin.delete("/{id}", id);
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

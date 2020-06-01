package br.com.devdojo.javaclient;

import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Studant;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class JavaSpringClientTest {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:8080/v1/protected/studants")
                .basicAuthorization("toyo", "devdojo").build();

        Studant studant = restTemplate.getForObject("/{id}", Studant.class, 1);
        ResponseEntity<Studant> forEntity = restTemplate.getForEntity("/{id}", Studant.class, 1);
        System.out.println(studant);
        System.out.println(forEntity.getBody());

//        Studant[] studants = restTemplate.getForObject("/", Studant[].class);
//        System.out.println(Arrays.toString(studants));
//
//        ResponseEntity<List<Studant>> exchange = restTemplate.exchange("/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Studant>>() {
//        });
//        System.out.println(exchange.getBody());

        ResponseEntity<PageableResponse<Studant>> exchange = restTemplate.exchange("/?sort=id, desc&sort=name, asc", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Studant>>() {
        });
        System.out.println(exchange);
    }
}

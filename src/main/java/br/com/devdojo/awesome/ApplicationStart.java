package br.com.devdojo.awesome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//classe que inicia para uma aplicação spring boot.
@SpringBootApplication //é a mesma coisa de usar as 3 anotações @Configuration, @EnableAutoConfiguration e @ComponentScan
public class ApplicationStart {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStart.class, args);
    }
}

package br.com.devdojo;

import br.com.devdojo.model.Studant;
import br.com.devdojo.repository.StudantRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudantRepositoryTest {

    @Autowired
    private StudantRepository studantRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createShouldPersistData() {
        Studant studant = new Studant("Luciano", "luciano@yahoo.com.br");
        this.studantRepository.save(studant);
        assertThat(studant.getId()).isNotNull();
        assertThat(studant.getName()).isEqualTo("Luciano");
        assertThat(studant.getEmail()).isEqualTo("luciano@yahoo.com.br");
    }

    @Test
    public void deleteShouldRemoveDate() {
        Studant studant = new Studant("Luciano", "luciano@yahoo.com.br");
        this.studantRepository.save(studant);
        studantRepository.delete(studant);
        assertThat(studantRepository.findOne(studant.getId())).isNull();
    }

    @Test
    public void updateShouldChangeAndPersistDate() {
        Studant studant = new Studant("Luciano", "luciano@yahoo.com.br");
        this.studantRepository.save(studant);
        studant.setName("Luciano222");
        studant.setEmail("luciano@yahoo.com.br222");
        studant = this.studantRepository.save(studant);
        studant = this.studantRepository.findOne(studant.getId());
        assertThat(studant.getName()).isEqualTo("Luciano222");
        assertThat(studant.getEmail()).isEqualTo("luciano@yahoo.com.br222");
    }

    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
        Studant studant = new Studant("Luciano", "luciano@yahoo.com.br");
        Studant studant2 = new Studant("luciano", "luciano222@yahoo.com.br");
        this.studantRepository.save(studant);
        this.studantRepository.save(studant2);
        List<Studant> studantList = studantRepository.findByNameIgnoreCaseContaining("luciano");
        assertThat(studantList.size()).isEqualTo(2);
    }

    @Test
    public void createWhenNameIsNullShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("O campo nome do estudante é obrigatório.");
        this.studantRepository.save(new Studant());
    }

    @Test
    public void createWhenEmailIsNullShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        Studant studant = new Studant();
        studant.setName("Luciano");
        this.studantRepository.save(studant);
    }

    @Test
    public void createWhenEmailIsNotValidShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Digite um email válido.");
        Studant studant = new Studant();
        studant.setName("Luciano");
        studant.setEmail("Luciano");
        this.studantRepository.save(studant);
    }
}

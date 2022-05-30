package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private VisitSDJpaService service;

    private Visit visit;
    private Set<Visit> visits;

    @BeforeEach
    void setUp() {
        visit = new Visit();
        visits = Set.of(visit);
    }

    @DisplayName("Test Find All")
    @Test
    void findAll() {
        // given
        given(visitRepository.findAll()).willReturn(visits);
        // when
        Set<Visit> foundVisits = service.findAll();
        // then
        then(visitRepository).should().findAll();
        assertThat(foundVisits).hasSize(1);
    }

    @Test
    void findById() {
        // given
        given(visitRepository.findById(anyLong())).willReturn(Optional.of(visit));
        // when
        Visit foundVisit = service.findById(1L);
        // then
        then(visitRepository).should().findById(anyLong());
        assertThat(foundVisit).isNotNull();
    }

    @Test
    void save() {
        // given
        given(visitRepository.save(any(Visit.class))).willReturn(visit);
        // when
        Visit savedVisit = service.save(new Visit());
        // then
        then(visitRepository).should().save(any(Visit.class));
        assertThat(savedVisit).isNotNull();
    }

    @Test
    void delete() {
        // when
        service.delete(visit);
        // then
        then(visitRepository).should(atLeastOnce()).delete(any(Visit.class));
    }

    @Test
    void deleteById() {
        // when
        service.deleteById(1L);
        // then
        then(visitRepository).should(atLeastOnce()).deleteById(1L);
    }
}
package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock(lenient = true)
    private SpecialtyRepository specialtyRepository;

    @InjectMocks
    private SpecialitySDJpaService service;

    @Test
    void deleteByObject() {
        Speciality speciality = new Speciality();
        service.delete(speciality);
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    void findById() {
        Speciality speciality = new Speciality();

        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));
        Speciality foundSpeciality = service.findById(1L);

        assertThat(foundSpeciality).isNotNull();

        then(specialtyRepository).should().findById(1L);
    }

    @Test
    void findByIdBdd() {
        //given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));

        //when
        Speciality foundSpeciality = service.findById(1L);

        // then
        assertThat(foundSpeciality).isNotNull();
        then(specialtyRepository).should().findById(1L);
        then(specialtyRepository).should(times(1)).findById(1L);
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }


    @Test
    void deleteById() {
        service.deleteById(1L);
        service.deleteById(1L);
        then(specialtyRepository).should(times(2)).deleteById(1L);
    }

    @Test
    void deleteByIdAtLeast() {
        service.deleteById(1L);
        service.deleteById(1L);
        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);
    }

    @Test
    void deleteByIdAtMost() {
        //when
        service.deleteById(1L);
        service.deleteById(1L);

        // then
        then(specialtyRepository).should(atMost(5)).deleteById(1L);
    }

    @Test
    void deleteByIdNever() {
        // when
        service.deleteById(1L);
        service.deleteById(1L);

        // then
        then(specialtyRepository).should(atMost(5)).deleteById(1L);
        then(specialtyRepository).should(never()).deleteById(5L);
    }

    @Test
    void testDelete() {
        // when
        service.delete(new Speciality());

        // then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("boom")).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIdThrows() {
        // given
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException("boom"));

        // when
        assertThrows(RuntimeException.class, () -> service.findById(1L));

        // then
        then(specialtyRepository).should().findById(1L);
    }

    @Test
    void testDeleteBDD() {
        // given
        willThrow(new RuntimeException("boom")).given(specialtyRepository).delete(any());

        // when
        assertThrows(RuntimeException.class, () -> service.delete(new Speciality()));

        // then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLambda() {
        // given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription(MATCH_ME);

        Speciality savedSpeciality = new Speciality();
        savedSpeciality.setId(1L);

        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpeciality);

        // when
        Speciality returnedSpeciality = service.save(speciality);

        // then
        assertThat(returnedSpeciality.getId()).isEqualTo(1L);
    }

    @Test
    void testSaveLambdaNoMatch() {
        // given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription("Not a match");

        Speciality savedSpeciality = new Speciality();
        savedSpeciality.setId(1L);

        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpeciality);

        // when
        Speciality returnedSpeciality = service.save(speciality);

        // then
        assertNull(returnedSpeciality);
    }
}
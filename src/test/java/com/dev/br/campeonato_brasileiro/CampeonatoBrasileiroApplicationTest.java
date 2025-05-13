package com.dev.br.campeonato_brasileiro;

import com.dev.br.campeonato_brasileiro.service.SomeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CampeonatoBrasileiroApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    void applicationStartsSuccessfully() {
        String[] beanNames = context.getBeanDefinitionNames();
        assertThat(beanNames).isNotEmpty();
    }

    @Test
    void testSomeServiceMock() {
        // Criando um mock manualmente com Mockito
        SomeService someService = mock(SomeService.class);
        when(someService.getMensagem()).thenReturn("Teste do serviço!");

        assertThat(someService.getMensagem()).isEqualTo("Teste do serviço!");
        verify(someService, times(1)).getMensagem();
    }

    @Test
    void mainMethodRunsWithoutException() {
        String[] args = {}; // Simulando argumentos de inicialização
        CampeonatoBrasileiroApplication.main(args); // Chamando o método main()
    }
}

package com.dev.br.campeonato_brasileiro.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Configuração para testes que precisa do objeto CampeonatoProperties
 */
@Profile("test")
@Configuration
public class MockitoConfiguration {

    @Bean
    @Primary
    public CampeonatoProperties campeonatoProperties() {
        CampeonatoProperties properties = Mockito.mock(CampeonatoProperties.class);
        Mockito.when(properties.getDataInicio()).thenReturn(java.time.LocalDate.now());
        return properties;
    }
}

package com.dev.br.campeonato_brasileiro.config;

//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@TestPropertySource(properties = {
//        "campeonato.data-inicio=2025-05-04"
//})
//public class CampeonatoPropertiesTest {
//
//    @Autowired
//    private CampeonatoProperties campeonatoProperties;
//
//    @Test
//    public void testCarregamentoPropriedades() {
//        // Verifica se o objeto foi injetado corretamente
//        assertNotNull(campeonatoProperties);
//
//        // Verifica se a data foi carregada corretamente
//        LocalDate dataEsperada = LocalDate.of(2025, 5, 4);
//        assertEquals(dataEsperada, campeonatoProperties.getDataInicio());
//    }
//
//    @Test
//    public void testSettersGetters() {
//        // Cria uma instância manualmente
//        CampeonatoProperties props = new CampeonatoProperties();
//
//        // Testa o setter
//        LocalDate novaData = LocalDate.of(2025, 6, 1);
//        props.setDataInicio(novaData);
//
//        // Verifica se o getter retorna o valor correto
//        assertEquals(novaData, props.getDataInicio());
//    }
//}

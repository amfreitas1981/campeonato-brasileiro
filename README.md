# 🏆 Campeonato Brasileiro - Série A

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-100%25-success)
![Java](https://img.shields.io/badge/java-17-blue)
![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Version](https://img.shields.io/badge/version-1.0.0-brightgreen)


## Descrição

Este projeto é uma simulação dinâmica do Campeonato Brasileiro - Série A, desenvolvido com Java 21, 
Spring Boot, Oracle Database 11g, e documentação via Swagger. A aplicação permite gerar rodadas, simular 
partidas, exibir a classificação e exportar os dados para planilhas, além de reiniciar o campeonato.

---

## ⚙️ Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Oracle Database 11g
- NÃO APLICADO: Flyway (migração de dados) --> Observação: Seria aplicado se tivesse usado a versão paga e disponível para 
  ser executada no Oracle Database 21c.
- Swagger/OpenAPI
- Apache POI (exportação Excel)
- Maven
- IntelliJ IDEA

---

## 📂 Estrutura do Projeto
    src/ 
    └── main/ 
        ├── java/com/dev/br/campeonato_brasileiro/ │ 
        ├── config │ 
        ├── controller │ 
        ├── dto │ 
        ├── model │ 
        ├── repository │ 
        └── service 
        └── resources/ 
            ├── application.yml 
    └── test/ 
        ├── java/com/dev/br/campeonato_brasileiro/ │ 
        ├── config │ 
        ├── controller │ 
        ├── dto │ 
        ├── model │ 
        ├── repository │ 
        └── service

---

## 🚀 Como Executar o Projeto

### ✅ Pré-requisitos

- Java 21
- Maven 3.9+
- Oracle Database 11g (em execução, porque não tenho a nova versão)
- IDE recomendada: IntelliJ

### ⚙️ Passos para rodar

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/campeonato-brasileiro.git
cd campeonato-brasileiro

# Rodar o projeto
./mvnw spring-boot:run

⚠️ O Flyway criará automaticamente as tabelas e inserirá os times na primeira execução. Faria, se tiver a versão paga. Do contrário, as ações de banco foram executadas manualmente.
```

### 🔗 Endpoints da API (via Swagger)

Após subir o projeto, acesse:
http://localhost:8080/swagger-ui/index.html

Endpoints:

- times-campeonato-controller --> Responsável por inserir as 20 equipes e consultar, via Banco de Dados.
 
  | **Método**  | **Endpoint**                 | **Descrição**                                |
  |-------------|------------------------------|----------------------------------------------|
  | POST        | /api/teams/insert            | Criar/inserir as equipes na tabela           |
  | GET         | /api/teams/consult           | Consultar as equipes na tabela               |

- campeonato-controller --> Simula partidas, no geral e a cada rodada disputada, exporta planilha com as 
simulações dos jogos realizados no Campeonato Brasileiro, com a opção para reiniciar, quando precisar 
organizar nova competição, nos mesmos moldes.
  
  | **Método**     | **Endpoint**                                        | **Descrição**                                                                        |
  |----------------|-----------------------------------------------------|--------------------------------------------------------------------------------------|
  | POST           | /api/championship/simulation                        | Lista a classificação geral                                                          |
  | POST           | /api/championship/generate-rounds                   | Geração das partidas                                                                 |
  | GET            | /api/championship/rounds                            | Consultar as partidas                                                                |
  | GET            | /api/championship/round/{number}/matches            | Consultar os resultados de uma rodada específica                                     |
  | GET            | /api/championship/round/export-classification       | Exportar os resultados via planilha .xslx                                            |
  | GET            | /api/championship/classification                    | Exibir a classificação do campeonato                                                 |
  | GET            | /api/championship/classification/sulamericana       | Exibir as equipes classificadas para a Copa Sul Americana                            |
  | GET            | /api/championship/classification/libertadores       | Exibir as equipes classificadas para a Taça Libertadores da América                  |
  | GET            | /api/championship/classification/downgrade          | Exibir as equipes que foram rebaixadas para a Série B                                |
  | DELETE         | /api/championship/restart                           | Remover os dados do banco de dados e iniciar uma nova competição, no mesmo formato   |

- classificacao-controller --> Informa, resumidamente a classificação do campeonato, de modo geral, com 
os resultados simulados, os classificados para a Taça Libertadores da América, a Copa Sul Americana e os 
rebaixados.
 
  | **Método** | **Endpoint**                     | **Descrição**                                            |
  |------------|----------------------------------|----------------------------------------------------------|
  | GET        | /api/classification/sulamericana | Lista as equipes classificadas para a Copa Sul Americana |
  | GET        | /api/classification/libertadores | Times classificados para a Libertadores                  |
  | GET        | /api/classification/general      | Lista a classificação geral                              |
  | GET        | /api/classification/downgrade    | Lista as equipes rebaixadas                              |


### 📊 Exportação da Tabela
A tabela de classificação pode ser exportada no formato *.xlsx* com cabeçalhos organizados. O arquivo será baixado automaticamente via endpoint `/exportar/classificacao`.

### 🧪 Testes e Simulação
Simular rodadas individualmente ou todas de uma vez.

Classificação ordenada com critérios:

- Pontos
- Vitórias
- Saldo de gols
- Gols marcados


## Configurações Técnicas

- application.yml
- application-test.yml
- Swagger


### Criação de tabelas SQL
Seguem as tabelas que foram criadas direto no banco de dados:
```bash
-- REMOVER DADOS DO BANCO, DAS TABELAS E INSTRUÇÕES CRIADAS, CASO DE ERROS OU AJUSTES
-- Remover as triggers
DROP TRIGGER TRG_TIMES_ID;
DROP TRIGGER TRG_PARTIDAS_ID;
DROP TRIGGER TRG_CLASSIFICACAO_ID;

-- Remover as tabelas
DROP TABLE CLASSIFICACAO;
DROP TABLE PARTIDAS;
DROP TABLE TIMES;

-- Remover as sequências
DROP SEQUENCE SEQ_TIMES;
DROP SEQUENCE SEQ_PARTIDAS;
DROP SEQUENCE SEQ_CLASSIFICACAO;

-- CRIAÇÃO E DEFINIÇÃO

-- CRIAÇÃO DAS TABELAS NO BANCO DE DADOS
CREATE SEQUENCE SEQ_TIMES START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SEQ_PARTIDAS START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SEQ_CLASSIFICACAO START WITH 1 INCREMENT BY 1;

-- Tabela TIMES
CREATE TABLE TIMES (
    ID NUMBER PRIMARY KEY,
    NOME VARCHAR2(100) NOT NULL,
    SIGLA VARCHAR2(10) NOT NULL,
    ESTADO VARCHAR2(50)
);

-- Tabela PARTIDAS
CREATE TABLE PARTIDAS (
    ID NUMBER PRIMARY KEY,
    TIMES_CASA_ID NUMBER NOT NULL,
    TIMES_FORA_ID NUMBER NOT NULL,
    DATA_PARTIDA DATE NOT NULL,
    GOLS_CASA NUMBER DEFAULT 0,
    GOLS_VISITANTE NUMBER DEFAULT 0, 
	ROUND NUMBER DEFAULT 0,
    FOREIGN KEY (TIMES_CASA_ID) REFERENCES TIMES(ID),
    FOREIGN KEY (TIMES_FORA_ID) REFERENCES TIMES(ID)
);

-- Tabela CLASSIFICACAO
CREATE TABLE CLASSIFICACAO (
    ID NUMBER PRIMARY KEY,
    TIMES_ID NUMBER NOT NULL,
    PONTOS NUMBER DEFAULT 0,
    VITORIAS NUMBER DEFAULT 0,
    EMPATES NUMBER DEFAULT 0,
    DERROTAS NUMBER DEFAULT 0,
    GOLS_PRO NUMBER DEFAULT 0,
    GOLS_CONTRA NUMBER DEFAULT 0,
    SALDO_GOLS NUMBER DEFAULT 0, 
	ROUND NUMBER DEFAULT 0, 
	POSICAO NUMBER DEFAULT 0,
    FOREIGN KEY (TIMES_ID) REFERENCES TIMES(ID)
);

CREATE OR REPLACE TRIGGER TRG_TIMES_ID
BEFORE INSERT ON TIMES
FOR EACH ROW
BEGIN
  :NEW.ID := SEQ_TIMES.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_PARTIDAS_ID
BEFORE INSERT ON PARTIDAS
FOR EACH ROW
BEGIN
  :NEW.ID := SEQ_PARTIDAS.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER TRG_CLASSIFICACAO_ID
BEFORE INSERT ON CLASSIFICACAO
FOR EACH ROW
BEGIN
  :NEW.ID := SEQ_CLASSIFICACAO.NEXTVAL;
END;
/

select * from times;
select * from partidas;
select * from classificacao;
```


### Cobertura de código
Foi realizada total cobertura de código, via JUnit5 e Mockito, acrescido das regras abaixo:

Testes para o CampeonatoController
Este conjunto de testes foi desenvolvido para validar o funcionamento do CampeonatoController, que é 
responsável por gerenciar as operações relacionadas ao campeonato brasileiro de futebol.

Classes de Teste Implementadas
1. CampeonatoControllerTest
   Testes unitários que verificam isoladamente cada método do controller, utilizando Mockito para simular 
   as dependências.
   Principais cenários testados:

   * Listar partidas por rodada
   * Resetar campeonato
   * Exportar classificação para Excel
   * Consultar classificação geral
   * Consultar times classificados para Libertadores
   * Consultar times classificados para Sul-Americana
   * Consultar times rebaixados
   * Gerar partidas
   * Listar todas as partidas
   * Simular campeonato com e sem partidas já geradas

2. CampeonatoControllerIntegrationTest
   Testes de integração usando MockMvc para validar o comportamento do controller no contexto da API REST.
   Principais cenários testados:

Testar os endpoints HTTP (GET, POST, DELETE)
Validar os códigos de status das respostas
Verificar o formato e conteúdo das respostas JSON
Testar os headers específicos (como em download de arquivos)

3. Classes Auxiliares

MockitoConfiguration: Configuração para injetar mocks de dependências específicas em testes
Classificacao.java: Modelo de dados para representação da classificação dos times
CampeonatoProperties.java: Configurações do campeonato utilizadas pelos serviços

Como Executar os Testes
Para executar estes testes, você precisará de:

Java 21 instalado
Maven configurado

Dependências:

Spring Boot Test
JUnit 5
Mockito
Lombok

Executar os testes via Maven:

```bash
mvn test
```

Ou pela sua IDE, executando as classes individualmente.
Cobertura de Testes
Os testes cobrem 100% dos métodos do CampeonatoController, verificando:

Fluxos de sucesso
Manipulação correta das entidades
Delegação adequada para os serviços e repositórios
Respostas HTTP apropriadas

Melhorias e TODOs implementados
No código do controller, foram implementados os TODOs:

Geração e salvamento de partidas
Consulta para verificar se as partidas foram geradas
Simulação de partidas com verificação prévia da existência das mesmas

Os testes validam essas implementações conforme os requisitos funcionais esperados.


### 🤝 Contribuições
Pull requests são bem-vindos! Sinta-se à vontade para abrir issues com sugestões ou melhorias.

### 📄 Licença
Este projeto está licenciado sob a MIT License.

### 👨‍💻 Autor
Desenvolvido por Alexandre Freitas


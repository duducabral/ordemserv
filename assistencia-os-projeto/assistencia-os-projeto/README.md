# API de Abertura de Ordem de Serviço

Projeto Spring Boot 3.3.13, Java 17+, Maven, Spring Data JPA, H2 e Bean Validation.

## Abrir no IntelliJ
1. Extraia o ZIP.
2. No IntelliJ, abra a pasta `ordens-servico` como projeto Maven.
3. Aguarde baixar as dependências (internet necessária).
4. Execute `AssistenciaApplication`.
5. Para executar testes, use `mvn test` no terminal (com Maven instalado) ou rode `OrdemServicoIntegrationTest` pelo IntelliJ.

## Requisição
POST http://localhost:8080/ordens
Content-Type: application/json

```json
{"clienteId":1,"equipamentoId":1,"descricaoProblema":"Notebook não liga após queda de energia."}
```

Dados iniciais: cliente 1 ativo com equipamento 1; cliente 2 inativo com equipamento 3; cliente 3 ativo com equipamento 2. Os IDs são gerados pelo H2 e podem mudar se a base for alterada.

## H2
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:assistencia
Usuário: sa
Senha: (vazia)

Tabelas: CLIENTES, EQUIPAMENTOS, ORDENS_SERVICO, HISTORICO_STATUS.

## Testes
Sete testes de integração verificam sucesso 201, validação 400, cliente/equipamento 404, regras 422, histórico e protocolos diferentes.

## Observação de arquitetura
O modelo de domínio valida cliente ativo e propriedade do equipamento e determina status e histórico. O serviço coordena repositórios, horário e protocolo; o controller usa apenas DTOs.

## Observação de produção
O sufixo hexadecimal de 4 caracteres admite 65.536 valores por dia. O índice UNIQUE no banco impede duplicidade, inclusive sob concorrência. A geração faz consulta e novas tentativas para reduzir colisões, mas não faz retry após conflito concorrente de UNIQUE: para produção com alta concorrência, implemente retry transacional ou aumente o espaço de protocolos (se o contrato permitir).

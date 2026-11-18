# DSCommerce

API REST de e-commerce desenvolvida com Spring Boot em arquitetura em camadas
(Controller -> Service -> Repository), com persistência via JPA/Hibernate, mapeamento
com MapStruct, autenticação stateless com JWT (HS256), autorização por perfil,
documentação OpenAPI/Swagger e H2 em memória para desenvolvimento e testes.

## Stack

- Java 17
- Spring Boot 3.5.16
- Spring Data JPA / Hibernate
- Spring Web (MVC)
- Spring Security (Resource Server + JWT)
- Spring Validation (Bean Validation)
- MapStruct 1.6.3
- SpringDoc OpenAPI 2.7.0 (Swagger UI)
- H2 (runtime e testes)
- PostgreSQL (driver de runtime)
- Maven 3.8+

## Como executar

```bash
./mvnw clean compile
./mvnw test
./mvnw spring-boot:run
```

Serviços:

- API: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- H2 Console: `http://localhost:8081/h2-console`
  - JDBC URL: `jdbc:h2:mem:dscommerce`
  - Usuário: `sa`
  - Senha: vazia

O profile ativo por padrão é `test` (`application-test.properties`), que usa H2 em memória,
`ddl-auto=create-drop` e recarrega o `data.sql` a cada inicialização.

### Variáveis de ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `JWT_SECRET` | valor de desenvolvimento no `application.properties` | Chave Base64 usada para assinar o JWT (mínimo 256 bits em HS256) |
| `JWT_DURATION` | `900` | Validade do token em segundos |
| `JWT_ISSUER` | `dscommerce` | Claim `iss` do token |
| `CORS_ORIGINS` | `http://localhost:3000` | Origens permitidas, separadas por vírgula |

O valor padrão de `JWT_SECRET` existe apenas para facilitar o ambiente local. Em produção
defina a variável com um segredo forte e rotacionável.

## Autenticação e autorização (JWT)

### Fluxo

1. O cliente envia `POST /auth/login` com email e senha.
2. O `AuthenticationManager` valida as credenciais via `UserDetailsServiceImpl`.
3. O `JwtService` gera um token HS256 com `sub` (email), `roles`, `iss`, `iat`, `exp`.
4. O cliente envia o token em `Authorization: Bearer <token>`.
5. O filtro do Resource Server valida assinatura, expiração e issuer, e o
   `JwtAuthenticationConverter` converte o claim `roles` em authorities.

A aplicação é stateless: não há sessão HTTP nem CSRF habilitado.

### Login

```bash
curl -X POST "http://localhost:8081/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@dscommerce.com",
    "password": "12345678"
  }'
```

Resposta `200`:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

Credenciais inválidas retornam `401` com corpo JSON padronizado.

### Usuários do seed

| ID | Email | Senha | Perfil |
|---|---|---|---|
| 1 | `admin@dscommerce.com` | `12345678` | `ROLE_ADMIN` |
| 2 | `maria@email.com` | `12345678` | `ROLE_CLIENT` |
| 3 | `joao@email.com` | `12345678` | `ROLE_CLIENT` |
| 4 | `ana@email.com` | `12345678` | `ROLE_CLIENT` |
| 5 | `pedro@email.com` | `12345678` | `ROLE_CLIENT` |

As senhas do seed são armazenadas como hash BCrypt em `data.sql`.

### Matriz de acesso

| Rota | Método | Acesso |
|---|---|---|
| `/auth/login` | `POST` | Público |
| `/users` | `POST` | Público (sempre cria `ROLE_CLIENT`) |
| `/users/**` | `GET`, `PUT`, `DELETE` | `ROLE_ADMIN` |
| `/products/**` | `GET` | Público |
| `/products/**` | `POST`, `PUT`, `DELETE` | `ROLE_ADMIN` |
| `/orders` | `POST` | Autenticado |
| `/orders/**` | `GET` | Autenticado (cliente vê apenas os próprios) |
| `/orders/**` | `PUT`, `DELETE` | `ROLE_ADMIN` |
| `/payments/**` | Todos | `ROLE_ADMIN` |
| `/order-items/**` | Todos | `ROLE_ADMIN` |
| `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html` | `GET` | Público |
| `/h2-console/**` | Todos | Público (apenas ambiente local) |
| Qualquer outra rota | Todos | Autenticado |

Regras de posse aplicadas no `OrderService`:

- `GET /orders/{id}`: admin ou dono do pedido; caso contrário `403`.
- `GET /orders`: admin recebe todos; cliente recebe apenas os seus.
- `POST /orders`: admin pode informar qualquer `clientId`; cliente só pode informar o próprio
  `clientId` (e o campo é obrigatório por validação).

### Respostas de erro de segurança

`401 Unauthorized` (token ausente, inválido ou credenciais erradas):

```json
{
  "timestamp": "2026-11-13T12:00:00Z",
  "status": 401,
  "error": "Não autenticado",
  "message": "Autenticação necessária",
  "path": "/users"
}
```

`403 Forbidden` (autenticado sem permissão):

```json
{
  "timestamp": "2026-11-13T12:00:00Z",
  "status": 403,
  "error": "Acesso negado",
  "message": "Você não tem permissão para acessar este recurso",
  "path": "/users"
}
```

### Arquivos de segurança

- `config/SecurityConfig.java`: filter chain, CORS, regras por rota, `PasswordEncoder`,
  `AuthenticationManager`.
- `security/JwtConfig.java`: `SecretKey`, `JwtEncoder`, `JwtDecoder` e conversor de roles.
- `security/JwtService.java`: emissão do token.
- `security/UserDetailsServiceImpl.java`: carrega o usuário e suas authorities.
- `security/RestAuthenticationEntryPoint.java`: resposta JSON de 401.
- `security/RestAccessDeniedHandler.java`: resposta JSON de 403.
- `services/AuthService.java` e `controllers/AuthController.java`: fluxo de login.
- `dto/LoginDTO.java` e `dto/TokenDTO.java`: contrato de entrada e saída.
- `src/test/java/com/teste/dscommerce/AuthIntegrationTests.java`: testes de integração.

## Endpoints

### Autenticação

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `POST` | `/auth/login` | Autentica e gera o JWT | `200` com `TokenDTO` / `401` |

### Produtos

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `GET` | `/products` | Lista paginada, ordenada por `name` | `200` com `Page<ProductDTO>` |
| `GET` | `/products/{id}` | Busca por ID | `200` / `404` |
| `POST` | `/products` | Cria produto (ADMIN) | `201` + `Location` / `422` |
| `PUT` | `/products/{id}` | Atualiza produto (ADMIN) | `200` / `404` / `422` |
| `DELETE` | `/products/{id}` | Remove produto (ADMIN) | `204` / `404` / `400` |

```bash
curl "http://localhost:8081/products?page=0&size=10"
```

```bash
curl -X POST "http://localhost:8081/products" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mouse Gamer",
    "description": "Mouse ergonomico com sensor de 16000 DPI.",
    "price": 249.90,
    "imgUrl": "https://exemplo.com/mouse.jpg"
  }'
```

### Usuários

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `GET` | `/users` | Lista paginada (ADMIN) | `200` com `Page<UserDTO>` |
| `GET` | `/users/{id}` | Busca por ID (ADMIN) | `200` / `404` |
| `GET` | `/users/email/{email}` | Busca por email (ADMIN) | `200` / `404` |
| `GET` | `/users/name/{name}` | Busca por nome, paginada (ADMIN) | `200` / `404` |
| `POST` | `/users` | Cadastro público (sempre `ROLE_CLIENT`) | `201` + `Location` / `422` / `400` |
| `PUT` | `/users/{id}` | Atualiza usuário (ADMIN) | `200` / `404` / `422` |
| `DELETE` | `/users/{id}` | Remove usuário (ADMIN) | `204` / `404` / `400` |

```bash
curl -X POST "http://localhost:8081/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Maria Silva",
    "email": "maria.silva@email.com",
    "phone": "(11) 98888-7777",
    "birthDate": "1995-07-11",
    "password": "12345678"
  }'
```

A senha é obrigatória apenas no `POST` (`UserInsertDTO`) e é gravada com BCrypt. O
`UserUpdateDTO` não possui senha, portanto o `PUT` não altera a senha. O campo `roles` no
`UserDTO` é um conjunto de authorities e nunca expõe a senha.

### Pedidos

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `GET` | `/orders` | Lista paginada (admin: todos; cliente: próprios) | `200` com `Page<OrderDTO>` |
| `GET` | `/orders/{id}` | Busca por ID (admin ou dono) | `200` / `403` / `404` |
| `POST` | `/orders` | Cria pedido | `201` + `Location` / `404` / `422` |
| `PUT` | `/orders/{id}` | Atualiza momento/status (ADMIN) | `200` / `404` / `422` |
| `DELETE` | `/orders/{id}` | Remove pedido (ADMIN) | `204` / `404` |

```bash
curl -X POST "http://localhost:8081/orders" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": 2,
    "items": [
      { "productId": 1, "quantity": 2 }
    ]
  }'
```

O preço do item é sempre obtido do produto no momento da criação; o total é calculado a
partir dos itens. O status inicial é `WAITING_PAYMENT`.

### Pagamentos

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `GET` | `/payments` | Lista paginada (ADMIN) | `200` com `Page<PaymentDTO>` |
| `GET` | `/payments/{id}` | Busca por ID (ADMIN) | `200` / `404` |
| `POST` | `/payments` | Cria pagamento para um pedido (ADMIN) | `201` + `Location` / `404` / `422` |
| `PUT` | `/payments/{id}` | Atualiza o momento (ADMIN) | `200` / `404` / `422` |
| `DELETE` | `/payments/{id}` | Remove pagamento (ADMIN) | `204` / `404` |

```bash
curl -X POST "http://localhost:8081/payments" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 1,
    "moment": "2026-11-13T12:30:00Z"
  }'
```

### Itens de pedido

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `GET` | `/order-items` | Lista paginada (ADMIN) | `200` com `Page<OrderItemDTO>` |
| `GET` | `/order-items/{orderId}/{productId}` | Busca pela chave composta (ADMIN) | `200` / `404` |
| `POST` | `/order-items` | Cria item (ADMIN) | `200` / `404` / `422` |
| `PUT` | `/order-items/{orderId}/{productId}` | Atualiza item (ADMIN) | `200` / `404` / `422` |
| `DELETE` | `/order-items/{orderId}/{productId}` | Remove item (ADMIN) | `204` / `404` |

## Estrutura do projeto

```text
dscommerce/
├── src/
│   ├── main/
│   │   ├── java/com/teste/dscommerce/
│   │   │   ├── DscommerceApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── SwaggerConfig.java
│   │   │   ├── controllers/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── PaymentController.java
│   │   │   │   ├── OrderItemController.java
│   │   │   │   └── handlers/ControllerExceptionHandler.java
│   │   │   ├── dto/
│   │   │   ├── entities/
│   │   │   ├── exceptions/
│   │   │   ├── mappers/
│   │   │   ├── repositories/
│   │   │   ├── security/
│   │   │   │   ├── JwtConfig.java
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── UserDetailsServiceImpl.java
│   │   │   │   ├── RestAuthenticationEntryPoint.java
│   │   │   │   └── RestAccessDeniedHandler.java
│   │   │   └── services/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-test.properties
│   │       └── data.sql
│   └── test/java/com/teste/dscommerce/
│       ├── DscommerceApplicationTests.java
│       └── AuthIntegrationTests.java
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## Modelo de dados

- `User` 1:N `Order` (`tb_order.user_id`)
- `Order` 1:1 `Payment` (chave compartilhada via `@MapsId`)
- `Order` 1:N `OrderItem` e `Product` 1:N `OrderItem` (chave composta `order_id` + `product_id`)
- `Product` N:N `Category` (`tb_product_category`)
- `User` N:N `Role` (`tb_user_role`)

`OrderStatus`: `WAITING_PAYMENT`, `PAID`, `SHIPPED`, `DELIVERED`, `CANCELED`.

## Testes

```bash
./mvnw test
```

Cobertura atual:

- `AuthIntegrationTests`: login com sucesso, senha inválida (401), rota protegida sem token (401),
  produto público (200), cliente bloqueado em `/users` (403), admin liberado em `/users` (200).
- `DscommerceApplicationTests`: carregamento do contexto Spring.

## Revisão de JPA: diagnóstico e plano

### Pontos fortes

- Separação consistente em camadas, com DTOs e MapStruct.
- Injeção de dependência por construtor nos serviços e configurações.
- `spring.jpa.open-in-view=false` e `@Transactional` explícito nas leituras e escritas.
- Paginação padronizada e tratamento de exceções centralizado.
- Modelagem de `OrderItem` como classe de associação com chave composta.
- Resource Server com JWT e checagem de posse em pedidos.

### Achados

| Severidade | Achado | Local | Status |
|---|---|---|---|
| Alta | Preço do item aceito do DTO em vez de derivado do produto | `OrderItemService.java:63`, `OrderItemService.java:74` | Concluído |
| Alta | `Propagation.SUPPORTS` em operações de escrita (delete não transacional) | `ProductService.java:63`, `UserService.java:104`, `OrderService.java:126`, `PaymentService.java:69`, `OrderItemService.java:82` | Concluído |
| Alta | `OrderStatus` sem `@Enumerated`, gravando ordinal | `Order.java:29` | Concluído |
| Alta | `Payment` com `@GeneratedValue` + `@MapsId` (semântica contraditória) | `Payment.java:17-26` | Concluído |
| Alta | Pagamento duplicado para o mesmo pedido não é impedido | `PaymentService.java:47-55` | Concluído |
| Alta | Dinheiro em `Double` | `Product.java:26`, `OrderItem.java:15`, `Order.java:100` | Concluído |
| Alta | `Instant` mapeado como `TIMESTAMP WITHOUT TIME ZONE` | `Order.java:27`, `Payment.java:21` | Concluído |
| Média | N+1 ao mapear pedidos em listagens | `OrderService.java:58-64`, `OrderMapper.java` | Pendente |
| Média | `User.roles` em `FetchType.EAGER` | `User.java:36` | Pendente |
| Média | `OrderService.update` aceita `moment` do DTO e não valida transições de status | `OrderService.java:88-98` | Pendente |
| Média | `equals/hashCode` inconsistente entre entidades e instáveis para ids nulos | `Category.java:53-62`, `OrderItemPK.java:38-50` | Concluído |
| Média | `Role.authority` sem restrição de unicidade | `Role.java:17` | Concluído |
| Média | Ausência de Flyway/Liquibase; seed com IDs implícitos | `data.sql`, `application-test.properties` | Pendente |
| Média | Categorias existem no modelo, mas não são expostas na API | `ProductDTO.java`, `ProductMapper.java:13-15` | Pendente |
| Baixa | `setSubTotal` deriva preço e divide por `quantity` (risco de NPE/divisão por zero) | `OrderItem.java:43-48` | Concluído |
| Baixa | Listagem de itens ordenada por `quantity` | `OrderItemService.java:50` | Concluído |
| Baixa | Ausência de `@Version` para concorrência otimista | `Order.java` | Concluído |
| Baixa | `imgUrl` sem tamanho explícito (default 255) | `Product.java:27` | Pendente |
| Baixa | Estilo: `GenerationType` qualificado em alguns pontos, `Serializable` com FQN, pacote `com.teste` | Várias entidades | Pendente |

### Plano de correção

**Fase 1 - Risco e baixo esforço** (parcialmente concluída)

- [x] Derivar preço sempre de `Product` em `OrderItemService`.
- [x] Trocar `Propagation.SUPPORTS` por `REQUIRED` nos deletes.
- [x] Remover `setSubTotal`.
- [x] Impedir pagamento duplicado (sincronizar status do pedido ao pagar ainda pendente).
- [ ] Validar transições de status e ignorar `moment` no update.
- [x] Aplicar `@Enumerated(EnumType.STRING)` e ajustar o `data.sql`.

**Fase 2 - Modelagem e consistência** (concluída)

- [x] Migrar valores monetários para `BigDecimal` com `precision`/`scale` (`numeric(12,2)`).
- [x] Usar timestamp com time zone para `Instant` (`timestamp with time zone`).
- [x] Padronizar `equals/hashCode` (id apenas quando não nulo, `hashCode` estável).
- [x] Unicidade de `Role.authority` (Fase 1) e `Payment.order_id` (PK via `@MapsId`).
- [x] Adicionar `@Version` em `Order` (seed com `version = 0`).

**Fase 3 - Performance e estrutura**

- Adicionar `@EntityGraph` nas consultas de pedidos para eliminar N+1.
- Avaliar `User.roles` como `LAZY` com `@EntityGraph` no login.
- Introduzir Flyway/Liquibase e seeds reproduzíveis.
- Decidir se categorias serão expostas na API ou removidas do modelo.

**Fase 4 - Evolução**

- Refinar autorização com `@PreAuthorize` para regras de posse.
- Adicionar refresh token, rotação de segredo e rate limit no login.
- Criar profile de PostgreSQL com migrations e testes com Testcontainers.

## Principais mudanças refletidas neste README

- Spring Security com Resource Server e JWT HS256.
- Login próprio em `POST /auth/login` com `LoginDTO` e `TokenDTO`.
- Roles persistidas em `tb_role` e associadas a `User` via `tb_user_role`.
- Autorização por rota e por posse de pedido.
- Respostas JSON padronizadas para 401 e 403.
- CRUDs de pedidos, pagamentos e itens de pedido.
- Consultas de usuário por email e por nome.
- Hash BCrypt válido para os usuários do seed.
- Testes de integração de autenticação e autorização.
- Driver PostgreSQL adicionado para evolução do ambiente de banco.
- Correções da Fase 1 da revisão de JPA (preço derivado do produto, `@Enumerated(STRING)`,
  `@MapsId` no pagamento, bloqueio de pagamento duplicado, unicidade de `Role.authority`,
  `equals/hashCode` e ordenação dos itens de pedido).
- Correções da Fase 2 da revisão de JPA (valores monetários em `BigDecimal`, `Instant` com
  time zone, `equals/hashCode` padronizado e `@Version` em `Order`).

## Observações e pendências conhecidas

- `spring.profiles.active=test` está fixo no `application.properties`; o ideal é deixar o profile
  ser escolhido por ambiente.
- O H2 Console está liberado sem autenticação e deve ser desabilitado fora do ambiente local.
- `application-test.properties` está em `src/main/resources`; para uso estritamente de testes,
  o local recomendado é `src/test/resources`.
- Ainda não existe profile/configuração de PostgreSQL nem migrations.
- O segredo JWT padrão é apenas para desenvolvimento.
- A ordenação das listagens é fixa por serviço (`name` para produtos/usuários, `moment` para
  pedidos e pagamentos).

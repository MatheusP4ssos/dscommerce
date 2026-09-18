# DSCommerce

Sistema de e-commerce desenvolvido com Spring Boot, em arquitetura em camadas (Controller → Service → Repository), utilizando JPA/Hibernate para persistência, MapStruct para mapeamento de DTOs, SpringDoc/OpenAPI para documentação e H2 como banco de dados.

## Funcionalidades implementadas

- Entidades do domínio de e-commerce: `User`, `Order`, `Product`, `Category`, `OrderItem`, `Payment` e `OrderStatus`
- Relacionamentos JPA completos (1:N, 1:1, N:N e classe de associação com chave composta)
- **API REST completa de Produtos** em arquitetura em camadas:
  - `GET /products` — listagem com paginação
  - `GET /products/{id}` — consulta por ID
  - `POST /products` — inserção
  - `PUT /products/{id}` — atualização
  - `DELETE /products/{id}` — exclusão
- Paginação com ordenação fixa por `name`
- Mapeamento `Product ↔ ProductDTO` com **MapStruct** (geração automática em tempo de compilação)
- Tratamento de exceções padronizado com `@ControllerAdvice` / `@ExceptionHandler`
- Documentação interativa com **Swagger UI** (SpringDoc)
- Seed de dados completo (`data.sql`): 30 produtos, 6 categorias, 5 usuários, 8 pedidos, pagamentos e itens de pedido

## Tecnologias

- Java 17
- Spring Boot 3.5.16
- Spring Data JPA / Hibernate
- Spring Web (MVC)
- MapStruct 1.6.3
- SpringDoc OpenAPI 2.7.0 (Swagger UI)
- H2 Database (em memória)
- Maven

## Pré-requisitos

- Java 17 ou superior
- Maven 3.8+

## Como Rodar

```bash
# Compilar o projeto
./mvnw clean compile

# Executar os testes
./mvnw test

# Rodar a aplicação
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

Swagger UI disponível em: `http://localhost:8080/swagger-ui.html`

H2 Console disponível em: `http://localhost:8080/h2-console`

## Estrutura do Projeto

```
dscommerce/
├── src/
│   ├── main/
│   │   ├── java/com/teste/dscommerce/
│   │   │   ├── DscommerceApplication.java
│   │   │   ├── config/                  → SwaggerConfig (OpenAPI)
│   │   │   ├── controllers/             → ProductController, StandardError
│   │   │   │   └── handlers/            → ControllerExceptionHandler
│   │   │   ├── dto/                     → ProductDTO, CustomError
│   │   │   ├── entities/                → User, Order, OrderItem, OrderItemPK,
│   │   │   │                              OrderStatus, Payment, Product, Category
│   │   │   ├── exceptions/              → ResourceNotFoundException
│   │   │   ├── mappers/                 → ProductMapper (MapStruct)
│   │   │   ├── repositories/            → ProductRepository
│   │   │   └── services/                → ProductService
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-test.properties
│   │       └── data.sql                 → seed de dados
│   └── test/
│       └── java/com/teste/dscommerce/
│           └── DscommerceApplicationTests.java
├── pom.xml
└── mvnw / mvnw.cmd
```

## API - Endpoints de Produtos

| Método | Rota | Descrição | Retorno |
|--------|------|-----------|---------|
| `GET` | `/products` | Lista produtos com paginação e ordenação fixa por `name` | `200` — `Page<ProductDTO>` |
| `GET` | `/products/{id}` | Busca produto por ID | `200` — `ProductDTO` / `404` |
| `POST` | `/products` | Insere novo produto | `201` — `ProductDTO` com header `Location` |
| `PUT` | `/products/{id}` | Atualiza produto existente | `200` — `ProductDTO` / `404` |
| `DELETE` | `/products/{id}` | Remove produto | `204` sem corpo / `404` |

### Exemplos

**Listar produtos (página 0, tamanho 10, ordenado por `name`):**

```bash
curl "http://localhost:8080/products?page=0&size=10"
```

**Inserir produto:**

```bash
curl -X POST "http://localhost:8080/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mouse Gamer",
    "description": "Mouse ergonomico com sensor 16000 DPI.",
    "price": 249.90,
    "imgUrl": "https://.../mouse.jpg"
  }'
```

> A rota `GET /products` recebe `page` e `size` como `@RequestParam` (valores padrão: `0` e `10`). A ordenação é sempre por `name`, definida em `ProductService.findAll`.

## MapStruct

O projeto utiliza o **MapStruct** como annotation processor. A interface `ProductMapper` declara os métodos de conversão e a implementação `ProductMapperImpl` é gerada automaticamente na compilação (em `target/generated-sources/annotations`).

```java
@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductDTO toDTO(Product entity);
  Product toEntity(ProductDTO dto);
}
```

- `componentModel = "spring"` registra a implementação como bean `@Component`, permitindo a injeção via construtor no `ProductService`.
- O `ProductDTO` possui construtor padrão e setters, requisitos para o mapeamento DTO → Entidade.

## Tratamento de Exceções

A `ResourceNotFoundException`, lançada pelo service quando um produto não é encontrado, é capturada pelo `ControllerExceptionHandler` (`@ControllerAdvice`), que devolve uma resposta JSON padronizada:

```json
{
  "timestamp": "2026-09-04T15:40:00Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "Id não encontrado: 999",
  "path": "/products/{id}"
}
```

## Modelo de Dados - Diagrama de Entidades

```
┌─────────────────────────┐         ┌─────────────────────────┐
│        tb_user          │         │       tb_order          │
├─────────────────────────┤         ├─────────────────────────┤
│ id (PK, BIGINT)         │    1  N │ id (PK, BIGINT)         │
│ name (VARCHAR)          │────────▶│ moment (TIMESTAMP)      │
│ email (VARCHAR)         │         │ status (VARCHAR)        │
│ phone (VARCHAR)         │         │ user_id (FK, BIGINT)    │
│ birth_date (DATE)       │         └────────────┬────────────┘
│ password (VARCHAR)      │                      │
│ roles (VARCHAR)         │                      │ 1
└─────────────────────────┘                      │
                                                 │ 1
                                                 ▼
                                    ┌─────────────────────────┐
                                    │       tb_payment        │
                                    ├─────────────────────────┤
                                    │ id (PK, BIGINT)         │
                                    │ moment (TIMESTAMP)      │
                                    │ order_id (FK, BIGINT)   │
                                    └─────────────────────────┘

┌─────────────────────────┐            ┌───────────────────────────┐
│       tb_order          │ 1        N │       tb_order_item       │ N        1 ┌─────────────────────────┐
├─────────────────────────┤───────────▶├───────────────────────────┤◀───────────┤      tb_product         │
│ id (PK, BIGINT)         │            │ order_id (PK_FK, BIGINT)  │            ├─────────────────────────┤
│ ...                     │            │ product_id (PK_FK, BIGINT)│            │ id (PK, BIGINT)         │
└─────────────────────────┘            │ quantity (INTEGER)        │            │ name (VARCHAR)          │
                                       │ price (DOUBLE)            │            │ description (TEXT)      │
                                       └───────────────────────────┘            │ price (DOUBLE)          │
                                                                                │ img_url (VARCHAR)       │
                                                                                └────────────┬────────────┘
                                                                                             │
                                                                                             │ M:N
                                                                                             ▼
                                                                               ┌─────────────────────────┐
                                                                               │  tb_product_category    │
                                                                               │   (Tabela de Junção)    │
                                                                               ├─────────────────────────┤
                                                                               │ product_id (FK, BIGINT) │
                                                                               │ category_id (FK, BIGINT)│
                                                                               └────────────┬────────────┘
                                                                                            │
                                                                                            │ N:M
                                                                                            ▼
                                                                               ┌─────────────────────────┐
                                                                               │      tb_category        │
                                                                               ├─────────────────────────┤
                                                                               │ id (PK, BIGINT)         │
                                                                               │ name (VARCHAR)          │
                                                                               └─────────────────────────┘
```

## Relacionamentos JPA

### 1. OneToMany / ManyToOne — User ↔ Order

Um **usuário** pode ter vários **pedidos**, mas cada pedido pertence a apenas um usuário.

**User.java:**
```java
@OneToMany(mappedBy = "client")
private List<Order> orders = new ArrayList<>();
```

**Order.java:**
```java
@ManyToOne
@JoinColumn(name = "user_id")
private User client;
```

| Anotação | O que faz |
|----------|-----------|
| `@OneToMany(mappedBy = "client")` | Define que User é o lado "inverso" do relacionamento. O campo `client` em Order é o dono da relationship. |
| `@ManyToOne` | Indica que vários Orders apontam para um User. |
| `@JoinColumn(name = "user_id")` | Define o nome da coluna de chave estrangeira na tabela `tb_order`. |

### 2. OneToOne — Order ↔ Payment

Cada **pedido** possui apenas um **pagamento**, e cada pagamento pertence a apenas um pedido.

**Order.java:**
```java
@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
private Payment payment;
```

**Payment.java:**
```java
@OneToOne
@MapsId
private Order order;
```

| Anotação | O que faz |
|----------|-----------|
| `@OneToOne(mappedBy = "order")` | Define que Order é o lado "inverso". Payment é o dono do relacionamento. |
| `@MapsId` | Faz com que a chave primária de Payment seja a mesma de Order (mesmo ID). |
| `cascade = CascadeType.ALL` | Ao deletar um Order, o Payment associado também é deletado. |

### 3. ManyToMany — Product ↔ Category

Um **produto** pode pertencer a várias **categorias**, e uma categoria pode conter vários produtos.

**Product.java:**
```java
@ManyToMany
@JoinTable(name = "tb_product_category",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id")
)
private Set<Category> categories = new HashSet<>();
```

**Category.java:**
```java
@ManyToMany(mappedBy = "categories")
private Set<Product> products = new HashSet<>();
```

| Anotação | O que faz |
|----------|-----------|
| `@ManyToMany` | Define o relacionamento N:N entre Product e Category. |
| `@JoinTable` | Cria a tabela de junção `tb_product_category` no banco de dados. |
| `joinColumns` | Coluna que referencia a entidade dona do relacionamento (Product). |
| `inverseJoinColumns` | Coluna que referencia a entidade do lado inverso (Category). |
| `mappedBy = "categories"` | Indica que Category é o lado "inverso". O campo `categories` em Product é o dono. |
| `Set<>` (em vez de `List`) | Evita duplicatas e é mais eficiente para operações de busca em relacionamentos M:N. |

### 4. Enum — OrderStatus

O campo `status` usa um enum para garantir valores válidos:

**OrderStatus.java:**
```java
public enum OrderStatus {
    WAITING_PAYMENT,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELED;
}
```

**Order.java:**
```java
private OrderStatus status;
```

> O JPA armazena o nome do enum como String no banco. Para armazenar o ordinal (número), use `@Enumerated(EnumType.ORDINAL)`.

### 5. ManyToMany com Classe de Associação — Order ↔ Product (OrderItem)

Quando o relacionamento N:N precisa guardar dados extras (por exemplo, `quantity` e `price` do item no momento da compra), o ideal é **não** usar `@ManyToMany` direto.  
Nesse caso, criamos uma **classe de associação** (`OrderItem`) e modelamos como:

- `Order 1:N OrderItem`
- `Product 1:N OrderItem`

Assim, o N:N entre `Order` e `Product` é representado indiretamente por `OrderItem`.

> A chave primária de `OrderItem` é **composta** pela combinação de `order_id` + `product_id`.  
> Isso é representado pela classe `OrderItemPK` anotada com `@Embeddable` e injetada via `@EmbeddedId`.

**OrderItem.java (entidade de associação):**
```java
@Entity
@Table(name = "tb_order_item")
public class OrderItem {

  @EmbeddedId
  private OrderItemPK id = new OrderItemPK();
  private Integer quantity;
  private Double price;

  public OrderItem() {
  }

  public OrderItem(Order order, Product product, Integer quantity, Double price) {
    id.setOrder(order);
    id.setProduct(product);
    this.quantity = quantity;
    this.price = price;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Order getOrder() {
    return id.getOrder();
  }

  public void setOrder(Order order) {
    id.setOrder(order);
  }

  public Product getProduct() {
    return id.getProduct();
  }

  public void setProduct(Product product) {
    id.setProduct(product);
  }
}
```

**OrderItemPK.java (chave composta):**
```java
@Embeddable
public class OrderItemPK implements Serializable {

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  public OrderItemPK() {
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }
}
```

**Quando usar `@ManyToMany` direto vs classe de associação**

| Cenário | Melhor abordagem |
|---------|------------------|
| Apenas vínculo entre as entidades (sem campos extras) | `@ManyToMany` com tabela de junção |
| Precisa de atributos no relacionamento (`quantity`, `price`, `discount`, etc.) | Classe de associação (`OrderItem`) |
| Precisa de regras de negócio/auditoria por item do vínculo | Classe de associação (`OrderItem`) |

> Regra prática: se a tabela de junção virar uma entidade com significado próprio no domínio, trate-a como entidade (classe de associação), não como `@ManyToMany` simples.

### 6. Geração de Chaves Primárias

Todas as entidades usam auto-incremento:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

`GenerationType.IDENTITY` utiliza o auto-incremento do banco de dados (compatível com H2, MySQL, PostgreSQL).

---

## Observações

- O profile ativo por padrão é `test`, que utiliza H2 **em memória** (`jdbc:h2:mem:dscommerce`). Os dados são recarregados a cada inicialização pelo `data.sql`.
- Para produção, recomenda-se substituir o H2 por PostgreSQL ou MySQL.
- A listagem paginada (`GET /products`) sempre ordena por `name`, independentemente do request.

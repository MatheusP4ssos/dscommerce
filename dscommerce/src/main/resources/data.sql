-- =============================================
-- SEED COMPLETO: DSCOMMERCE
-- =============================================

-- =============================================
-- 1. USERS
-- =============================================
INSERT INTO tb_user (name, email, phone, birth_date, password, roles) VALUES
('Administrador', 'admin@dscommerce.com', '(11) 99999-0000', '1985-01-15', '$2a$10$HY2l0QqJ2RwqGzB5x3Yz3uXqZ8bYf0dK1mN4pO6rS8tU0vW2xY4', 'ROLE_ADMIN'),
('Maria Silva', 'maria@email.com', '(11) 98888-1111', '1990-05-20', '$2a$10$HY2l0QqJ2RwqGzB5x3Yz3uXqZ8bYf0dK1mN4pO6rS8tU0vW2xY4', 'ROLE_CLIENT'),
('Joao Souza', 'joao@email.com', '(21) 97777-2222', '1988-08-10', '$2a$10$HY2l0QqJ2RwqGzB5x3Yz3uXqZ8bYf0dK1mN4pO6rS8tU0vW2xY4', 'ROLE_CLIENT'),
('Ana Costa', 'ana@email.com', '(31) 96666-3333', '1995-12-03', '$2a$10$HY2l0QqJ2RwqGzB5x3Yz3uXqZ8bYf0dK1mN4pO6rS8tU0vW2xY4', 'ROLE_CLIENT'),
('Pedro Lima', 'pedro@email.com', '(41) 95555-4444', '1992-03-25', '$2a$10$HY2l0QqJ2RwqGzB5x3Yz3uXqZ8bYf0dK1mN4pO6rS8tU0vW2xY4', 'ROLE_CLIENT');

-- =============================================
-- 2. CATEGORIES
-- =============================================
INSERT INTO tb_category (name) VALUES ('Livros');
INSERT INTO tb_category (name) VALUES ('Eletronicos');
INSERT INTO tb_category (name) VALUES ('Moda');
INSERT INTO tb_category (name) VALUES ('Casa');
INSERT INTO tb_category (name) VALUES ('Jardim');
INSERT INTO tb_category (name) VALUES ('Brinquedos');

-- =============================================
-- 3. PRODUCTS (30 itens)
-- =============================================

-- Livros
INSERT INTO tb_product (name, description, price, img_url) VALUES ('O Senhor dos Aneis', 'Livro de fantasia epica de J.R.R. Tolkien.', 90.50, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Codigo Limpo', 'Boas praticas de programacao de Robert C. Martin.', 85.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Domain-Driven Design', 'Modelagem de software complexo de Eric Evans.', 120.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('O Pequeno Principe', 'Classico da literatura mundial de Antoine de Saint-Exupery.', 35.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/4-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('1984', 'Distopia klassica de George Orwell.', 45.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/5-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Clean Architecture', 'Guia de arquitetura de software de Robert C. Martin.', 110.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/6-big.jpg');

-- Eletronicos
INSERT INTO tb_product (name, description, price, img_url) VALUES ('iPhone 15 Pro', 'Smartphone Apple iPhone 15 Pro 256GB.', 8999.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/7-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Samsung Galaxy S24', 'Smartphone Samsung Galaxy S24 Ultra 512GB.', 7499.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/8-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('iPad Air', 'Tablet Apple iPad Air M2 256GB.', 6299.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/9-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('MacBook Pro 14', 'Notebook Apple MacBook Pro 14 polegadas M3 Pro.', 16999.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/10-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('AirPods Pro 2', 'Fones Apple AirPods Pro 2a geracao com cancelamento de ruido.', 1899.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/11-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Kindle Paperwhite', 'Leitor eletronico Amazon Kindle Paperwhite 11a geracao.', 549.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/12-big.jpg');

-- Moda
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Camiseta Polo', 'Camiseta polo social e confortavel.', 89.90, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/13-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Tenis Nike Air Max', 'Tenis Nike Air Max 270 conforto e estilo.', 599.90, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/14-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Jaqueta Adidas', 'Jaqueta esportiva Adidas Impermeavel.', 349.90, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/15-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Relogio Casio', 'Relogio classico Casio resistente a agua.', 199.90, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/16-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Bolsa Feminina', 'Bolsa de couro sintetico elegante e resistente.', 159.90, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/17-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Carteira de Couro', 'Carteira masculina de couro legitimo.', 129.90, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/18-big.jpg');

-- Casa
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Cafeteira Nespresso', 'Cafeteira Nespresso Vertuo com 30 cápsulas.', 499.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/19-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Aspirador Robot', 'Aspirador robot inteligente com mapeamento.', 1299.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/20-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Jogo de Panelas', 'Jogo de panelas antiaderente 5 pecas.', 349.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/21-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Air Fryer', 'Air Fryer digital 5L com timer digital.', 399.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/22-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Luminaria LED', 'Luminaria de mesa LED dimmeravel e moderna.', 89.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/23-big.jpg');

-- Jardim
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Kit Ferramentas Jardim', 'Kit com 5 ferramentas para jardinagem.', 79.90, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/24-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Mesa de Jardim', 'Mesa de jardim dobravel em alumínio.', 299.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/25-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Cadeira Retratil', 'Cadeira retratil de exterior com estrutura reforcada.', 189.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/26-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Vasos Decorativos', 'Kit com 3 vasos decorativos de ceramica.', 69.90, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/27-big.jpg');

-- Brinquedos
INSERT INTO tb_product (name, description, price, img_url) VALUES ('LEGO Star Wars', 'Kit LEGO Star Wars Millennium Falcon 75375 pecas.', 599.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/28-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Jogo de Tabuleiro', 'Jogo de tabuleiro strategy para toda familia.', 149.00, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/29-big.jpg');
INSERT INTO tb_product (name, description, price, img_url) VALUES ('Cubo Magico', 'Cubo magico speed 3x3 profissional.', 49.90, 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/30-big.jpg');

-- =============================================
-- 4. PRODUCT <-> CATEGORY (associações N:N)
-- =============================================

-- Livros (cat 1)
INSERT INTO tb_product_category (product_id, category_id) VALUES (1, 1);  -- O Senhor dos Aneis
INSERT INTO tb_product_category (product_id, category_id) VALUES (2, 1);  -- Codigo Limpo
INSERT INTO tb_product_category (product_id, category_id) VALUES (3, 1);  -- DDD
INSERT INTO tb_product_category (product_id, category_id) VALUES (4, 1);  -- O Pequeno Principe
INSERT INTO tb_product_category (product_id, category_id) VALUES (5, 1);  -- 1984
INSERT INTO tb_product_category (product_id, category_id) VALUES (6, 1);  -- Clean Architecture
INSERT INTO tb_product_category (product_id, category_id) VALUES (12, 1); -- Kindle (tambem como leitura)

-- Eletronicos (cat 2)
INSERT INTO tb_product_category (product_id, category_id) VALUES (7, 2);   -- iPhone 15
INSERT INTO tb_product_category (product_id, category_id) VALUES (8, 2);   -- Samsung Galaxy
INSERT INTO tb_product_category (product_id, category_id) VALUES (9, 2);   -- iPad Air
INSERT INTO tb_product_category (product_id, category_id) VALUES (10, 2);  -- MacBook Pro
INSERT INTO tb_product_category (product_id, category_id) VALUES (11, 2);  -- AirPods
INSERT INTO tb_product_category (product_id, category_id) VALUES (12, 2);  -- Kindle (tambem eletronico)

-- Moda (cat 3)
INSERT INTO tb_product_category (product_id, category_id) VALUES (13, 3);  -- Camiseta Polo
INSERT INTO tb_product_category (product_id, category_id) VALUES (14, 3);  -- Tenis Nike
INSERT INTO tb_product_category (product_id, category_id) VALUES (15, 3);  -- Jaqueta Adidas
INSERT INTO tb_product_category (product_id, category_id) VALUES (16, 3);  -- Relogio Casio
INSERT INTO tb_product_category (product_id, category_id) VALUES (17, 3);  -- Bolsa Feminina
INSERT INTO tb_product_category (product_id, category_id) VALUES (18, 3);  -- Carteira de Couro

-- Casa (cat 4)
INSERT INTO tb_product_category (product_id, category_id) VALUES (19, 4);  -- Cafeteira
INSERT INTO tb_product_category (product_id, category_id) VALUES (20, 4);  -- Aspirador Robot
INSERT INTO tb_product_category (product_id, category_id) VALUES (21, 4);  -- Jogo de Panelas
INSERT INTO tb_product_category (product_id, category_id) VALUES (22, 4);  -- Air Fryer
INSERT INTO tb_product_category (product_id, category_id) VALUES (23, 4);  -- Luminaria LED

-- Jardim (cat 5)
INSERT INTO tb_product_category (product_id, category_id) VALUES (24, 5);  -- Kit Ferramentas
INSERT INTO tb_product_category (product_id, category_id) VALUES (25, 5);  -- Mesa de Jardim
INSERT INTO tb_product_category (product_id, category_id) VALUES (26, 5);  -- Cadeira Retratil
INSERT INTO tb_product_category (product_id, category_id) VALUES (27, 5);  -- Vasos Decorativos

-- Brinquedos (cat 6)
INSERT INTO tb_product_category (product_id, category_id) VALUES (28, 6);  -- LEGO
INSERT INTO tb_product_category (product_id, category_id) VALUES (29, 6);  -- Jogo de Tabuleiro
INSERT INTO tb_product_category (product_id, category_id) VALUES (30, 6);  -- Cubo Magico
INSERT INTO tb_product_category (product_id, category_id) VALUES (29, 1);  -- Jogo de Tabuleiro (tambem como livro/estrategia)

-- =============================================
-- 5. ORDERS (8 pedidos)
-- =============================================
INSERT INTO tb_order (moment, status, user_id) VALUES
('2024-01-15 10:30:00', 3, 2);    -- Order 1: Maria - DELIVERED
INSERT INTO tb_order (moment, status, user_id) VALUES
('2024-02-20 14:15:00', 3, 3);    -- Order 2: Joao - DELIVERED
INSERT INTO tb_order (moment, status, user_id) VALUES
('2024-03-10 09:45:00', 2, 2);    -- Order 3: Maria - SHIPPED
INSERT INTO tb_order (moment, status, user_id) VALUES
('2024-04-05 16:20:00', 1, 4);    -- Order 4: Ana - PAID
INSERT INTO tb_order (moment, status, user_id) VALUES
('2024-05-12 11:00:00', 1, 5);    -- Order 5: Pedro - PAID
INSERT INTO tb_order (moment, status, user_id) VALUES
('2024-06-18 13:30:00', 0, 3);    -- Order 6: Joao - WAITING_PAYMENT
INSERT INTO tb_order (moment, status, user_id) VALUES
('2024-07-22 08:00:00', 4, 4);    -- Order 7: Ana - CANCELED
INSERT INTO tb_order (moment, status, user_id) VALUES
('2024-08-01 17:45:00', 2, 2);    -- Order 8: Maria - SHIPPED

-- =============================================
-- 6. PAYMENTS (1:1 com cada Order)
-- =============================================
INSERT INTO tb_payment (order_id, moment) VALUES (1, '2024-01-15 10:35:00');
INSERT INTO tb_payment (order_id, moment) VALUES (2, '2024-02-20 14:20:00');
INSERT INTO tb_payment (order_id, moment) VALUES (3, '2024-03-10 09:50:00');
INSERT INTO tb_payment (order_id, moment) VALUES (4, '2024-04-05 16:25:00');
INSERT INTO tb_payment (order_id, moment) VALUES (5, '2024-05-12 11:05:00');
INSERT INTO tb_payment (order_id, moment) VALUES (6, '2024-06-18 13:35:00');
INSERT INTO tb_payment (order_id, moment) VALUES (7, '2024-07-22 08:05:00');
INSERT INTO tb_payment (order_id, moment) VALUES (8, '2024-08-01 17:50:00');

-- =============================================
-- 7. ORDER ITEMS (itens dos pedidos)
-- =============================================

-- Order 1 (Maria - DELIVERED): 3 itens
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (1, 1, 1, 90.50);   -- O Senhor dos Aneis
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (1, 4, 2, 35.00);   -- O Pequeno Principe x2
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (1, 7, 1, 8999.00); -- iPhone 15

-- Order 2 (Joao - DELIVERED): 2 itens
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (2, 10, 1, 16999.00); -- MacBook Pro
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (2, 11, 1, 1899.00);  -- AirPods Pro

-- Order 3 (Maria - SHIPPED): 4 itens
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (3, 2, 1, 85.00);   -- Codigo Limpo
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (3, 3, 1, 120.00);  -- DDD
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (3, 6, 1, 110.00);  -- Clean Architecture
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (3, 12, 1, 549.00); -- Kindle

-- Order 4 (Ana - PAID): 3 itens
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (4, 13, 2, 89.90);   -- Camiseta Polo x2
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (4, 14, 1, 599.90);  -- Tenis Nike
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (4, 16, 1, 199.90);  -- Relogio Casio

-- Order 5 (Pedro - PAID): 2 itens
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (5, 20, 1, 1299.00); -- Aspirador Robot
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (5, 22, 1, 399.00);  -- Air Fryer

-- Order 6 (Joao - WAITING_PAYMENT): 3 itens
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (6, 28, 1, 599.00);  -- LEGO Star Wars
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (6, 29, 2, 149.00);  -- Jogo de Tabuleiro x2
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (6, 30, 3, 49.90);   -- Cubo Magico x3

-- Order 7 (Ana - CANCELED): 2 itens
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (7, 19, 1, 499.00);  -- Cafeteira Nespresso
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (7, 21, 1, 349.00);  -- Jogo de Panelas

-- Order 8 (Maria - SHIPPED): 3 itens
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (8, 5, 1, 45.00);   -- 1984
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (8, 8, 1, 7499.00);  -- Samsung Galaxy S24
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (8, 15, 1, 349.90);  -- Jaqueta Adidas

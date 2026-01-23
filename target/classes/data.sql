-- CREATE TABLE users (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   name VARCHAR(150),
--   email VARCHAR(150) UNIQUE,
--   phone VARCHAR(20),
--   password VARCHAR(255),
--   role VARCHAR(20),
--   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- CREATE TABLE addresses (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   user_id BIGINT,
--   line1 VARCHAR(255),
--   city VARCHAR(100),
--   state VARCHAR(100),
--   pincode VARCHAR(20),
--   FOREIGN KEY (user_id) REFERENCES users(id)
-- );

-- CREATE TABLE orders (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   user_id BIGINT,
--   pickup_address_id BIGINT,
--   delivery_address_id BIGINT,
--   status VARCHAR(50),
--   amount DECIMAL(12,2),
--   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--   FOREIGN KEY (user_id) REFERENCES users(id),
--   FOREIGN KEY (pickup_address_id) REFERENCES addresses(id),
--   FOREIGN KEY (delivery_address_id) REFERENCES addresses(id)
-- );

-- CREATE TABLE order_items (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   order_id BIGINT,
--   product_id BIGINT,
--   description VARCHAR(255),
--   quantity INT DEFAULT 1,
--   weight DOUBLE,
--   price DECIMAL(10,2),
--   FOREIGN KEY (order_id) REFERENCES orders(id),
--   FOREIGN KEY (product_id) REFERENCES products(product_id)
-- );

-- CREATE TABLE couriers (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   user_id BIGINT,
--   vehicle_info VARCHAR(255),
--   current_lat DOUBLE,
--   current_lng DOUBLE,
--   status VARCHAR(50),
--   FOREIGN KEY (user_id) REFERENCES users(id)
-- );

-- CREATE TABLE delivery_assignments (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   order_id BIGINT,
--   courier_id BIGINT,
--   assigned_at TIMESTAMP,
--   accepted_at TIMESTAMP,
--   delivered_at TIMESTAMP,
--   FOREIGN KEY (order_id) REFERENCES orders(id),
--   FOREIGN KEY (courier_id) REFERENCES couriers(id)
-- );

-- CREATE TABLE tracking_events (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   order_id BIGINT,
--   event_type VARCHAR(50),
--   lat DOUBLE,
--   lng DOUBLE,
--   timestamp TIMESTAMP,
--   meta TEXT,
--   FOREIGN KEY (order_id) REFERENCES orders(id)
-- );

-- CREATE TABLE payments (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   order_id BIGINT,
--   amount DECIMAL(12,2),
--   payment_status VARCHAR(50),
--   provider_ref VARCHAR(255),
--   FOREIGN KEY (order_id) REFERENCES orders(id)
-- );
-- CREATE TABLE products (
--   product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   product_name VARCHAR(255) NOT NULL,
--   description VARCHAR(255) NOT NULL,
--   price DECIMAL(10,2) NOT NULL,
--   stock INT NOT NULL,
--   category VARCHAR(100),
--   sku VARCHAR(100)
-- );

-- -- Insert sample products
-- INSERT INTO products (product_name, description, price, stock, category, sku) VALUES
-- ('Electronics', 'Electronic items shipping', 99.99, 100, 'Electronics', 'ELEC-001'),
-- ('Books', 'Books and publications', 29.99, 200, 'Books', 'BOOK-001'),
-- ('Clothing', 'Apparel and clothing', 49.99, 150, 'Clothing', 'CLOTH-001'),
-- ('Furniture', 'Furniture items', 299.99, 50, 'Furniture', 'FURN-001'),
-- ('Groceries', 'Grocery items', 14.99, 500, 'Groceries', 'GROC-001');

-- -- Insert sample users
-- INSERT INTO users (name, email, phone, password, role, created_at) VALUES
-- ('John Doe', 'john@example.com', '9876543210', '$2a$10$R9h6cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ee3KMUgKLvvQa9jS', 'USER', NOW()),
-- ('Jane Smith', 'jane@example.com', '9876543211', '$2a$10$R9h6cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ee3KMUgKLvvQa9jS', 'USER', NOW()),
-- ('Admin User', 'admin@example.com', '9876543212', '$2a$10$R9h6cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ee3KMUgKLvvQa9jS', 'ADMIN', NOW()),
-- ('Courier One', 'courier1@example.com', '9876543213', '$2a$10$R9h6cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ee3KMUgKLvvQa9jS', 'DELIVERY_GUY', NOW()),
-- ('Courier Two', 'courier2@example.com', '9876543214', '$2a$10$R9h6cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ee3KMUgKLvvQa9jS', 'DELIVERY_GUY', NOW());

-- -- Insert sample addresses
-- -- INSERT INTO addresses (user_id, line1, city, state, pincode) VALUES
-- (1, '123 Main St', 'New York', 'NY', '10001'),
-- (1, '456 Oak Ave', 'Los Angeles', 'CA', '90001'),
-- (2, '789 Pine Rd', 'Chicago', 'IL', '60601'),
-- (2, '321 Elm St', 'Houston', 'TX', '77001'),
-- (3, '654 Maple Dr', 'Phoenix', 'AZ', '85001');

-- -- Insert sample orders
-- INSERT INTO orders (user_id, pickup_address_id, delivery_address_id, status, amount, created_at) VALUES
-- (1, 1, 2, 'CREATED', 149.98, NOW()),
-- (1, 1, 2, 'ASSIGNED', 99.99, NOW()),
-- (2, 3, 4, 'CREATED', 299.99, NOW()),
-- (2, 3, 4, 'DELIVERED', 199.99, NOW());

-- -- Insert sample couriers
-- INSERT INTO couriers (user_id, vehicle_info, current_lat, current_lng, status) VALUES
-- (4, 'Honda Activa - MH04AB1234', 28.7041, 77.1025, 'ACTIVE'),
-- (5, 'Bajaj Discover - MH04AB5678', 28.7041, 77.1025, 'INACTIVE');

-- -- Insert sample delivery assignments
-- INSERT INTO delivery_assignments (order_id, courier_id, assigned_at, accepted_at, delivered_at) VALUES
-- (2, 1, NOW(), NOW(), NULL),
-- (4, 1, NOW(), NOW(), NOW());

-- -- Insert sample tracking events
-- INSERT INTO tracking_events (order_id, event_type, lat, lng, timestamp, meta) VALUES
-- (1, 'ORDER_CREATED', 28.7041, 77.1025, NOW(), 'Order placed by user'),
-- (1, 'PICKUP_SCHEDULED', 28.7041, 77.1025, NOW(), 'Pickup scheduled'),
-- (2, 'COURIER_ASSIGNED', 28.7041, 77.1025, NOW(), 'Courier assigned'),
-- (2, 'IN_TRANSIT', 28.7050, 77.1100, NOW(), 'Package in transit'),
-- (4, 'DELIVERED', 28.7050, 77.1100, NOW(), 'Package delivered');

-- -- Insert sample payments
-- INSERT INTO payments (order_id, amount, payment_status, provider_ref) VALUES
-- (1, 149.98, 'PENDING', 'TXN_20251223_001'),
-- (2, 99.99, 'COMPLETED', 'TXN_20251223_002'),
-- (3, 299.99, 'PENDING', 'TXN_20251223_003'),
-- (4, 199.99, 'COMPLETED', 'TXN_20251223_004');

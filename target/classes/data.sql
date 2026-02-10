insert into users (email, name, password, phone, role)
values
('u1@gmail.com','User One','pwd','9999999991','USER'),
('u2@gmail.com','User Two','pwd','9999999992','USER');

insert into addresses (user_id, line1, city, state, pincode)
values
(1,'Kotla Road','Delhi','Delhi','110049'),
(2,'MG Road','Bangalore','KA','560001');

insert into products (name, price, stock)
values
('Laptop', 50000, 10),
('Phone', 20000, 25);

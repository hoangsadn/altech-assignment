-- SQL script to create tables and insert sample data


-- Insert sample data into user_profile table with role column
INSERT INTO users (username, password, role) VALUES
  ('admin', '$2a$10$HhfkbCDnEr3bZWNUauM6N.lRsWVwllliQXtr.eTRBNhFtVr0TXln6', 'ADMIN'),
  ('user', '$2a$10$OzfXs0o4uorISDeGOy5/X.GrVrdzPUtGp25HLJP.Akl0v2DTln/B.', 'USER');
--
-- -- Insert sample data into product table
INSERT INTO product (id, name, description, category, price, stock, version, image_url) VALUES
  (1,'Laptop', 'High performance laptop', 'Electronics', 1200.00, 10, 0, 'a '),
  (2,'Smartphone', 'Latest model smartphone', 'Electronics', 800.00, 20, 0,' a'),
  (3,'Headphones', 'Noise cancelling headphones', 'Accessories', 150.00, 30, 0, ' a');
--
-- INSERT INTO basket (id, user_id ) VALUES
--   (1, 1); -- User's basket
--
-- INSERT INTO basket_products (basket_id, product_id) VALUES
--   (1, 1), -- User's basket with one laptop
--   (1, 2); -- User's basket with two smartphones

-- insert deal sample
INSERT INTO deal (description, code, expiration, active) VALUES
('Buy 1 get 50% off the second', 'BuyOneGetOne', '2025-08-01T23:59:59', true),
 ('20% off all electronics', 'TEN_PERCENT_OFF', '2025-08-15T23:59:59', true);
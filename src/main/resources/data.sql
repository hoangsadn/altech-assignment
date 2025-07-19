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

-- -- Insert sample data into basket table
-- INSERT INTO basket (user_id) VALUES
--   (1),
--   (2);

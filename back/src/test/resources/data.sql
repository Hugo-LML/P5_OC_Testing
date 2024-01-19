INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES
  ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
  ('Lemerle', 'Hugo', true, 'test@gmail.com', '$2a$10$kpC.ncOduhLmmcrvwUHBxe1vU67vuykJMGYgk/hM76cmsjUSNa7jK');

INSERT INTO SESSIONS (name, description, teacher_id, date)
VALUES
  ('NewSession', 'Description', 1, '2024-01-19 00:00:00'),
  ('NewSession2', 'Description2', 1, '2024-01-19 00:00:00'),;
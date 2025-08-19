INSERT INTO users
(username, password, enabled, roles, email, age, gender, bio)
VALUES
('sikwate',
'{bcrypt}$2a$10$.GQqJ6hN06FmfTlHypz4q.TEGI23TNNHHllJWM2H51sloSYR4gte6',
true,
'ROLE_USER',
'jaymezing@gmail.com',
26,
'Male',
'In the edges mapping my lost synapses, mending my neuroplasticity...')
ON CONFLICT (username) DO NOTHING;
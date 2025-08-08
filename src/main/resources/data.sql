INSERT INTO authorities
(username, authority)
VALUES
('sikwate', 'ROLE_ADMIN')
ON CONFLICT (username, authority) DO NOTHING;

INSERT INTO users
(username, password, enabled, age, sex, bio)
VALUES
('sikwate','{bcrypt}$2a$10$.GQqJ6hN06FmfTlHypz4q.TEGI23TNNHHllJWM2H51sloSYR4gte6', true,
26,
'M',
'In the edges mapping my lost synapses, mending my neuroplasticity...')
ON CONFLICT (username) DO NOTHING;
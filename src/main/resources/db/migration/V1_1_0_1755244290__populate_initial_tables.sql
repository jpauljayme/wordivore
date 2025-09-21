INSERT INTO users
(username, password, enabled, roles, email, gender, bio)
VALUES
('sikwate',
'{bcrypt}$2a$10$.GQqJ6hN06FmfTlHypz4q.TEGI23TNNHHllJWM2H51sloSYR4gte6',
true,
'ROLE_USER',
'jaymezing@gmail.com',
'Male',
'In the edges mapping my lost synapses, mending my neuroplasticity...')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users
(username, password, enabled, roles, email, gender, bio)
VALUES
('admin',
'{bcrypt}$2a$12$93N/wmtk3bG2kWNGYl3KSOYOvQI0G98.SwWiwthOOaCoVwcTDFwAW',
true,
'ROLE_ADMIN',
'admin@gmail.com',
'Male',
'Admin forreal')
ON CONFLICT (username) DO NOTHING;
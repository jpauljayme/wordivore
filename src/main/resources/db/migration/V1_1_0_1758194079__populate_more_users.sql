INSERT INTO users
(username, password, enabled, roles, email, gender, bio)
VALUES
('testuser@1',
'{bcrypt}$2a$12$eNvXHgHRTD6XfhiV.ynNtunSPXdUw.eHJP.fJSwj1CGKtkMDG5HvO',
true,
'ROLE_USER',
'testuser@1@gmail.com',
'Female',
'I can never read all the books I want; I can never be all the people I want and live all the lives I want.')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users
(username, password, enabled, roles, email, gender, bio)
VALUES
('testuser@2',
'{bcrypt}$2a$12$eNvXHgHRTD6XfhiV.ynNtunSPXdUw.eHJP.fJSwj1CGKtkMDG5HvO',
true,
'ROLE_USER',
'testuser@2@gmail.com',
'Female',
'I took a deep breath and listened to the old brag of my heart. I am, I am, I am.')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users
(username, password, enabled, roles, email, gender, bio)
VALUES
('testuser@3',
'{bcrypt}$2a$12$eNvXHgHRTD6XfhiV.ynNtunSPXdUw.eHJP.fJSwj1CGKtkMDG5HvO',
true,
'ROLE_USER',
'testuser@3@gmail.com',
'Female',
'Kiss me, and you will see how important I am.')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users
(username, password, enabled, roles, email, gender, bio)
VALUES
('testuser@4',
'{bcrypt}$2a$12$eNvXHgHRTD6XfhiV.ynNtunSPXdUw.eHJP.fJSwj1CGKtkMDG5HvO',
true,
'ROLE_USER',
'testuser@4@gmail.com',
'Female',
'Perhaps when we find ourselves wanting everything, it is because we are dangerously close to wanting nothing.')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users
(username, password, enabled, roles, email, gender, bio)
VALUES
('testuser@5',
'{bcrypt}$2a$12$eNvXHgHRTD6XfhiV.ynNtunSPXdUw.eHJP.fJSwj1CGKtkMDG5HvO',
true,
'ROLE_USER',
'testuser@5@gmail.com',
'Female',
'The silence depressed me. It wasn''t the silence of silence. It was my own silence.')
ON CONFLICT (username) DO NOTHING;

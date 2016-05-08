--Initial Mock Data For Testing Purposes
INSERT INTO ThesisTarget(id,min_age,max_age,gender,currency,min_income,max_income) VALUES (1,10,20,'M','EUR',20000,25000);
INSERT INTO ThesisTarget(id,min_age,max_age,gender,currency,min_income,max_income) VALUES (2,20,30,'F','USD',25000,30000);
INSERT INTO ThesisTarget(id,min_age,max_age,gender,currency,min_income,max_income) VALUES (3,30,40,'M','EUR',35000,40000);
INSERT INTO ThesisTarget(id,min_age,max_age,gender,currency,min_income,max_income) VALUES (4,40,50,'F','USD',45000,50000);

INSERT INTO Thesis(id,country,subject,target_id) VALUES (1,'ES',00000000,1);
INSERT INTO Thesis(id,country,subject,target_id) VALUES (2,'ES',00000001,2);
INSERT INTO Thesis(id,country,subject,target_id) VALUES (3,'ES',00000002,3);
INSERT INTO Thesis(id,country,subject,target_id) VALUES (4,'ES',00000003,4);
INSERT INTO Thesis(id,country,subject,target_id) VALUES (5,'ITA',00000000,1);
INSERT INTO Thesis(id,country,subject,target_id) VALUES (6,'ITA',00000001,2);
INSERT INTO Thesis(id,country,subject,target_id) VALUES (7,'ITA',00000002,3);
INSERT INTO Thesis(id,country,subject,target_id) VALUES (8,'ITA',00000003,4);

--Auth Info
INSERT INTO User(id,username,password) values (1,'Nightswimming','NSM');
INSERT INTO User(id,username,password) values (2,'Nightswimming2','NSM2');
INSERT INTO User(id,username,password) values (3,'Nightswimming3','NSM3'); 

INSERT INTO Role(id,name) values (1,'ADMIN');
INSERT INTO Role(id,name) values (2,'USER');
INSERT INTO Role(id,name) values (3,'GUEST'); 

INSERT INTO User_Role(user_id, roles_id) values (1,1);
INSERT INTO User_Role(user_id, roles_id) values (2,2);
INSERT INTO User_Role(user_id, roles_id) values (3,3);
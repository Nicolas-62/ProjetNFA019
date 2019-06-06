-- Ajout compte admin :
insert into user(id,identifiant,password,droits) values(1,'AdminAdmin','adminadmin',1);
-- Ajout de medecins : 
insert into user(id,identifiant,password,droits) values(8,'NicolasLourdel','123Soleil!',2);
insert into medecin(nom, prenom, specialite, tel, mail, adresse, ville, cp,user_id) 
values('lourdel','nicolas','generaliste','0320998877','nicolas_lourdel@gmail.com','8 rue des oies','Arras','62000',8);
insert into user(id,identifiant,password,droits) values(2,'NicolasSergent','123Soleil!',2);
insert into medecin(nom, prenom, specialite, tel, mail, adresse, ville, cp,user_id) 
values('Sergent','nicolas','sexologue','0320776655','nicolas_sergent@yahoo.fr','10 rue de l''hippodrome','Marcq en Baroeul','59600',2);
insert into user(id,identifiant,password,droits) values(3,'FrançoisDugenou','123Soleil!',2);
insert into medecin(nom, prenom, specialite, tel, mail, adresse, ville, cp,user_id) 
values('DuGenou','françois','kinesithérapeute','0320429043','dugenou@yahoo.fr','10 rue de marseille','Agny','62230',3);

-- Ajout de creneaux pour chaque medecin
-- Medecin 1
insert into creneaux(h_debut,m_debut,h_fin,m_fin,medecin_id)
values(9,0,9,30,1); /* 1 = premier medecin inséré soit nicolas lourdel, 
						on peut prendre avec lui chaque jour de 9h00 à 9h30 */
insert into creneaux(h_debut,m_debut,h_fin,m_fin,medecin_id)
values(9,30,10,0,1);
-- Medecin 2
insert into creneaux(h_debut,m_debut,h_fin,m_fin,medecin_id)
values(9,0,9,30,2);
insert into creneaux(h_debut,m_debut,h_fin,m_fin,medecin_id)
values(9,30,10,0,2);
-- Medecin 3
insert into creneaux(h_debut,m_debut,h_fin,m_fin,medecin_id)
values(9,0,9,30,3);
insert into creneaux(h_debut,m_debut,h_fin,m_fin,medecin_id)
values(9,30,10,0,3);

-- Ajout de patients :
insert into user(id,identifiant,password,droits) values(4,'RogerDupond','123Soleil!',3);
insert into patient(num_secu,civilite,nom,prenom,tel,mail,adresse,ville,cp,sexe,user_id)
values('1860159350299','M.','Dupond','Roger','0320435678','dupond@gmail.com','12 allee des chênes','Boulogne','62220','h',4);
insert into user(id,identifiant,password,droits) values(5,'FredericLechamp','fred345!',3);
insert into patient(num_secu,civilite,nom,prenom,tel,mail,adresse,ville,cp,sexe,user_id)
values('3340159350299','M.','Lechamp','Frederic','0360435678','fredlechamp@gmail.com','3 rue des blés','Marseille','13000','h',5);
insert into user(id,identifiant,password,droits) values(6,'SylvieLemoine','syl504!',3);
insert into patient(num_secu,civilite,nom,prenom,tel,mail,adresse,ville,cp,sexe,user_id)
values('1860153957299','Mme','Lemoine','Sylvie','09880435678','syllem@gmail.com','34 rue de la bourse','Nantes','33000','f',6);
insert into user(id,identifiant,password,droits) values(7,'PaulineLoiseau','paulloi333!',3);
insert into patient(num_secu,civilite,nom,prenom,tel,mail,adresse,ville,cp,sexe,user_id)
values('1860153957344','Mme','Loiseau','Pauline','02600435678','loiseau@gmail.com','90 rue de la poste','Bordeaux','22000','f',7);



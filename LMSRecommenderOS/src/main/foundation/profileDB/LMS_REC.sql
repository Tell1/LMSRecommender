drop table LMS_COURSE_DONE;
drop table LMS_DOC_DONE;
drop table LMS_EXE_DONE;
drop table LMS_REC;

create table LMS_REC (usr_ID INTEGER,usr_age INTEGER,usr_name varchar(100),usr_degree varchar(100),usr_zipcode INTEGER,usr_avg_grade INTEGER, usr_avg_difficulty INTEGER, usr_avg_eval INTEGER,doc_ID INTEGER,doc_name varchar(100),doc_date varchar(50),doc_subject varchar(100),doc_difficulty INTEGER,rat_ID INTEGER,rat_rating INTEGER,constraint pk_usr_doc primary key (usr_ID));--,index fk_course (course_ID),constraint fk_course foreign key (course_ID) references LMS_COURSE_DONE (course_ID);
insert into LMS_REC values(1,19,'Pepe2013','Informatica',123,6,7,5,1,'Algorithms,Knuth','01-Jan-1994','Algoritmia',1,1,4);
insert into LMS_REC values(2,21,'Juan2013','Informatica',123,2,8,6,2,'Artificial Neural Networks,Rosenblatt','01-Jan-1994','Inteligencia_Artificial',2,2,2);
insert into LMS_REC values(3,33,'Jose2013','Informatica',123,3,9,4,'Redes',3,'Networks,Tanenbaum','01-Jan-1994','Redes',3,3,1);
insert into LMS_REC values(4,14,'Miguel2013','Informatica',123,6,7,3,'Inteligencia_Artificial',4,'Data Structures and Algorithm Analysis,Shaffer','01-Jan-1994','Algoritmia',4,4,2);
insert into LMS_REC values(5,23,'Sara2013',Comunicacion_Audiovisual,123,6,7,2,'Redes',5,'Artificial Neural Networks,Rosenblatt','01-Jan-1994','Inteligencia_Artificial',5,5,5);
insert into LMS_REC values(6,22,'Pepe2013','Electronica_Industrial',1234,6,7,5,'Algoritmia',6,'Networks,Tanenbaum','01-Jan-1994','Redes',6,6,3);

create table LMS_COURSE_DONE (course_ID INTEGER,course_name varchar(100),usr_ID INTEGER,constraint pk_courseD primary key (course_ID),constraint fk_courseD foreign key (usr_ID) references LMS_REC (usr_ID));
insert into LMS_COURSE_DONE values(1, 'Algoritmia',1);
insert into LMS_COURSE_DONE values(2, 'Inteligencia_Artificial',1);
insert into LMS_COURSE_DONE values(3, 'Redes',1);
insert into LMS_COURSE_DONE values(4, 'Redes',2);
insert into LMS_COURSE_DONE values(5, 'Algoritmia',2);

create table LMS_DOC_DONE (docD_ID INTEGER,docD_name varchar(100),usr_ID INTEGER,constraint pk_docD primary key (docD_ID),constraint fk_docD foreign key (usr_ID) references LMS_REC (usr_ID));
insert into LMS_DOC_DONE values(1,'Algorithms,Knuth',1);
insert into LMS_DOC_DONE values(2,'Artificial Neural Networks,Rosenblatt',1);
insert into LMS_DOC_DONE values(3,'Artificial Neural Networks,Rosenblatt',2);

create table LMS_EXE_DONE (exeD_ID INTEGER,exeD_name varchar(100),usr_ID INTEGER,constraint pk_exeD primary key (exeD_ID),constraint fk_exeD foreign key (usr_ID) references LMS_REC (usr_ID));
insert into LMS_EXE_DONE values(1,'Ejercicios de complejidades',1);
insert into LMS_EXE_DONE values(2,'Ejercicios de perceptron simple',1);
insert into LMS_EXE_DONE values(3,'Ejercicios de perceptron simple',2);
insert into book (id, titolo, anno_pubblicazione) values(nextval('book_seq'), 'I Malavoglia',1881);
insert into book (id, titolo, anno_pubblicazione) values(nextval('book_seq'), 'I Promessi Sposi',1827);
insert into author (id, nome, cognome, data_nascita, data_morte, nazionalita) values (nextval('author_seq'), 'Italo', 'Calvino', '1923-10-15', '1985-09-19', 'Italiana');
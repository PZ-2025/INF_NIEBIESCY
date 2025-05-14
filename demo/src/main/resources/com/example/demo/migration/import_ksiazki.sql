-- Dodanie ksiazki
INSERT INTO ksiazki (tytul, id_autora, id_gatunku, wydawnictwo, data_dodania, ISBN, ilosc)
VALUES
-- Adam Mickiewicz
('Poematy',
    (SELECT id_autora FROM autorzy WHERE nazwa = 'Adam Mickiewicz'),
    (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
    'Wydawnictwo E', NOW(), '978-83-5678-901-2', 6),

-- Henryk Sienkiewicz
('Quo Vadis',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Henryk Sienkiewicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo F', NOW(), '978-83-6789-012-3', 10),

('Ogniem i Mieczem',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Henryk Sienkiewicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo G', NOW(), '978-83-7890-123-4', 8),

('Potop',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Henryk Sienkiewicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo H', NOW(), '978-83-8901-234-5', 6),

('Pan Wolodyjowski',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Henryk Sienkiewicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo I', NOW(), '978-83-9012-345-6', 7),

('Bez dogmatu',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Henryk Sienkiewicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo J', NOW(), '978-83-0123-456-7', 9),

('W pustyni i w puszczy',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Henryk Sienkiewicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura dziecieca'),
  'Wydawnictwo K', NOW(), '978-83-1234-567-8', 5),

('Na marne',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Henryk Sienkiewicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo L', NOW(), '978-83-2345-678-9', 6),

-- Wislawa Szymborska
('Lektury nadobowiazkowe',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Wislawa Szymborska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo M', NOW(), '978-83-3456-789-0', 12),

('Milosc',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Wislawa Szymborska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo N', NOW(), '978-83-4567-890-1', 8),

('Ruchome piaski',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Wislawa Szymborska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo O', NOW(), '978-83-5678-901-2', 7),

('Poczatek',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Wislawa Szymborska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo P', NOW(), '978-83-6789-012-3', 9),

('Wiersze wybrane',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Wislawa Szymborska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo Q', NOW(), '978-83-7890-123-4', 10),

-- Juliusz Slowacki
('Kordian',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Juliusz Slowacki'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat romantyczny'),
  'Wydawnictwo R', NOW(), '978-83-8901-234-5', 6),

('Beniowski',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Juliusz Slowacki'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo S', NOW(), '978-83-9012-345-6', 8),

('Balladyna',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Juliusz Slowacki'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo T', NOW(), '978-83-0123-456-7', 7),

('Horsztynski',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Juliusz Slowacki'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo U', NOW(), '978-83-1234-567-8', 5),

('Lilla Weneda',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Juliusz Slowacki'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo V', NOW(), '978-83-2345-678-9', 6),

('Ojciec zadzumionych',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Juliusz Slowacki'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo W', NOW(), '978-83-3456-789-0', 5),

('Krol-Duch',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Juliusz Slowacki'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja epicka'),
  'Wydawnictwo X', NOW(), '978-83-4567-890-1', 6),

-- Maria Dabrowska
('Noce i dnie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Maria Dabrowska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Literackie', NOW(), '978-83-1111-100-1', 10),

('Usmiech losu',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Maria Dabrowska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Znak', NOW(), '978-83-1111-100-2', 8),

-- Zofia Nalkowska
('Granica',
    (SELECT id_autora FROM autorzy WHERE nazwa = 'Zofia Nalkowska'),
    (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
    'Wydawnictwo A', NOW(), '978-83-5678-901-2', 7),

('Zupa',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Zofia Nalkowska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo B', NOW(), '978-83-6789-012-3', 5),

-- Stanislaw Wyspianski
('Wesele',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Stanislaw Wyspianski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat romantyczny'),
  'Wydawnictwo D', NOW(), '978-83-8901-234-5', 8),

('Noc listopadowa',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Stanislaw Wyspianski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat romantyczny'),
  'Wydawnictwo E', NOW(), '978-83-9012-345-6', 7),

('Protesilas i Laodamia',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Stanislaw Wyspianski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat romantyczny'),
  'Wydawnictwo F', NOW(), '978-83-0123-456-7', 6),

-- Tadeusz Rejtan
('Pan Tadeusz',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Tadeusz Rejtan'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo G', NOW(), '978-83-2345-678-9', 10),

('Lalka',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Tadeusz Rejtan'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat romantyczny'),
  'Wydawnictwo I', NOW(), '978-83-4567-890-1', 8),

-- Jan Kochanowski
('Treny',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Jan Kochanowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo J', NOW(), '978-83-5678-901-2', 6),

('Fraszki',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Jan Kochanowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo K', NOW(), '978-83-6789-012-3', 5),

('Piesni',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Jan Kochanowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo L', NOW(), '978-83-7890-123-4', 7),

-- Bronislaw Pilsudski
('Ziemia Pilsudskiego',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Bronislaw Pilsudski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Biografia'),
  'Wydawnictwo M', NOW(), '978-83-9012-345-6', 6),

('Podroze do Japonii',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Bronislaw Pilsudski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo N', NOW(), '978-83-2345-678-9', 5),

('Na tropach Japonczykow',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Bronislaw Pilsudski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo O', NOW(), '978-83-3456-789-0', 7),

-- Wladyslaw Reymont
('Chlopi',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Wladyslaw Reymont'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo P', NOW(), '978-83-7890-123-4', 10),

('Ziemia obiecana',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Wladyslaw Reymont'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo Q', NOW(), '978-83-8901-234-5', 8),

('Mocny czlowiek',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Wladyslaw Reymont'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo R', NOW(), '978-83-9012-345-6', 7),

-- Czeslaw Milosz
('Zniewolony umysl',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Czeslaw Milosz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo S', NOW(), '978-83-1234-567-9', 9),

('Dolina Issy',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Czeslaw Milosz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Biografia'),
  'Wydawnictwo T', NOW(), '978-83-2345-678-9', 8),

('Kroniki',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Czeslaw Milosz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo U', NOW(), '978-83-3456-789-0', 7),

('Lustra Lustra',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Adam Asnyk'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Science-Fiction'),
  'Wydawnictwo Delta', NOW(), '978-83-8695-361-3', 7),

('Lustra Zdrada',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Adam Asnyk'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Biografia'),
  'Wydawnictwo Delta', NOW(), '978-83-5963-732-0', 3),

('Tajemnica Kraina ciszy',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Adam Asnyk'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Delta', NOW(), '978-83-2901-759-2', 8),

('Wiatr historii Lustra',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Adam Asnyk'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo Alfa', NOW(), '978-83-4436-003-1', 9),

('Zdrada Przeznaczenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Adam Asnyk'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Biografia'),
  'Wydawnictwo Delta', NOW(), '978-83-5776-795-9', 9),

('Tajemnica Cienie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Aleksander Kaminski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Alfa', NOW(), '978-83-3494-502-9', 9),

('Tajemnica Przeznaczenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Aleksander Kaminski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Biografia'),
  'Wydawnictwo Alfa', NOW(), '978-83-3736-960-6', 9),

('Wspomnienia Tajemnicaa',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Andrzej Sapkowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo Beta', NOW(), '978-83-0576-333-4', 3),

('Cienie Lustra',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Andrzej Sapkowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo Alfa', NOW(), '978-83-4874-127-7', 6),

('Cienie Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Andrzej Sapkowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Delta', NOW(), '978-83-2695-761-9', 8),

('Wiedzmin',
 (SELECT id_autora FROM autorzy WHERE nazwa = 'Andrzej Sapkowski'),
 (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
 'Wydawnictwo Delta', NOW(), '978-83-2695-761-9', 8),

('Cienie Duchy przeszlosci',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Andrzej Sapkowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Gamma', NOW(), '978-83-2308-952-7', 6),

('Duchy przeszlosci Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Boleslaw Prus'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Alfa', NOW(), '978-83-2998-668-9', 3),

('Lustra Kraina ciszy 1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Boleslaw Prus'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura dziecieca'),
  'Wydawnictwo Beta', NOW(), '978-83-4389-284-3', 7),

('Wspomnienia Kraina ciszy',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Boleslaw Prus'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Fantasy'),
  'Wydawnictwo Gamma', NOW(), '978-83-5083-846-3', 9),

('Wiatr historii Miasto mgly',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Boleslaw Prus'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Fantasy'),
  'Wydawnictwo Delta', NOW(), '978-83-0271-851-3', 6),

('Tajemnica Wspomnienia',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Dariusz Michalski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Science-Fiction'),
  'Wydawnictwo Delta', NOW(), '978-83-0444-354-0', 5),

('Ostatni dzien Zdrada',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Dariusz Michalski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Horror'),
  'Wydawnictwo Delta', NOW(), '978-83-0992-033-7', 5),

('Dzien gniewu Dzien gniewu',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Dariusz Michalski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura dziecieca'),
  'Wydawnictwo Beta', NOW(), '978-83-5495-300-4', 4),

('Dzien gniewu Tajemnica',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Dariusz Michalski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat romantyczny'),
  'Wydawnictwo Alfa', NOW(), '978-83-3520-931-3', 9),

('Tajemnica Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Dariusz Michalski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat romantyczny'),
  'Wydawnictwo Alfa', NOW(), '978-83-0725-621-8', 6),

('Przeznaczenie Zdrada',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Elzbieta Cherezinska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo Alfa', NOW(), '978-83-1162-268-2', 3),

('Wspomnienia Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Elzbieta Cherezinska'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Alfa', NOW(), '978-83-5765-220-0', 9),

('Dzien gniewu Duchy przeszlosci 1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ewa Stachniak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Horror'),
  'Wydawnictwo Beta', NOW(), '978-83-2938-495-6', 8),

('Ostatni dzien Lustra',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ewa Stachniak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo Delta', NOW(), '978-83-5914-140-3', 5),

('Przebudzenie Wiatr historii',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ewa Stachniak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo Beta', NOW(), '978-83-9158-434-8', 10),

('Wiatr historii Duchy przeszlosci',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ewa Stachniak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Biografia'),
  'Wydawnictwo Alfa', NOW(), '978-83-2105-162-1', 9),

('Lustra Miasto mgly',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ewa Stachniak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Delta', NOW(), '978-83-2509-012-6', 5),

('Lustra Wspomnienia',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ewa Stachniak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo Delta', NOW(), '978-83-5517-345-0', 8),

('Zdrada Cienie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Grzegorz Roman'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Gamma', NOW(), '978-83-5097-572-5', 6),

('Tajemnica Tajemnica',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Grzegorz Roman'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Science-Fiction'),
  'Wydawnictwo Beta', NOW(), '978-83-8353-998-6', 3),


('Duchy przeszlosci Miasto mgly',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Grzegorz Roman'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Gamma', NOW(), '978-83-0787-007-3', 8),

('Wspomnienia Zdrada',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Gajos'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Alfa', NOW(), '978-83-0585-893-3', 9),

('Duchy przeszlosci Przeznaczenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Gajos'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Horror'),
  'Wydawnictwo Delta', NOW(), '978-83-7762-302-3', 6),

('Przeznaczenie Wiatr historii',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Gajos'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo Delta', NOW(), '978-83-8735-961-0', 4),

('Duchy przeszlosci Wspomnienia',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Gajos'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat romantyczny'),
  'Wydawnictwo Beta', NOW(), '978-83-3233-177-1', 8),

('Przeznaczenie Ostatni dzien',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Gajos'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Beta', NOW(), '978-83-4870-387-3', 3),


('Wspomnienia Zapomniany swiat as',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Korczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Delta', NOW(), '978-83-4707-237-5', 7),

('Zdrada Dzien gniewu',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Korczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Gamma', NOW(), '978-83-0868-070-3', 3),

('Wiatr historii Dzien gniewu',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Korczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Gamma', NOW(), '978-83-7605-129-1', 9),

('Miasto mgly Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Korczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Horror'),
  'Wydawnictwo Alfa', NOW(), '978-83-8387-569-8', 3),

('Miasto mgly Ostatni dzien',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Janusz Korczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Horror'),
  'Wydawnictwo Delta', NOW(), '978-83-2661-946-6', 6),

('Lustra Miasto mgly Grotowskiego',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Jerzy Grotowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo Delta', NOW(), '978-83-4427-900-7', 8),

('Tajemnica Cienie Grotowskiego',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Jerzy Grotowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Biografia'),
  'Wydawnictwo Gamma', NOW(), '978-83-6797-142-6', 7),

('Tajemnica Duchy przeszlosci',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Jerzy Grotowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja epicka'),
  'Wydawnictwo Alfa', NOW(), '978-83-6400-563-8', 9),

('Zapomniany swiat Zdrada',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Jerzy Pilch'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Alfa', NOW(), '978-83-4535-373-3', 5),

('Lustra Ostatni dzien',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Jerzy Pilch'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Delta', NOW(), '978-83-4248-874-6', 5),

('Duchy przeszlosci Przeznaczenie Pilch',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Jerzy Pilch'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Delta', NOW(), '978-83-3002-171-2', 9),

('Duchy przeszlosci Lustra',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Kazimierz Brandys'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Gamma', NOW(), '978-83-1167-734-9', 9),

('Wspomnienia Duchy przeszlosci 1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Kazimierz Brandys'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja epicka'),
  'Wydawnictwo Alfa', NOW(), '978-83-1692-850-5', 8),

('Cienie Miasto mgly',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Kazimierz Brandys'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Gamma', NOW(), '978-83-3622-755-2', 10),

('Miasto mgly Przeznaczenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Kazimierz Brandys'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo Delta', NOW(), '978-83-6550-884-4', 6),

('Tajemnica Ostatni  1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Kazimierz Brandys'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo Beta', NOW(), '978-83-8049-267-8', 4),

('Dzien gniewu Wiatr historii',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Kazimierz Brandys'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo Alfa', NOW(), '978-83-4892-187-5', 9),

('Zapomniany swiat Dzien gniewu',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Krzysztof Kieslowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura dziecieca'),
  'Wydawnictwo Gamma', NOW(), '978-83-2566-378-9', 10),

('Przeznaczenie Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Krzysztof Kieslowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Delta', NOW(), '978-83-8722-042-6', 6),

('Lustra Zdrada Kieslowki',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Krzysztof Kieslowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Gamma', NOW(), '978-83-2457-878-0', 4),

('Duchy przeszlosci Zdradaa',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Krzysztof Kieslowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Gamma', NOW(), '978-83-8506-110-4', 3),

('Kraina ciszy Przebudzenie 1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Leopold Tyrmand'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Alfa', NOW(), '978-83-6838-181-3', 10),

('Miasto mgly Duchy przeszlosci Tyrmasd',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Leopold Tyrmand'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Delta', NOW(), '978-83-7750-935-1', 5),

('Cienie Dzien gniewu',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Leopold Tyrmand'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Alfa', NOW(), '978-83-4594-869-3', 7),

('Przebudzenie Wiatr historii Tyrmad',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Leopold Tyrmand'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura dziecieca'),
  'Wydawnictwo Delta', NOW(), '978-83-3262-403-6', 5),

('Tajemnica Przebudzenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Leopold Tyrmand'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Delta', NOW(), '978-83-4972-499-5', 8),

('Ostatni dzien Wspomnienia',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Marek Hlasko'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo Gamma', NOW(), '978-83-8953-464-2', 7),

('Ostatni dzien Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Marek Hlasko'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Biografia'),
  'Wydawnictwo Delta', NOW(), '978-83-0889-104-7', 6),

('Wspomnienia Kraina ciszy Hlasko',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Marek Hlasko'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura dziecieca'),
  'Wydawnictwo Gamma', NOW(), '978-83-1907-615-2', 10),

('Lustra Dzien gniewu',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Marek Hlasko'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Beta', NOW(), '978-83-6114-800-2', 6),

('Miasto mgly Lustra',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Marek Hlasko'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja epicka'),
  'Wydawnictwo Delta', NOW(), '978-83-8987-600-6', 10),

('Przeznaczenie Cienie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Mieczyslaw Wojnicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Beta', NOW(), '978-83-1875-855-1', 4),

('Zdrada Duchy przeszlosci',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Mieczyslaw Wojnicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Science-Fiction'),
  'Wydawnictwo Beta', NOW(), '978-83-3122-322-0', 3),

('Przebudzenie Zapomniany swiat1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Mieczyslaw Wojnicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo Gamma', NOW(), '978-83-6333-816-9', 8),

('Zdrada Tajemnica',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Mieczyslaw Wojnicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Science-Fiction'),
  'Wydawnictwo Gamma', NOW(), '978-83-7387-620-2', 10),

('Tajemnica Ostatni dzien Esej',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Mieczyslaw Wojnicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo Beta', NOW(), '978-83-6453-891-4', 5),

('Zdrada Cienie Kadziela',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Natalia Kadziela'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo Delta', NOW(), '978-83-0892-101-1', 9),

('Miasto mgly Duchy przeszlosci',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Natalia Kadziela'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo Delta', NOW(), '978-83-3389-841-4', 3),

('Ostatni dzien Duchy przeszlosci',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Natalia Kadziela'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat romantyczny'),
  'Wydawnictwo Gamma', NOW(), '978-83-8069-070-1', 5),

('Zdrada Dzien  Olga',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Olga Tokarczuk'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Delta', NOW(), '978-83-4179-081-0', 10),

('Cienie Przebudzenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Olga Tokarczuk'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Alfa', NOW(), '978-83-4241-826-8', 7),

('Miasto mgly Zdrada',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Olga Tokarczuk'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo Beta', NOW(), '978-83-9530-837-1', 6),

('Przeznaczenie Kraina ciszy',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Pawel Huelle'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo Gamma', NOW(), '978-83-5303-525-6', 10),

('Przebudzenie Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Pawel Huelle'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Horror'),
  'Wydawnictwo Beta', NOW(), '978-83-0531-872-6', 3),

('Lustra Ostatni dzien1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Pawel Huelle'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja epicka'),
  'Wydawnictwo Beta', NOW(), '978-83-7846-897-2', 5),

('Przebudzenie Zdrada',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Pawel Huelle'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja epicka'),
  'Wydawnictwo Gamma', NOW(), '978-83-7010-270-1', 5),

('Duchy przeszlosci Cienie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Pawel Huelle'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo Beta', NOW(), '978-83-1581-944-5', 4),

('Wiatr historii Przeznaczenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Czerwinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo Gamma', NOW(), '978-83-9027-882-7', 3),

('Zapomniany swiat Dzien gniewu1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Czerwinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo Gamma', NOW(), '978-83-0389-498-4', 9),

('Tajemnica Przebudzenie 2',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Czerwinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Alfa', NOW(), '978-83-0063-717-1', 3),

('Zapomniany swiat Przebudzenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Czerwinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja epicka'),
  'Wydawnictwo Gamma', NOW(), '978-83-5833-308-7', 5),

('Wspomnienia Duchy przeszlosci',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Szymczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Alfa', NOW(), '978-83-0689-008-7', 4),

('Zdrada Miasto mgly',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Szymczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura dziecieca'),
  'Wydawnictwo Alfa', NOW(), '978-83-6019-837-5', 9),

('Lustra Cienie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Szymczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Alfa', NOW(), '978-83-9423-923-9', 3),

('Przebudzenie Kraina ciszy',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Szymczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Science-Fiction'),
  'Wydawnictwo Delta', NOW(), '978-83-9992-229-2', 6),

('Lustra Kraina ciszy',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Szymczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Horror'),
  'Wydawnictwo Delta', NOW(), '978-83-2602-445-9', 3),

('Przeznaczenie Kraina ciszy1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Piotr Szymczak'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja epicka'),
  'Wydawnictwo Delta', NOW(), '978-83-3455-480-2', 8),

('Cienie Wspomnienia',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Roman Dmowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Alfa', NOW(), '978-83-6696-099-3', 7),

('Zapomniany swiat Tajemnica',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Roman Dmowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo Delta', NOW(), '978-83-7491-244-6', 5),

('Przeznaczenie Wiatr historii1i',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Roman Dmowski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura dziecieca'),
  'Wydawnictwo Beta', NOW(), '978-83-7867-149-6', 8),

('Tajemnica Zapomniany swiat1',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ryszard Kapuscinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Beta', NOW(), '978-83-2152-411-1', 7),

('Miasto mgly Zdrada Kapuciski',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ryszard Kapuscinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Gamma', NOW(), '978-83-9599-340-1', 4),

('Ostatni dzien Dzien gniewu',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ryszard Kapuscinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Horror'),
  'Wydawnictwo Alfa', NOW(), '978-83-5582-629-8', 3),

('Tajemnica Ostatni dzien',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ryszard Kapuscinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo Delta', NOW(), '978-83-4982-942-3', 9),

('Zapomniany swiat Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ryszard Kapuscinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Science-Fiction'),
  'Wydawnictwo Gamma', NOW(), '978-83-3399-707-0', 4),

('Zapomniany swiat Przeznaczenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Ryszard Kapuscinski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Fantasy'),
  'Wydawnictwo Gamma', NOW(), '978-83-1805-308-6', 6),

('Dzien gniewu Duchy przeszlosci',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Stefan zeromski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Kryminal'),
  'Wydawnictwo Beta', NOW(), '978-83-4494-205-4', 6),

('Kraina ciszy Przebudzenie',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Stefan zeromski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Thriller'),
  'Wydawnictwo Alfa', NOW(), '978-83-4181-517-1', 7),

('Wiatr historii Ostatni dzien',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Stefan zeromski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Poezja'),
  'Wydawnictwo Beta', NOW(), '978-83-1559-287-0', 9),

('Lustra Zdradaa',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Stefan zeromski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Gamma', NOW(), '978-83-2677-893-7', 7),

('Duchy przeszlosci Zapomniany swiatt',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Stefan zeromski'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Dramat'),
  'Wydawnictwo Beta', NOW(), '978-83-0799-943-5', 10),

('Miasto mgly Wiatr historii',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Witold Gombrowicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Powiesc historyczna'),
  'Wydawnictwo Alfa', NOW(), '978-83-9451-600-5', 5),

('Lustra Tajemnica',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Witold Gombrowicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura faktu'),
  'Wydawnictwo Delta', NOW(), '978-83-3887-995-4', 7),

('Zdrada Ostatni dzien',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Witold Gombrowicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Esej'),
  'Wydawnictwo Gamma', NOW(), '978-83-5650-881-7', 6),

('Zapomniany swiat Kraina ciszy',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Witold Gombrowicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Literatura dziecieca'),
  'Wydawnictwo Delta', NOW(), '978-83-2348-762-6', 7),

('Wspomnienia Tajemnica',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Witold Gombrowicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Alfa', NOW(), '978-83-7505-093-3', 7),

('Wiatr historii Zapomniany swiat',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Witold Gombrowicz'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Fantasy'),
  'Wydawnictwo Beta', NOW(), '978-83-3442-283-4', 4),

('Ostatni dzien Ostatni dzien',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Zbigniew Herbert'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Delta', NOW(), '978-83-1397-941-1', 5),

('Zdrada Lustra',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Zbigniew Herbert'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Romans'),
  'Wydawnictwo Beta', NOW(), '978-83-7031-921-0', 8),

('Duchy przeszlosci Zdrada',
  (SELECT id_autora FROM autorzy WHERE nazwa = 'Zbigniew Herbert'),
  (SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = 'Fantasy'),
  'Wydawnictwo Alfa', NOW(), '978-83-5728-876-1', 6);


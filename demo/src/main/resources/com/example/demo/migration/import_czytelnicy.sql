INSERT IGNORE INTO `czytelnicy` (`id_czytelnika`, `imie`, `nazwisko`, `tel`, `email`, `haslo`, `ulica`, `miasto`)
VALUES
    (1, 'Konrad', 'Klatka', 432123123, 'konrad@wp.pl', '$2a$10$/qXFug0oVkSWTPDLQqbBBOfCkPmtxNVPP0XFZczNF6zdaCNOQMr3G', 'Dlugie 324', 'Jedlicze'),
    (2, 'Kamila', 'Biedron', 123123123, 'kamila@wp.pl', '$2a$10$qmLdaLYQ9z/feLeAFwqUD.NV4he7Phql1kJmlDXYqc7MEuxpHpw5m', 'Chrobrego 21', 'Krosno');

-- haslo==imie

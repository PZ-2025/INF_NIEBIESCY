-- Tworzenie bazy danych
CREATE DATABASE IF NOT EXISTS bibliotekadb;
USE bibliotekadb;

-- Tabela 'autorzy'
CREATE TABLE IF NOT EXISTS `autorzy` (
    `id_autora` int(11) NOT NULL AUTO_INCREMENT,
    `nazwa` varchar(50) NOT NULL,
    PRIMARY KEY (`id_autora`),
    UNIQUE KEY `unique_nazwa` (`nazwa`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabela 'czytelnicy'
CREATE TABLE IF NOT EXISTS `czytelnicy` (
    `id_czytelnika` int(11) NOT NULL AUTO_INCREMENT,
    `imie` varchar(20) NOT NULL,
    `nazwisko` varchar(30) NOT NULL,
    `tel` bigint(15) NOT NULL,
    `email` varchar(20) NOT NULL,
    `haslo` varchar(255) NOT NULL,
    `ulica` varchar(30) NOT NULL,
    `miasto` varchar(20) NOT NULL,
    PRIMARY KEY (`id_czytelnika`),
    UNIQUE KEY `unique_nazwa` (`email`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabela 'dostawcy'
CREATE TABLE IF NOT EXISTS `dostawcy` (
    `id_dostawcy` int(11) NOT NULL AUTO_INCREMENT,
    `nazwa_dostawcy` varchar(30) NOT NULL,
    `adres` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id_dostawcy`),
    UNIQUE KEY `unique_nazwa_dostawcy` (`nazwa_dostawcy`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabela 'ksiazki'
CREATE TABLE IF NOT EXISTS `ksiazki` (
    `id_ksiazki` int(11) NOT NULL AUTO_INCREMENT,
    `tytul` varchar(55) NOT NULL,
    `id_autora` int(11) NOT NULL,
    `wydawnictwo` varchar(30) NOT NULL,
    `data_dodania` DATETIME NOT NULL,
    `ISBN` varchar(20) NOT NULL,
    `ilosc` int(11) NOT NULL,
    PRIMARY KEY (`id_ksiazki`),
    KEY `fk_id_autora` (`id_autora`),
    FOREIGN KEY (`id_autora`) REFERENCES `autorzy` (`id_autora`),
    UNIQUE KEY `unique_tytul` (`tytul`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabela 'pracownicy'
CREATE TABLE IF NOT EXISTS `pracownicy` (
    `id_pracownika` int(11) NOT NULL AUTO_INCREMENT,
    `imie` varchar(20) NOT NULL,
    `nazwisko` varchar(20) NOT NULL,
    `tel` varchar(15) NOT NULL,
    `email` varchar(20) NOT NULL,
    `haslo` varchar(20) NOT NULL,
    `rola` varchar(10) NOT NULL,
    PRIMARY KEY (`id_pracownika`),
    UNIQUE KEY `unique_email` (`email`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabela 'wypozyczenia'
CREATE TABLE IF NOT EXISTS `wypozyczenia` (
    `id_wypozyczenia` int(11) NOT NULL AUTO_INCREMENT,
    `id_ksiazki` int(11) NOT NULL,
    `id_czytelnika` int(11) NOT NULL,
    `data_rezerwacji` datetime NOT NULL DEFAULT current_timestamp(),
    `data_oddania` datetime DEFAULT NULL,
    PRIMARY KEY (`id_wypozyczenia`),
    KEY `fk_id_czytelnika` (`id_czytelnika`),
    KEY `fk_id_ksiazki` (`id_ksiazki`),
    FOREIGN KEY (`id_czytelnika`) REFERENCES `czytelnicy` (`id_czytelnika`),
    FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazki` (`id_ksiazki`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabela 'zamowienia'
CREATE TABLE IF NOT EXISTS `zamowienia` (
    `id_zamowienia` int(11) NOT NULL AUTO_INCREMENT,
    `id_ksiazki` int(11) NOT NULL,
    `id_dostawcy` int(11) NOT NULL,
    `id_pracownika` int(11) NOT NULL,
    `ilosc` int(11) NOT NULL,
    PRIMARY KEY (`id_zamowienia`),
    KEY `fk_id_pracownika` (`id_pracownika`),
    KEY `fk_id_dostawcy` (`id_dostawcy`),
    KEY `fk_id_ksiazki2` (`id_ksiazki`),
    FOREIGN KEY (`id_pracownika`) REFERENCES `pracownicy` (`id_pracownika`),
    FOREIGN KEY (`id_dostawcy`) REFERENCES `dostawcy` (`id_dostawcy`),
    FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazki` (`id_ksiazki`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

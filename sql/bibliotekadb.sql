-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 01, 2025 at 01:24 AM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
USE bibliotekadb
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `autorzy`
--

CREATE TABLE `autorzy` (
  `id_autora` int(11) NOT NULL AUTO_INCREMENT,
  `nazwa` varchar(50) NOT NULL,
  PRIMARY KEY (`id_autora`)  -- Dodano PRIMARY KEY
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `czytelnicy`
--

CREATE TABLE `czytelnicy` (
  `id_czytelnika` int(11) NOT NULL AUTO_INCREMENT,
  `imie` varchar(20) NOT NULL,
  `nazwisko` varchar(30) NOT NULL,
  `tel` bigint(15) NOT NULL,
  `email` varchar(20) NOT NULL,
  `haslo` varchar(255) NOT NULL,
  `ulica` varchar(30) NOT NULL,
  `miasto` varchar(20) NOT NULL,
  PRIMARY KEY (`id_czytelnika`)  -- Dodano PRIMARY KEY
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Tabela z uzytkownikami aplikacji';

--
-- Dumping data for table `czytelnicy`
--

INSERT INTO `czytelnicy` (`id_czytelnika`, `imie`, `nazwisko`, `tel`, `email`, `haslo`, `ulica`, `miasto`) VALUES
(1, 'Konrad', 'Konrad', 123123123, 'Konrad', '$2a$10$gLoQ1tGsKfhYzT0qa.BwiePj5/EswISShCl8DG61IUWeK2NQGkTc2', 'Konrad 2', 'Konrad');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `dostawcy`
--

CREATE TABLE `dostawcy` (
  `id_dostawcy` int(11) NOT NULL AUTO_INCREMENT,
  `nazwa_dostawcy` varchar(30) NOT NULL,
  `adres` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_dostawcy`)  -- Dodano PRIMARY KEY
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ksiazki`
--

CREATE TABLE `ksiazki` (
  `id_ksiazki` int(11) NOT NULL AUTO_INCREMENT,
  `tytul` varchar(55) NOT NULL,
  `id_autora` int(11) NOT NULL,
  `wydawnictwo` varchar(30) NOT NULL,
  `data_dodania` DATETIME NOT NULL,
  `ISBN` varchar(20) NOT NULL,
  `ilosc` int(11) NOT NULL,
  PRIMARY KEY (`id_ksiazki`),  -- Dodano PRIMARY KEY
  KEY `fk_id_autora` (`id_autora`)  -- Dodano indeks dla `id_autora`
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Tabela ksiazek';

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `pracownicy`
--

CREATE TABLE `pracownicy` (
  `id_pracownika` int(11) NOT NULL AUTO_INCREMENT,
  `imie` varchar(20) NOT NULL,
  `nazwisko` varchar(20) NOT NULL,
  `tel` varchar(15) NOT NULL,
  `email` varchar(20) NOT NULL,
  `haslo` varchar(20) NOT NULL,
  `rola` varchar(10) NOT NULL,
  PRIMARY KEY (`id_pracownika`)  -- Dodano PRIMARY KEY
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `wypozyczenia`
--

CREATE TABLE `wypozyczenia` (
  `id_wypozyczenia` int(11) NOT NULL AUTO_INCREMENT,
  `id_ksiazki` int(11) NOT NULL,
  `id_czytelnika` int(11) NOT NULL,
  `data_rezerwacji` datetime NOT NULL DEFAULT current_timestamp(),
  `data_oddania` datetime DEFAULT NULL,
  PRIMARY KEY (`id_wypozyczenia`),  -- Dodano PRIMARY KEY
  KEY `fk_id_czytelnika` (`id_czytelnika`),  -- Dodano indeks dla `id_czytelnika`
  KEY `fk_id_ksiazki` (`id_ksiazki`)  -- Dodano indeks dla `id_ksiazki`
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Tabela wypozyczen ksiazek, laczy ksiazki i uzytkownika';

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `zamowienia`
--

CREATE TABLE `zamowienia` (
  `id_zamowienia` int(11) NOT NULL AUTO_INCREMENT,
  `id_ksiazki` int(11) NOT NULL,
  `id_dostawcy` int(11) NOT NULL,
  `id_pracownika` int(11) NOT NULL,
  `ilosc` int(11) NOT NULL,
  PRIMARY KEY (`id_zamowienia`),  -- Dodano PRIMARY KEY
  KEY `fk_id_pracownika` (`id_pracownika`),  -- Dodano indeks dla `id_pracownika`
  KEY `fk_id_dostawcy` (`id_dostawcy`),  -- Dodano indeks dla `id_dostawcy`
  KEY `fk_id_ksiazki2` (`id_ksiazki`)  -- Dodano indeks dla `id_ksiazki`
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indeksy dla zrzutów tabel
--

-- Indeksy dla tabeli `autorzy`
ALTER TABLE `autorzy`
  ADD PRIMARY KEY (`id_autora`);

-- Indeksy dla tabeli `czytelnicy`
ALTER TABLE `czytelnicy`
  ADD PRIMARY KEY (`id_czytelnika`);

-- Indeksy dla tabeli `dostawcy`
ALTER TABLE `dostawcy`
  ADD PRIMARY KEY (`id_dostawcy`);

-- Indeksy dla tabeli `ksiazki`
ALTER TABLE `ksiazki`
  ADD PRIMARY KEY (`id_ksiazki`),
  ADD KEY `fk_id_autora` (`id_autora`);

-- Indeksy dla tabeli `pracownicy`
ALTER TABLE `pracownicy`
  ADD PRIMARY KEY (`id_pracownika`);

-- Indeksy dla tabeli `wypozyczenia`
ALTER TABLE `wypozyczenia`
  ADD PRIMARY KEY (`id_wypozyczenia`),
  ADD KEY `fk_id_czytelnika` (`id_czytelnika`),
  ADD KEY `fk_id_ksiazki` (`id_ksiazki`);

-- Indeksy dla tabeli `zamowienia`
ALTER TABLE `zamowienia`
  ADD PRIMARY KEY (`id_zamowienia`),
  ADD KEY `fk_id_pracownika` (`id_pracownika`),
  ADD KEY `fk_id_dostawcy` (`id_dostawcy`),
  ADD KEY `fk_id_ksiazki2` (`id_ksiazki`);

-- AUTO_INCREMENT for dumped tables

ALTER TABLE `autorzy`
  MODIFY `id_autora` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `czytelnicy`
  MODIFY `id_czytelnika` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

ALTER TABLE `dostawcy`
  MODIFY `id_dostawcy` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `ksiazki`
  MODIFY `id_ksiazki` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `pracownicy`
  MODIFY `id_pracownika` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `wypozyczenia`
  MODIFY `id_wypozyczenia` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `zamowienia`
  MODIFY `id_zamowienia` int(11) NOT NULL AUTO_INCREMENT;

-- Constraints for dumped tables

ALTER TABLE `ksiazki`
  ADD CONSTRAINT `fk_id_autora` FOREIGN KEY (`id_autora`) REFERENCES `autorzy` (`id_autora`);

ALTER TABLE `wypozyczenia`
  ADD CONSTRAINT `fk_id_czytelnika` FOREIGN KEY (`id_czytelnika`) REFERENCES `czytelnicy` (`id_czytelnika`),
  ADD CONSTRAINT `fk_id_ksiazki` FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazki` (`id_ksiazki`);

ALTER TABLE `zamowienia`
  ADD CONSTRAINT `fk_id_dostawcy` FOREIGN KEY (`id_dostawcy`) REFERENCES `dostawcy` (`id_dostawcy`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_id_ksiazki2` FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazki` (`id_ksiazki`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_id_pracownika` FOREIGN KEY (`id_pracownika`) REFERENCES `pracownicy` (`id_pracownika`);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

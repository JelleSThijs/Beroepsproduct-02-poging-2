-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Gegenereerd op: 31 mei 2026 om 23:22
-- Serverversie: 10.4.32-MariaDB
-- PHP-versie: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `beroepsproduct-02-jelle-thijs`
--

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `items`
--

CREATE TABLE `items` (
  `ID` int(15) NOT NULL,
  `name` varchar(20) NOT NULL,
  `summary` varchar(500) NOT NULL,
  `image` varchar(500) NOT NULL,
  `maker` varchar(50) NOT NULL,
  `releaseYear` int(4) NOT NULL,
  `type` varchar(4) NOT NULL,
  `userId` int(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Gegevens worden geëxporteerd voor tabel `items`
--


-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `itemtags`
--

CREATE TABLE `itemtags` (
  `itemId` int(15) NOT NULL,
  `tagId` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `tags`
--

CREATE TABLE `tags` (
  `ID` int(3) NOT NULL,
  `name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Gegevens worden geëxporteerd voor tabel `tags`
--

INSERT INTO `tags` (`ID`, `name`) VALUES
(1, 'Action'),
(2, 'Comedy'),
(3, 'Drama'),
(4, 'Sci-Fi'),
(5, 'Horror'),
(6, 'Romance'),
(7, 'Thriller'),
(8, 'Animation'),
(9, 'Action'),
(10, 'Comedy'),
(11, 'Drama'),
(12, 'Sci-Fi'),
(13, 'Horror'),
(14, 'Romance'),
(15, 'Thriller'),
(16, 'Animation'),
(17, 'Action'),
(18, 'Comedy'),
(19, 'Drama'),
(20, 'Sci-Fi'),
(21, 'Horror'),
(22, 'Romance'),
(23, 'Thriller'),
(24, 'Animation');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `users`
--

CREATE TABLE `users` (
  `ID` int(12) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phoneNumber` varchar(12) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `isAdmin` bit(1) DEFAULT b'0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexen voor geëxporteerde tabellen
--

--
-- Indexen voor tabel `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fk_items_user` (`userId`);

--
-- Indexen voor tabel `itemtags`
--
ALTER TABLE `itemtags`
  ADD PRIMARY KEY (`itemId`,`tagId`),
  ADD KEY `fk_itemtags_tag` (`tagId`);

--
-- Indexen voor tabel `tags`
--
ALTER TABLE `tags`
  ADD PRIMARY KEY (`ID`);

--
-- Indexen voor tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT voor geëxporteerde tabellen
--

--
-- AUTO_INCREMENT voor een tabel `items`
--
ALTER TABLE `items`
  MODIFY `ID` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT voor een tabel `tags`
--
ALTER TABLE `tags`
  MODIFY `ID` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT voor een tabel `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- Beperkingen voor geëxporteerde tabellen
--

--
-- Beperkingen voor tabel `items`
--
ALTER TABLE `items`
  ADD CONSTRAINT `fk_items_user` FOREIGN KEY (`userId`) REFERENCES `users` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Beperkingen voor tabel `itemtags`
--
ALTER TABLE `itemtags`
  ADD CONSTRAINT `fk_itemtags_item` FOREIGN KEY (`itemId`) REFERENCES `items` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_itemtags_tag` FOREIGN KEY (`tagId`) REFERENCES `tags` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

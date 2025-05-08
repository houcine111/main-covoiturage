-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 07 mai 2025 à 14:09
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `covoiturage_main`
--

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role` enum('ADMIN','CONDUCTEUR','USER') DEFAULT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `admin`
--

INSERT INTO `admin` (`id`, `password`, `email`, `role`, `username`) VALUES
(1, 'admin', 'admin@admin.com', 'ADMIN', 'admin1');

-- --------------------------------------------------------

--
-- Structure de la table `admin_seq`
--

CREATE TABLE `admin_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `admin_seq`
--

INSERT INTO `admin_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Structure de la table `conducteur`
--

CREATE TABLE `conducteur` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `immatriculation_photo` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `numero_telephone` varchar(255) DEFAULT NULL,
  `permis_photo` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CONDUCTEUR','USER') DEFAULT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `conducteur`
--

INSERT INTO `conducteur` (`id`, `email`, `genre`, `immatriculation_photo`, `nom`, `numero_telephone`, `permis_photo`, `prenom`, `role`, `username`) VALUES
(1, 'johndoe@example.com', 'Homme', 'https://picsum.photos/seed/picsum/200/300', 'john', '0123456789', 'https://picsum.photos/seed/picsum/200/300q', 'doe', 'CONDUCTEUR', 'johndoe');

-- --------------------------------------------------------

--
-- Structure de la table `conducteur_seq`
--

CREATE TABLE `conducteur_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `conducteur_seq`
--

INSERT INTO `conducteur_seq` (`next_val`) VALUES
(51);

-- --------------------------------------------------------

--
-- Structure de la table `conducteur_trajets`
--

CREATE TABLE `conducteur_trajets` (
  `conducteur_id` bigint(20) NOT NULL,
  `trajets_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `personne`
--

CREATE TABLE `personne` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `genre` varchar(255) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `numerotelephone` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `role` enum('ADMIN','CONDUCTEUR','USER') DEFAULT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `personne`
--

INSERT INTO `personne` (`id`, `email`, `genre`, `nom`, `numerotelephone`, `prenom`, `role`, `username`) VALUES
(1, 'johndoe@gmail.com', 'Homme', 'john', '0601020304', 'doe', 'USER', 'johndoe');

-- --------------------------------------------------------

--
-- Structure de la table `personne_seq`
--

CREATE TABLE `personne_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `personne_seq`
--

INSERT INTO `personne_seq` (`next_val`) VALUES
(51);

-- --------------------------------------------------------

--
-- Structure de la table `trajet`
--

CREATE TABLE `trajet` (
  `id` bigint(20) NOT NULL,
  `depart` varchar(255) DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `heure_depart` time(6) DEFAULT NULL,
  `conducteur_id` bigint(20) NOT NULL,
  `prix` double NOT NULL,
  `reserved_for_women` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `trajet`
--

INSERT INTO `trajet` (`id`, `depart`, `destination`, `heure_depart`, `conducteur_id`, `prix`, `reserved_for_women`) VALUES
(1, 'Mohammedia', 'Tetouan', '09:30:00.000000', 1, 50, b'0'),
(2, 'Casablanca', 'Rabat', '09:30:00.000000', 1, 50, b'0'),
(52, 'Mohammedia', 'Rabat', '09:30:00.000000', 1, 50, b'0');

-- --------------------------------------------------------

--
-- Structure de la table `trajet_participants`
--

CREATE TABLE `trajet_participants` (
  `trajet_id` bigint(20) NOT NULL,
  `participants_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `trajet_seq`
--

CREATE TABLE `trajet_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `trajet_seq`
--

INSERT INTO `trajet_seq` (`next_val`) VALUES
(151);

-- --------------------------------------------------------

--
-- Structure de la table `trajet_stations`
--

CREATE TABLE `trajet_stations` (
  `trajet_id` bigint(20) NOT NULL,
  `stations` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `trajet_stations`
--

INSERT INTO `trajet_stations` (`trajet_id`, `stations`) VALUES
(1, 'Mohammedia'),
(1, 'Bouznika'),
(2, 'Mohammedia'),
(2, 'Bouznika');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKc0r9atamxvbhjjvy5j8da1kam` (`email`),
  ADD UNIQUE KEY `UKgfn44sntic2k93auag97juyij` (`username`);

--
-- Index pour la table `conducteur`
--
ALTER TABLE `conducteur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKrne1vp2qh4emnl0exycn9p31c` (`email`),
  ADD UNIQUE KEY `UKqt99jfweqqccp7edca0pe094n` (`username`);

--
-- Index pour la table `conducteur_trajets`
--
ALTER TABLE `conducteur_trajets`
  ADD UNIQUE KEY `UKkmhbuowxxi5yyax0ivuwgwbft` (`trajets_id`),
  ADD KEY `FKncvtyofdvem3wnyum735aejcl` (`conducteur_id`);

--
-- Index pour la table `personne`
--
ALTER TABLE `personne`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKlif11ug7pqkdimuk0beonbfng` (`email`),
  ADD UNIQUE KEY `UK9qi3fxtf05vne6ddf51mmx1a0` (`username`);

--
-- Index pour la table `trajet`
--
ALTER TABLE `trajet`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkmhqy20yan3y99d2gyo6916by` (`conducteur_id`);

--
-- Index pour la table `trajet_participants`
--
ALTER TABLE `trajet_participants`
  ADD KEY `FKkqjdgstoh6pybrarj5xlx5hhi` (`participants_id`),
  ADD KEY `FKhufp0w2w4e0ifajlabc47f8bb` (`trajet_id`);

--
-- Index pour la table `trajet_stations`
--
ALTER TABLE `trajet_stations`
  ADD KEY `FKaja28j1ppyuwghyjafkdudnhf` (`trajet_id`);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `conducteur_trajets`
--
ALTER TABLE `conducteur_trajets`
  ADD CONSTRAINT `FK2ccc6oqegokembgd78d1exiks` FOREIGN KEY (`trajets_id`) REFERENCES `trajet` (`id`),
  ADD CONSTRAINT `FKncvtyofdvem3wnyum735aejcl` FOREIGN KEY (`conducteur_id`) REFERENCES `conducteur` (`id`);

--
-- Contraintes pour la table `trajet`
--
ALTER TABLE `trajet`
  ADD CONSTRAINT `FKkmhqy20yan3y99d2gyo6916by` FOREIGN KEY (`conducteur_id`) REFERENCES `conducteur` (`id`);

--
-- Contraintes pour la table `trajet_participants`
--
ALTER TABLE `trajet_participants`
  ADD CONSTRAINT `FKhufp0w2w4e0ifajlabc47f8bb` FOREIGN KEY (`trajet_id`) REFERENCES `trajet` (`id`),
  ADD CONSTRAINT `FKkqjdgstoh6pybrarj5xlx5hhi` FOREIGN KEY (`participants_id`) REFERENCES `personne` (`id`);

--
-- Contraintes pour la table `trajet_stations`
--
ALTER TABLE `trajet_stations`
  ADD CONSTRAINT `FKaja28j1ppyuwghyjafkdudnhf` FOREIGN KEY (`trajet_id`) REFERENCES `trajet` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 01, 2026 at 10:09 AM
-- Server version: 10.11.14-MariaDB-0ubuntu0.24.04.1
-- PHP Version: 8.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `absensi_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `absensi`
--

CREATE TABLE `absensi` (
  `id` int(11) NOT NULL,
  `jadwal_id` int(11) NOT NULL,
  `mahasiswa_id` int(11) NOT NULL,
  `waktu_absen` datetime NOT NULL,
  `status` enum('HADIR','TELAT','ALPHA') NOT NULL DEFAULT 'ALPHA',
  `alasan` text DEFAULT NULL,
  `konfirmasi` varchar(20) NOT NULL DEFAULT 'NA',
  `asdos_id` int(11) DEFAULT NULL,
  `waktu_konfirmasi` datetime DEFAULT NULL,
  `catatan_asdos` text DEFAULT NULL,
  `dihitung_hadir` tinyint(1) DEFAULT 0 COMMENT '1=hadir, 0=tidak hadir/belum dikonfirmasi'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `absensi`
--

INSERT INTO `absensi` (`id`, `jadwal_id`, `mahasiswa_id`, `waktu_absen`, `status`, `alasan`, `konfirmasi`, `asdos_id`, `waktu_konfirmasi`, `catatan_asdos`, `dihitung_hadir`) VALUES
(23, 5, 1, '2026-06-01 17:00:15', 'HADIR', NULL, 'NA', NULL, NULL, NULL, 1),
(24, 5, 7, '2026-06-01 17:00:51', 'HADIR', NULL, 'NA', NULL, NULL, NULL, 1),
(25, 5, 6, '2026-06-01 17:00:55', 'HADIR', NULL, 'NA', NULL, NULL, NULL, 1),
(26, 5, 3, '2026-06-01 17:01:37', 'HADIR', NULL, 'NA', NULL, NULL, NULL, 1),
(27, 5, 4, '2026-06-01 17:01:42', 'HADIR', NULL, 'NA', NULL, NULL, NULL, 1),
(28, 16, 21, '2026-06-01 17:01:59', 'HADIR', NULL, 'NA', NULL, NULL, NULL, 1),
(29, 16, 22, '2026-06-01 17:02:03', 'TELAT', NULL, 'NA', NULL, NULL, NULL, 1),
(31, 5, 21, '2026-06-01 17:03:50', 'HADIR', NULL, 'NA', NULL, NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `asdos`
--

CREATE TABLE `asdos` (
  `id` int(11) NOT NULL,
  `nip` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `aktif` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `asdos`
--

INSERT INTO `asdos` (`id`, `nip`, `nama`, `username`, `password`, `aktif`) VALUES
(1, 'ASD001', 'Rizky Pratama', 'rizky', '02099aec8a53b954db0194eedad2c4f9', 1),
(2, 'ASD002', 'Dewi Sartika', 'dewi', '02099aec8a53b954db0194eedad2c4f9', 1);

-- --------------------------------------------------------

--
-- Table structure for table `jadwal_kuliah`
--

CREATE TABLE `jadwal_kuliah` (
  `id` int(11) NOT NULL,
  `matakuliah_id` int(11) NOT NULL,
  `hari` enum('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu','Minggu') NOT NULL,
  `jam_mulai` time NOT NULL DEFAULT '10:30:00',
  `batas_telat` time NOT NULL DEFAULT '10:45:00' COMMENT 'Jam terakhir dihitung hadir penuh',
  `ruang` varchar(255) NOT NULL,
  `keterangan` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jadwal_kuliah`
--

INSERT INTO `jadwal_kuliah` (`id`, `matakuliah_id`, `hari`, `jam_mulai`, `batas_telat`, `ruang`, `keterangan`) VALUES
(1, 1, 'Senin', '10:00:00', '11:45:00', 'Patt.II-3A', 'Analisa Algoritma'),
(2, 2, 'Senin', '15:00:00', '15:45:00', 'Patt.I-3C', 'Analisa Algoritma'),
(3, 8, 'Senin', '10:00:00', '12:30:00', 'Patt.I-3B', 'Arsitektur dan Organisasi Komputer'),
(4, 9, 'Senin', '12:30:00', '15:00:00', 'Patt.III-3D', 'Arsitektur dan Organisasi Komputer'),
(5, 10, 'Senin', '15:00:00', '17:30:00', 'Patt.II-3A', 'Arsitektur dan Organisasi Komputer'),
(6, 15, 'Senin', '07:30:00', '10:00:00', 'Patt.III-3B', 'Otomata'),
(7, 22, 'Senin', '07:30:00', '10:00:00', 'Patt.II-3D', 'PBO Induk'),
(8, 23, 'Senin', '10:00:00', '12:30:00', 'Patt.II-3B', 'PBO Induk'),
(9, 24, 'Senin', '15:00:00', '17:30:00', 'Patt.II-3C', 'PBO Induk'),
(10, 29, 'Senin', '07:30:00', '09:15:00', 'Patt.II-3C', 'PKN'),
(11, 30, 'Senin', '10:00:00', '11:45:00', 'Patt.III-3B', 'PKN'),
(12, 31, 'Senin', '12:30:00', '14:15:00', 'Patt.III-3C', 'PKN'),
(13, 32, 'Senin', '15:00:00', '16:45:00', 'Patt.III-3B', 'PKN'),
(14, 35, 'Senin', '10:30:00', '12:30:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(15, 36, 'Senin', '13:00:00', '15:00:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(16, 37, 'Senin', '15:30:00', '17:30:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(17, 46, 'Senin', '13:00:00', '15:00:00', 'Laboratorium Teknologi Mobile', 'Praktikum SC-PKT'),
(18, 47, 'Senin', '15:30:00', '17:30:00', 'Laboratorium Teknologi Mobile', 'Praktikum SC-PKT'),
(19, 63, 'Senin', '12:30:00', '15:00:00', 'Patt.III-3B', 'SC-PKT Induk'),
(20, 69, 'Senin', '07:30:00', '10:00:00', 'Patt.I-1A', 'Technopreneurship'),
(21, 70, 'Senin', '10:00:00', '12:30:00', 'Patt.III-3D', 'Technopreneurship'),
(22, 3, 'Selasa', '10:00:00', '11:45:00', 'Patt.II-3C', 'Analisa Algoritma'),
(23, 4, 'Selasa', '15:00:00', '16:45:00', 'Patt.III-3BA', 'Analisa Algoritma'),
(24, 5, 'Selasa', '15:00:00', '16:45:00', 'Patt.I-3B', 'Analisa Algoritma'),
(25, 16, 'Selasa', '15:00:00', '17:30:00', 'Patt.I-3A', 'Otomata'),
(26, 17, 'Selasa', '15:00:00', '17:30:00', 'Patt.III-3A', 'Otomata'),
(27, 25, 'Selasa', '07:30:00', '10:00:00', 'Patt.II-3C', 'PBO Induk'),
(28, 33, 'Selasa', '07:30:00', '09:15:00', 'Patt.III-3B', 'PKN'),
(29, 38, 'Selasa', '08:00:00', '10:00:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(30, 39, 'Selasa', '15:30:00', '17:30:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(31, 48, 'Selasa', '08:00:00', '10:00:00', 'Laboratorium Basis Data', 'Praktikum SC-PKT'),
(32, 49, 'Selasa', '13:00:00', '15:00:00', 'Laboratorium Teknologi Mobile', 'Praktikum SC-PKT'),
(33, 50, 'Selasa', '13:00:00', '15:00:00', 'Laboratorium Basis Data', 'Praktikum SC-PKT'),
(34, 51, 'Selasa', '15:30:00', '17:30:00', 'Laboratorium Basis Data', 'Praktikum SC-PKT'),
(35, 56, 'Selasa', '10:00:00', '12:30:00', 'Patt.I-1A', 'RPL'),
(36, 57, 'Selasa', '12:30:00', '15:00:00', 'Patt.III-3CS', 'RPL'),
(37, 64, 'Selasa', '07:30:00', '10:00:00', 'Patt.I-3B', 'SC-PKT Induk'),
(38, 65, 'Selasa', '10:00:00', '12:30:00', 'Patt.III-3C', 'SC-PKT Induk'),
(39, 6, 'Rabu', '07:30:00', '09:15:00', 'Patt.III-3D', 'Analisa Algoritma'),
(40, 7, 'Rabu', '15:00:00', '16:45:00', 'Patt.II-3B', 'Analisa Algoritma'),
(41, 11, 'Rabu', '10:00:00', '12:30:00', 'Patt.I-3C', 'Arsitektur dan Organisasi Komputer'),
(42, 12, 'Rabu', '12:30:00', '15:00:00', 'Patt.III-2B', 'Arsitektur dan Organisasi Komputer'),
(43, 18, 'Rabu', '07:30:00', '10:00:00', 'Patt.I-3B', 'Otomata'),
(44, 19, 'Rabu', '12:30:00', '15:00:00', 'Patt.II-3A', 'Otomata'),
(45, 34, 'Rabu', '15:00:00', '16:45:00', 'Patt.II-3D', 'PKN'),
(46, 40, 'Rabu', '10:30:00', '12:30:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(47, 41, 'Rabu', '15:30:00', '17:30:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(48, 52, 'Rabu', '08:00:00', '10:00:00', 'Laboratorium Teknologi Mobile', 'Praktikum SC-PKT'),
(49, 53, 'Rabu', '13:00:00', '15:00:00', 'Laboratorium Teknologi Mobile', 'Praktikum SC-PKT'),
(50, 58, 'Rabu', '07:30:00', '10:00:00', 'Patt.III-3A', 'RPL'),
(51, 59, 'Rabu', '12:30:00', '15:00:00', 'Patt.II-3B', 'RPL'),
(52, 60, 'Rabu', '15:00:00', '17:30:00', 'Patt.II-3CH', 'RPL'),
(53, 71, 'Rabu', '07:30:00', '10:00:00', 'Patt.III-2C', 'Technopreneurship'),
(54, 13, 'Kamis', '07:30:00', '10:00:00', 'Patt.I-3B', 'Arsitektur dan Organisasi Komputer'),
(55, 14, 'Kamis', '12:30:00', '15:00:00', 'Patt.III-3D', 'Arsitektur dan Organisasi Komputer'),
(56, 20, 'Kamis', '07:30:00', '10:00:00', 'Patt.III-3D', 'Otomata'),
(57, 26, 'Kamis', '07:30:00', '10:00:00', 'Patt.I-3D', 'PBO Induk'),
(58, 27, 'Kamis', '12:30:00', '15:00:00', 'Patt.III-2B', 'PBO Induk'),
(59, 28, 'Kamis', '15:00:00', '17:30:00', 'Patt.II-3A', 'PBO Induk'),
(60, 42, 'Kamis', '10:30:00', '12:30:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(61, 43, 'Kamis', '13:00:00', '15:00:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(62, 44, 'Kamis', '15:30:00', '17:30:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(63, 54, 'Kamis', '13:00:00', '15:00:00', 'Laboratorium Teknologi Mobile', 'Praktikum SC-PKT'),
(64, 55, 'Kamis', '15:30:00', '17:30:00', 'Laboratorium Basis Data', 'Praktikum SC-PKT'),
(65, 61, 'Kamis', '12:30:00', '15:00:00', 'Patt.I-3B', 'RPL'),
(66, 62, 'Kamis', '15:00:00', '17:30:00', 'Patt.III-3A', 'RPL'),
(67, 66, 'Kamis', '10:00:00', '12:30:00', 'Patt.I-1A', 'SC-PKT Induk'),
(68, 67, 'Kamis', '12:30:00', '15:00:00', 'Patt.I-3D', 'SC-PKT Induk'),
(69, 68, 'Kamis', '15:00:00', '17:30:00', 'Patt.I-3B', 'SC-PKT Induk'),
(70, 72, 'Kamis', '07:30:00', '10:00:00', 'Patt.III-3C', 'Technopreneurship'),
(71, 21, 'Jumat', '07:30:00', '10:00:00', 'Patt.III-3B', 'Otomata'),
(72, 45, 'Jumat', '08:00:00', '10:00:00', 'Laboratorium Internet Of Thing', 'Praktikum PBO'),
(73, 73, 'Jumat', '10:00:00', '12:30:00', 'Patt.II-3B', 'Technopreneurship'),
(74, 74, 'Jumat', '10:00:00', '12:30:00', 'Patt.I-3A', 'Technopreneurship'),
(75, 75, 'Jumat', '13:30:00', '16:00:00', 'Patt.III-3B', 'Technopreneurship');

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `id` int(11) NOT NULL,
  `nim` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `prodi` varchar(100) DEFAULT 'Teknik Informatika',
  `angkatan` year(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mahasiswa`
--

INSERT INTO `mahasiswa` (`id`, `nim`, `nama`, `prodi`, `angkatan`) VALUES
(1, '123240208', 'Muhammad Azfa Imaduddin', 'Teknik Informatika', '2024'),
(2, '123240264', 'Noven Bugijangge', 'Teknik Informatika', '2024'),
(3, '123240173', 'Erlan Rifqi Davin D', 'Teknik Informatika', '2024'),
(4, '123240175', 'Rio Meidi A', 'Teknik Informatika', '2024'),
(5, '123240082', 'M. Zidane Zulfikar', 'Teknik Informatika', '2024'),
(6, '123240031', 'M. Afif Pratama', 'Teknik Informatika', '2024'),
(7, '123240101', 'Kurnia Ardiningrum', 'Teknik Informatika', '2024'),
(8, '123240237', 'M. Dzikri Ginoga', 'Teknik Informatika', '2024'),
(9, '123240203', 'Kafka Akmal Dani', 'Teknik Informatika', '2024'),
(10, '123240193', 'Alvin Andhika Putra', 'Teknik Informatika', '2024'),
(11, '123240104', 'Rafi Dzaka Pratama Putra', 'Teknik Informatika', '2024'),
(12, '123240120', 'Raihan Natawangsa', 'Teknik Informatika', '2024'),
(13, '123240063', 'Muhammad Arif Hermawan', 'Teknik Informatika', '2024'),
(14, '123240241', 'Ikhsan Wahyu Endriarto', 'Teknik Informatika', '2024'),
(15, '123240145', 'Michael Bintang', 'Teknik Informatika', '2024'),
(16, '123240139', 'Constantine Rylianno Sumakul', 'Teknik Informatika', '2024'),
(17, '123240002', 'Fahri Hidayatullah', 'Teknik Informatika', '2024'),
(18, '123240016', 'Nayla Saskia Zallianti', 'Teknik Informatika', '2024'),
(19, '123240248', 'Reno Miftahudin', 'Teknik Informatika', '2024'),
(20, '123240247', 'Bintang Shada Kawibya Putra', 'Teknik Informatika', '2024'),
(21, '123240100', 'Nicolaus Narindra Lianto', 'Teknik Informatika', '2024'),
(22, '123240092', 'Aushaf Fathin Irsyad Nabil', 'Teknik Informatika', '2024'),
(23, '123240245', 'Lintang Cahaya Eka Putra', 'Teknik Informatika', '2024'),
(24, '123240134', 'Daniel Requel', 'Teknik Informatika', '2024'),
(25, '123240004', 'Abdillah Abhipraya Abrar', 'Teknik Informatika', '2024'),
(26, '123240017', 'Gian Abi Firdaus', 'Teknik Informatika', '2024');

-- --------------------------------------------------------

--
-- Table structure for table `matakuliah`
--

CREATE TABLE `matakuliah` (
  `id` int(11) NOT NULL,
  `kode` varchar(20) NOT NULL,
  `nama` varchar(150) NOT NULL,
  `kelas` varchar(5) DEFAULT NULL,
  `sks` int(11) DEFAULT 3,
  `dosen` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `matakuliah`
--

INSERT INTO `matakuliah` (`id`, `kode`, `nama`, `kelas`, `sks`, `dosen`) VALUES
(1, '123210252', 'Analisa Algoritma', 'IF-H', 2, 'Heru Cahya Rustamaji Dr. S.Si., M.T.'),
(2, '123210252', 'Analisa Algoritma', 'IF-B', 2, 'Ahmad Taufiq Akbar S.Si., M.Cs.'),
(3, '123210252', 'Analisa Algoritma', 'IF-D', 2, 'Mangaras Yanu F S.T., M.Eng.'),
(4, '123210252', 'Analisa Algoritma', 'IF-C', 2, 'Ahmad Taufiq Akbar S.Si., M.Cs.'),
(5, '123210252', 'Analisa Algoritma', 'IF-F', 2, 'Mangaras Yanu F S.T., M.Eng.'),
(6, '123210252', 'Analisa Algoritma', 'IF-E', 2, 'Mangaras Yanu F S.T., M.Eng.'),
(7, '123210252', 'Analisa Algoritma', 'IF-A', 2, 'Ahmad Taufiq Akbar S.Si., M.Cs.'),
(8, '123210293', 'Arsitektur dan Organisasi Komputer', 'IF-A', 3, 'Heriyanto Dr. A.Md., S.Kom., M.Cs.'),
(9, '123210293', 'Arsitektur dan Organisasi Komputer', 'IF-C', 3, 'Andi Nurkholis S.Kom., M.Kom.'),
(10, '123210293', 'Arsitektur dan Organisasi Komputer', 'IF-H', 3, 'Heriyanto Dr. A.Md., S.Kom., M.Cs.'),
(11, '123210293', 'Arsitektur dan Organisasi Komputer', 'IF-D', 3, 'Andi Nurkholis S.Kom., M.Kom.'),
(12, '123210293', 'Arsitektur dan Organisasi Komputer', 'IF-B', 3, 'Andi Nurkholis S.Kom., M.Kom.'),
(13, '123210293', 'Arsitektur dan Organisasi Komputer', 'IF-F', 3, 'Andi Nurkholis S.Kom., M.Kom.'),
(14, '123210293', 'Arsitektur dan Organisasi Komputer', 'IF-E', 3, 'Andi Nurkholis S.Kom., M.Kom.'),
(15, '123210263', 'Otomata Dan Pengantar Kompilasi', 'IF-D', 3, 'Heru Cahya Rustamaji Dr. S.Si., M.T.'),
(16, '123210263', 'Otomata Dan Pengantar Kompilasi', 'IF-A', 3, 'Rifki Indra P S.Kom., M.Eng.'),
(17, '123210263', 'Otomata Dan Pengantar Kompilasi', 'IF-F', 3, 'Heru Cahya Rustamaji Dr. S.Si., M.T.'),
(18, '123210263', 'Otomata Dan Pengantar Kompilasi', 'IF-B', 3, 'Rifki Indra P S.Kom., M.Eng.'),
(19, '123210263', 'Otomata Dan Pengantar Kompilasi', 'IF-H', 3, 'Heru Cahya Rustamaji Dr. S.Si., M.T.'),
(20, '123210263', 'Otomata Dan Pengantar Kompilasi', 'IF-C', 3, 'Heru Cahya Rustamaji Dr. S.Si., M.T.'),
(21, '123210263', 'Otomata Dan Pengantar Kompilasi', 'IF-E', 3, 'Heru Cahya Rustamaji Dr. S.Si., M.T.'),
(22, '123210303', 'Pemrograman Berorientasi Objek', 'IF-A', 3, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(23, '123210303', 'Pemrograman Berorientasi Objek', 'IF-C', 3, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(24, '123210303', 'Pemrograman Berorientasi Objek', 'IF-B', 3, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(25, '123210303', 'Pemrograman Berorientasi Objek', 'IF-E', 3, 'Rudi Cahyadi S.Si., M.T.'),
(26, '123210303', 'Pemrograman Berorientasi Objek', 'IF-D', 3, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(27, '123210303', 'Pemrograman Berorientasi Objek', 'IF-F', 3, 'Rudi Cahyadi S.Si., M.T.'),
(28, '123210303', 'Pemrograman Berorientasi Objek', 'IF-H', 3, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(29, '100210082', 'Pendidikan Kewarganegaraan', 'IF-E', 2, 'Danang Prasetyo, S.Pd., M.Pd.'),
(30, '100210082', 'Pendidikan Kewarganegaraan', 'IF-D', 2, 'Danang Prasetyo, S.Pd., M.Pd.'),
(31, '100210082', 'Pendidikan Kewarganegaraan', 'IF-A', 2, 'Yunie Herawati Ir., M.Hum.'),
(32, '100210082', 'Pendidikan Kewarganegaraan', 'IF-B', 2, 'Danang Prasetyo, S.Pd., M.Pd.'),
(33, '100210082', 'Pendidikan Kewarganegaraan', 'IF-H', 2, 'Danang Prasetyo, S.Pd., M.Pd.'),
(34, '100210082', 'Pendidikan Kewarganegaraan', 'IF-C', 2, 'Rr. Yudiswara Ayu Permatasari , M.Phil.'),
(35, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-A', 1, 'Yuli Fauziah S.T., M.T.'),
(36, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-B', 1, 'Frans Richard Kodong S.T., M.Kom., Ph.D'),
(37, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-C', 1, 'Herlina Jayadianti Dr. S.T., M.T.'),
(38, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-D', 1, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(39, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-E', 1, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(40, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-F', 1, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(41, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-G', 1, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(42, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-I', 1, 'Mangaras Yanu F S.T., M.Eng.'),
(43, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-H', 1, 'Aldila Putri Linanzha , S.Kom., M.Cs.'),
(44, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-J', 1, 'Wilis Kaswidjanti S.Si., M.Kom.'),
(45, '123210321', 'Praktikum Pemrograman Berorientasi Objek', 'IF-K', 1, 'Novrido Charibaldi Dr. S.Kom., M.Kom.'),
(46, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-A', 1, 'Heru Cahya Rustamaji Dr. S.Si., M.T.'),
(47, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-B', 1, 'Awang Hendrianto P. Dr. S.T., M.T.'),
(48, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-C', 1, 'Heru Cahya Rustamaji Dr. S.Si., M.T.'),
(49, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-D', 1, 'Mangaras Yanu F S.T., M.Eng.'),
(50, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-H', 1, 'Andiko Putro Suryotomo S.Kom., M.Cs.'),
(51, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-E', 1, 'Herlina Jayadianti Dr. S.T., M.T.'),
(52, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-F', 1, 'Frans Richard Kodong S.T., M.Kom., Ph.D'),
(53, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-G', 1, 'Herlina Jayadianti Dr. S.T., M.T.'),
(54, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-I', 1, 'Heru Cahya Rustamaji Dr. S.Si., M.T.'),
(55, '123210311', 'Praktikum Sistem Cerdas dan Pendukung Keputusan', 'IF-J', 1, 'Herlina Jayadianti Dr. S.T., M.T.'),
(56, '123210283', 'Rekayasa Perangkat Lunak', 'IF-H', 3, 'Heriyanto Dr. A.Md., S.Kom., M.Cs.'),
(57, '123210283', 'Rekayasa Perangkat Lunak', 'IF-F', 3, 'Simon Pulung Nugroho S.Kom., M.Cs.'),
(58, '123210283', 'Rekayasa Perangkat Lunak', 'IF-A', 3, 'Heriyanto Dr. A.Md., S.Kom., M.Cs.'),
(59, '123210283', 'Rekayasa Perangkat Lunak', 'IF-B', 3, 'Heriyanto Dr. A.Md., S.Kom., M.Cs.'),
(60, '123210283', 'Rekayasa Perangkat Lunak', 'IF-C', 3, 'Heriyanto Dr. A.Md., S.Kom., M.Cs.'),
(61, '123210283', 'Rekayasa Perangkat Lunak', 'IF-D', 3, 'Heriyanto Dr. A.Md., S.Kom., M.Cs.'),
(62, '123210283', 'Rekayasa Perangkat Lunak', 'IF-E', 3, 'Simon Pulung Nugroho S.Kom., M.Cs.'),
(63, '123210273', 'Sistem Cerdas dan Pendukung Keputusan', 'IF-E', 3, 'Agus Sasmito Aribowo S.Kom., M.Cs., Ph.D'),
(64, '123210273', 'Sistem Cerdas dan Pendukung Keputusan', 'IF-H', 3, 'Herlina Jayadianti Dr. S.T., M.T.'),
(65, '123210273', 'Sistem Cerdas dan Pendukung Keputusan', 'IF-D', 3, 'Agus Sasmito Aribowo S.Kom., M.Cs., Ph.D'),
(66, '123210273', 'Sistem Cerdas dan Pendukung Keputusan', 'IF-A', 3, 'Agus Sasmito Aribowo S.Kom., M.Cs., Ph.D'),
(67, '123210273', 'Sistem Cerdas dan Pendukung Keputusan', 'IF-F', 3, 'Herlina Jayadianti Dr. S.T., M.T.'),
(68, '123210273', 'Sistem Cerdas dan Pendukung Keputusan', 'IF-B', 3, 'Agus Sasmito Aribowo S.Kom., M.Cs., Ph.D'),
(69, '100210153', 'Technopreneurship', 'IF-A', 3, 'Awang Hendrianto P. Dr. S.T., M.T.'),
(70, '100210153', 'Technopreneurship', 'IF-B', 3, 'Awang Hendrianto P. Dr. S.T., M.T.'),
(71, '100210153', 'Technopreneurship', 'IF-C', 3, 'Awang Hendrianto P. Dr. S.T., M.T.'),
(72, '100210153', 'Technopreneurship', 'IF-D', 3, 'Awang Hendrianto P. Dr. S.T., M.T.'),
(73, '100210153', 'Technopreneurship', 'IF-F', 3, 'Rochmat Husaini S.Kom., M.Kom.'),
(74, '100210153', 'Technopreneurship', 'IF-H', 3, 'Oliver Samuel S. S.Kom., M.Eng.'),
(75, '100210153', 'Technopreneurship', 'IF-E', 3, 'Rochmat Husaini S.Kom., M.Kom.');

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_rekap_absensi`
-- (See below for the actual view)
--
CREATE TABLE `v_rekap_absensi` (
);

-- --------------------------------------------------------

--
-- Structure for view `v_rekap_absensi`
--
DROP TABLE IF EXISTS `v_rekap_absensi`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_rekap_absensi`  AS SELECT `m`.`nim` AS `nim`, `m`.`nama` AS `nama_mahasiswa`, `mk`.`kode` AS `kode`, `mk`.`nama` AS `mata_kuliah`, `mk`.`kelas` AS `kelas`, `j`.`ruang` AS `ruang`, `j`.`tanggal` AS `tanggal`, `j`.`jam_mulai` AS `jam_mulai`, `a`.`waktu_absen` AS `waktu_absen`, `a`.`status` AS `status`, `a`.`alasan` AS `alasan`, `a`.`konfirmasi` AS `konfirmasi`, `a`.`dihitung_hadir` AS `dihitung_hadir`, `asd`.`nama` AS `nama_asdos`, `a`.`catatan_asdos` AS `catatan_asdos` FROM ((((`absensi` `a` join `jadwal_kuliah` `j` on(`a`.`jadwal_id` = `j`.`id`)) join `matakuliah` `mk` on(`j`.`matakuliah_id` = `mk`.`id`)) join `mahasiswa` `m` on(`a`.`mahasiswa_id` = `m`.`id`)) left join `asdos` `asd` on(`a`.`asdos_id` = `asd`.`id`)) ORDER BY `j`.`tanggal` DESC, `m`.`nim` ASC ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_jadwal_mhs` (`jadwal_id`,`mahasiswa_id`),
  ADD KEY `mahasiswa_id` (`mahasiswa_id`),
  ADD KEY `asdos_id` (`asdos_id`);

--
-- Indexes for table `asdos`
--
ALTER TABLE `asdos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nip` (`nip`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `jadwal_kuliah`
--
ALTER TABLE `jadwal_kuliah`
  ADD PRIMARY KEY (`id`),
  ADD KEY `matakuliah_id` (`matakuliah_id`);

--
-- Indexes for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nim` (`nim`);

--
-- Indexes for table `matakuliah`
--
ALTER TABLE `matakuliah`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `absensi`
--
ALTER TABLE `absensi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT for table `asdos`
--
ALTER TABLE `asdos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `jadwal_kuliah`
--
ALTER TABLE `jadwal_kuliah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=76;

--
-- AUTO_INCREMENT for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `matakuliah`
--
ALTER TABLE `matakuliah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=76;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `absensi`
--
ALTER TABLE `absensi`
  ADD CONSTRAINT `absensi_ibfk_1` FOREIGN KEY (`jadwal_id`) REFERENCES `jadwal_kuliah` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `absensi_ibfk_2` FOREIGN KEY (`mahasiswa_id`) REFERENCES `mahasiswa` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `absensi_ibfk_3` FOREIGN KEY (`asdos_id`) REFERENCES `asdos` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `jadwal_kuliah`
--
ALTER TABLE `jadwal_kuliah`
  ADD CONSTRAINT `jadwal_kuliah_ibfk_1` FOREIGN KEY (`matakuliah_id`) REFERENCES `matakuliah` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

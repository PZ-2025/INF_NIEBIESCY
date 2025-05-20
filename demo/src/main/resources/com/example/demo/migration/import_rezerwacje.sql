-- Dodanie rezerwacje
INSERT INTO `rezerwacje` (`id_rezerwacji`, `id_czytelnika`, `id_ksiazki`, `data_zamowienia`, `planowana_data`, `status`)
VALUES
      (11, 1, 40, '2025-05-20', '2025-05-23', 1),
      (12, 1, 46, '2025-05-20', '2025-05-23', 1),
      (13, 3, 40, '2025-05-20', '2025-05-21', 0),
      (14, 1, 46, '2025-05-20', '2025-05-21', 0);
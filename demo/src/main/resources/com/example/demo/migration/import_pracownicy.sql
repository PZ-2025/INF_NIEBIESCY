INSERT INTO `pracownicy` (`id_pracownika`, `imie`, `nazwisko`, `tel`, `email`, `haslo`, `ulica`, `miasto`, `rola`)
VALUES
(1, 'Anna', 'Kowalska', '123456789', 'admin@biblioteka.pl', '$2a$12$2NeRb.GBQcoKv4pINQzXPOjDLCBSs08cQ/leA8xjVWoW/5afTSy/O', 'Cicha 12', 'Rzeszów', 'admin'),
-- haslo123
(2, 'Piotr', 'Nowak', '987654321', 'pnowak@biblioteka.pl', '$2a$12$8LsAc2GPd.qFnOmf/5nViO0Z42YXBrsrg40u0oEgwcmX6FOKatMgG', 'Robotnicza 4', 'Rzeszów', 'bibliotekarz'),
-- qwerty456
(3, 'Katarzyna', 'Wiśniewska', '555666777', 'kwisniewska@biblioteka.pl', '$2a$12$FmyCU0P.kB86ROqHcN96W.4nVmjSPv.2AQYlNzeIT.lz8mcf4YlsW', 'Sikorskiego 42', 'Rzeszów', 'bibliotekarz'),
-- pass789
(4, 'Marek', 'Wójcik', '888999000', 'mwojcik@biblioteka.pl', '$2a$12$e/OAewjM1a0EJ2YTSwrwPu.YYKHWxpowVmF9bblVcZowMVFKsQBiu', 'Niepodległości 6', 'Rzeszów', 'bibliotekarz'),
-- bibliotekarz1
(5, 'Ewa', 'Kaczmarek', '222333444', 'ekaczmarek@biblioteka.pl', '$2a$12$uPQX6us6FIaoMgCkvuJAvOxAYipThZksjRFipe0ZNnTT8UfM1H5Fm', 'Krakowska 4', 'Ropczyce', 'bibliotekarz'),
-- securepass
(6, 'Tomasz', 'Mazur', '444555666', 'tmazur@biblioteka.pl', '$2a$12$40NVhxCYfYwPNakpHT4idOHcv6kW8HPaqRsFzz4HHIn3aDL85VANi', 'Cieplińskiego 24', 'Rzeszów', 'bibliotekarz'),
-- abc123
(7, 'Magdalena', 'Krawczyk', '777888999', 'mkrawczyk@biblioteka.pl', '$2a$12$h1jrupzFJpqn6RzwOujGLuddCPDJiR7x6VG8gEFj2fPqczhpVDL8u', 'Niepodległości 63', 'Rzeszów', 'bibliotekarz'),
-- haslo456
(8, 'Paweł', 'Zieliński', '111222333', 'pzielinski@biblioteka.pl', '$2a$12$heYmAKqU1DoJx76QRshAI.HhD1O/KnpRuyeMEFRazzsrxIab5zXGy', 'Pana Tadeusza 5/2', 'Dębica', 'bibliotekarz'),
-- password321
(9, 'Agnieszka', 'Szymańska', '666777888', 'aszymanska@biblioteka.pl', '$2a$12$zyTi4P8vC2jg76rndGJ7LOYF8I6kezApJW8z74ieveZLEyfi4czw6', 'Łukasiewicza 23', 'Rzeszów', 'bibliotekarz'),
-- libpass
(10, 'Jakub', 'Woźniak', '999000111', 'jwozniak@biblioteka.pl', '$2a$12$8.6dOs.ymqffhRJueKdnguIAvWL3OACB.RknpLvy2wj/FtxbCM4xK', 'Rejtana 12/4', 'Rzeszów', 'bibliotekarz');
-- passlib123
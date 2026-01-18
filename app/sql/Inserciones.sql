USE kidzbrain_db;

INSERT INTO tbl_grados (nombre, rango_edad) VALUES 
('1º y 2º Primaria', '6-8 años'),
('3º y 4º Primaria', '8-10 años'),
('5º y 6º Primaria', '10-12 años');

INSERT INTO tbl_materias (nombre) VALUES 
('Matemáticas'),
('Ciencias Naturales');

DROP TABLE IF EXISTS tbl_progreso;

CREATE TABLE tbl_progreso (
    id_progreso INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_leccion INT NOT NULL,  -- CAMBIO: Referencia directa a la Lección
    completado TINYINT(1) DEFAULT 0, -- 1 = Completada
    puntuacion_obtenida INT DEFAULT 0, -- Opcional: si la lección tiene un quiz final
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES tbl_usuarios(id_usuario),
    FOREIGN KEY (id_leccion) REFERENCES tbl_lecciones(id_leccion)
);

INSERT INTO tbl_lecciones (titulo, descripcion, orden, id_grado, id_materia) VALUES 
('Aprendiendo a contar', 'Aprende el concepto de contar', 1, 1, 1),
('¡Contando más allá del diez!', 'Aprende a nombrar los números mayores a 10', 2, 1, 1),
('Haciendo grupitos de a diez', 'Identifica las decenas y unidades', 3, 1, 1),
('Operaciones', 'Aprende a manejar operaciones como multiplicaciones', 1, 2, 1),
('Área y perímetro', 'Define el área y perímetro de figuras geométricas', 2, 2, 1),
('Ángulos', 'Aprende lo que son los ángulos, su tipo e identificación', 3, 2, 1),
('Fracciones y decimales', 'Aprende a sumar y restar fracciones y decimales', 1, 3, 1),
('Porcentajes', 'Aprende a calcular porcentajes', 2, 3, 1),
('El volumen (3D)', 'Comprende el volumen de cuerpos geométricos', 3, 3, 1);

INSERT INTO tbl_lecciones (titulo, descripcion, orden, id_grado, id_materia) VALUES 
('Las partes del cuerpo', 'Explora las partes que conforman el cuerpo humano', 1, 1, 2),
('Los 5 sentidos', 'Aprende los sentidos que posee el humano', 2, 1, 2),
('Las 3 R', 'Aprende sobre el cuidado del medio ambiente', 3, 1, 2),
('La materia', 'Conoce la materia y sus estados de agregación', 1, 2, 2),
('El sistema solar', 'Explora el sistema solar y los movimientos de la tierra', 2, 2, 2),
('Ecosistemas', 'Aprende sobre ecosistemas de México y el ciclo del agua', 3, 2, 2),
('Sistemas humanos', 'Conoce los diversos sistemas de órganos humanos', 1, 3, 2),
('Energía', 'Descubre qué es la energía y sus diferentes tipos', 2, 3, 2),
('Biología avanzada', 'Conoce qué compone los seres vivos', 3, 3, 2);

-- INSERT DE LECCIONES MATEMÁTICAS UWU
INSERT INTO tbl_juegos (descripcion, nombre_juego, tipo, id_leccion) VALUES
('Realiza sumas simples mediante el CONTEO de canicas.', 'Adicanicas', 'Cálculos simples', 1),
('Practica restas de números mayores a la centena mediante el cálculo del cambio.', 'La Tienda de Abarrotes', 'Cálculos simples', 4),
('Realiza aproximaciones de ángulos en un círculo a simple vista.', 'Pizzángulos', 'Destreza mental', 6),
('Identifica y replica fracciones sencillas.', 'Cafetería "BrainCafé"', 'Destreza mental', 7),
('Realiza multiplicaciones sencillas para cuadrar el volumen de un tanque.', 'Constructores', 'Cálculos simples', 9);

USE kidzbrain_db;

INSERT INTO tbl_grados (nombre, rango_edad) VALUES 
('1º y 2º Primaria', '6-8 años'),
('3º y 4º Primaria', '8-10 años'),
('5º y 6º Primaria', '10-12 años');

INSERT INTO tbl_materias (nombre) VALUES 
('Matemáticas'),
('Ciencias Naturales');

INSERT INTO tbl_lecciones (titulo, descripcion, orden, id_grado, id_materia) VALUES 
('Aprendiendo a contar', 'Aprende el concepto de contar', 1, 1, 1),
('¡Contando más allá del diez!', 'Aprende a nombrar los números mayores a 10', 2, 1, 1),
('Haciendo grupitos de a diez', 'Identifica las decenas y unidades', 3, 1, 1),
('Los patrones y series', 'Entiende el concepto de patrón y series aritméticas', 4, 1, 1),
('Operaciones', 'Aprende a manejar operaciones como multiplicaciones', 1, 2, 1),
('Área y perímetro', 'Define el área y perímetro de figuras geométricas', 2, 2, 1),
('Ángulos', 'Aprende lo que son los ángulos, su tipo e identificación', 3, 2, 1),
('Múltiplos y divisores', 'Aprende lo que son los múltiplos y divisores de un número natural', 4, 2, 1),
('Fracciones y decimales', 'Aprende a sumar y restar fracciones y decimales', 1, 3, 1),
('Porcentajes', 'Aprende a calcular porcentajes', 2, 3, 1),
('El volumen (3D)', 'Comprende el volumen de cuerpos geométricos', 3, 3, 1),
('Probabilidad básica', 'Aprende a calcular probabilidades sencillas a partir del concepto', 4, 3, 1);

INSERT INTO tbl_lecciones (titulo, descripcion, orden, id_grado, id_materia) VALUES 
('Las partes del cuerpo', 'Explora las partes que conforman el cuerpo humano', 1, 1, 2),
('Los 5 sentidos', 'Aprende los sentidos que posee el humano', 2, 1, 2),
('Las 3 R', 'Aprende sobre el cuidado del medio ambiente', 3, 1, 2),
('Los hábitats', 'Aprende sobre el entorno de los animales', 4, 1, 2),
('La materia', 'Conoce la materia y sus estados de agregación', 1, 2, 2),
('El sistema solar', 'Explora el sistema solar y los movimientos de la tierra', 2, 2, 2),
('Ecosistemas', 'Aprende sobre ecosistemas de México y el ciclo del agua', 3, 2, 2),
('¡A comer sano!', 'Aprende sobre dietas saludables', 4, 2, 2),
('Sistemas humanos', 'Conoce los diversos sistemas de órganos humanos', 1, 3, 2),
('Energía', 'Descubre qué es la energía y sus diferentes tipos', 2, 3, 2),
('Biología avanzada', 'Conoce qué compone los seres vivos', 3, 3, 2),
('La continuidad de la vida', 'Aprende sobre la reproducción de plantas y animales', 4, 3, 2);

-- INSERT DE JUEGOS DE MATEMÁTICAS UWU
INSERT INTO tbl_juegos (descripcion, nombre_juego, tipo, id_leccion) VALUES
('Realiza sumas simples mediante el CONTEO de canicas.', 'Adicanicas', 'Cálculos simples', 1),
('Practica restas de números mayores a la centena mediante el cálculo del cambio.', 'La Tienda de Abarrotes', 'Cálculos simples', 5),
('Realiza aproximaciones de ángulos en un círculo a simple vista.', 'Pizzángulos', 'Destreza mental', 7),
('Identifica múltiplos y divisores de números varios.', 'La feria de los globos', 'Cálculos simples', 8),
('Identifica y replica fracciones sencillas.', 'BrainCafé', 'Destreza mental', 9),
('Realiza multiplicaciones sencillas para cuadrar el volumen de un tanque.', 'Constructores', 'Cálculos simples', 11),
('Identifica probabilidades sencillas.', 'ProbaBubba', 'Destreza mental', 12);

-- INSERT DE JUEGOS DE CIENCIAS UWU
INSERT INTO tbl_juegos (descripcion, nombre_juego, tipo, id_leccion) VALUES
('Aprende a reconocer las partes del cuerpo humano.', 'El cuerpo humano', 'Conexion', 13),
('Aprender sobre los sentidos', '¿Qué sientes?', 'Preguntas', 14),
('Reconoce los tipos de basura', 'Las 3R', 'Conexion', 15),
('Reconoce los hábitats de animales diversos', 'Aventura animal', 'Preguntas', 16),
('Reconoce los componentes de una alimentación saludable', 'La lonchera de Brainbot', 'Conexion', 20);

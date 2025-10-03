
--1) Таблица car
CREATE TABLE IF NOT EXISTS hogwarts.car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price NUMERIC(10,2) CHECK (price >= 0)
);

-- 2) Таблица person

CREATE TABLE IF NOT EXISTS hogwarts.person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    age INT CHECK (age >= 0),
    has_driver_license BOOLEAN DEFAULT FALSE,
    car_id BIGINT REFERENCES hogwarts.car(id) ON DELETE SET NULL
);

-- 3) Пример вставки данных в car

INSERT INTO hogwarts.car (brand, model, price)
VALUES
    ('Toyota', 'Camry', 25000),
    ('Ford', 'Mustang', 55000)
ON CONFLICT DO NOTHING;


-- 4) Пример вставки данных в person

INSERT INTO hogwarts.person (name, age, has_driver_license, car_id)
VALUES
    ('Harry Potter', 17, TRUE, 1),
    ('Hermione Granger', 17, TRUE, 1),
    ('Ronald Weasley', 17, TRUE, 2),
    ('Draco Malfoy', 17, TRUE, 2)
ON CONFLICT DO NOTHING;

-- 5) Проверка данных

SELECT
    p.id, p.name, p.age, p.has_driver_license,
    c.brand AS car_brand, c.model AS car_model, c.price AS car_price
FROM hogwarts.person p
LEFT JOIN hogwarts.car c ON p.car_id = c.id
ORDER BY p.id;
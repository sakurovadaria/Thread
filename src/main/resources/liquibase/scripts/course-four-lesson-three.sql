--liquibase formatted sql

--changeset dshakurova:add-index-student-name
CREATE INDEX IF NOT EXISTS idx_student_name
    ON student (name);

--changeset dshakurova:add-index-faculty-name-color
CREATE INDEX IF NOT EXISTS idx_faculty_name_color
    ON faculty (name, color);

-- Проверка: список индексов в student
SELECT indexname, indexdef
FROM pg_indexes
WHERE tablename = 'student';

-- Проверка: список индексов в faculty
SELECT indexname, indexdef
FROM pg_indexes
WHERE tablename = 'faculty';

SELECT * FROM databasechangelog;

 1) STUDENT
ALTER TABLE IF EXISTS student
    DROP CONSTRAINT IF EXISTS chk_student_age;

ALTER TABLE student
    ADD CONSTRAINT chk_student_age CHECK (age >= 16);

ALTER TABLE student
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;

ALTER TABLE IF EXISTS student
    DROP CONSTRAINT IF EXISTS uq_student_name;

ALTER TABLE student
    ADD CONSTRAINT uq_student_name UNIQUE (name);

SELECT id, name, age, faculty_id
FROM student
ORDER BY id;

-- 2) FACULTY
ALTER TABLE IF EXISTS faculty
    DROP CONSTRAINT IF EXISTS uq_faculty_name_color;

ALTER TABLE faculty
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE faculty
    ALTER COLUMN color SET NOT NULL;

ALTER TABLE faculty
    ADD CONSTRAINT uq_faculty_name_color UNIQUE (name, color);

SELECT id, name, color
FROM faculty
ORDER BY id;
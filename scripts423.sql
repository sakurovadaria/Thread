-- 1) Все студенты с факультетами
SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM student s
LEFT JOIN faculty f ON s.faculty_id = f.id
ORDER BY s.name;


-- 2) Только студенты с аватарками

SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name,
    s.avatar_id
FROM student s
LEFT JOIN faculty f ON s.faculty_id = f.id
WHERE s.avatar_id IS NOT NULL
ORDER BY s.name;
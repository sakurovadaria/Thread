select
    s."age",
    s."id",
    s."name"
from
    "student" s;


select * from faculty;

select * from student;

select * from student where age between 10 and 20;

select name from student;

select * from student where name like '%o%';

select * from student where age < id;

select * from student order by age;
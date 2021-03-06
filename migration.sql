use ironmind_db;

insert into exercises (name, muscle) VALUES
('Sumo Deadlift', 'Legs/Back'), ('Front Squat', 'Legs'),
('Box Squat', 'Legs'), ('Overhead Squat', 'Legs'),
('Zercher Squat', 'Legs'), ('Decline Bench', 'Chest'),
('Incline Bench', 'Chest'), ('Overhead Press', 'Shoulders'),
('Clean and Jerk', 'Legs'), ('Snatch', 'Legs'),
('Power Clean', 'Legs'), ('Power Snatch', 'Legs'),
('DB Bench Press', 'Chest'), ('DB Shoulder Press', 'Shoulders'),
('Lateral Raise', 'Shoulders'), ('DB Curl', 'Biceps'),
('Lat Pulldown', 'Back');

select * from sub_set;

insert into sub_set (exercise_name, reps, weight, work_set_id)
values ('Deadlift', 3, 365, 1), ('Deadlift', 3, 395, 1), ('Deadlift', 3, 425, 1);

drop table sub_set;
drop table work_set;
drop table template;
drop table template_work_sets;

insert into programs (name, client_id) VALUES ('prog', 2);
insert into programs (id, name, client_id) VALUES (4, 'testing', 1);



select * from users;

insert into completedsets (day, estimated1rm, exercise_id, exercise_name, reps, totalweight, weight, client_id)
VALUES (1, 200, 1, 'Deadlift', 20, 2000, 100, 1);

describe users;

update users set bio = "My name is Andre" where id = 3;

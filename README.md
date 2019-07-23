# ExerciseOne

## A) 

Entity Relationship Diagram considering the hierarchy of Java classes:

![InfoScreen](/docs/schema1.png)

## B)

Method implemented in _com.emiliano.exerciseone.dataaccess.JDBCDao.groupStudentsByFirstLetterOfLastName_

## C)

Method implemented in _com.emiliano.exerciseone.dataaccess.JDBCDao.findStudentsBySubject_

The method does not need to verify if students are repeated, due to the primary key constraint in table Enrollment.


## D)

Other two possible diagrams of the database schema are: 

- Separate tables for each type of person:

![InfoScreen](/docs/schema2.png)

Pros: easy to insert, update and delete entities.

Cons: altering one column may imply to alter the same column in the other tables, in order to keep the database consistency. Besides, some queries may imply to combine the results of two or more SELECT statements from different tables.

- One common table for all persons, with a column to distinguish the person type:

![InfoScreen](/docs/schema3.png)

Pros: easy to insert, update, delete and select entities.

Cons: as new attributes are added, we may end up with a huge table with lots of columns and null values.


## E)

I would simplify the query as follows:

```
SELECT p.firstName, p.lastName 
FROM janitor j 
INNER JOIN person p ON p.id = j.id
WHERE j.workingArea = ‘Hallway’;
```

## F)

I would use an SQL View. Views ar good solutions for read only reports, where your frontend can easily display the contents from a single table.

Example:
```
CREATE VIEW denormalizedEmployee AS
    SELECT person.dni, person.firstName, person.lastName, person.birthDate, employee.startingDate
    FROM person, employee
    WHERE person.dni = employee.dni
```

## G)

Method implemented in _com.emiliano.exerciseone.dataaccess.JDBCDao.findStudentsByAgeRange_

Example for students with age between 19 and 21:
```
SELECT person.*, student.currentYear, TIMESTAMPDIFF(YEAR,birthDate,CURDATE()) AS age 
FROM student INNER JOIN person ON student.dni = person.dni 
HAVING age >= 19 AND age <= 21
```

## H)

The approach used to model the entities hierarchy requires some work when inserting and selecting data. 
For example, saving a student implies to insert two records, one in table _student_, and other in _person_.

To simplify this work, we can use one of the following approaches: (1) handle the updates and selections with an object-relational mapping framework, or (2) delegate the logic of CRUD operations to the database engine by means of stored procedures. 

Stored procedure for persisting students:

```
CREATE PROCEDURE save_student (IN dni INT,IN firstName VARCHAR(50),IN lastName VARCHAR(50),IN birthDate DATE,IN currentYear INT)
BEGIN
    insert into person values (dni, firstName, lastName, birthDate);
    insert into student values (dni , currentYear);
END
```

As an example, a student can be persisted with the following statement:

```
CALL save_student(38736278, 'Elena', 'Fernandez', '2001-10-10', 2);
```

- Pros: with stored procedures we can delegate some complex CRUD operations to the database engine.
- Cons: stored procedures may not be the best place to put complex logic.  


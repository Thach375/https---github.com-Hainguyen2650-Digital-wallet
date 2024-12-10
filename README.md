set up database using Xampp and phpmyadmin 

open Xampp, start Apache and MySQL
to the path http://localhost/phpmyadmin/index.php
create database "db_demo" and create browse "registration"
create structure:
    Fullname text return Null
    DateOfBirth date return Null
    Gender text return Null
    CitizenID int return Null
    Email text return Null
    UserName text return Null
    Password text return Null
    QA_Questions text return Null
    QA_Answer text return Null

In VS Code, add the file `mysql-connector-java-8.0.29.jar` to the dependencies.
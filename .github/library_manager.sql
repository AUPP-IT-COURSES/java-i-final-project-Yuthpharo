-- Create the database
CREATE DATABASE IF NOT EXISTS library_manager;

-- Use the database
USE library_manager;


-- Create the users table first

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT ,
    username VARCHAR(255) UNIQUE NOT NULL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);


-- Create the books table
CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL
);

-- Create the transactions table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT,
    user_id INT,
    transaction_type VARCHAR(50) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (username) REFERENCES users(username)
);



-- Create the requested_books table
CREATE TABLE IF NOT EXISTS requested_books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_title VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username) 
);






-- Show the updated structure of the users table
DESCRIBE users;

-- Show the structure of the users table
DESCRIBE users;

-- Show the structure of the requested_books table
DESCRIBE requested_books;

-- Show all tables in the database
SELECT * FROM users;
SHOW TABLES;

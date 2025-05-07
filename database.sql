CREATE TABLE roles (
    role_id INT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE users (
    user_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE citizen (
    citizen_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT UNSIGNED NULL, 
    cccd_id VARCHAR(255) NOT NULL UNIQUE,
    job VARCHAR(255) NOT NULL,
    dob DATETIME NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE resident (
    resident_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    householder_id BIGINT UNSIGNED NOT NULL,  
    address VARCHAR(255) NOT NULL,
    
    FOREIGN KEY (householder_id) REFERENCES citizen(citizen_id)
);

ALTER TABLE citizen
ADD CONSTRAINT fk_resident_id
FOREIGN KEY (resident_id) REFERENCES resident(resident_id);

CREATE TABLE accommodation_information (
    ai_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    citizen_id BIGINT UNSIGNED NOT NULL,  
    description VARCHAR(255) NOT NULL,

    FOREIGN KEY (citizen_id) REFERENCES citizen(citizen_id)
);

CREATE TABLE accountant (
    accountant_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL, 
    salary DOUBLE NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE manager (
    manager_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,  

    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE service (
    service_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    fee DOUBLE NOT NULL
);

CREATE TABLE service_registration (
    sr_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT UNSIGNED NOT NULL,  
    service_id BIGINT UNSIGNED NOT NULL, 

    FOREIGN KEY (resident_id) REFERENCES resident(resident_id),
    FOREIGN KEY (service_id) REFERENCES service(service_id)
);

CREATE TABLE collection_period (
    cp_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cp_name VARCHAR(255) NOT NULL,
    resident_id BIGINT UNSIGNED NOT NULL, 
    accountant_id BIGINT UNSIGNED NOT NULL,  
    collection_date DATE NOT NULL,

    FOREIGN KEY (resident_id) REFERENCES resident(resident_id),
    FOREIGN KEY (accountant_id) REFERENCES accountant(accountant_id)
);

CREATE TABLE payment (
    payment_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    citizen_id BIGINT UNSIGNED NOT NULL, 
    cp_id BIGINT UNSIGNED NOT NULL, 
    cost DOUBLE NOT NULL,

    FOREIGN KEY (citizen_id) REFERENCES citizen(citizen_id),
    FOREIGN KEY (cp_id) REFERENCES collection_period(cp_id)
);

CREATE TABLE revenue (
    revenue_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    revenue_name VARCHAR(255) NOT NULL,
    cost DOUBLE NOT NULL
);

ALTER TABLE resident
ADD CONSTRAINT unique_householder UNIQUE(householder_id);

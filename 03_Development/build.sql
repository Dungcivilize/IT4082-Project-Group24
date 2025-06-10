-- BẢNG CĂN HỘ
CREATE TABLE `apartment` (
    `apartment_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `apartment_code` VARCHAR(10) NOT NULL,
    `floor` INT NOT NULL,
    `area` DECIMAL(10,2) NULL CHECK (`area` > 0),
    `status` ENUM('occupied', 'empty') NULL DEFAULT 'empty',
    UNIQUE (`apartment_code`)
);

-- BẢNG TÀI KHOẢN NGƯỜI DÙNG
CREATE TABLE `users` (
    `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NULL,
    `phone` VARCHAR(20) NULL,
    `role` ENUM('admin', 'accountant', 'resident') NOT NULL DEFAULT 'resident'
);

-- BẢNG LỊCH SỬ SỞ HỮU CĂN HỘ
CREATE TABLE `apartment_ownership` (
    `ownership_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `apartment_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NULL,  -- NULL nghĩa là đang sở hữu
    `status` ENUM('active', 'inactive') DEFAULT 'active',
    FOREIGN KEY (`apartment_id`) REFERENCES `apartment`(`apartment_id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE
);



-- BẢNG CƯ DÂN
CREATE TABLE `resident` (
    `resident_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `ownership_id` INT NOT NULL,  -- Thay đổi từ apartment_id sang ownership_id
    `full_name` VARCHAR(100) NOT NULL,
    `birth_date` DATE NOT NULL,
    `gender` ENUM('male', 'female', 'other') NOT NULL,
    `identity_card` VARCHAR(20) UNIQUE,
    `phone` VARCHAR(20),
    `email` VARCHAR(100),
    `occupation` VARCHAR(100),
    `resident_type` ENUM('owner', 'member') NOT NULL,
    `relationship` VARCHAR(50),
    `status` ENUM('living', 'moved_out', 'temporary_absent') DEFAULT 'living',
    FOREIGN KEY (`ownership_id`) REFERENCES `apartment_ownership`(`ownership_id`) ON DELETE CASCADE
);

-- BẢNG TẠM TRÚ
CREATE TABLE `temporary_resident` (
    `temporary_resident_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `ownership_id` INT NOT NULL,  -- Thay đổi từ apartment_id sang ownership_id
    `full_name` VARCHAR(100) NOT NULL,
    `birth_date` DATE NOT NULL,
    `gender` ENUM('male', 'female', 'other') NOT NULL,
    `identity_card` VARCHAR(20) UNIQUE,
    `phone` VARCHAR(20),
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `reason` TEXT,
    FOREIGN KEY (`ownership_id`) REFERENCES `apartment_ownership`(`ownership_id`) ON DELETE CASCADE,
    CHECK (`start_date` <= `end_date`)
);

-- BẢNG TẠM VẮNG
CREATE TABLE `temporary_absent` (
    `temporary_absent_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `resident_id` INT NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `temporary_address` TEXT NOT NULL,
    `reason` TEXT,
    FOREIGN KEY (`resident_id`) REFERENCES `resident`(`resident_id`) ON DELETE CASCADE,
    CHECK (`start_date` <= `end_date`)
);

-- BẢNG PHƯƠNG TIỆN
CREATE TABLE `vehicle` (
    `vehicle_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `ownership_id` INT NOT NULL,  -- Thay đổi từ apartment_id sang ownership_id
    `type` ENUM('car', 'motorcycle', 'bicycle') NOT NULL,
    `license_plate` VARCHAR(20) NOT NULL UNIQUE,
    `owner_id` INT NOT NULL,
    FOREIGN KEY (`ownership_id`) REFERENCES `apartment_ownership`(`ownership_id`) ON DELETE CASCADE,
    FOREIGN KEY (`owner_id`) REFERENCES `resident`(`resident_id`) ON DELETE CASCADE
);

-- BẢNG KỲ THU PHÍ
CREATE TABLE `payment_period` (
    `payment_period_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `month` INT NOT NULL CHECK (`month` BETWEEN 1 AND 12),
    `year` INT NOT NULL CHECK (`year` > 2000),
    `status` ENUM('collecting', 'completed') DEFAULT 'collecting',
    `note` TEXT,
    UNIQUE (`month`, `year`)
);

-- BẢNG LOẠI DỊCH VỤ
CREATE TABLE `service_type` (
    `service_type_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `service_name` VARCHAR(50) NOT NULL,
    `service_type` ENUM('electricity', 'water', 'motorbike', 'car', 'management', 'maintenance') NOT NULL,
    `unit_price` DECIMAL(10,2) NOT NULL CHECK (`unit_price` >= 0)
);

-- BẢNG CHI TIẾT THANH TOÁN
CREATE TABLE `payment_detail` (
    `payment_detail_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `payment_period_id` INT NOT NULL,
    `ownership_id` INT NOT NULL,  -- Thay đổi từ apartment_id sang ownership_id
    `service_type_id` INT NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL CHECK (`amount` >= 0),  -- Số lượng sử dụng
    `price` DECIMAL(10,2) NOT NULL CHECK (`price` >= 0),    -- Số tiền = amount * unit_price
    `status` ENUM('UNPAID', 'PROCESSING', 'PAID') DEFAULT 'UNPAID',  -- Trạng thái thanh toán
    `transaction_code` VARCHAR(50),
    `paid_at` TIMESTAMP NULL,  -- Thời điểm thanh toán
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`payment_period_id`) REFERENCES `payment_period`(`payment_period_id`) ON DELETE CASCADE,
    FOREIGN KEY (`ownership_id`) REFERENCES `apartment_ownership`(`ownership_id`) ON DELETE CASCADE,
    FOREIGN KEY (`service_type_id`) REFERENCES `service_type`(`service_type_id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_payment_detail` (`payment_period_id`, `ownership_id`, `service_type_id`)
);

INSERT INTO apartment (apartment_code, floor, area, status) VALUES
('A101', 1, 75.5, 'occupied'),
('A102', 1, 80.0, 'empty'),
('B201', 2, 60.0, 'occupied'),
('C301', 3, 90.0, 'occupied');


INSERT INTO users (username, password, full_name, email, phone, role) VALUES
('admin01', 'pass123', 'Nguyen Van A', 'a@gmail.com', '0909000001', 'admin'),
('acc01', 'pass123', 'Le Thi B', 'b@gmail.com', '0909000002', 'accountant'),
('res01', 'pass123', 'Tran Van C', 'c@gmail.com', '0909000003', 'resident'),
('res02', 'pass123', 'Pham Thi D', 'd@gmail.com', '0909000004', 'resident');

INSERT INTO apartment_ownership (apartment_id, user_id, start_date, end_date, status) VALUES
(1, 3, '2023-01-01', NULL, 'active'), -- A101 - Tran Van C
(3, 4, '2023-06-15', NULL, 'active'); -- B201 - Pham Thi D


INSERT INTO resident (ownership_id, full_name, birth_date, gender, identity_card, phone, email, occupation, resident_type, relationship, status) VALUES
(1, 'Tran Van C', '1985-05-15', 'male', '012345678', '0909000003', 'c@gmail.com', 'Engineer', 'owner', NULL, 'living'),
(1, 'Nguyen Thi E', '1987-08-20', 'female', '012345679', '0909000005', 'e@gmail.com', 'Teacher', 'member', 'wife', 'living'),
(2, 'Pham Thi D', '1990-12-10', 'female', '012345680', '0909000004', 'd@gmail.com', 'Doctor', 'owner', NULL, 'living');


INSERT INTO vehicle (ownership_id, type, license_plate, owner_id) VALUES
(1, 'car', '30A-12345', 1),
(1, 'motorcycle', '29B1-56789', 2),
(2, 'car', '30C-22222', 3);

INSERT INTO payment_period (month, year, status, note) VALUES
(6, 2025, 'collecting', 'Thu phí tháng 6/2025'),
(5, 2025, 'completed', 'Thu phí tháng 5/2025');


INSERT INTO service_type (service_name, service_type, unit_price) VALUES
('Tiền Điện', 'electricity', 3500),
('Tiền Nước', 'water', 10000),
('Phí xe máy', 'motorbike', 50000),
('Phí ô tô', 'car', 100000),
('Phí quản lý', 'management', 200000);


-- A101 (ownership_id=1), kỳ 6/2025
INSERT INTO payment_detail (payment_period_id, ownership_id, service_type_id, amount, price, status) VALUES
(1, 1, 1, 100, 350000, 'UNPAID'),   -- Điện
(1, 1, 2, 20, 200000, 'UNPAID'),    -- Nước
(1, 1, 3, 1, 50000, 'UNPAID'),     -- Xe máy
(1, 1, 5, 1, 200000, 'UNPAID');     -- Quản lý

-- B201 (ownership_id=3), kỳ 6/2025
INSERT INTO payment_detail (payment_period_id, ownership_id, service_type_id, amount, price, status) VALUES
(1, 2, 1, 80, 280000, 'UNPAID'),    -- Điện
(1, 2, 2, 25, 250000, 'UNPAID'),    -- Nước
(1, 2, 4, 1, 100000, 'UNPAID'),     -- Ô tô
(1, 2, 5, 1, 200000, 'UNPAID');     -- Quản lý
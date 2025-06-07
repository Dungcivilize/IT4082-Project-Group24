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
    `role` ENUM('admin', 'accountant', 'resident') NOT NULL DEFAULT 'resident',
    `apartment_id` INT UNIQUE,  -- 1 căn hộ chỉ có 1 tài khoản duy nhất
    FOREIGN KEY (`apartment_id`) REFERENCES `apartment`(`apartment_id`) ON DELETE SET NULL
);

-- BẢNG CƯ DÂN
CREATE TABLE `resident` (
    `resident_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `apartment_id` INT NOT NULL,
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
    FOREIGN KEY (`apartment_id`) REFERENCES `apartment`(`apartment_id`) ON DELETE CASCADE
);

-- BẢNG TẠM TRÚ
CREATE TABLE `temporary_resident` (
    `temporary_resident_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `apartment_id` INT NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `birth_date` DATE NOT NULL,
    `gender` ENUM('male', 'female', 'other') NOT NULL,
    `identity_card` VARCHAR(20) UNIQUE,
    `phone` VARCHAR(20),
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `reason` TEXT,
    FOREIGN KEY (`apartment_id`) REFERENCES `apartment`(`apartment_id`) ON DELETE CASCADE,
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
    `apartment_id` INT NOT NULL,
    `type` ENUM('car', 'motorcycle', 'bicycle') NOT NULL,
    `license_plate` VARCHAR(20) NOT NULL UNIQUE,
    `owner_id` INT NOT NULL,
    FOREIGN KEY (`apartment_id`) REFERENCES `apartment`(`apartment_id`) ON DELETE CASCADE,
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
    `apartment_id` INT NOT NULL,
    `service_type_id` INT NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL CHECK (`amount` >= 0),  -- Số lượng sử dụng
    `status` ENUM('pending', 'paid') DEFAULT 'pending',  -- Trạng thái thanh toán của chi tiết
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`payment_period_id`) REFERENCES `payment_period`(`payment_period_id`) ON DELETE CASCADE,
    FOREIGN KEY (`apartment_id`) REFERENCES `apartment`(`apartment_id`) ON DELETE CASCADE,
    FOREIGN KEY (`service_type_id`) REFERENCES `service_type`(`service_type_id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_payment_detail` (`payment_period_id`, `apartment_id`, `service_type_id`)
);

-- BẢNG THANH TOÁN
CREATE TABLE `payment` (
    `payment_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `payment_detail_id` INT NOT NULL,
    `price` DECIMAL(10,2) NOT NULL CHECK (`price` >= 0), -- Số tiền = amount * unit_price
    `paid_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `transaction_code` VARCHAR(50),
    `status` ENUM('PAID', 'UNPAID', 'PROCESSING') DEFAULT 'UNPAID',
    `note` TEXT,
    FOREIGN KEY (`payment_detail_id`) REFERENCES `payment_detail`(`payment_detail_id`) ON DELETE CASCADE
);

-- TRIGGER để tự động tính price khi tạo payment
DELIMITER //
CREATE TRIGGER before_payment_insert 
BEFORE INSERT ON payment
FOR EACH ROW
BEGIN
    DECLARE calculated_price DECIMAL(10,2);
    
    -- Tính price = amount * unit_price
    SELECT pd.amount * st.unit_price INTO calculated_price
    FROM payment_detail pd
    JOIN service_type st ON pd.service_type_id = st.service_type_id
    WHERE pd.payment_detail_id = NEW.payment_detail_id;
    
    -- Gán giá trị đã tính vào price
    SET NEW.price = calculated_price;
END//

-- TRIGGER để cập nhật trạng thái payment_detail khi payment được thanh toán
CREATE TRIGGER after_payment_status_update
AFTER UPDATE ON payment
FOR EACH ROW
BEGIN
    IF NEW.status = 'PAID' THEN
        -- Cập nhật trạng thái của payment_detail thành paid
        UPDATE payment_detail 
        SET status = 'paid'
        WHERE payment_detail_id = NEW.payment_detail_id;
        
        -- Kiểm tra xem tất cả payment_detail trong kỳ đã được thanh toán chưa
        -- Nếu đã thanh toán hết thì cập nhật trạng thái của payment_period
        IF NOT EXISTS (
            SELECT 1 
            FROM payment_detail pd
            WHERE pd.payment_period_id = (
                SELECT payment_period_id 
                FROM payment_detail 
                WHERE payment_detail_id = NEW.payment_detail_id
            )
            AND pd.status = 'pending'
        ) THEN
            UPDATE payment_period pp
            SET pp.status = 'completed'
            WHERE pp.payment_period_id = (
                SELECT payment_period_id 
                FROM payment_detail 
                WHERE payment_detail_id = NEW.payment_detail_id
            );
        END IF;
    END IF;
END//

DELIMITER ;
-- Tạo function để đếm số lượng xe theo loại của một căn hộ
DELIMITER //
CREATE FUNCTION count_vehicles(p_apartment_id INT, p_vehicle_type VARCHAR(20))
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE vehicle_count INT;
    
    SELECT COUNT(*) INTO vehicle_count
    FROM vehicle
    WHERE apartment_id = p_apartment_id AND type = p_vehicle_type;
    
    RETURN vehicle_count;
END //
DELIMITER ;

-- Trigger để tự động cập nhật số lượng xe vào payment_detail
DELIMITER //
CREATE TRIGGER before_payment_detail_insert
BEFORE INSERT ON payment_detail
FOR EACH ROW
BEGIN
    DECLARE service_type_name VARCHAR(20);
    
    -- Lấy loại dịch vụ
    SELECT service_type INTO service_type_name
    FROM service_type
    WHERE service_type_id = NEW.service_type_id;
    
    -- Nếu là dịch vụ gửi xe máy, tự động cập nhật số lượng xe máy
    IF service_type_name = 'motorbike' THEN
        SET NEW.amount = count_vehicles(NEW.apartment_id, 'motorcycle');
    -- Nếu là dịch vụ gửi ô tô, tự động cập nhật số lượng ô tô
    ELSEIF service_type_name = 'car' THEN
        SET NEW.amount = count_vehicles(NEW.apartment_id, 'car');
    END IF;
END //
DELIMITER ;

-- Trigger để tự động cập nhật payment_detail khi thêm/xóa xe
DELIMITER //
CREATE TRIGGER after_vehicle_change
AFTER INSERT ON vehicle
FOR EACH ROW
BEGIN
    DECLARE current_period_id INT;
    DECLARE service_type_id INT;
    
    -- Lấy kỳ thu phí hiện tại (status = 'collecting')
    SELECT payment_period_id INTO current_period_id
    FROM payment_period
    WHERE status = 'collecting'
    ORDER BY year DESC, month DESC
    LIMIT 1;
    
    IF current_period_id IS NOT NULL THEN
        -- Nếu là xe máy
        IF NEW.type = 'motorcycle' THEN
            -- Lấy service_type_id của dịch vụ gửi xe máy
            SELECT service_type_id INTO service_type_id
            FROM service_type
            WHERE service_type = 'motorbike'
            LIMIT 1;
            
            IF service_type_id IS NOT NULL THEN
                -- Cập nhật hoặc thêm mới payment_detail
                INSERT INTO payment_detail (payment_period_id, apartment_id, service_type_id, amount, status)
                VALUES (current_period_id, NEW.apartment_id, service_type_id, 
                       (SELECT COUNT(*) FROM vehicle 
                        WHERE apartment_id = NEW.apartment_id AND type = 'motorcycle'), 'pending')
                ON DUPLICATE KEY UPDATE 
                    amount = (SELECT COUNT(*) FROM vehicle 
                             WHERE apartment_id = NEW.apartment_id AND type = 'motorcycle');
            END IF;
        
        -- Nếu là ô tô
        ELSEIF NEW.type = 'car' THEN
            -- Lấy service_type_id của dịch vụ gửi ô tô
            SELECT service_type_id INTO service_type_id
            FROM service_type
            WHERE service_type = 'car'
            LIMIT 1;
            
            IF service_type_id IS NOT NULL THEN
                -- Cập nhật hoặc thêm mới payment_detail
                INSERT INTO payment_detail (payment_period_id, apartment_id, service_type_id, amount, status)
                VALUES (current_period_id, NEW.apartment_id, service_type_id,
                       (SELECT COUNT(*) FROM vehicle 
                        WHERE apartment_id = NEW.apartment_id AND type = 'car'), 'pending')
                ON DUPLICATE KEY UPDATE 
                    amount = (SELECT COUNT(*) FROM vehicle 
                             WHERE apartment_id = NEW.apartment_id AND type = 'car');
            END IF;
        END IF;
    END IF;
END //
DELIMITER ;

-- Trigger tương tự cho DELETE
DELIMITER //
CREATE TRIGGER after_vehicle_delete
AFTER DELETE ON vehicle
FOR EACH ROW
BEGIN
    DECLARE current_period_id INT;
    DECLARE service_type_id INT;
    
    -- Lấy kỳ thu phí hiện tại (status = 'collecting')
    SELECT payment_period_id INTO current_period_id
    FROM payment_period
    WHERE status = 'collecting'
    LIMIT 1;
    
    -- Nếu là xe máy
    IF OLD.type = 'motorcycle' THEN
        -- Lấy service_type_id của dịch vụ gửi xe máy
        SELECT service_type_id INTO service_type_id
        FROM service_type
        WHERE service_type = 'motorbike';
        
        -- Cập nhật payment_detail
        UPDATE payment_detail
        SET amount = count_vehicles(OLD.apartment_id, 'motorcycle')
        WHERE payment_period_id = current_period_id
        AND apartment_id = OLD.apartment_id
        AND service_type_id = service_type_id
        AND status = 'pending';
    
    -- Nếu là ô tô
    ELSEIF OLD.type = 'car' THEN
        -- Lấy service_type_id của dịch vụ gửi ô tô
        SELECT service_type_id INTO service_type_id
        FROM service_type
        WHERE service_type = 'car';
        
        -- Cập nhật payment_detail
        UPDATE payment_detail
        SET amount = count_vehicles(OLD.apartment_id, 'car')
        WHERE payment_period_id = current_period_id
        AND apartment_id = OLD.apartment_id
        AND service_type_id = service_type_id
        AND status = 'pending';
    END IF;
END //
DELIMITER ; 



-- 1. Thêm căn hộ
INSERT INTO `apartment` (`apartment_code`, `floor`, `area`, `status`) VALUES
('A101', 1, 75.5, 'occupied'),
('A102', 1, 85.0, 'occupied'),
('A103', 1, 65.0, 'empty'),
('B201', 2, 75.5, 'occupied');

-- 2. Thêm tài khoản người dùng
INSERT INTO `users` (`username`, `password`, `full_name`, `email`, `phone`, `role`, `apartment_id`) VALUES
('admin', '$2a$12$abc', 'Admin User', 'admin@example.com', '0901234567', 'admin', NULL),
('accountant', '$2a$12$def', 'Accountant User', 'accountant@example.com', '0901234568', 'accountant', NULL),
('resident1', '$2a$12$ghi', 'Nguyễn Văn A', 'nga@example.com', '0901234569', 'resident', 1),
('resident2', '$2a$12$jkl', 'Trần Thị B', 'thib@example.com', '0901234570', 'resident', 2);

-- 3. Thêm cư dân
INSERT INTO `resident` (`apartment_id`, `full_name`, `birth_date`, `gender`, `identity_card`, `phone`, `email`, `occupation`, `resident_type`, `relationship`, `status`) VALUES
(1, 'Nguyễn Văn A', '1980-01-01', 'male', '001234567890', '0901234569', 'nga@example.com', 'Engineer', 'owner', NULL, 'living'),
(1, 'Nguyễn Thị X', '1985-05-15', 'female', '001234567891', '0901234571', 'ntx@example.com', 'Teacher', 'member', 'Vợ', 'living'),
(2, 'Trần Thị B', '1975-12-25', 'female', '001234567892', '0901234570', 'thib@example.com', 'Doctor', 'owner', NULL, 'living'),
(2, 'Trần Văn Y', '2010-08-20', 'male', '001234567893', NULL, NULL, 'Student', 'member', 'Con', 'living');

-- 4. Thêm người tạm trú
INSERT INTO `temporary_resident` (`apartment_id`, `full_name`, `birth_date`, `gender`, `identity_card`, `phone`, `start_date`, `end_date`, `reason`) VALUES
(1, 'Lê Văn C', '1990-03-15', 'male', '001234567894', '0901234572', '2024-01-01', '2024-06-30', 'Người giúp việc'),
(2, 'Phạm Thị D', '1995-07-20', 'female', '001234567895', '0901234573', '2024-02-01', '2024-12-31', 'Người thân');

-- 5. Thêm tạm vắng
INSERT INTO `temporary_absent` (`resident_id`, `start_date`, `end_date`, `temporary_address`, `reason`) VALUES
(2, '2024-03-01', '2024-03-15', '123 ABC Street, XYZ City', 'Công tác');

-- 6. Thêm loại dịch vụ
INSERT INTO `service_type` (`service_name`, `service_type`, `unit_price`) VALUES
('Tiền điện', 'electricity', 3500),
('Tiền nước', 'water', 15000),
('Phí gửi xe máy', 'motorbike', 100000),
('Phí gửi ô tô', 'car', 1200000),
('Phí quản lý', 'management', 500000);

-- 7. Thêm kỳ thu phí
INSERT INTO `payment_period` (`month`, `year`, `status`, `note`) VALUES
(3, 2024, 'collecting', 'Kỳ thu phí tháng 3/2024');

-- 8. Thêm xe (trigger sẽ tự động tạo payment_detail)
INSERT INTO `vehicle` (`apartment_id`, `type`, `license_plate`, `owner_id`) VALUES
(1, 'motorcycle', '29B-567.89', 1),  -- Xe máy của chủ hộ A101
(1, 'car', '30A-123.45', 1),         -- Ô tô của chủ hộ A101
(2, 'motorcycle', '29B-567.90', 3),  -- Xe máy của chủ hộ A102
(2, 'motorcycle', '29B-567.91', 3);  -- Xe máy thứ 2 của chủ hộ A102

-- 9. Thêm các chi tiết thanh toán khác
INSERT INTO `payment_detail` (`payment_period_id`, `apartment_id`, `service_type_id`, `amount`, `status`) 
SELECT 
    1 as payment_period_id,
    a.apartment_id,
    st.service_type_id,
    CASE 
        WHEN st.service_type = 'electricity' THEN 100
        WHEN st.service_type = 'water' THEN 15
        WHEN st.service_type = 'management' THEN 1
        ELSE 0
    END as amount,
    'pending' as status
FROM apartment a
CROSS JOIN service_type st
WHERE a.status = 'occupied'
AND st.service_type IN ('electricity', 'water', 'management')
AND NOT EXISTS (
    SELECT 1 
    FROM payment_detail pd 
    WHERE pd.payment_period_id = 1 
    AND pd.apartment_id = a.apartment_id 
    AND pd.service_type_id = st.service_type_id
);

-- 10. Tạo payment cho tất cả payment_detail
INSERT INTO `payment` (`payment_detail_id`, `price`, `transaction_code`, `status`, `note`)
SELECT 
    pd.payment_detail_id,
    0,  -- Trigger sẽ tự động tính price
    CONCAT('TRX', LPAD(pd.payment_detail_id, 3, '0')),
    'UNPAID',
    CONCAT('Phí ', st.service_name, ' tháng 3/2024 - ', a.apartment_code)
FROM payment_detail pd
JOIN service_type st ON pd.service_type_id = st.service_type_id
JOIN apartment a ON pd.apartment_id = a.apartment_id
WHERE pd.payment_period_id = 1;

-- Kiểm tra kết quả
SELECT 
    a.apartment_code,
    st.service_name,
    pd.amount,
    p.price,
    p.status
FROM payment p
JOIN payment_detail pd ON p.payment_detail_id = pd.payment_detail_id
JOIN service_type st ON pd.service_type_id = st.service_type_id
JOIN apartment a ON pd.apartment_id = a.apartment_id
ORDER BY a.apartment_code, st.service_name; 

select * from `users`

select * from resident;
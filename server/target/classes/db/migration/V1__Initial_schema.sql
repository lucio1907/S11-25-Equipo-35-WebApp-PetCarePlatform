-- Create the database
CREATE DATABASE IF NOT EXISTS pet_health_tracker;
USE pet_health_tracker;

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    profile_picture_url VARCHAR(500),
    email_verified BOOLEAN DEFAULT FALSE,
    reset_password_token VARCHAR(255),
    reset_token_expires DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: pets
CREATE TABLE IF NOT EXISTS pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    species ENUM('DOG', 'CAT', 'BIRD', 'RABBIT', 'FISH', 'REPTILE', 'OTHER') NOT NULL,
    breed VARCHAR(100),
    date_of_birth DATE,
    age_years INT,
    age_months INT,
    weight DECIMAL(5,2),
    weight_unit ENUM('KG', 'LB') DEFAULT 'KG',
    profile_picture_url VARCHAR(500),
    microchip_number VARCHAR(100),
    color VARCHAR(100),
    gender ENUM('MALE', 'FEMALE', 'UNKNOWN'),
    health_notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_pets_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: vaccinations
CREATE TABLE IF NOT EXISTS vaccinations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    vaccine_name VARCHAR(200) NOT NULL,
    vaccine_type ENUM('CORE', 'NON_CORE', 'OPTIONAL') NOT NULL,
    administration_date DATE NOT NULL,
    next_due_date DATE,
    veterinarian_name VARCHAR(200),
    clinic_name VARCHAR(200),
    lot_number VARCHAR(100),
    notes TEXT,
    attachment_url VARCHAR(500),
    is_completed BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    INDEX idx_vaccinations_pet_id (pet_id),
    INDEX idx_vaccinations_due_date (next_due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: veterinary_visits
CREATE TABLE IF NOT EXISTS veterinary_visits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    visit_date DATE NOT NULL,
    visit_type ENUM('ROUTINE_CHECKUP', 'EMERGENCY', 'VACCINATION', 'SURGERY', 'DENTAL', 'OTHER') NOT NULL,
    veterinarian_name VARCHAR(200),
    clinic_name VARCHAR(200),
    reason_for_visit TEXT,
    diagnosis TEXT,
    treatment_notes TEXT,
    prescribed_medications TEXT,
    cost DECIMAL(10,2),
    next_visit_date DATE,
    attachments_urls JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    INDEX idx_vet_visits_pet_id (pet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: feeding_schedule
CREATE TABLE IF NOT EXISTS feeding_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    food_type VARCHAR(200) NOT NULL,
    food_brand VARCHAR(200),
    portion_size DECIMAL(6,2),
    portion_unit ENUM('G', 'KG', 'CUP', 'ML', 'OZ') DEFAULT 'G',
    feeding_frequency ENUM('DAILY_ONCE', 'DAILY_TWICE', 'DAILY_THRICE', 'CUSTOM') NOT NULL,
    custom_schedule JSON,
    special_instructions TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    INDEX idx_feeding_schedule_pet_id (pet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: feeding_records
CREATE TABLE IF NOT EXISTS feeding_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    feeding_schedule_id BIGINT,
    feeding_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    food_type VARCHAR(200),
    portion_consumed DECIMAL(6,2),
    notes TEXT,
    was_eaten BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    FOREIGN KEY (feeding_schedule_id) REFERENCES feeding_schedule(id) ON DELETE SET NULL,
    INDEX idx_feeding_records_pet_id (pet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: medications
CREATE TABLE IF NOT EXISTS medications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    medication_name VARCHAR(200) NOT NULL,
    dosage VARCHAR(100),
    frequency VARCHAR(100),
    start_date DATE,
    end_date DATE,
    reason TEXT,
    administering_instructions TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    INDEX idx_medications_pet_id (pet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: reminders
CREATE TABLE IF NOT EXISTS reminders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    reminder_type ENUM('VACCINATION', 'VETERINARY_VISIT', 'MEDICATION', 'FEEDING', 'GROOMING', 'OTHER') NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    due_date DATE NOT NULL,
    due_time TIME,
    is_recurring BOOLEAN DEFAULT FALSE,
    recurrence_pattern ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY', 'CUSTOM'),
    custom_recurrence JSON,
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP NULL,
    notification_sent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_reminders_user_pet (user_id, pet_id),
    INDEX idx_reminders_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: deworming_treatments
CREATE TABLE IF NOT EXISTS deworming_treatments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    treatment_name VARCHAR(200) NOT NULL,
    administration_date DATE NOT NULL,
    next_due_date DATE,
    medication_type ENUM('TABLET', 'LIQUID', 'SPOT_ON', 'INJECTION', 'OTHER'),
    dosage VARCHAR(100),
    veterinarian_name VARCHAR(200),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    INDEX idx_deworming_pet_id (pet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- TABLE: users
-- =========================================================

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    lastLogin TIMESTAMP,

    specialty VARCHAR(100),

    phone VARCHAR(20),

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    createdId BIGINT,
    modifiedId BIGINT
);

-- =========================================================
-- TABLE: roles
-- =========================================================

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(50) NOT NULL UNIQUE
);

-- =========================================================
-- TABLE: roles_users
-- =========================================================

CREATE TABLE roles_users (
    id BIGSERIAL PRIMARY KEY,

    roleId BIGINT NOT NULL,
    userId BIGINT NOT NULL,

    status BOOLEAN NOT NULL DEFAULT TRUE,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    createdId BIGINT,
    modifiedId BIGINT,

    CONSTRAINT fk_roles_users_role
        FOREIGN KEY (roleId)
        REFERENCES roles(id),

    CONSTRAINT fk_roles_users_user
        FOREIGN KEY (userId)
        REFERENCES users(id),

    CONSTRAINT uq_roles_users
        UNIQUE (roleId, userId)
);

-- =========================================================
-- TABLE: customers
-- =========================================================

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,

    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,

    documentNumber VARCHAR(20) NOT NULL UNIQUE,

    phone VARCHAR(20),
    email VARCHAR(150) UNIQUE,
    address TEXT,

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    createdId BIGINT,
    modifiedId BIGINT
);

-- =========================================================
-- TABLE: vehicles
-- =========================================================

CREATE TABLE vehicles (
    id BIGSERIAL PRIMARY KEY,

    customerId BIGINT NOT NULL,

    licensePlate VARCHAR(20) NOT NULL UNIQUE,

    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,

    year SMALLINT,

    color VARCHAR(50),

    vin VARCHAR(100) UNIQUE,

    mileage INTEGER NOT NULL,

    fuelType VARCHAR(50),
    transmission VARCHAR(50),

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    createdId BIGINT,
    modifiedId BIGINT,

    CONSTRAINT fk_vehicles_customer
        FOREIGN KEY (customerId)
        REFERENCES customers(id)
);

-- =========================================================
-- TABLE: work_orders
-- =========================================================

CREATE TABLE work_orders (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(50) NOT NULL UNIQUE,

    customerId BIGINT NOT NULL,
    vehicleId BIGINT NOT NULL,
    mechanicUserId BIGINT,

    vehicleStatus VARCHAR(50),
    priority VARCHAR(20),

    intakeDate TIMESTAMP NOT NULL,
    estimatedDate TIMESTAMP,
    deliveryDate TIMESTAMP,

    reportedProblem TEXT NOT NULL,
    diagnosis TEXT,
    observations TEXT,

    intakeMileage INTEGER,

    fuelLevel VARCHAR(50),
    accessories TEXT,
    visibleDamage TEXT,

    totalParts NUMERIC(12,2) NOT NULL DEFAULT 0,
    totalLabor NUMERIC(12,2) NOT NULL DEFAULT 0,
    subtotal NUMERIC(12,2) NOT NULL DEFAULT 0,
    igv NUMERIC(12,2) NOT NULL DEFAULT 0,
    total NUMERIC(12,2) NOT NULL DEFAULT 0,

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    createdId BIGINT,
    modifiedId BIGINT,

    CONSTRAINT fk_work_orders_customer
        FOREIGN KEY (customerId)
        REFERENCES customers(id),

    CONSTRAINT fk_work_orders_vehicle
        FOREIGN KEY (vehicleId)
        REFERENCES vehicles(id),

    CONSTRAINT fk_work_orders_mechanic
        FOREIGN KEY (mechanicUserId)
        REFERENCES users(id)
);

-- =========================================================
-- TABLE: parts
-- =========================================================

CREATE TABLE parts (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(50) NOT NULL UNIQUE,

    name VARCHAR(150) NOT NULL,

    description TEXT,

    category VARCHAR(100),
    brand VARCHAR(100),

    stock INTEGER NOT NULL,
    minimumStock INTEGER NOT NULL,

    location VARCHAR(100),

    purchasePrice NUMERIC(12,2) NOT NULL,
    salePrice NUMERIC(12,2) NOT NULL,

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    createdId BIGINT,
    modifiedId BIGINT
);

-- =========================================================
-- TABLE: parts_work_orders
-- =========================================================

CREATE TABLE parts_work_orders (
    id BIGSERIAL PRIMARY KEY,

    partId BIGINT NOT NULL,
    workOrderId BIGINT NOT NULL,

    quantity INTEGER NOT NULL,

    unitPrice NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL,

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    createdId BIGINT,
    modifiedId BIGINT,

    CONSTRAINT fk_parts_work_orders_part
        FOREIGN KEY (partId)
        REFERENCES parts(id),

    CONSTRAINT fk_parts_work_orders_work_order
        FOREIGN KEY (workOrderId)
        REFERENCES work_orders(id)
);

-- =========================================================
-- TABLE: invoices
-- =========================================================

CREATE TABLE invoices (
    id BIGSERIAL PRIMARY KEY,

    workOrderId BIGINT NOT NULL,
    customerId BIGINT NOT NULL,

    number VARCHAR(50) NOT NULL UNIQUE,

    receiptType VARCHAR(50) NOT NULL,

    subtotal NUMERIC(12,2) NOT NULL,
    tax NUMERIC(12,2) NOT NULL,
    total NUMERIC(12,2) NOT NULL,

    issueDate TIMESTAMP NOT NULL,

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    createdId BIGINT,
    modifiedId BIGINT,

    CONSTRAINT fk_invoices_work_order
        FOREIGN KEY (workOrderId)
        REFERENCES work_orders(id),

    CONSTRAINT fk_invoices_customer
        FOREIGN KEY (customerId)
        REFERENCES customers(id)
);

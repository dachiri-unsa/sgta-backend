-- =========================================================
-- DATABASE: Mechanical Workshop Management System
-- PostgreSQL Physical Model
-- =========================================================

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
    email VARCHAR(150),
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
-- TABLE: work_details
-- =========================================================

CREATE TABLE work_details (
    id BIGSERIAL PRIMARY KEY,

    work_order_id BIGINT NOT NULL,

    description TEXT NOT NULL,

    hours_worked NUMERIC(8,2) NOT NULL DEFAULT 0,
    labor_cost NUMERIC(12,2) NOT NULL DEFAULT 0,

    start_date TIMESTAMP,
    end_date TIMESTAMP,

    observations TEXT,

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_id BIGINT,
    modified_id BIGINT,

    CONSTRAINT fk_work_details_work_order
        FOREIGN KEY (work_order_id)
        REFERENCES work_orders(id)
);

-- =========================================================
-- TABLE: suppliers
-- =========================================================

CREATE TABLE suppliers (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(150) NOT NULL,

    ruc VARCHAR(20) NOT NULL UNIQUE,

    phone VARCHAR(20),
    email VARCHAR(150),
    address TEXT,

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_id BIGINT,
    modified_id BIGINT
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
-- TABLE: parts_suppliers
-- =========================================================

CREATE TABLE parts_suppliers (
    id BIGSERIAL PRIMARY KEY,

    part_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,

    supplier_code VARCHAR(100),

    purchase_price NUMERIC(12,2) NOT NULL DEFAULT 0,

    preferred BOOLEAN NOT NULL DEFAULT FALSE,

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_id BIGINT,
    modified_id BIGINT,

    CONSTRAINT fk_parts_suppliers_part
        FOREIGN KEY (part_id)
        REFERENCES parts(id),

    CONSTRAINT fk_parts_suppliers_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id),

    CONSTRAINT uq_parts_suppliers
        UNIQUE (part_id, supplier_id)
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
-- TABLE: stock_movements
-- =========================================================

CREATE TABLE stock_movements (
    id BIGSERIAL PRIMARY KEY,

    part_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    movement_type VARCHAR(50) NOT NULL,

    quantity INTEGER NOT NULL,

    previous_stock INTEGER NOT NULL,
    new_stock INTEGER NOT NULL,

    reason TEXT,
    reference VARCHAR(100),

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_id BIGINT,
    modified_id BIGINT,

    CONSTRAINT fk_stock_movements_part
        FOREIGN KEY (part_id)
        REFERENCES parts(id),

    CONSTRAINT fk_stock_movements_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
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

-- =========================================================
-- TABLE: payments
-- =========================================================

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,

    invoice_id BIGINT NOT NULL,

    payment_method VARCHAR(50) NOT NULL,

    amount NUMERIC(12,2) NOT NULL,

    reference VARCHAR(100),

    payment_date TIMESTAMP NOT NULL,

    status BOOLEAN NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_id BIGINT,
    modified_id BIGINT,

    CONSTRAINT fk_payments_invoice
        FOREIGN KEY (invoice_id)
        REFERENCES invoices(id)
);

-- =========================================================
-- TABLE: order_status_histories
-- =========================================================

CREATE TABLE order_status_histories (
    id BIGSERIAL PRIMARY KEY,

    work_order_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    previous_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,

    comment TEXT,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_id BIGINT,

    CONSTRAINT fk_order_status_histories_work_order
        FOREIGN KEY (work_order_id)
        REFERENCES work_orders(id),

    CONSTRAINT fk_order_status_histories_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX idx_vehicles_customer_id
    ON vehicles(customerId);

CREATE INDEX idx_work_orders_customer_id
    ON work_orders(customerId);

CREATE INDEX idx_work_orders_vehicle_id
    ON work_orders(vehicleId);

CREATE INDEX idx_work_orders_mechanic_user_id
    ON work_orders(mechanicUserId);

CREATE INDEX idx_parts_work_orders_part_id
    ON parts_work_orders(partId);

CREATE INDEX idx_parts_work_orders_work_order_id
    ON parts_work_orders(workOrderId);

CREATE INDEX idx_invoices_work_order_id
    ON invoices(workOrderId);

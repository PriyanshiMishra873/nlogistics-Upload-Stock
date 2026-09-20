CREATE DATABASE IF NOT EXISTS nlogistics CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE nlogistics;

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS sales_transactions, stock_upload_log, inventory_ledger, stock, shipments, customers, users, companies, products;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE companies (
 company_id INT PRIMARY KEY AUTO_INCREMENT,
 company_name VARCHAR(150) NOT NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
 user_id INT PRIMARY KEY AUTO_INCREMENT,
 company_id INT NOT NULL,
 full_name VARCHAR(120) NOT NULL,
 email VARCHAR(160) UNIQUE NOT NULL,
 password_hash VARCHAR(255) DEFAULT NULL,
 role VARCHAR(60) DEFAULT 'Company Staff Ops',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(company_id) REFERENCES companies(company_id)
);

CREATE TABLE customers (
 customer_id INT PRIMARY KEY AUTO_INCREMENT,
 customer_name VARCHAR(150) NOT NULL,
 email VARCHAR(160),
 phone VARCHAR(30),
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shipments (
 shipment_id INT PRIMARY KEY AUTO_INCREMENT,
 company_id INT NOT NULL,
 tracking_no VARCHAR(80) UNIQUE NOT NULL,
 shipment_status VARCHAR(40) DEFAULT 'CREATED',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(company_id) REFERENCES companies(company_id)
);

CREATE TABLE products (
 product_id INT PRIMARY KEY AUTO_INCREMENT,
 product_name VARCHAR(150) NOT NULL,
 category VARCHAR(100),
 hsn_code VARCHAR(40),
 unit_of_measure VARCHAR(30) NOT NULL,
 unit_cost DECIMAL(14,2) NOT NULL DEFAULT 0,
 unit_price DECIMAL(14,2) NOT NULL DEFAULT 0,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock (
 stock_id INT PRIMARY KEY AUTO_INCREMENT,
 company_id INT NOT NULL,
 product_id INT NOT NULL,
 warehouse_location VARCHAR(160) NOT NULL,
 quantity_on_hand DECIMAL(14,3) NOT NULL DEFAULT 0,
 batch_no VARCHAR(80),
 expiry_date DATE,
 last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 FOREIGN KEY(company_id) REFERENCES companies(company_id),
 FOREIGN KEY(product_id) REFERENCES products(product_id),
 INDEX idx_stock_company_product(company_id,product_id)
);

CREATE TABLE inventory_ledger (
 ledger_id INT PRIMARY KEY AUTO_INCREMENT,
 product_id INT NOT NULL,
 transaction_type ENUM('IN','OUT','ADJUSTMENT') NOT NULL,
 quantity DECIMAL(14,3) NOT NULL,
 unit_cost_at_txn DECIMAL(14,2) NOT NULL DEFAULT 0,
 reference_type VARCHAR(40),
 reference_id VARCHAR(80),
 txn_date DATE NOT NULL,
 FOREIGN KEY(product_id) REFERENCES products(product_id)
);

CREATE TABLE stock_upload_log (
 upload_id INT PRIMARY KEY AUTO_INCREMENT,
 company_id INT NOT NULL,
 uploaded_by INT NOT NULL,
 file_name VARCHAR(255) NOT NULL,
 total_records INT NOT NULL DEFAULT 0,
 success_count INT NOT NULL DEFAULT 0,
 failure_count INT NOT NULL DEFAULT 0,
 error_report_path VARCHAR(500),
 uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(company_id) REFERENCES companies(company_id),
 FOREIGN KEY(uploaded_by) REFERENCES users(user_id)
);

CREATE TABLE sales_transactions (
 transaction_id INT PRIMARY KEY AUTO_INCREMENT,
 product_id INT NOT NULL,
 customer_id INT NOT NULL,
 shipment_id INT NULL,
 quantity_sold DECIMAL(14,3) NOT NULL,
 sale_price_snapshot DECIMAL(14,2) NOT NULL,
 sale_amount DECIMAL(16,2) GENERATED ALWAYS AS (quantity_sold * sale_price_snapshot) STORED,
 sale_date DATE NOT NULL,
 FOREIGN KEY(product_id) REFERENCES products(product_id),
 FOREIGN KEY(customer_id) REFERENCES customers(customer_id),
 FOREIGN KEY(shipment_id) REFERENCES shipments(shipment_id)
);

CREATE INDEX idx_sales_date ON sales_transactions(sale_date);
CREATE INDEX idx_ledger_product_date ON inventory_ledger(product_id,txn_date);

INSERT INTO companies(company_name) VALUES ('N Logistics India'),('N Logistics West');
INSERT INTO users(company_id,full_name,email,role) VALUES
(1,'Admin User','admin@nlogistics.local','Super Admin'),
(1,'Operations Staff','ops@nlogistics.local','Company Staff Ops'),
(2,'Finance Staff','finance@nlogistics.local','Company Staff Finance');
INSERT INTO customers(customer_name,email,phone) VALUES
('ABC Retail Pvt Ltd','abc@example.com','9876543210'),
('Metro Traders','metro@example.com','9876543211');
INSERT INTO shipments(company_id,tracking_no,shipment_status) VALUES
(1,'NLOG-10001','IN TRANSIT'),(1,'NLOG-10002','DELIVERED');
INSERT INTO products(product_name,category,hsn_code,unit_of_measure,unit_cost,unit_price) VALUES
('Industrial Safety Gloves','Safety','4015','box',450,650),
('Packing Carton Large','Packaging','4819','box',80,125),
('Stretch Film 500m','Packaging','3920','roll',250,390);
INSERT INTO stock(company_id,product_id,warehouse_location,quantity_on_hand,batch_no) VALUES
(1,1,'Mumbai Warehouse',120,'GLOV-SEP26'),
(1,2,'Mumbai Warehouse',500,'CART-SEP26'),
(2,3,'Goregaon Warehouse',85,'FILM-SEP26');
INSERT INTO inventory_ledger(product_id,transaction_type,quantity,unit_cost_at_txn,reference_type,reference_id,txn_date) VALUES
(1,'IN',120,450,'Upload','1',CURDATE()),
(2,'IN',500,80,'Upload','1',CURDATE()),
(3,'IN',85,250,'Upload','2',CURDATE());
INSERT INTO stock_upload_log(company_id,uploaded_by,file_name,total_records,success_count,failure_count,error_report_path)
VALUES(1,1,'opening_stock.csv',2,2,0,NULL);
INSERT INTO sales_transactions(product_id,customer_id,shipment_id,quantity_sold,sale_price_snapshot,sale_date)
VALUES(1,1,1,5,650,CURDATE()),(2,2,2,10,125,CURDATE());

DROP VIEW IF EXISTS v_sales_summary;
CREATE VIEW v_sales_summary AS
SELECT DATE_FORMAT(sale_date,'%Y-%m') sale_month,
       COUNT(*) transaction_count,
       SUM(quantity_sold) units_sold,
       SUM(sale_amount) sales_value
FROM sales_transactions GROUP BY DATE_FORMAT(sale_date,'%Y-%m');

DROP PROCEDURE IF EXISTS sp_product_sales;
DELIMITER //
CREATE PROCEDURE sp_product_sales(IN p_product_id INT)
BEGIN
 SELECT p.product_id,p.product_name,
        COALESCE(SUM(s.quantity_sold),0) units_sold,
        COALESCE(SUM(s.sale_amount),0) sales_value
 FROM products p LEFT JOIN sales_transactions s ON p.product_id=s.product_id
 WHERE p.product_id=p_product_id GROUP BY p.product_id,p.product_name;
END//
DELIMITER ;

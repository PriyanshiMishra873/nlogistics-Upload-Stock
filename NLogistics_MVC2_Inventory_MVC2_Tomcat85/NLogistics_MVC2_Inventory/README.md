# N Logistics Inventory MVC2

A complete Eclipse Dynamic Web Project for **Tomcat 8.5 / Servlet 3.1 / Java 8 / MySQL** based on the supplied SRS tables.

## Modules
1. Products
2. Stock
3. Inventory Ledger
4. Stock Upload Log
5. Sales Transactions
6. Dashboard

## MVC2 structure
- `model` – JavaBeans/entities
- `operations` – service interfaces
- `implementors` – JDBC/service implementations
- `controller` – Servlet controllers
- `db_config.GetConnection` – MySQL connection
- `WebContent/*.jsp` – UI/views
- `database/nlogistics_inventory.sql` – complete database + seed data

## Important Tomcat 8.5 compatibility
This project uses `javax.servlet` through Tomcat 8.5 / Servlet 3.1. It does **not** use `jakarta.servlet`.

## Database setup
1. Open MySQL Workbench.
2. Run `database/nlogistics_inventory.sql`.
3. Open `src/db_config/GetConnection.java`.
4. Change:
   `YOUR_MYSQL_PASSWORD`
   to your actual MySQL root password.
5. Add MySQL Connector/J 8.x to `WebContent/WEB-INF/lib/`.
   Recommended: `mysql-connector-j-8.0.33.jar`.

## Eclipse
1. File → Import → Existing Projects into Workspace.
2. Select this extracted project.
3. Add Apache Tomcat 8.5 in Servers.
4. Right-click project → Properties → Targeted Runtimes → select Tomcat 8.5.
5. Put the MySQL connector JAR in `WebContent/WEB-INF/lib`.
6. Project → Clean.
7. Right-click project → Run As → Run on Server.
8. Open `/NLogisticsInventoryMVC2/DashboardController`.

## CRUD
Every supplied SRS table has Add, View, Edit and Delete functionality.

## Calculations
- `sales_amount = quantity_sold * sale_price_snapshot` is a MySQL generated column.
- Dashboard shows product count, quantity on hand, sales value and ledger count.
- A monthly sales view and product-sales stored procedure are included.

## Notes
The SRS screenshots reference parent entities `companies`, `users`, `customers` and `shipments`. Minimal supporting tables and seed records are included so all foreign keys work immediately.

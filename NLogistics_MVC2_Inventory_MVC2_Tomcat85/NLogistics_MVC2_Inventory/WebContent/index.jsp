<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard | N Logistics</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<div class="sidebar">
		<div class="brand">
			N <span>Logistics</span>
		</div>
		<div class="nav">
			<a href="DashboardController">▦ Dashboard</a><a
				href="ProductController">▣ Products</a><a href="StockController">▤
				Stock</a><a href="InventoryLedgerController">↔ Inventory Ledger</a><a
				href="StockUploadLogController">⇧ Stock Upload Logs</a><a
				href="SalesTransactionController">₹ Sales Transactions</a>
		</div>
	</div>
	<main class="main">
		<div class="topbar">
			<div>
				<h1>Inventory Dashboard</h1>
				<div class="muted">N Logistics • MVC2 Management System</div>
			</div>
		</div>
		<%
		if (request.getAttribute("dbError") != null) {
		%><div class="alert">
			Database connection error:
			<%=request.getAttribute("dbError")%></div>
		<%
		}
		%>
		<div class="cards">
			<div class="card">
				<div class="label">Products</div>
				<div class="metric"><%=request.getAttribute("products")%></div>
			</div>
			<div class="card">
				<div class="label">Quantity on Hand</div>
				<div class="metric"><%=request.getAttribute("stock")%></div>
			</div>
			<div class="card">
				<div class="label">Sales Value</div>
				<div class="metric">
					₹
					<%=String.format("%.2f", request.getAttribute("sales"))%></div>
			</div>
			<div class="card">
				<div class="label">Ledger Entries</div>
				<div class="metric"><%=request.getAttribute("ledger")%></div>
			</div>
		</div>
		<div class="panel">
			<h2>Inventory & Sales Management</h2>
			<p class="muted">Use the navigation to manage products, stock,
				inventory movements, upload logs and sales transactions. Calculated
				sales amount is maintained by the database.</p>
			<div class="actions">
				<a class="btn btn-primary" href="ProductController">Manage
					Products</a><a class="btn btn-secondary"
					href="SalesTransactionController">View Sales</a>
			</div>
		</div>
	</main>
</body>
</html>
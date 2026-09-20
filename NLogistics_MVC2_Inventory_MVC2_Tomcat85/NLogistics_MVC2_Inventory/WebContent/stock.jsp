<%@ page contentType="text/html;charset=UTF-8" %>
<%
    java.util.List<model.Stock> items = (java.util.List<model.Stock>) request.getAttribute("items");
    if (items == null) {
        response.sendRedirect("StockController");
        return;
    }
%>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Stock | N Logistics</title><link rel="stylesheet" href="css/style.css"></head><body>
<div class="sidebar"><div class="brand">N <span>Logistics</span></div><div class="nav">
<a href="DashboardController">📊 Dashboard</a><a href="ProductController">📦 Products</a><a href="StockController">📋 Stock</a><a href="InventoryLedgerController">📑 Inventory Ledger</a><a href="StockUploadLogController">📥 Stock Upload Logs</a><a href="SalesTransactionController">💰 Sales Transactions</a>
</div></div><main class="main">
<div class="topbar"><div><h1>Stock</h1><div class="muted">Warehouse stock by company and product</div></div><a class="btn btn-primary" href="stock-form.jsp">+ Add Stock</a></div>
<div class="panel"><div class="table-wrap"><table><tr><th>ID</th><th>Company</th><th>Product</th><th>Warehouse</th><th>Qty</th><th>Batch</th><th>Expiry</th><th>Updated</th><th>Actions</th></tr>
<% for(model.Stock x:items){ %><tr><td><%=x.getStockId()%></td><td><%=x.getCompanyName()%></td><td><%=x.getProductName()%></td><td><%=x.getWarehouseLocation()%></td><td><b><%=x.getQuantityOnHand()%></b></td><td><%=x.getBatchNo()%></td><td><%=x.getExpiryDate()%></td><td><%=x.getLastUpdated()%></td><td><a class="btn btn-secondary btn-sm" href="StockController?action=edit&id=<%=x.getStockId()%>">Edit</a> <a class="btn btn-danger btn-sm" href="StockController?action=delete&id=<%=x.getStockId()%>" onclick="return confirm('Delete this stock record?')">Delete</a></td></tr><% } %></table></div></div>
</main></body></html>
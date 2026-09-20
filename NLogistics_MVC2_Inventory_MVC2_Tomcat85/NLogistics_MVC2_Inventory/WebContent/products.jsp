<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Products | N Logistics</title><link rel="stylesheet" href="css/style.css"></head><body>
<div class="sidebar"><div class="brand">N <span>Logistics</span></div><div class="nav">
<a href="DashboardController">▦ Dashboard</a><a href="ProductController">▣ Products</a><a href="StockController">▤ Stock</a><a href="InventoryLedgerController">↔ Inventory Ledger</a><a href="StockUploadLogController">⇧ Stock Upload Logs</a><a href="SalesTransactionController">₹ Sales Transactions</a>
</div></div><main class="main">
<div class="topbar"><div><h1>Products</h1><div class="muted">Product master and pricing</div></div><a class="btn btn-primary" href="product-form.jsp">+ Add Product</a></div>
<div class="panel"><div class="table-wrap"><table><tr><th>ID</th><th>Name</th><th>Category</th><th>HSN</th><th>Unit</th><th>Cost</th><th>Price</th><th>Created</th><th>Actions</th></tr>
<% java.util.List<model.Product> items=(java.util.List<model.Product>)request.getAttribute("items"); for(model.Product p:items){ %>
<tr><td><%=p.getProductId()%></td><td><b><%=p.getProductName()%></b></td><td><%=p.getCategory()%></td><td><%=p.getHsnCode()%></td><td><%=p.getUnitOfMeasure()%></td><td>₹ <%=p.getUnitCost()%></td><td>₹ <%=p.getUnitPrice()%></td><td><%=p.getCreatedAt()%></td><td><a class="btn btn-secondary btn-sm" href="ProductController?action=edit&id=<%=p.getProductId()%>">Edit</a> <a class="btn btn-danger btn-sm" href="ProductController?action=delete&id=<%=p.getProductId()%>" onclick="return confirm('Delete this product?')">Delete</a></td></tr>
<% } %></table></div></div>
</main></body></html>
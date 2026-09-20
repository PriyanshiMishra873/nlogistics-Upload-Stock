<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Stock | N Logistics</title><link rel="stylesheet" href="css/style.css"></head><body>
<div class="sidebar"><div class="brand">N <span>Logistics</span></div><div class="nav">
<a href="DashboardController">▦ Dashboard</a><a href="ProductController">▣ Products</a><a href="StockController">▤ Stock</a><a href="InventoryLedgerController">↔ Inventory Ledger</a><a href="StockUploadLogController">⇧ Stock Upload Logs</a><a href="SalesTransactionController">₹ Sales Transactions</a>
</div></div><main class="main">
<% model.Stock x=(model.Stock)request.getAttribute("item"); boolean edit=x!=null; %>
<div class="topbar"><div><h1><%=edit?"Edit Stock":"Add Stock"%></h1><div class="muted">Use IDs from the seeded companies/products</div></div></div>
<div class="panel"><form method="post" action="StockController"><%if(edit){%><input type="hidden" name="stockId" value="<%=x.getStockId()%>"><%}%><div class="form-grid">
<div class="field"><label>Company ID</label><input required type="number" name="companyId" value="<%=edit?x.getCompanyId():"1"%>"></div>
<div class="field"><label>Product ID</label><input required type="number" name="productId" value="<%=edit?x.getProductId():"1"%>"></div>
<div class="field"><label>Warehouse Location</label><input required name="warehouseLocation" value="<%=edit?x.getWarehouseLocation():"Main Warehouse"%>"></div>
<div class="field"><label>Quantity on Hand</label><input required type="number" step="0.01" min="0" name="quantityOnHand" value="<%=edit?x.getQuantityOnHand():"0"%>"></div>
<div class="field"><label>Batch No</label><input name="batchNo" value="<%=edit?x.getBatchNo():""%>"></div>
<div class="field"><label>Expiry Date</label><input type="date" name="expiryDate" value="<%=edit&&x.getExpiryDate()!=null?x.getExpiryDate():""%>"></div>
</div><div class="actions"><button class="btn btn-primary">Save Stock</button><a class="btn btn-secondary" href="StockController">Cancel</a></div></form></div>
</main></body></html>
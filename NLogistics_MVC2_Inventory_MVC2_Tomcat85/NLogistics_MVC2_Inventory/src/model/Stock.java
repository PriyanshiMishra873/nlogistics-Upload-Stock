package model;
public class Stock {
    private int stockId, companyId, productId; private String companyName, productName, warehouseLocation, batchNo, expiryDate, lastUpdated;
    private double quantityOnHand;
    public int getStockId(){return stockId;} public void setStockId(int v){stockId=v;}
    public int getCompanyId(){return companyId;} public void setCompanyId(int v){companyId=v;}
    public int getProductId(){return productId;} public void setProductId(int v){productId=v;}
    public String getCompanyName(){return companyName;} public void setCompanyName(String v){companyName=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public String getWarehouseLocation(){return warehouseLocation;} public void setWarehouseLocation(String v){warehouseLocation=v;}
    public double getQuantityOnHand(){return quantityOnHand;} public void setQuantityOnHand(double v){quantityOnHand=v;}
    public String getBatchNo(){return batchNo;} public void setBatchNo(String v){batchNo=v;}
    public String getExpiryDate(){return expiryDate;} public void setExpiryDate(String v){expiryDate=v;}
    public String getLastUpdated(){return lastUpdated;} public void setLastUpdated(String v){lastUpdated=v;}
}

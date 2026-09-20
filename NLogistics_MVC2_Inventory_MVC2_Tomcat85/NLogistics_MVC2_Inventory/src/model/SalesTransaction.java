package model;
public class SalesTransaction {
    private int transactionId, productId, customerId; private Integer shipmentId; private String productName, customerName, saleDate;
    private double quantitySold, salePriceSnapshot, saleAmount;
    public int getTransactionId(){return transactionId;} public void setTransactionId(int v){transactionId=v;}
    public int getProductId(){return productId;} public void setProductId(int v){productId=v;}
    public int getCustomerId(){return customerId;} public void setCustomerId(int v){customerId=v;}
    public Integer getShipmentId(){return shipmentId;} public void setShipmentId(Integer v){shipmentId=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public double getQuantitySold(){return quantitySold;} public void setQuantitySold(double v){quantitySold=v;}
    public double getSalePriceSnapshot(){return salePriceSnapshot;} public void setSalePriceSnapshot(double v){salePriceSnapshot=v;}
    public double getSaleAmount(){return saleAmount;} public void setSaleAmount(double v){saleAmount=v;}
    public String getSaleDate(){return saleDate;} public void setSaleDate(String v){saleDate=v;}
}

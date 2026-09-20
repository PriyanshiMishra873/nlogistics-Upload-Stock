package model;
public class Product {
    private int productId; private String productName; private String category; private String hsnCode;
    private String unitOfMeasure; private double unitCost; private double unitPrice; private String createdAt;
    public int getProductId(){return productId;} public void setProductId(int v){productId=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getHsnCode(){return hsnCode;} public void setHsnCode(String v){hsnCode=v;}
    public String getUnitOfMeasure(){return unitOfMeasure;} public void setUnitOfMeasure(String v){unitOfMeasure=v;}
    public double getUnitCost(){return unitCost;} public void setUnitCost(double v){unitCost=v;}
    public double getUnitPrice(){return unitPrice;} public void setUnitPrice(double v){unitPrice=v;}
    public String getCreatedAt(){return createdAt;} public void setCreatedAt(String v){createdAt=v;}
}

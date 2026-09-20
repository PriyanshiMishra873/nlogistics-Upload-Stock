package implementors;
import db_config.GetConnection;
import model.Product;
import operations.ProductOperations;
import java.sql.*; import java.util.*;
public class ProductImplementor implements ProductOperations {
    private Product map(ResultSet rs) throws SQLException {
        Product p=new Product(); p.setProductId(rs.getInt("product_id")); p.setProductName(rs.getString("product_name"));
        p.setCategory(rs.getString("category")); p.setHsnCode(rs.getString("hsn_code")); p.setUnitOfMeasure(rs.getString("unit_of_measure"));
        p.setUnitCost(rs.getDouble("unit_cost")); p.setUnitPrice(rs.getDouble("unit_price")); p.setCreatedAt(rs.getString("created_at")); return p;
    }
    public List<Product> getAll(){List<Product> list=new ArrayList<Product>(); String q="SELECT * FROM products ORDER BY product_id DESC";
        try(Connection c=GetConnection.getConnection(); PreparedStatement ps=c.prepareStatement(q); ResultSet rs=ps.executeQuery()){while(rs.next())list.add(map(rs));}catch(Exception e){e.printStackTrace();} return list;}
    public Product getById(int id){String q="SELECT * FROM products WHERE product_id=?"; try(Connection c=GetConnection.getConnection(); PreparedStatement ps=c.prepareStatement(q)){ps.setInt(1,id);ResultSet rs=ps.executeQuery();if(rs.next())return map(rs);}catch(Exception e){e.printStackTrace();}return null;}
    public boolean save(Product p){String q="INSERT INTO products(product_name,category,hsn_code,unit_of_measure,unit_cost,unit_price) VALUES(?,?,?,?,?,?)";
        try(Connection c=GetConnection.getConnection();PreparedStatement ps=c.prepareStatement(q)){ps.setString(1,p.getProductName());ps.setString(2,p.getCategory());ps.setString(3,p.getHsnCode());ps.setString(4,p.getUnitOfMeasure());ps.setDouble(5,p.getUnitCost());ps.setDouble(6,p.getUnitPrice());return ps.executeUpdate()>0;}catch(Exception e){e.printStackTrace();return false;}}
    public boolean update(Product p){String q="UPDATE products SET product_name=?,category=?,hsn_code=?,unit_of_measure=?,unit_cost=?,unit_price=? WHERE product_id=?";
        try(Connection c=GetConnection.getConnection();PreparedStatement ps=c.prepareStatement(q)){ps.setString(1,p.getProductName());ps.setString(2,p.getCategory());ps.setString(3,p.getHsnCode());ps.setString(4,p.getUnitOfMeasure());ps.setDouble(5,p.getUnitCost());ps.setDouble(6,p.getUnitPrice());ps.setInt(7,p.getProductId());return ps.executeUpdate()>0;}catch(Exception e){e.printStackTrace();return false;}}
    public boolean delete(int id){try(Connection c=GetConnection.getConnection();PreparedStatement ps=c.prepareStatement("DELETE FROM products WHERE product_id=?")){ps.setInt(1,id);return ps.executeUpdate()>0;}catch(Exception e){e.printStackTrace();return false;}}
}

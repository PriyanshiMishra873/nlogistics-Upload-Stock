package controller;
import implementors.ProductImplementor; import model.Product; import javax.servlet.*; import javax.servlet.annotation.WebServlet; import javax.servlet.http.*; import java.io.IOException;
@WebServlet("/ProductController")
public class ProductController extends HttpServlet {
 private static final long serialVersionUID = 1L;
 private ProductImplementor op=new ProductImplementor();
 protected void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{String a=req.getParameter("action");if("edit".equals(a)){req.setAttribute("item",op.getById(Integer.parseInt(req.getParameter("id"))));req.getRequestDispatcher("/product-form.jsp").forward(req,res);}else if("delete".equals(a)){op.delete(Integer.parseInt(req.getParameter("id")));res.sendRedirect("ProductController");}else{req.setAttribute("items",op.getAll());req.getRequestDispatcher("/products.jsp").forward(req,res);}}
 protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{Product p=new Product();String id=req.getParameter("productId");if(id!=null&&!id.isEmpty())p.setProductId(Integer.parseInt(id));p.setProductName(req.getParameter("productName"));p.setCategory(req.getParameter("category"));p.setHsnCode(req.getParameter("hsnCode"));p.setUnitOfMeasure(req.getParameter("unitOfMeasure"));p.setUnitCost(Double.parseDouble(req.getParameter("unitCost")));p.setUnitPrice(Double.parseDouble(req.getParameter("unitPrice")));if(id!=null&&!id.isEmpty())op.update(p);else op.save(p);res.sendRedirect("ProductController");}
}

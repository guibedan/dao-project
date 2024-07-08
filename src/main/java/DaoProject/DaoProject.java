package DaoProject;

import java.util.Date;
import java.util.List;

import db.DB;
import entities.Department;
import entities.Seller;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;

public class DaoProject {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
//		Seller seller = sellerDao.findById(3);
//		System.out.println(seller);
		
//		sellerDao.deleteById(8);
		
//		Department dp = departmentDao.findById(1);
		
//		Seller sr = new Seller(1, "Romario", "romario@jesus.com.br", new Date(), 509000.0, dp);

//		sellerDao.insert(sr);
		
//		List<Seller> list = sellerDao.findAll();
//		list.forEach(System.out::println);
		
//		Department dp = new Department(5, "Tools");
		
//		departmentDao.insert(dp);
		
		departmentDao.deleteById(6);
		
		List<Department> list = departmentDao.findAll();
		list.forEach(System.out::println);

		DB.closeConnection();
	}
	
}

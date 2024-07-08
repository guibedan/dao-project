package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;
import model.dao.SellerDao;

public class SellerDaoJDBC implements SellerDao {

	private Connection connection;

	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller obj) {
		
		String query = "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) "
				+ "VALUES (?, ?, ?, ?, ?);";

		try {
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setString(1, obj.getName());
			stm.setString(2, obj.getEmail());
			stm.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			stm.setDouble(4, obj.getBaseSalary());
			stm.setInt(5, obj.getDepartment().getId());

			int err = stm.executeUpdate();
			
			if (err < 1)
				throw new DbException("[ERROR] Erro ao cadastrar Vendedor");

			System.out.println("[SUCESSES] Vendedor adicionando!");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = connection.prepareStatement("UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		
		String query = "DELETE FROM seller WHERE Id = ?;";

		try {
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, id);

			int err = stm.executeUpdate();
			
			if (err < 1)
				throw new DbException("[ERROR] Vendedor não existe");

			System.out.println("[SUCESSES] Vendedor excluido!");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public Seller findById(Integer id) {

		String query = "SELECT s.Id, s.Name, s.Email, s.BirthDate, s.BaseSalary, s.DepartmentId, d.Name AS DepName "
				+ "FROM seller s " 
				+ "INNER JOIN department d ON s.DepartmentId = d.Id " 
				+ "WHERE s.Id = ?;";

		try {
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();
			
			if(!rs.next())
				return null;

			return returnSeller(rs);
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Seller> findAll() {
		String query = "SELECT s.Id, s.Name, s.Email, s.BirthDate, s.BaseSalary, s.DepartmentId, d.Name AS DepName "
				+ "FROM seller s " 
				+ "INNER JOIN department d ON s.DepartmentId = d.Id;";

		try {
			PreparedStatement stm = connection.prepareStatement(query);

			ResultSet rs = stm.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			
			while(rs.next()) {
				list.add(returnSeller(rs));
			}

			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public List<Seller> findByDepartment(Department dp) {
		String query = "SELECT s.Id, s.Name, s.Email, s.BirthDate, s.BaseSalary, s.DepartmentId, d.Name AS DepName "
				+ "FROM seller s " 
				+ "INNER JOIN department d ON s.DepartmentId = d.Id "
				+ "WHERE s.DepartmentId = ?;";

		try {
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, dp.getId());
			
			ResultSet rs = stm.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			
			while(rs.next()) {
				list.add(returnSeller(rs));
			}

			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	private Seller returnSeller(ResultSet rs) throws SQLException {
		return new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), rs.getDate("BirthDate"),
				rs.getDouble("BaseSalary"), new Department(rs.getInt("DepartmentId"), rs.getString("DepName")));
	}

}

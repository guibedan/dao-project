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
import model.dao.DepartmentDao;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection connection;

	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department obj) {
		
		String query = "INSERT INTO department (Name) VALUES (?);";

		try {
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setString(1, obj.getName());

			int err = stm.executeUpdate();
			
			if (err < 1)
				throw new DbException("[ERROR] Erro ao cadastrar Departamento");

			System.out.println("[SUCESSES] Departamento adicionando!");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {
			st = connection.prepareStatement("UPDATE department " + "SET Name = ? " + "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {

		String query = "DELETE FROM department WHERE Id = ?;";

		try {
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, id);

			int err = stm.executeUpdate();

			if (err < 1)
				throw new DbException("[ERROR] Departamento não existe");

			System.out.println("[SUCESSES] Departamento excluido!");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public Department findById(Integer id) {

		String query = "SELECT Id, Name FROM department WHERE Id = ?;";

		try {
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();

			if (!rs.next())
				return null;

			return new Department(rs.getInt("Id"), rs.getString("Name"));
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Department> findAll() {
		String query = "SELECT Id, Name FROM department;";

		try {
			PreparedStatement stm = connection.prepareStatement(query);

			ResultSet rs = stm.executeQuery();

			List<Department> list = new ArrayList<Department>();

			while (rs.next()) {
				list.add(new Department(rs.getInt("Id"), rs.getString("Name")));
			}

			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

}

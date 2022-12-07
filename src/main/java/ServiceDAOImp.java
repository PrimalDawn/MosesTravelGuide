import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAOImp {

	static final String DB_URL = "jdbc:mysql://mosestravel.cljarowffwyg.us-east-2.rds.amazonaws.com:3306/MosesTravel";
	static final String USER = "admin";
	static final String PASS = "HelloWorld";
	
	public static String getServicesNames() {
		List<String> li=new ArrayList<String>();
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();){
				ResultSet rs = stmt.executeQuery("SELECT DISTINCT Name FROM Services");
				// Extract data from result set
				while (rs.next()) {
					// Retrieve by column name
					li.add(rs.getString("Name"));
				}
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String s=String.join(",",li);
		return s;
	}

	public static List<Service> getAllServices() {
		List<Service> li=new ArrayList<Service>();
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			Statement stmt = conn.createStatement();
		) {
			ResultSet rs;
				rs = stmt.executeQuery("SELECT id, name, price, startTime, endTime, capacity FROM Services");
			while (rs.next()) {
				Service s = new Service(Integer.parseInt(rs.getString("id")), rs.getString("Name"),
						rs.getDouble("Price"), rs.getTimestamp("StartTIme"), rs.getTimestamp("EndTime"),
						rs.getInt("capacity"));
				li.add(s);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return li;
	}

	public static List<Service> getServices(int[] list) {
		List<Service> li=new ArrayList<Service>();
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();){
			for(int i:list)
			{
				ResultSet rs = stmt.executeQuery("SELECT name, price, startTime, endTime, capacity FROM Services WHERE id="+i);
				// Extract data from result set
				while (rs.next()) {
					// Retrieve by column name
					Service e=new Service(i,rs.getString("Name"),rs.getDouble("Price"),rs.getTimestamp("StartTIme"),rs.getTimestamp("EndTime"),rs.getInt("capacity"));
					li.add(e);
				}

			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return li;
	}

	public static Service getService(int id) {
		Service e=null;
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();){
				ResultSet rs = stmt.executeQuery("SELECT name, price, startTime, endTime, capacity FROM Services WHERE id="+id);
				if(rs.next())
					e=new Service(id,rs.getString("name"),rs.getDouble("price"),rs.getTimestamp("startTIme"),rs.getTimestamp("endTime"),rs.getInt("capacity"));
		} catch (SQLException ex) {
			ex.printStackTrace();
		} 
		return e;
	}

	public static void updateService(Service e) {
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		         Statement stmt = conn.createStatement();
		         ResultSet rs = stmt.executeQuery("SELECT name, price, startTime, endTime, capacity FROM Services WHERE id="+e.ID);) {
		         // Extract data from result set
		         if (rs.next()) {
		            // Retrieve by column name
		        	stmt.executeUpdate("UPDATE Services SET Name='"+e.getName()+"', StartTime='"+e.getStartDate()+"', EndTime='"+e.getEndDate()+"', price="+e.getPrice()+", capacity="+e.getCapacity()+" WHERE id="+e.getID());
		         }
		         else
		         {
		        	 stmt.executeUpdate("INSERT INTO Services (name, startTime, endTime, price, capacity) VALUES('"+e.getName()+"', '"+e.getStartDate()+"', '"+e.getEndDate()+"', "+e.getPrice()+", "+e.getCapacity()+")");
		         }
				 stmt.close();
		      } catch (SQLException ex) {
		         ex.printStackTrace();
		      } 
		
	}
	
	public static Service addNewService(Service e) {
		Service serv=e;
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		        Statement stmt = conn.createStatement();) {
				stmt.executeUpdate("INSERT INTO Services (name, startTime, endTime, price, capacity) VALUES('"+e.getName()+"', '"+e.getStartDate()+"', '"+e.getEndDate()+"', "+e.getPrice()+", "+e.getCapacity()+")");
		         // Extract data from result se
				ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM Services");
				if(rs.next())
		       	 	serv.setID(rs.getInt(1));
				 stmt.close();
		      } catch (SQLException ex) {
		         ex.printStackTrace();
		      } 
		return serv;
	}

	public static void deleteService(Service e) {
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		         Statement stmt = conn.createStatement();
		         ResultSet rs = stmt.executeQuery("SELECT name, price, startTime, endTime, capacity FROM Services WHERE id="+e.ID);) {
		         // Extract data from result set
		         if (rs.next()) {
		            // Retrieve by column name
		        	stmt.executeUpdate("DELETE from Services WHERE id="+e.getID());
		         }
				 stmt.close();
		      } catch (SQLException ex) {
		         ex.printStackTrace();
		      } 

	}

}

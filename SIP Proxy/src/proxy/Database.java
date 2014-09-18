
package proxy;

import com.mysql.jdbc.Driver;
import java.sql.*;

public class Database
{
	private static Connection	connection;

	public Database(String ip, String dbname, String name, String pass) {
		try {
			new Driver();
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + "/"
					+ dbname, name, pass);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addUser(String telCislo, String celeMeno, String heslo,
			String domena) {
		try {
			Statement st = connection.createStatement();
			String kluc = "sip:" + telCislo + "@" + domena;
			st.executeUpdate("INSERT pouzivatel VALUES(NULL,'" + telCislo
					+ "','" + celeMeno + "','" + heslo + "','" + domena + "','"
					+ kluc + "')");

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Member getUser(String idUzivatela) {
		try {
			Statement st = connection.createStatement();
			String query = "SELECT * FROM pouzivatel WHERE `kluc` LIKE '"
					+ idUzivatela + "'";
			ResultSet rs = st.executeQuery(query);

			if (rs.next()) {
				String name = new Integer(rs.getInt("telefonne_cislo")).toString();
				Member user = new Member(name, rs.getString("heslo"), rs.getString("domena"));
				return user;
			}

			st.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void log(String sprava) {
		try {
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO logy (id, sprava) values (NULL, ?)");
			stmt.setString(1, sprava);
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

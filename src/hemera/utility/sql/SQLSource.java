package hemera.utility.sql;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * <code>SQLSource</code> defines the immutable structure
 * representing a SQL data source.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public class SQLSource {
	/**
	 * The <code>String</code> key.
	 */
	public final String key;
	/**
	 * The <code>String</code> address of the host.
	 */
	public final String host;
	/**
	 * The <code>int</code> port of the host.
	 */
	public final int port;
	/**
	 * The <code>String</code> name of the database.
	 */
	public final String dbName;
	/**
	 * The <code>BasicDataSource</code> instance.
	 */
	public final BasicDataSource datasource;
	
	/**
	 * Constructor of <code>SQLSource</code>.
	 * @param key The <code>String</code> key used to
	 * identify the attached data source.
	 * @param host The <code>String</code> address of
	 * the host.
	 * @param port The <code>int</code> port of the
	 * host to connect to.
	 * @param dbName The <code>String</code> name of
	 * the database.
	 * @param dbUsername The <code>String</code> user
	 * name used to login to the database.
	 * @param dbPassword The <code>String</code> password
	 * used to login to the database.
	 */
	SQLSource(final String key, final String host, final int port, final String dbName,
			final String dbUsername, final String dbPassword) {
		this.key = key;
		this.host = host;
		this.port = port;
		this.dbName = dbName;
		// Create database URL string.
		final StringBuilder urlbuilder = new StringBuilder();
		urlbuilder.append("jdbc:mysql://").append(host).append(":").append(port).append("/").append(this.dbName);
		final String url = urlbuilder.toString();
		// Create data source.
		final BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName("com.mysql.jdbc.Driver");
		datasource.setUrl(url);
		datasource.setTestOnBorrow(true);
		datasource.setUsername(dbUsername);
		datasource.setPassword(dbPassword);
		this.datasource = datasource;
	}
}
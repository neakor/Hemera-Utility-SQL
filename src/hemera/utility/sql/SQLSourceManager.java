package hemera.utility.sql;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jcraft.jsch.JSchException;

/**
 * <code>SQLSourceManager</code> defines the singleton
 * management unit responsible for maintaining a set of
 * SQL <code>SQLSource</code> instances.
 * <p>
 * <code>SQLSourceManager</code> is thread-safe while
 * providing high concurrency capabilities to allow
 * concurrent access to the managed data sources.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum SQLSourceManager {
	/**
	 * The <code>SQLDataSource</code> singleton.
	 */
	instance;

	/**
	 * The <code>Concurrent</code> of <code>String</code>
	 * key to <code>SQLSource</code>.
	 */
	private final ConcurrentMap<String, SQLSource> sources;

	/**
	 * Constructor of <code>SQLSourceManager</code>.
	 */
	private SQLSourceManager() {
		this.sources = new ConcurrentHashMap<String, SQLSource>();
	}

	/**
	 * Attach the data source with specified values
	 * to the manager if the key is not associated
	 * with a source already.
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
	 * @return The previous <code>SQLSource</code>
	 * associated with the key.
	 */
	public SQLSource attachIfAbscent(final String key, final String host, final int port,
			final String dbName, final String dbUsername, final String dbPassword) {
		final SQLSource existing = this.sources.get(key);
		if (existing != null) return existing;
		final SQLSource source = new SQLSource(key, host, port, dbName, dbUsername, dbPassword);
		return this.sources.putIfAbsent(key, source);
	}

	/**
	 * Attach the data source with specified values
	 * to the manager if the key is not associated
	 * with a source already.
	 * <p>
	 * This method sets up a SSH tunnel to the host
	 * with given public key file. And a local-host
	 * SQL connection is setup on the local port
	 * to forward all SQL packets to the remote host
	 * at the specified port.
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
	 * @param sshKey The <code>String</code> path to
	 * the SSH key file.
	 * @param sshUsername The <code>String</code>
	 * user name used login to the SSH host.
	 * @param localPort The <code>int</code> forwarding
	 * port number to use on the local machine.
	 * @return The previous <code>SQLSource</code>
	 * associated with the key.
	 * @throws JSchException If SSH tunnel setup failed.
	 */
	public SQLSource attachIfAbscent(final String key, final String host, final int port,
			final String dbName, final String dbUsername, final String dbPassword,
			final String sshKey, final String sshUsername, final int localPort) throws JSchException {
		final SQLSource existing = this.sources.get(key);
		if (existing != null) return existing;
		final SQLSSHSource source = new SQLSSHSource(key, host, port, dbName, dbUsername, dbPassword,
				sshKey, sshUsername, localPort);
		return this.sources.putIfAbsent(key, source);
	}

	/**
	 * Retrieve a SQL source identified by given key.
	 * @param key The <code>String</code> key used to
	 * identify the data source.
	 * @return The <code>SQLSource</code> instance.
	 */
	public SQLSource getSource(final String key) {
		return this.sources.get(key);
	}
}
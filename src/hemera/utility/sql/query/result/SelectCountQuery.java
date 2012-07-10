package hemera.utility.sql.query.result;

import java.sql.SQLException;

/**
 * <code>SelectCountQuery</code> defines the selection
 * query that selects the number of rows with specified
 * conditions.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public class SelectCountQuery extends AbstractSelectQuery {
	
	/**
	 * Constructor of <code>SelectCountQuery</code>.
	 * @param key The <code>String</code> key used to
	 * identify the data source.
	 */
	public SelectCountQuery(final String key) {
		super(key);
	}
	
	@Override
	protected String buildResultTemplate() {
		return "count(*)";
	}
	
	/**
	 * Retrieve the count integer value from of the
	 * result column specified.
	 * @return The <code>int</code> value.
	 * @throws SQLException If result set access failed.
	 */
	public int getCountValue() throws SQLException {
		if (this.resultset == null) return 0;
		else return this.resultset.getInt(1);
	}
}

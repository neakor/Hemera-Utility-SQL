package hemera.utility.sql.query.update;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hemera.utility.sql.data.value.BooleanColumnValue;
import hemera.utility.sql.data.value.ColumnValue;
import hemera.utility.sql.data.value.DoubleColumnValue;
import hemera.utility.sql.data.value.EncryptColumnValue;
import hemera.utility.sql.data.value.IntColumnValue;
import hemera.utility.sql.data.value.LongColumnValue;
import hemera.utility.sql.data.value.StringColumnValue;
import hemera.utility.sql.interfaces.IModifyQuery;
import hemera.utility.sql.query.AbstractQuery;
import hemera.utility.sql.util.QueryExecutor;

/**
 * <code>InsertQuery</code> defines the implementation
 * of a SQL query that inserts a new row into specified
 * table with given values. This implementation depends
 * on the values in <code>ESQLConfig</code> for database
 * name.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public class InsertQuery extends AbstractQuery implements IModifyQuery {
	/**
	 * The <code>String</code> name of the table to
	 * operate the query on.
	 */
	private final String tablename;
	/**
	 * The <code>List</code> of <code>DataPair</code>.
	 */
	private final List<ColumnValue> data;

	/**
	 * Constructor of <code>InsertQuery</code>.
	 * @param key The <code>String</code> key used to
	 * identify the data source.
	 * @param tablename The <code>String</code> name of
	 * the table.
	 */
	public InsertQuery(final String key, final String tablename) {
		super(key);
		this.tablename = tablename;
		this.data = new ArrayList<ColumnValue>();
	}

	/**
	 * Add the column-name value pair for the new row
	 * to be inserted.
	 * @param column The <code>String</code> column
	 * name.
	 * @param value The <code>int</code> value for
	 * the column.
	 */
	public void addData(final String column, final int value) {
		this.data.add(new IntColumnValue(this.tablename, column, value));
	}
	
	/**
	 * Add the column-name value pair for the new row
	 * to be inserted.
	 * @param column The <code>String</code> column
	 * name.
	 * @param value The <code>long</code> value for
	 * the column.
	 */
	public void addData(final String column, final long value) {
		this.data.add(new LongColumnValue(this.tablename, column, value));
	}
	
	/**
	 * Add the column-name value pair for the new row
	 * to be inserted.
	 * @param column The <code>String</code> column
	 * name.
	 * @param value The <code>double</code> value for
	 * the column.
	 */
	public void addData(final String column, final double value) {
		this.data.add(new DoubleColumnValue(this.tablename, column, value));
	}

	/**
	 * Add the column-name value pair for the new row
	 * to be inserted.
	 * @param column The <code>String</code> column
	 * name.
	 * @param value The <code>boolean</code> value for
	 * the column.
	 */
	public void addData(final String column, final boolean value) {
		this.data.add(new BooleanColumnValue(this.tablename, column, value));
	}
	
	/**
	 * Add the column-name value pair for the new row
	 * to be inserted.
	 * @param column The <code>String</code> column
	 * name.
	 * @param value The <code>String</code> value for
	 * the column.
	 */
	public void addData(final String column, final String value) {
		this.data.add(new StringColumnValue(this.tablename, column, value));
	}
	
	/**
	 * Add the column-name value pair for the new row
	 * to be inserted and encrypted with given key.
	 * @param key The <code>String</code> encryption
	 * key.
	 * @param column The <code>String</code> column
	 * name.
	 * @param value The <code>String</code> value for
	 * the column.
	 */
	public void addEncryptData(final String key, final String column, final String value) {
		this.data.add(new EncryptColumnValue(this.tablename, column, value, key));
	}
	
	@Override
	public Integer execute() throws SQLException {
		return QueryExecutor.instance.execute(this);
	}

	@Override
	protected String buildTemplate() {
		final StringBuilder builder = new StringBuilder();
		// Header.
		builder.append("insert into `").append(this.source.dbName).append("`");
		builder.append(".`").append(this.tablename).append("` ");
		// All columns.
		builder.append("(");
		final int size = this.data.size();
		final int last = size - 1;
		for (int i = 0; i < size; i++) {
			final ColumnValue data = this.data.get(i);
			builder.append("`").append(data.table).append("`.");
			builder.append("`").append(data.column).append("`");
			if (i != last) builder.append(",");
		}
		builder.append(") ");
		// Value place-holders.
		builder.append("values (");
		for (int i = 0; i < size; i++) {
			final ColumnValue data = this.data.get(i);
			if (data instanceof EncryptColumnValue) {
				builder.append("AES_ENCRYPT(?, ?)");
			} else {
				builder.append("?");
			}
			if (i != last) builder.append(",");
		}
		builder.append(");");
		return builder.toString();
	}

	@Override
	protected void insertValues(final PreparedStatement statement) throws SQLException {
		final int size = this.data.size();
		int index = 1;
		for (int i = 0; i < size; i++) {
			final ColumnValue data = this.data.get(i);
			final int count = data.insertValue(index, statement);
			index += count;
		}
	}
}

package hemera.utility.sql.data.value;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <code>DeltaValue</code> defines the table column value
 * that contains an integer delta change to the value of
 * the column.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.3
 */
public final class DeltaValue extends ColumnValue {
	/**
	 * The <code>int</code> delta amount.
	 */
	public final int delta;

	/**
	 * Constructor of <code>DeltaValue</code>.
	 * @param table The <code>String</code> name of
	 * the table.
	 * @param column The <code>String</code> name of
	 * the column.
	 * @param delta The <code>int</code> delta amount.
	 */
	public DeltaValue(String table, String column, final int delta) {
		super(table, column);
		this.delta = delta;
	}

	@Override
	public void insertValue(final int index, final int statementColumsCount, final PreparedStatement statement) throws SQLException {}
	
	@Override
	public int getValuesCount() {
		return 0;
	}
	
	@Override
	public int getInsertCountPerValue() {
		return 0;
	}
}

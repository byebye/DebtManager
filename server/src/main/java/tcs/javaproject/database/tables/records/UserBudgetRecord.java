/**
 * This class is generated by jOOQ
 */
package tcs.javaproject.database.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.4"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserBudgetRecord extends org.jooq.impl.TableRecordImpl<tcs.javaproject.database.tables.records.UserBudgetRecord> implements org.jooq.Record2<java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = 333871890;

	/**
	 * Setter for <code>debtmanager.user_budget.user_id</code>.
	 */
	public void setUserId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>debtmanager.user_budget.user_id</code>.
	 */
	public java.lang.Integer getUserId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>debtmanager.user_budget.budget_id</code>.
	 */
	public void setBudgetId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>debtmanager.user_budget.budget_id</code>.
	 */
	public java.lang.Integer getBudgetId() {
		return (java.lang.Integer) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return tcs.javaproject.database.tables.UserBudget.USER_BUDGET.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return tcs.javaproject.database.tables.UserBudget.USER_BUDGET.BUDGET_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getBudgetId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserBudgetRecord value1(java.lang.Integer value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserBudgetRecord value2(java.lang.Integer value) {
		setBudgetId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserBudgetRecord values(java.lang.Integer value1, java.lang.Integer value2) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached UserBudgetRecord
	 */
	public UserBudgetRecord() {
		super(tcs.javaproject.database.tables.UserBudget.USER_BUDGET);
	}

	/**
	 * Create a detached, initialised UserBudgetRecord
	 */
	public UserBudgetRecord(java.lang.Integer userId, java.lang.Integer budgetId) {
		super(tcs.javaproject.database.tables.UserBudget.USER_BUDGET);

		setValue(0, userId);
		setValue(1, budgetId);
	}
}

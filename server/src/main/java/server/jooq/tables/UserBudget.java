/**
 * This class is generated by jOOQ
 */
package server.jooq.tables;

import server.jooq.Debtmanager;
import server.jooq.Keys;
import server.jooq.tables.records.UserBudgetRecord;

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
public class UserBudget extends org.jooq.impl.TableImpl<UserBudgetRecord> {

	private static final long serialVersionUID = 22742705;

	/**
	 * The reference instance of <code>debtmanager.user_budget</code>
	 */
	public static final UserBudget USER_BUDGET = new UserBudget();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<UserBudgetRecord> getRecordType() {
		return UserBudgetRecord.class;
	}

	/**
	 * The column <code>debtmanager.user_budget.user_id</code>.
	 */
	public final org.jooq.TableField<UserBudgetRecord, java.lang.Integer> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>debtmanager.user_budget.budget_id</code>.
	 */
	public final org.jooq.TableField<UserBudgetRecord, java.lang.Integer> BUDGET_ID = createField("budget_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * Create a <code>debtmanager.user_budget</code> table reference
	 */
	public UserBudget() {
		this("user_budget", null);
	}

	/**
	 * Create an aliased <code>debtmanager.user_budget</code> table reference
	 */
	public UserBudget(java.lang.String alias) {
		this(alias, UserBudget.USER_BUDGET);
	}

	private UserBudget(java.lang.String alias, org.jooq.Table<UserBudgetRecord> aliased) {
		this(alias, aliased, null);
	}

	private UserBudget(java.lang.String alias, org.jooq.Table<UserBudgetRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Debtmanager.DEBTMANAGER, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<UserBudgetRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<UserBudgetRecord, ?>>asList(Keys.USER_BUDGET__USER_BUDGET_USER_ID_FKEY, Keys.USER_BUDGET__USER_BUDGET_BUDGET_ID_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserBudget as(java.lang.String alias) {
		return new UserBudget(alias, this);
	}

	/**
	 * Rename this table
	 */
	public UserBudget rename(java.lang.String name) {
		return new UserBudget(name, null);
	}
}

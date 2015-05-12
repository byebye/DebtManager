/**
 * This class is generated by jOOQ
 */
package tcs.javaproject.database.jooq;

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
public class Debtmanager extends org.jooq.impl.SchemaImpl {

	private static final long serialVersionUID = 1016524431;

	/**
	 * The reference instance of <code>debtmanager</code>
	 */
	public static final Debtmanager DEBTMANAGER = new Debtmanager();

	/**
	 * No further instances allowed
	 */
	private Debtmanager() {
		super("debtmanager");
	}

	@Override
	public final java.util.List<org.jooq.Sequence<?>> getSequences() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getSequences0());
		return result;
	}

	private final java.util.List<org.jooq.Sequence<?>> getSequences0() {
		return java.util.Arrays.<org.jooq.Sequence<?>>asList(
			tcs.javaproject.database.jooq.Sequences.BANK_TRANSFERS_ID_SEQ,
			tcs.javaproject.database.jooq.Sequences.BUDGETS_ID_SEQ,
			tcs.javaproject.database.jooq.Sequences.PAYMENTS_ID_SEQ,
			tcs.javaproject.database.jooq.Sequences.USERS_ID_SEQ);
	}

	@Override
	public final java.util.List<org.jooq.Table<?>> getTables() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final java.util.List<org.jooq.Table<?>> getTables0() {
		return java.util.Arrays.<org.jooq.Table<?>>asList(
			tcs.javaproject.database.jooq.tables.BankTransfers.BANK_TRANSFERS,
			tcs.javaproject.database.jooq.tables.Budgets.BUDGETS,
			tcs.javaproject.database.jooq.tables.Payments.PAYMENTS,
			tcs.javaproject.database.jooq.tables.UserBudget.USER_BUDGET,
			tcs.javaproject.database.jooq.tables.Users.USERS);
	}
}

/**
 * This class is generated by jOOQ
 */
package server.jooq;

import server.jooq.tables.*;

/**
 * Convenience access to all tables in debtmanager
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.4"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

	/**
	 * The table debtmanager.bank_transfers
	 */
	public static final BankTransfers BANK_TRANSFERS = BankTransfers.BANK_TRANSFERS;

	/**
	 * The table debtmanager.budgets
	 */
	public static final Budgets BUDGETS = Budgets.BUDGETS;

	/**
	 * The table debtmanager.payments
	 */
	public static final Payments PAYMENTS = Payments.PAYMENTS;

	/**
	 * The table debtmanager.user_budget
	 */
	public static final UserBudget USER_BUDGET = UserBudget.USER_BUDGET;

	/**
	 * The table debtmanager.users
	 */
	public static final Users USERS = Users.USERS;
}

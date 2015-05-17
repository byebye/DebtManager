/**
 * This class is generated by jOOQ
 */
package server.jooq;

import server.jooq.tables.*;
import server.jooq.tables.records.*;

/**
 * A class modelling foreign key relationships between tables of the <code>debtmanager</code> 
 * schema
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.4"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final org.jooq.Identity<BankTransfersRecord, java.lang.Integer> IDENTITY_BANK_TRANSFERS = Identities0.IDENTITY_BANK_TRANSFERS;
	public static final org.jooq.Identity<BudgetsRecord, java.lang.Integer> IDENTITY_BUDGETS = Identities0.IDENTITY_BUDGETS;
	public static final org.jooq.Identity<PaymentsRecord, java.lang.Integer> IDENTITY_PAYMENTS = Identities0.IDENTITY_PAYMENTS;
	public static final org.jooq.Identity<UsersRecord, java.lang.Integer> IDENTITY_USERS = Identities0.IDENTITY_USERS;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final org.jooq.UniqueKey<BankTransfersRecord> BANK_TRANSFERS_PKEY = UniqueKeys0.BANK_TRANSFERS_PKEY;
	public static final org.jooq.UniqueKey<BudgetsRecord> BUDGETS_PKEY = UniqueKeys0.BUDGETS_PKEY;
	public static final org.jooq.UniqueKey<PaymentsRecord> PAYMENTS_PKEY = UniqueKeys0.PAYMENTS_PKEY;
	public static final org.jooq.UniqueKey<UsersRecord> USERS_PKEY = UniqueKeys0.USERS_PKEY;
	public static final org.jooq.UniqueKey<UsersRecord> USERS_EMAIL_KEY = UniqueKeys0.USERS_EMAIL_KEY;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final org.jooq.ForeignKey<BankTransfersRecord, BudgetsRecord> BANK_TRANSFERS__BANK_TRANSFERS_BUDGET_ID_FKEY = ForeignKeys0.BANK_TRANSFERS__BANK_TRANSFERS_BUDGET_ID_FKEY;
	public static final org.jooq.ForeignKey<BankTransfersRecord, UsersRecord> BANK_TRANSFERS__BANK_TRANSFERS_WHO_FKEY = ForeignKeys0.BANK_TRANSFERS__BANK_TRANSFERS_WHO_FKEY;
	public static final org.jooq.ForeignKey<BankTransfersRecord, UsersRecord> BANK_TRANSFERS__BANK_TRANSFERS_WHOM_FKEY = ForeignKeys0.BANK_TRANSFERS__BANK_TRANSFERS_WHOM_FKEY;
	public static final org.jooq.ForeignKey<BudgetsRecord, UsersRecord> BUDGETS__BUDGETS_OWNER_ID_FKEY = ForeignKeys0.BUDGETS__BUDGETS_OWNER_ID_FKEY;
	public static final org.jooq.ForeignKey<PaymentsRecord, BudgetsRecord> PAYMENTS__PAYMENTS_BUDGET_ID_FKEY = ForeignKeys0.PAYMENTS__PAYMENTS_BUDGET_ID_FKEY;
	public static final org.jooq.ForeignKey<PaymentsRecord, UsersRecord> PAYMENTS__PAYMENTS_USER_ID_FKEY = ForeignKeys0.PAYMENTS__PAYMENTS_USER_ID_FKEY;
	public static final org.jooq.ForeignKey<UserBudgetRecord, UsersRecord> USER_BUDGET__USER_BUDGET_USER_ID_FKEY = ForeignKeys0.USER_BUDGET__USER_BUDGET_USER_ID_FKEY;
	public static final org.jooq.ForeignKey<UserBudgetRecord, BudgetsRecord> USER_BUDGET__USER_BUDGET_BUDGET_ID_FKEY = ForeignKeys0.USER_BUDGET__USER_BUDGET_BUDGET_ID_FKEY;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends org.jooq.impl.AbstractKeys {
		public static org.jooq.Identity<BankTransfersRecord, java.lang.Integer> IDENTITY_BANK_TRANSFERS = createIdentity(BankTransfers.BANK_TRANSFERS, BankTransfers.BANK_TRANSFERS.ID);
		public static org.jooq.Identity<BudgetsRecord, java.lang.Integer> IDENTITY_BUDGETS = createIdentity(Budgets.BUDGETS, Budgets.BUDGETS.ID);
		public static org.jooq.Identity<PaymentsRecord, java.lang.Integer> IDENTITY_PAYMENTS = createIdentity(Payments.PAYMENTS, Payments.PAYMENTS.ID);
		public static org.jooq.Identity<UsersRecord, java.lang.Integer> IDENTITY_USERS = createIdentity(Users.USERS, Users.USERS.ID);
	}

	private static class UniqueKeys0 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.UniqueKey<BankTransfersRecord> BANK_TRANSFERS_PKEY = createUniqueKey(BankTransfers.BANK_TRANSFERS, BankTransfers.BANK_TRANSFERS.ID);
		public static final org.jooq.UniqueKey<BudgetsRecord> BUDGETS_PKEY = createUniqueKey(Budgets.BUDGETS, Budgets.BUDGETS.ID);
		public static final org.jooq.UniqueKey<PaymentsRecord> PAYMENTS_PKEY = createUniqueKey(Payments.PAYMENTS, Payments.PAYMENTS.ID);
		public static final org.jooq.UniqueKey<UsersRecord> USERS_PKEY = createUniqueKey(Users.USERS, Users.USERS.ID);
		public static final org.jooq.UniqueKey<UsersRecord> USERS_EMAIL_KEY = createUniqueKey(Users.USERS, Users.USERS.EMAIL);
	}

	private static class ForeignKeys0 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<BankTransfersRecord, BudgetsRecord> BANK_TRANSFERS__BANK_TRANSFERS_BUDGET_ID_FKEY = createForeignKey(Keys.BUDGETS_PKEY, BankTransfers.BANK_TRANSFERS, BankTransfers.BANK_TRANSFERS.BUDGET_ID);
		public static final org.jooq.ForeignKey<BankTransfersRecord, UsersRecord> BANK_TRANSFERS__BANK_TRANSFERS_WHO_FKEY = createForeignKey(Keys.USERS_PKEY, BankTransfers.BANK_TRANSFERS, BankTransfers.BANK_TRANSFERS.WHO);
		public static final org.jooq.ForeignKey<BankTransfersRecord, UsersRecord> BANK_TRANSFERS__BANK_TRANSFERS_WHOM_FKEY = createForeignKey(Keys.USERS_PKEY, BankTransfers.BANK_TRANSFERS, BankTransfers.BANK_TRANSFERS.WHOM);
		public static final org.jooq.ForeignKey<BudgetsRecord, UsersRecord> BUDGETS__BUDGETS_OWNER_ID_FKEY = createForeignKey(Keys.USERS_PKEY, Budgets.BUDGETS, Budgets.BUDGETS.OWNER_ID);
		public static final org.jooq.ForeignKey<PaymentsRecord, BudgetsRecord> PAYMENTS__PAYMENTS_BUDGET_ID_FKEY = createForeignKey(Keys.BUDGETS_PKEY, Payments.PAYMENTS, Payments.PAYMENTS.BUDGET_ID);
		public static final org.jooq.ForeignKey<PaymentsRecord, UsersRecord> PAYMENTS__PAYMENTS_USER_ID_FKEY = createForeignKey(Keys.USERS_PKEY, Payments.PAYMENTS, Payments.PAYMENTS.USER_ID);
		public static final org.jooq.ForeignKey<UserBudgetRecord, UsersRecord> USER_BUDGET__USER_BUDGET_USER_ID_FKEY = createForeignKey(Keys.USERS_PKEY, UserBudget.USER_BUDGET, UserBudget.USER_BUDGET.USER_ID);
		public static final org.jooq.ForeignKey<UserBudgetRecord, BudgetsRecord> USER_BUDGET__USER_BUDGET_BUDGET_ID_FKEY = createForeignKey(Keys.BUDGETS_PKEY, UserBudget.USER_BUDGET, UserBudget.USER_BUDGET.BUDGET_ID);
	}
}
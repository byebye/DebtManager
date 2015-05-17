/**
 * This class is generated by jOOQ
 */
package server.jooq.tables;

import server.jooq.Debtmanager;
import server.jooq.Keys;
import server.jooq.tables.records.PaymentsRecord;

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
public class Payments extends org.jooq.impl.TableImpl<PaymentsRecord> {

	private static final long serialVersionUID = 457270906;

	/**
	 * The reference instance of <code>debtmanager.payments</code>
	 */
	public static final Payments PAYMENTS = new Payments();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<PaymentsRecord> getRecordType() {
		return PaymentsRecord.class;
	}

	/**
	 * The column <code>debtmanager.payments.id</code>.
	 */
	public final org.jooq.TableField<PaymentsRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>debtmanager.payments.term</code>.
	 */
	public final org.jooq.TableField<PaymentsRecord, java.sql.Date> TERM = createField("term", org.jooq.impl.SQLDataType.DATE.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>debtmanager.payments.budget_id</code>.
	 */
	public final org.jooq.TableField<PaymentsRecord, java.lang.Integer> BUDGET_ID = createField("budget_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>debtmanager.payments.user_id</code>.
	 */
	public final org.jooq.TableField<PaymentsRecord, java.lang.Integer> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>debtmanager.payments.description</code>.
	 */
	public final org.jooq.TableField<PaymentsRecord, java.lang.String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.VARCHAR.length(200), this, "");

	/**
	 * The column <code>debtmanager.payments.amount</code>.
	 */
	public final org.jooq.TableField<PaymentsRecord, java.math.BigDecimal> AMOUNT = createField("amount", org.jooq.impl.SQLDataType.NUMERIC.precision(6, 2).nullable(false), this, "");

	/**
	 * The column <code>debtmanager.payments.accounted</code>.
	 */
	public final org.jooq.TableField<PaymentsRecord, java.lang.Boolean> ACCOUNTED = createField("accounted", org.jooq.impl.SQLDataType.BOOLEAN.defaulted(true), this, "");

	/**
	 * Create a <code>debtmanager.payments</code> table reference
	 */
	public Payments() {
		this("payments", null);
	}

	/**
	 * Create an aliased <code>debtmanager.payments</code> table reference
	 */
	public Payments(java.lang.String alias) {
		this(alias, Payments.PAYMENTS);
	}

	private Payments(java.lang.String alias, org.jooq.Table<PaymentsRecord> aliased) {
		this(alias, aliased, null);
	}

	private Payments(java.lang.String alias, org.jooq.Table<PaymentsRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Debtmanager.DEBTMANAGER, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<PaymentsRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_PAYMENTS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<PaymentsRecord> getPrimaryKey() {
		return Keys.PAYMENTS_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<PaymentsRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<PaymentsRecord>>asList(Keys.PAYMENTS_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<PaymentsRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<PaymentsRecord, ?>>asList(Keys.PAYMENTS__PAYMENTS_BUDGET_ID_FKEY, Keys.PAYMENTS__PAYMENTS_USER_ID_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Payments as(java.lang.String alias) {
		return new Payments(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Payments rename(java.lang.String name) {
		return new Payments(name, null);
	}
}

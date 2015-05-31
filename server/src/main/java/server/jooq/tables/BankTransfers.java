/**
 * This class is generated by jOOQ
 */
package server.jooq.tables;

import org.jooq.impl.SQLDataType;

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
public class BankTransfers extends org.jooq.impl.TableImpl<server.jooq.tables.records.BankTransfersRecord> {

	private static final long serialVersionUID = 346586301;

	/**
	 * The reference instance of <code>debtmanager.bank_transfers</code>
	 */
	public static final server.jooq.tables.BankTransfers BANK_TRANSFERS = new server.jooq.tables.BankTransfers();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<server.jooq.tables.records.BankTransfersRecord> getRecordType() {
		return server.jooq.tables.records.BankTransfersRecord.class;
	}

	/**
	 * The column <code>debtmanager.bank_transfers.id</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BankTransfersRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>debtmanager.bank_transfers.settle_id</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BankTransfersRecord, java.lang.Integer> SETTLE_ID = createField("settle_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>debtmanager.bank_transfers.who</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BankTransfersRecord, java.lang.Integer> WHO = createField("who", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>debtmanager.bank_transfers.whom</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BankTransfersRecord, java.lang.Integer> WHOM = createField("whom", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>debtmanager.bank_transfers.amount</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BankTransfersRecord, java.math.BigDecimal> AMOUNT = createField("amount", org.jooq.impl.SQLDataType.NUMERIC.precision(6, 2).nullable(false), this, "");

	/**
	 * The column <code>debtmanager.bank_transfers.paid</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BankTransfersRecord, java.lang.Integer> PAID = createField("paid", SQLDataType.INTEGER.defaulted(true), this, "");

	/**
	 * Create a <code>debtmanager.bank_transfers</code> table reference
	 */
	public BankTransfers() {
		this("bank_transfers", null);
	}

	/**
	 * Create an aliased <code>debtmanager.bank_transfers</code> table reference
	 */
	public BankTransfers(java.lang.String alias) {
		this(alias, server.jooq.tables.BankTransfers.BANK_TRANSFERS);
	}

	private BankTransfers(java.lang.String alias, org.jooq.Table<server.jooq.tables.records.BankTransfersRecord> aliased) {
		this(alias, aliased, null);
	}

	private BankTransfers(java.lang.String alias, org.jooq.Table<server.jooq.tables.records.BankTransfersRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, server.jooq.Debtmanager.DEBTMANAGER, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<server.jooq.tables.records.BankTransfersRecord, java.lang.Integer> getIdentity() {
		return server.jooq.Keys.IDENTITY_BANK_TRANSFERS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<server.jooq.tables.records.BankTransfersRecord> getPrimaryKey() {
		return server.jooq.Keys.BANK_TRANSFERS_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<server.jooq.tables.records.BankTransfersRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<server.jooq.tables.records.BankTransfersRecord>>asList(server.jooq.Keys.BANK_TRANSFERS_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<server.jooq.tables.records.BankTransfersRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<server.jooq.tables.records.BankTransfersRecord, ?>>asList(server.jooq.Keys.BANK_TRANSFERS__BANK_TRANSFERS_SETTLE_ID_FKEY, server.jooq.Keys.BANK_TRANSFERS__BANK_TRANSFERS_WHO_FKEY, server.jooq.Keys.BANK_TRANSFERS__BANK_TRANSFERS_WHOM_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public server.jooq.tables.BankTransfers as(java.lang.String alias) {
		return new server.jooq.tables.BankTransfers(alias, this);
	}

	/**
	 * Rename this table
	 */
	public server.jooq.tables.BankTransfers rename(java.lang.String name) {
		return new server.jooq.tables.BankTransfers(name, null);
	}
}

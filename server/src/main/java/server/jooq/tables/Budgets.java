/**
 * This class is generated by jOOQ
 */
package server.jooq.tables;

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
public class Budgets extends org.jooq.impl.TableImpl<server.jooq.tables.records.BudgetsRecord> {

	private static final long serialVersionUID = 1403370394;

	/**
	 * The reference instance of <code>debtmanager.budgets</code>
	 */
	public static final server.jooq.tables.Budgets BUDGETS = new server.jooq.tables.Budgets();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<server.jooq.tables.records.BudgetsRecord> getRecordType() {
		return server.jooq.tables.records.BudgetsRecord.class;
	}

	/**
	 * The column <code>debtmanager.budgets.id</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BudgetsRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>debtmanager.budgets.name</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BudgetsRecord, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(50).nullable(false), this, "");

	/**
	 * The column <code>debtmanager.budgets.description</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BudgetsRecord, java.lang.String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.VARCHAR.length(200), this, "");

	/**
	 * The column <code>debtmanager.budgets.owner_id</code>.
	 */
	public final org.jooq.TableField<server.jooq.tables.records.BudgetsRecord, java.lang.Integer> OWNER_ID = createField("owner_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>debtmanager.budgets</code> table reference
	 */
	public Budgets() {
		this("budgets", null);
	}

	/**
	 * Create an aliased <code>debtmanager.budgets</code> table reference
	 */
	public Budgets(java.lang.String alias) {
		this(alias, server.jooq.tables.Budgets.BUDGETS);
	}

	private Budgets(java.lang.String alias, org.jooq.Table<server.jooq.tables.records.BudgetsRecord> aliased) {
		this(alias, aliased, null);
	}

	private Budgets(java.lang.String alias, org.jooq.Table<server.jooq.tables.records.BudgetsRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, server.jooq.Debtmanager.DEBTMANAGER, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<server.jooq.tables.records.BudgetsRecord, java.lang.Integer> getIdentity() {
		return server.jooq.Keys.IDENTITY_BUDGETS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<server.jooq.tables.records.BudgetsRecord> getPrimaryKey() {
		return server.jooq.Keys.BUDGETS_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<server.jooq.tables.records.BudgetsRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<server.jooq.tables.records.BudgetsRecord>>asList(server.jooq.Keys.BUDGETS_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<server.jooq.tables.records.BudgetsRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<server.jooq.tables.records.BudgetsRecord, ?>>asList(server.jooq.Keys.BUDGETS__BUDGETS_OWNER_ID_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public server.jooq.tables.Budgets as(java.lang.String alias) {
		return new server.jooq.tables.Budgets(alias, this);
	}

	/**
	 * Rename this table
	 */
	public server.jooq.tables.Budgets rename(java.lang.String name) {
		return new server.jooq.tables.Budgets(name, null);
	}
}

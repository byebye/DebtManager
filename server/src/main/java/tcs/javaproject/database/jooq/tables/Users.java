/**
 * This class is generated by jOOQ
 */
package tcs.javaproject.database.jooq.tables;

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
public class Users extends org.jooq.impl.TableImpl<tcs.javaproject.database.jooq.tables.records.UsersRecord> {

	private static final long serialVersionUID = -1529072898;

	/**
	 * The reference instance of <code>debtmanager.users</code>
	 */
	public static final tcs.javaproject.database.jooq.tables.Users USERS = new tcs.javaproject.database.jooq.tables.Users();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<tcs.javaproject.database.jooq.tables.records.UsersRecord> getRecordType() {
		return tcs.javaproject.database.jooq.tables.records.UsersRecord.class;
	}

	/**
	 * The column <code>debtmanager.users.id</code>.
	 */
	public final org.jooq.TableField<tcs.javaproject.database.jooq.tables.records.UsersRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>debtmanager.users.email</code>.
	 */
	public final org.jooq.TableField<tcs.javaproject.database.jooq.tables.records.UsersRecord, java.lang.String> EMAIL = createField("email", org.jooq.impl.SQLDataType.VARCHAR.length(120), this, "");

	/**
	 * The column <code>debtmanager.users.name</code>.
	 */
	public final org.jooq.TableField<tcs.javaproject.database.jooq.tables.records.UsersRecord, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(60).nullable(false), this, "");

	/**
	 * The column <code>debtmanager.users.bank_account</code>.
	 */
	public final org.jooq.TableField<tcs.javaproject.database.jooq.tables.records.UsersRecord, java.math.BigInteger> BANK_ACCOUNT = createField("bank_account", org.jooq.impl.SQLDataType.DECIMAL_INTEGER.precision(22).nullable(false), this, "");

	/**
	 * The column <code>debtmanager.users.password_hash</code>.
	 */
	public final org.jooq.TableField<tcs.javaproject.database.jooq.tables.records.UsersRecord, java.lang.String> PASSWORD_HASH = createField("password_hash", org.jooq.impl.SQLDataType.CHAR.length(64).nullable(false), this, "");

	/**
	 * Create a <code>debtmanager.users</code> table reference
	 */
	public Users() {
		this("users", null);
	}

	/**
	 * Create an aliased <code>debtmanager.users</code> table reference
	 */
	public Users(java.lang.String alias) {
		this(alias, tcs.javaproject.database.jooq.tables.Users.USERS);
	}

	private Users(java.lang.String alias, org.jooq.Table<tcs.javaproject.database.jooq.tables.records.UsersRecord> aliased) {
		this(alias, aliased, null);
	}

	private Users(java.lang.String alias, org.jooq.Table<tcs.javaproject.database.jooq.tables.records.UsersRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, tcs.javaproject.database.jooq.Debtmanager.DEBTMANAGER, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<tcs.javaproject.database.jooq.tables.records.UsersRecord, java.lang.Integer> getIdentity() {
		return tcs.javaproject.database.jooq.Keys.IDENTITY_USERS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<tcs.javaproject.database.jooq.tables.records.UsersRecord> getPrimaryKey() {
		return tcs.javaproject.database.jooq.Keys.USERS_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<tcs.javaproject.database.jooq.tables.records.UsersRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<tcs.javaproject.database.jooq.tables.records.UsersRecord>>asList(tcs.javaproject.database.jooq.Keys.USERS_PKEY, tcs.javaproject.database.jooq.Keys.USERS_EMAIL_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public tcs.javaproject.database.jooq.tables.Users as(java.lang.String alias) {
		return new tcs.javaproject.database.jooq.tables.Users(alias, this);
	}

	/**
	 * Rename this table
	 */
	public tcs.javaproject.database.jooq.tables.Users rename(java.lang.String name) {
		return new tcs.javaproject.database.jooq.tables.Users(name, null);
	}
}
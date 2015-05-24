/**
 * This class is generated by jOOQ
 */
package server.jooq;

/**
 * Convenience access to all sequences in debtmanager
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.4"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

	/**
	 * The sequence <code>debtmanager.bank_transfers_id_seq</code>
	 */
	public static final org.jooq.Sequence<java.lang.Long> BANK_TRANSFERS_ID_SEQ = new org.jooq.impl.SequenceImpl<java.lang.Long>("bank_transfers_id_seq", server.jooq.Debtmanager.DEBTMANAGER, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>debtmanager.budgets_id_seq</code>
	 */
	public static final org.jooq.Sequence<java.lang.Long> BUDGETS_ID_SEQ = new org.jooq.impl.SequenceImpl<java.lang.Long>("budgets_id_seq", server.jooq.Debtmanager.DEBTMANAGER, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>debtmanager.payments_id_seq</code>
	 */
	public static final org.jooq.Sequence<java.lang.Long> PAYMENTS_ID_SEQ = new org.jooq.impl.SequenceImpl<java.lang.Long>("payments_id_seq", server.jooq.Debtmanager.DEBTMANAGER, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>debtmanager.settlements_id_seq</code>
	 */
	public static final org.jooq.Sequence<java.lang.Long> SETTLEMENTS_ID_SEQ = new org.jooq.impl.SequenceImpl<java.lang.Long>("settlements_id_seq", server.jooq.Debtmanager.DEBTMANAGER, org.jooq.impl.SQLDataType.BIGINT.nullable(false));

	/**
	 * The sequence <code>debtmanager.users_id_seq</code>
	 */
	public static final org.jooq.Sequence<java.lang.Long> USERS_ID_SEQ = new org.jooq.impl.SequenceImpl<java.lang.Long>("users_id_seq", server.jooq.Debtmanager.DEBTMANAGER, org.jooq.impl.SQLDataType.BIGINT.nullable(false));
}

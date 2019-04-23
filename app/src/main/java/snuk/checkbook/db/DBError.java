package snuk.checkbook.db;

public class DBError {
	enum DBErrorCode { ERROR, INVALID_VALUE, NO_VALUE, NO_CHANGE, NO_DB, SUCCESS }

	public DBErrorCode code;
	public String msg;

	public DBError(DBErrorCode code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public DBError(DBErrorCode code){
		this(code, "");
	}

	@Override
	public String toString() {
		return "DBError{" +
			"code=" + code +
			", msg='" + msg + '\'' +
			'}';
	}
}

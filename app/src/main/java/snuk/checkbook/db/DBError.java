package snuk.checkbook.db;

public class DBError extends Exception {
	public enum ErrorCode { ERROR, INVALID_VALUE, NO_VALUE, NO_CHANGE, NO_DB }

	public ErrorCode code;
	public String msg;

	public DBError(ErrorCode code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public DBError(ErrorCode code){
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

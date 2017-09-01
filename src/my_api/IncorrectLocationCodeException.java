package my_api;

public class IncorrectLocationCodeException extends Exception {

	Integer status;
	int code; 

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public IncorrectLocationCodeException(int status, int code, String message) {
		super(message);
		this.status = status;
		this.code = code;
	}
	
	public IncorrectLocationCodeException() {
	}
	private static final long serialVersionUID = 1L;

	
}

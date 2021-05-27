package dailyproject.moon.common.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultBean<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int NO_LOGIN = -1;

	public static final int SUCCESS = 200;
	public static final String M_SUCCESS = "success";

	public static final int FAIL = 500;
	public static final String M_FAIL = "fail";

	public static final int FAIL_NO_USER = 505;

	public static final int NO_PERMISSION = 2;

	private String message = M_SUCCESS;

	private int code = SUCCESS;

	private T data;

	public ResultBean () {
		super();
	}

	public ResultBean (T data) {
		super();
		this.data = data;
	}

	public ResultBean (Throwable e) {
		super();
		this.message = e.toString();
		this.code = FAIL;
	}


}

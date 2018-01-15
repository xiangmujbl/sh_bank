package com.mmec.exception;

/**
 * 业务层异常基类
 * @author Administrator
 *
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = -5557648315058672485L;
	
    private String errorCode = null;
    private String errorDesc = null;
    private String errorDescEn = null;    
    private String detail = null;
    
	 public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getErrorDescEn() {
		return errorDescEn;
	}

	public void setErrorDescEn(String errorDescEn) {
		this.errorDescEn = errorDescEn;
	}

	/** <默认构造函数>
     */
    public ServiceException()
    {
        super();
    }
    
    /** 构造函数
     * @param  errorCode 错误码
     */
    public ServiceException(String errorCode,String errorDesc,String errorDescEn)
    {
        super(errorCode);
        setErrorCode(errorCode);
        this.setErrorDesc(errorDesc);
        this.setErrorDescEn(errorDescEn);
    }
    public ServiceException(String errorCode,String errorDesc,String errorDescEn,String detail)
    {
        super(errorCode);
        setErrorCode(errorCode);
        this.setErrorDesc(errorDesc);
        this.setErrorDescEn(errorDescEn);
        this.setDetail(detail);
    }
    
    /** 构造函数
     * @param  message 错误信息
     * @param  errorCode 错误码
     */
    public ServiceException(String message, String errorCode)
    {
        super(message);
        setErrorCode(errorCode);
    }
    
    /** 构造函数
     * @param  t Throwable
     */
    public ServiceException(Throwable t)
    {
        super(t);
    }
    
    /** 构造函数
     * @param  t Throwable
     * @param  errorCode 错误码
     */
    public ServiceException(Throwable t, String errorCode)
    {
        super(t);
        setErrorCode(errorCode);
    }
    
    public String getErrorCode()
    {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

	@Override
	public String toString() {
		return "ServiceException [errorCode=" + errorCode + ", errorDesc="
				+ errorDesc + ", errorDescEn=" + errorDescEn + "]";
	}
}
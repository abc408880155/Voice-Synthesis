package com.zemise.exception;

/**
 * @Author zemise_
 * @Date 2023/5/26
 * @Description
 */
public class AudException extends RuntimeException {
    private final Integer code;

    private final String msg;


    public AudException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public AudException(Error error) {
        super(error.getMsg());
        this.code = error.getCode();
        this.msg = error.getMsg();
    }
}

package com.example.image.exception;

/**
 * @title: MyException
 * @projectName: Lzyp2
 * @author: xuezhijia
 * @description: 自定义异常类
 * @date: 2019/9/18 18:56
 */
public class MyException extends RuntimeException {

    private static final long serialVersionUID = -13245683221L;

    public MyException() {
        super();
    }

    public MyException(String msg) {
        super(msg);
    }

    public MyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MyException(Throwable cause) {
        super(cause);
    }

}

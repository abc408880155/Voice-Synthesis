package com.zemise.exception;

import lombok.Getter;

/**
 * @Author zemise_
 * @Date 2023/5/26
 * @Description
 */

@Getter
public enum Error {
    INCORRECT_AUTHORIZATION(401, "Invalid authentication, try check api request url"),
    INCORRECT_SHA256_ENCRYPTION(401, "Invalid Encryption, try check encryption method is right"),
    INCORRECT_API_KEY_PROVIDED(401, "Incorrect API key provided");


    private final Integer code;
    private final String msg;

    Error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

package com.example.skunivProject.domain.users.exception;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import com.example.skunivProject.global.apipayload.exception.GeneralException;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}

package com.example.skunivProject.domain.team.exception;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import com.example.skunivProject.global.apipayload.exception.GeneralException;

public class TeamException extends GeneralException {
    public TeamException(BaseErrorCode code) {
        super(code);
    }
}

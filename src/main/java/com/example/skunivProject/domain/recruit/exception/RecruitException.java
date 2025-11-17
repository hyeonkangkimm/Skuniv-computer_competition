package com.example.skunivProject.domain.recruit.exception;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import com.example.skunivProject.global.apipayload.exception.GeneralException;

public class RecruitException extends GeneralException {
    public RecruitException(BaseErrorCode code) {
        super(code);
    }
}

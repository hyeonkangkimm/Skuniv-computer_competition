package com.example.skunivProject.domain.chat.exception;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import com.example.skunivProject.global.apipayload.exception.GeneralException;

public class ChatException extends GeneralException {
    public ChatException(BaseErrorCode code) {
        super(code);
    }
}

package com.backend.simya.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class BaseException extends Exception{
    private BaseResponseStatus status;
}
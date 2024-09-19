package com.rakbank.userservice.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {

    private String errorMessage;

}

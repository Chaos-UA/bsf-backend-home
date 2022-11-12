package com.demo.bsf.account.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class NotFoundException(type: String, id: Any) :
    ResponseStatusException(HttpStatus.NOT_FOUND, String.format("%s with ID %s not found", type, id))
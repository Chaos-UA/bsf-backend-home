package com.demo.bsf.account.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class InternalException(reason: String) :
    ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, reason)
package com.demo.bsf.account.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class ConflictException(message: String) : ResponseStatusException(HttpStatus.CONFLICT, message)
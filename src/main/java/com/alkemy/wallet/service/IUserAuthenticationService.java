package com.alkemy.wallet.service;

import com.alkemy.wallet.model.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

public interface IUserAuthenticationService {

    ResponseEntity<Object> createAuthenticationToken(AuthenticationRequest authenticationRequest);
}

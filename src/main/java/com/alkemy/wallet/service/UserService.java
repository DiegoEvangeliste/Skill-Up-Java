package com.alkemy.wallet.service;

import com.alkemy.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    @Autowired
    private UserRepository userRepository;


}

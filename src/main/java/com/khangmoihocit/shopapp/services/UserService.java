package com.khangmoihocit.shopapp.services;

import com.khangmoihocit.shopapp.dtos.UserDTO;
import com.khangmoihocit.shopapp.exceptions.DataNotFoundException;
import com.khangmoihocit.shopapp.models.User;

public interface UserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password);
}

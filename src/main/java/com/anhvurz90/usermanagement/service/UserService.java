package com.anhvurz90.usermanagement.service;

import java.util.List;

import com.anhvurz90.usermanagement.service.dto.UserDTO;

public interface UserService {

	UserDTO save(UserDTO userDTO);

	List<UserDTO> findAll();

	UserDTO findById(Long id);

	UserDTO findByName(String name);

	UserDTO findByEmail(String email);

	void delete(Long id);
}

package com.anhvurz90.usermanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anhvurz90.usermanagement.domain.User;
import com.anhvurz90.usermanagement.repository.UserRepository;
import com.anhvurz90.usermanagement.service.UserService;
import com.anhvurz90.usermanagement.service.dto.UserDTO;
import com.anhvurz90.usermanagement.service.mapper.UserMapper;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDTO save(UserDTO userDTO) {
		log.debug("Request to save User: {}", userDTO);
		User user = userMapper.toEntity(userDTO);
		user = userRepo.save(user);
		return userMapper.toDto(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findAll() {
		log.debug("Request to get all Users");
		return userRepo.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		log.debug("Request to get User: {}", id);
		return	userRepo.findById(id)
				    	.map(userMapper::toDto)
				    	.orElse(null);
	}

	@Override
	public UserDTO findByName(String name) {
		return userRepo.findByName(name)
						.map(userMapper::toDto)
						.orElse(null);
	}
	
	@Override
	public UserDTO findByEmail(String email) {
		return userRepo.findByEmail(email)
						.map(userMapper::toDto)
						.orElse(null);
	}

	@Override
	public void delete(Long id) {
		log.debug("Request to delete User: {}", id);
		userRepo.deleteById(id);
	}

}

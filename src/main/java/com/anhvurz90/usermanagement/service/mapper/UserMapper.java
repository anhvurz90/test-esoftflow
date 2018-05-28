package com.anhvurz90.usermanagement.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.anhvurz90.usermanagement.domain.User;
import com.anhvurz90.usermanagement.service.dto.UserDTO;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

	User toEntity(UserDTO dto);

	UserDTO toDto(User entity);

	List<User> toEntity(List<UserDTO> dtoList);

	List<UserDTO> toDto(List<User> entityList);

	default User fromId(Long id) {
		if (id == null) {
			return null;
		}
		User user = new User();
		user.setId(id);
		return user;
	}
}

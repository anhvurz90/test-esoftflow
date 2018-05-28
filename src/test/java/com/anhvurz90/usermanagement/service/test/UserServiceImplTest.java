package com.anhvurz90.usermanagement.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.anhvurz90.usermanagement.domain.User;
import com.anhvurz90.usermanagement.repository.UserRepository;
import com.anhvurz90.usermanagement.service.dto.UserDTO;
import com.anhvurz90.usermanagement.service.impl.UserServiceImpl;
import com.anhvurz90.usermanagement.service.mapper.UserMapper;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepo;

	@Mock
	private UserMapper userMapper;

	@InjectMocks
	private UserServiceImpl userService;

	private UserDTO user0;
	private User user0_1;
	private UserDTO user0_2;
	private UserDTO user1;

	@Before
	public void setUp() {
		user0 = new UserDTO();
		user0.setName("AnhVu1");
		user0.setEmail("anhvurz90@yahoo.com");
		user0.setAddress("Nguyen Huu Huan");

		user0_1 = new User();
		user0_1.setId(1L);
		user0_1.setName(user0.getName());
		user0_1.setEmail(user0.getEmail());
		user0_1.setAddress(user0.getAddress());

		user0_2 = new UserDTO();
		user0_2.setId(user0_1.getId());
		user0_2.setName(user0_1.getName());
		user0_2.setEmail(user0_1.getEmail());
		user0_2.setAddress(user0_1.getAddress());

		user1 = new UserDTO();
		user1.setName("AnhVu2");
		user1.setEmail("anhvurz90@gmail.com");
		user1.setAddress("Nghi Tam");
	}

	@Test
	public void testSaveUser() {
		Mockito.when(userMapper.toEntity(user0)).thenReturn(user0_1);
		Mockito.when(userRepo.save(user0_1)).thenReturn(user0_1);
		Mockito.when(userMapper.toDto(user0_1)).thenReturn(user0_2);

		UserDTO userResult = userService.save(user0);
		assertNotNull(userResult);
		assertEquals(user0_2, userResult);

		verify(userRepo, times(1)).save(user0_1);
	}

	@Test
	public void testFindUserByIdFound() {
		Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(user0_1));
		Mockito.when(userMapper.toDto(user0_1)).thenReturn(user0_2);

		UserDTO userResult = userService.findById(1L);
		assertNotNull(userResult);
		assertEquals(user0_2, userResult);

		verify(userRepo, times(1)).findById(1L);
	}

	@Test
	public void testFindUserByIdNotFound() {
		Mockito.when(userRepo.findById(2L)).thenReturn(Optional.ofNullable(null));
		// Mockito.when(userMapper.toDto(user0_1)).thenReturn(user0_2);

		UserDTO userResult = userService.findById(2L);
		assertNull(userResult);

		verify(userRepo, times(1)).findById(2L);
	}

	@Test
	public void testFindUserByName_Found() {
		Mockito.when(userRepo.findByName("AnhVu1")).thenReturn(Optional.of(user0_1));
		Mockito.when(userMapper.toDto(user0_1)).thenReturn(user0_2);

		UserDTO userResult = userService.findByName("AnhVu1");
		assertNotNull(userResult);
		assertEquals(user0_2, userResult);

		verify(userRepo, times(1)).findByName("AnhVu1");
	}

	@Test
	public void testFindUserByName_NotFound() {
		Mockito.when(userRepo.findByName("Nobita")).thenReturn(Optional.ofNullable(null));
		// Mockito.when(userMapper.toDto(user0_1)).thenReturn(user0_2);

		UserDTO userResult = userService.findByName("Nobita");
		assertNull(userResult);

		verify(userRepo, times(1)).findByName("Nobita");
	}

	@Test
	public void testFindUserByEmail_Found() {
		Mockito.when(userRepo.findByEmail("anhvurz90@yahoo.com")).thenReturn(Optional.of(user0_1));
		Mockito.when(userMapper.toDto(user0_1)).thenReturn(user0_2);

		UserDTO userResult = userService.findByEmail("anhvurz90@yahoo.com");
		assertNotNull(userResult);
		assertEquals(user0_2, userResult);

		verify(userRepo, times(1)).findByEmail("anhvurz90@yahoo.com");
	}

	@Test
	public void testFindUserByEmail_NotFound() {
		Mockito.when(userRepo.findByEmail("anhvurz90@facebook.com")).thenReturn(Optional.ofNullable(null));
		// Mockito.when(userMapper.toDto(user0_1)).thenReturn(user0_2);

		UserDTO userResult = userService.findByEmail("anhvurz90@facebook.com");
		assertNull(userResult);

		verify(userRepo, times(1)).findByEmail("anhvurz90@facebook.com");
	}

	@Test
	public void testFindAllUsers() {
		List<User> users = new ArrayList<>();
		users.add(user0_1);
		Mockito.when(userRepo.findAll()).thenReturn(users);

		List<UserDTO> usersResult = userService.findAll();
		assertNotNull(usersResult);
		assertEquals(1, usersResult.size());

		verify(userRepo, times(1)).findAll();
	}

	@Test
	public void testDeleteUser() {
		userService.delete(1L);

		verify(userRepo, times(1)).deleteById(1L);
	}
}

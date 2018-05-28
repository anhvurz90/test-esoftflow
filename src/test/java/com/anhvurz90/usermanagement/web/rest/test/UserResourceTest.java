package com.anhvurz90.usermanagement.web.rest.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.anhvurz90.usermanagement.service.dto.UserDTO;
import com.anhvurz90.usermanagement.service.impl.UserServiceImpl;
import com.anhvurz90.usermanagement.web.rest.UserResource;
import com.anhvurz90.usermanagement.web.rest.error.BadRequestException;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

	@Mock
	private UserServiceImpl userService;

	@InjectMocks
	private UserResource userResource;

	private UserDTO user0;
	private UserDTO user0_1;
	private UserDTO user0_2;
	private UserDTO user1;

	@Before
	public void setUp() {
		user0 = new UserDTO();
		user0.setName("Anh Vu1");
		user0.setEmail("anhvurz90@yahoo.com");
		user0.setAddress("Nguyen Huu Huan");

		user0_1 = new UserDTO();
		user0_1.setId(1L);
		user0_1.setName(user0.getName());
		user0_1.setEmail(user0.getEmail());
		user0_1.setAddress(user0.getAddress());

		// user0_2 = new UserDTO();
		// user0_2.setId(user0_1.getId());
		// user0_2.setName(user0_1.getName());
		// user0_2.setEmail(user0_1.getEmail());
		// user0_2.setAddress(user0_1.getAddress());
		//
		// user1 = new UserDTO();
		// user1.setName("Anh Vu2");
		// user1.setEmail("anhvurz90@gmail.com");
		// user1.setAddress("Nghi Tam");
	}

	@Test
	public void testCreateUserOk() throws Exception {
		Mockito.when(userService.save(user0)).thenReturn(user0_1);

		ResponseEntity<UserDTO> response = userResource.createUser(user0);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(user0_1, response.getBody());

		verify(userService, times(1)).save(user0);
	}

	@Test(expected = BadRequestException.class)
	public void testCreateUserFailsBecauseIdExists() throws Exception {
		user0.setId(2L);
		userResource.createUser(user0);

		verify(userService, times(0)).save(user0);
	}

	@Test
	public void testUpdateUser() throws Exception {
		user0.setId(1L);
		Mockito.when(userService.save(user0)).thenReturn(user0);
		ResponseEntity<UserDTO> response = userResource.updateUser(user0);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(user0, response.getBody());

		verify(userService, times(1)).save(user0);
	}

	@Test
	public void testFindUserByIdFound() {
		Mockito.when(userService.findById(user0_1.getId())).thenReturn(user0_1);

		ResponseEntity<UserDTO> response = userResource.getUser(user0_1.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());

		UserDTO userResult = response.getBody();

		assertEquals(user0_1.getId(), userResult.getId());
		assertEquals(user0_1.getName(), userResult.getName());
		assertEquals(user0_1.getEmail(), userResult.getEmail());
		assertEquals(user0_1.getAddress(), userResult.getAddress());

		verify(userService, times(1)).findById(1L);
	}

	@Test
	public void testFindUserByIdNotFound() {
		Mockito.when(userService.findById(2L)).thenReturn(null);

		ResponseEntity<UserDTO> response = userResource.getUser(2L);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		verify(userService, times(1)).findById(2L);
	}

	@Test
	public void testFindUserByName_Found() {
		Mockito.when(userService.findByName(user0_1.getName())).thenReturn(user0_1);

		ResponseEntity<UserDTO> response = userResource.getUserByName(user0_1.getName());
		assertEquals(HttpStatus.OK, response.getStatusCode());

		UserDTO userResult = response.getBody();

		assertEquals(user0_1.getId(), userResult.getId());
		assertEquals(user0_1.getName(), userResult.getName());
		assertEquals(user0_1.getEmail(), userResult.getEmail());
		assertEquals(user0_1.getAddress(), userResult.getAddress());

		verify(userService, times(1)).findByName(user0_1.getName());
	}

	@Test
	public void testFindUserByName_NotFound() {
		Mockito.when(userService.findByName(user0_1.getName() + "ABC")).thenReturn(null);

		ResponseEntity<UserDTO> response = userResource.getUserByName(user0_1.getName() + "ABC");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		verify(userService, times(1)).findByName(user0_1.getName() + "ABC");
	}

	@Test
	public void testFindUserByEmail_Found() {
		Mockito.when(userService.findByEmail(user0_1.getEmail())).thenReturn(user0_1);

		ResponseEntity<UserDTO> response = userResource.getUserByEmail(user0_1.getEmail());
		assertEquals(HttpStatus.OK, response.getStatusCode());

		UserDTO userResult = response.getBody();

		assertEquals(user0_1.getId(), userResult.getId());
		assertEquals(user0_1.getName(), userResult.getName());
		assertEquals(user0_1.getEmail(), userResult.getEmail());
		assertEquals(user0_1.getAddress(), userResult.getAddress());

		verify(userService, times(1)).findByEmail(user0_1.getEmail());
	}

	@Test
	public void testFindUserByEmail_NotFound() {
		Mockito.when(userService.findByEmail(user0_1.getEmail() + "ABC")).thenReturn(null);

		ResponseEntity<UserDTO> response = userResource.getUserByEmail(user0_1.getEmail() + "ABC");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		verify(userService, times(1)).findByEmail(user0_1.getEmail() + "ABC");
	}

	@Test
	public void testFindAllUsers() {
		List<UserDTO> users = new ArrayList<>();
		users.add(user0);
		users.add(user1);
		Mockito.when(userService.findAll()).thenReturn(users);

		ResponseEntity<List<UserDTO>> response = userResource.getAllUsers();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());

		verify(userService, times(1)).findAll();
	}

	@Test
	public void testDeleteUser() {
		ResponseEntity<Void> response = userResource.deleteUser(1L);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(userService, times(1)).delete(1L);
	}
}

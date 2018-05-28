package com.anhvurz90.usermanagement.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anhvurz90.usermanagement.UserManagementApplication;
import com.anhvurz90.usermanagement.domain.User;
import com.anhvurz90.usermanagement.repository.UserRepository;
import com.anhvurz90.usermanagement.service.UserService;
import com.anhvurz90.usermanagement.service.dto.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserManagementApplication.class)
@Transactional
public class UserServiceIntTest {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	private User user0;
	private UserDTO user1;

	@Before
	public void setUp() {
		user0 = new User();
		user0.setName("Anh Vu1");
		user0.setEmail("anhvurz90@yahoo.com");
		user0.setAddress("Nguyen Huu Huan");
		user0 = userRepo.save(user0);

		user1 = new UserDTO();
		user1.setName("Anh Vu2");
		user1.setEmail("anhvurz90@gmail.com");
		user1.setAddress("Nghi Tam");
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCreateUser() throws Exception {
		UserDTO userResult = userService.save(user1);

		assertNotNull(userResult);
		assertNotNull(userResult.getId());

		assertEquals(user1.getName(), userResult.getName());
		assertEquals(user1.getEmail(), userResult.getEmail());
		assertEquals(user1.getAddress(), userResult.getAddress());

		assertEquals(2, userRepo.findAll().size());
	}

	@Test(expected = ConstraintViolationException.class)
	public void testCreateUserFails_InvalidEmail() throws Exception {
		UserDTO user = new UserDTO();
		user.setName("Vu");
		user.setEmail("nguyenanhvu@");
		user.setAddress("Tay Ho");
		userService.save(user);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testCreateUserFails_UserNameNotUnique() throws Exception {
		UserDTO user = new UserDTO();
		user.setName("Anh Vu1");
		user.setEmail("nguyenanhvu@facebook.com");
		user.setAddress("Tay Ho");
		userService.save(user);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testCreateUserFails_EmailNotUnique() throws Exception {
		UserDTO user = new UserDTO();
		user.setName("Anh Vu 3");
		user.setEmail("anhvurz90@yahoo.com");
		user.setAddress("Tay Ho");
		userService.save(user);
	}

	@Test
	public void testUpdateUser() throws Exception {
		user1.setId(user0.getId());
		userService.save(user1);

		assertEquals(1, userRepo.findAll().size());
		Optional<User> userResult = userRepo.findById(user0.getId());
		assertTrue(userResult.isPresent());

		assertEquals(user1.getName(), userResult.get().getName());
		assertEquals(user1.getEmail(), userResult.get().getEmail());
		assertEquals(user1.getAddress(), userResult.get().getAddress());
	}

	@Test
	public void testFindUserByIdFound() throws Exception {
		UserDTO userResult = userService.findById(user0.getId());

		assertNotNull(userResult);
		assertEquals(user0.getId(), userResult.getId());
		assertEquals(user0.getName(), userResult.getName());
		assertEquals(user0.getEmail(), userResult.getEmail());
		assertEquals(user0.getAddress(), userResult.getAddress());
	}

	@Test
	public void testFindUserByIdNotFound() throws Exception {
		UserDTO userResult = userService.findById(user0.getId() + 5);
		assertNull(userResult);
	}

	@Test
	public void testFindUserByNameFound() throws Exception {
		UserDTO userResult = userService.findByName(user0.getName());

		assertNotNull(userResult);
		assertEquals(user0.getId(), userResult.getId());
		assertEquals(user0.getName(), userResult.getName());
		assertEquals(user0.getEmail(), userResult.getEmail());
		assertEquals(user0.getAddress(), userResult.getAddress());
	}

	@Test
	public void testFindUserByName_NotFound() throws Exception {
		UserDTO userResult = userService.findByName(user0.getName() + "ABC");
		assertNull(userResult);
	}

	@Test
	public void testFindUserByEmail_Found() throws Exception {
		UserDTO userResult = userService.findByEmail(user0.getEmail());

		assertNotNull(userResult);
		assertEquals(user0.getId(), userResult.getId());
		assertEquals(user0.getName(), userResult.getName());
		assertEquals(user0.getEmail(), userResult.getEmail());
		assertEquals(user0.getAddress(), userResult.getAddress());
	}

	@Test
	public void testFindUserByEmail_NotFound() throws Exception {
		UserDTO userResult = userService.findByEmail(user0.getEmail() + "ABC");
		assertNull(userResult);
	}

	@Test
	public void testFindAllUsers() throws Exception {
		assertEquals(1, userService.findAll().size());
		userService.save(user1);
		assertEquals(2, userService.findAll().size());
	}

	@Test
	public void testDeleteUser() throws Exception {
		assertEquals(1, userRepo.findAll().size());
		userService.delete(user0.getId());
		assertEquals(0, userRepo.findAll().size());
	}
}

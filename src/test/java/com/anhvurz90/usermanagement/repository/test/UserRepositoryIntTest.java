package com.anhvurz90.usermanagement.repository.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anhvurz90.usermanagement.UserManagementApplication;
import com.anhvurz90.usermanagement.domain.User;
import com.anhvurz90.usermanagement.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserManagementApplication.class)
@Transactional
public class UserRepositoryIntTest {

	@Autowired
	private UserRepository userRepo;

	private User user0;
	private User user1;

	@Before
	public void setUp() {
		user0 = new User();
		user0.setName("AnhVu1");
		user0.setEmail("anhvurz90@yahoo.com");
		user0.setAddress("Nguyen Huu Huan");
		user0 = userRepo.save(user0);

		user1 = new User();
		user1.setName("AnhVu2");
		user1.setEmail("anhvurz90@gmail.com");
		user1.setAddress("Nghi Tam");
		user1 = userRepo.save(user1);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testFindUserByName_Found() throws Exception {
		assertTrue(userRepo.findByName("AnhVu1").isPresent());
		assertTrue(userRepo.findByName("AnhVu2").isPresent());
	}

	@Test
	public void testFindUserByName_NotFound() throws Exception {
		assertFalse(userRepo.findByName("AnhVu3").isPresent());
	}

	@Test
	public void testFindUserByEmail_Found() throws Exception {
		assertTrue(userRepo.findByEmail("anhvurz90@yahoo.com").isPresent());
		assertTrue(userRepo.findByEmail("anhvurz90@gmail.com").isPresent());
	}

	@Test
	public void testFindUserByEmail_NotFound() throws Exception {
		assertFalse(userRepo.findByEmail("anhvurz90@facebook.com").isPresent());
	}
}

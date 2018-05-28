package com.anhvurz90.usermanagement.web.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import com.anhvurz90.usermanagement.UserManagementApplication;
import com.anhvurz90.usermanagement.repository.UserRepository;
import com.anhvurz90.usermanagement.service.UserService;
import com.anhvurz90.usermanagement.service.dto.UserDTO;
import com.anhvurz90.usermanagement.web.rest.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserManagementApplication.class)
@Transactional
public class UserResourceIntTest {

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserResource userResource;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	private MockMvc restUserMockMvc;

	private UserDTO user0;
	private UserDTO user1;

	@Before
	public void setUp() {
		user0 = new UserDTO();
		user0.setName("Anh Vu1");
		user0.setEmail("anhvurz90@yahoo");
		user0.setAddress("Nguyen Huu Huan");
		user0 = userService.save(user0);

		user1 = new UserDTO();
		user1.setName("Anh Vu2");
		user1.setEmail("anhvurz90@gmail.com");
		user1.setAddress("Nghi Tam");

		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCreateUserOk() throws Exception {
		restUserMockMvc
				.perform(post("/users").contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(user1)))
				.andExpect(status().isCreated());

		assertEquals(2, userService.findAll().size());
	}

	@Test(expected = NestedServletException.class)
	public void testCreateUserFails_IdExists() throws Exception {
		user1.setId(2L);

		restUserMockMvc
				.perform(post("/users").contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(user1)));
	}

	@Test
	public void testCreateUserFails_EmailInvalid() throws Exception {
		UserDTO user = new UserDTO();
		user.setName("Nguyen Anh Vu");
		user.setEmail("nguyenanhvu@");
		user.setAddress("Tay Ho");

		restUserMockMvc
				.perform(post("/users").contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateUserFails_NameInvalid() throws Exception {
		UserDTO user = new UserDTO();
		user.setName("Vu");// Too short
		user.setEmail("nguyenanhvu@gmail.com");
		user.setAddress("Tay Ho");

		restUserMockMvc
				.perform(post("/users").contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(user)))
				.andExpect(status().isBadRequest());
	}

	@Test(expected = NestedServletException.class)
	public void testCreateUserFails_EmailNotUnique() throws Exception {
		UserDTO user = new UserDTO();
		user.setName("Nguyen Anh Vu");
		user.setEmail(user0.getEmail());
		user.setAddress("Tay Ho");

		restUserMockMvc
				.perform(post("/users").contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(user)));
	}

	@Test(expected = NestedServletException.class)
	public void testCreateUserFails_NameNotUnique() throws Exception {
		UserDTO user = new UserDTO();
		user.setName(user0.getName());
		user.setEmail("anhvu@facebook.com");
		user.setAddress("Tay Ho");

		restUserMockMvc
				.perform(post("/users").contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(user)));
	}

	@Test
	public void testUpdateUser() throws Exception {
		user1.setId(user0.getId());

		restUserMockMvc
				.perform(put("/users").contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(user1)))
				.andExpect(status().isOk());

		assertEquals(1, userService.findAll().size());

		UserDTO userResult = userService.findById(user0.getId());
		assertNotNull(userResult);

		assertEquals(user1.getName(), userResult.getName());
		assertEquals(user1.getEmail(), userResult.getEmail());
		assertEquals(user1.getAddress(), userResult.getAddress());
	}

	@Test
	public void testFindUserByIdFound() throws Exception {
		restUserMockMvc.perform(get("/users/" + user0.getId()).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(convertObjectToJsonString(user0)));
	}

	@Test
	public void testFindUserByIdNotFound() throws Exception {
		restUserMockMvc.perform(get("/users/" + user0.getId() + 10).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindUserByName_Found() throws Exception {
		restUserMockMvc.perform(get("/users/byName/" + user0.getName()).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(convertObjectToJsonString(user0)));
	}

	@Test
	public void testFindUserByName_NotFound() throws Exception {
		restUserMockMvc.perform(get("/users/byName/" + user0.getName() + "ABC").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindUserByEmail_Found() throws Exception {
		restUserMockMvc.perform(get("/users/byEmail/" + user0.getEmail()).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(convertObjectToJsonString(user0)));
	}

	@Test
	public void testFindUserByEmail_NotFound() throws Exception {
		restUserMockMvc.perform(get("/users/byEmail/" + user0.getEmail() + "ABC").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindAllUsers() throws Exception {
		List<UserDTO> users = new ArrayList<>();
		users.add(user0);

		restUserMockMvc.perform(get("/users").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(convertObjectToJsonString(users)));

		UserDTO userResult = userService.save(user1);

		users.add(userResult);
		restUserMockMvc.perform(get("/users").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(convertObjectToJsonString(users)));
	}

	@Test
	public void testDeleteUser() throws Exception {
		List<UserDTO> users = userService.findAll();
		assertEquals(1, users.size());

		restUserMockMvc.perform(delete("/users/" + user0.getId()).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		users = userService.findAll();
		assertEquals(0, users.size());
	}

	public byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsBytes(object);
	}

	public String convertObjectToJsonString(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

}

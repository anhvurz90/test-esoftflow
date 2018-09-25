package com.anhvurz90.usermanagement.web.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anhvurz90.usermanagement.service.UserService;
import com.anhvurz90.usermanagement.service.dto.UserDTO;
import com.anhvurz90.usermanagement.web.rest.error.BadRequestException;

@RestController
@RequestMapping("/users")
public class UserResource {

	private final Logger log = LoggerFactory.getLogger(UserResource.class);

	private static final String ENTITY_NAME = "user";

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
		log.debug("Rest request to create User: {}", userDTO);
		if (userDTO.getId() != null) {
			throw new BadRequestException("A new user cannot aleady have an ID");
		}

		UserDTO result = userService.save(userDTO);
		return ResponseEntity.created(new URI("/users" + result.getId())).body(result);
	}

	@PutMapping
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
		log.debug("Rest request to update User: {}", userDTO);
		if (userDTO.getId() == null) {
			return createUser(userDTO);
		}
		UserDTO result = userService.save(userDTO);
		return ResponseEntity.ok(result);
	}

	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		log.debug("Rest request to get Users");
		List<UserDTO> userPage = userService.findAll();
		return ResponseEntity.ok(userPage);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
		log.debug("Rest request to get User: {}", id);
		UserDTO user = userService.findById(id);

		return user == null ? ResponseEntity.notFound().build() : 
							  ResponseEntity.ok(user);
	}
	
	@GetMapping("/byName/{name}")
	public ResponseEntity<UserDTO> getUserByName(@PathVariable String name) {
		log.debug("Rest request to get User by name: {}", name);
		UserDTO user = userService.findByName(name);
		
		return user == null ? ResponseEntity.notFound().build() :
							  ResponseEntity.ok(user);
	}
	
	@GetMapping("/byEmail/{email}")
	public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
		log.debug("Rest request to get User by email: {}", email);
		UserDTO user = userService.findByEmail(email);

		return user == null ? ResponseEntity.notFound().build()	:
							  ResponseEntity.ok(user);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		log.debug("Rest request to remove User: {}", id);
		userService.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/getFile/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName) {
      log.debug("Rest request to get file: {}", fileName);

      InputStream in = getClass()
          .getResourceAsStream("/" + fileName);
      try {
        return ResponseEntity.ok()
                             .header("Access-Control-Allow-Headers","Content-Type")
                             .header("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                             .header("Access-Control-Allow-Origin","*")
                             .contentType(MediaType.IMAGE_PNG)
                             .body(IOUtils.toByteArray(in));
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }

  }
	
}

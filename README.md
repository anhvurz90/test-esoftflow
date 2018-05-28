# Restful Web Service: 
- com.anhvurz90.usermanagement.web.rest.UserResource.java

# Programming Language & Frameworks: 
- Java, Spring, Maven

# Test project:
- mvn clean test

# Run project:
- mvn spring-boot:run

# The APIs were tested:
- Manually via Postman
- Automatically via Unitest:
	* UserServiceImplTest.java
	* UserResourceTest.java
- Automatically via Integration test:
	* UserRepositoryIntTest.java
	* UserServiceIntTest.java
	* UserResourceIntTest.java
	
# Code improvement if I have more time:
- Create Java doc for classes & methods.
- User lombok to avoid getter, setter, toString, hashCode boilerplate code
- Add more meaningful error messages for field validation
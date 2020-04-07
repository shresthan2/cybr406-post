package com.cybr406.post;

import com.cybr406.post.security.user.AccountAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.cybr406.post.util.TestUtil.assertClassDeclaresMethod;
import static com.cybr406.post.util.TestUtil.assertMethodAnnotationIsPresent;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostHomework01Tests {

	static MockWebServer mockWebServer;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@BeforeAll
	static void setup() throws Exception {
		mockWebServer = new MockWebServer();
		mockWebServer.start(8888);
	}

	@AfterAll
	static void tearDown() throws Exception {
		mockWebServer.shutdown();
	}

	// Update SecurityConfiguration to use Account as its authentication provider
	@Test
	public void problem_01_configureNewAuthenticationProvider() throws Exception {
		Method method = assertClassDeclaresMethod(
				"Create a method called configureGlobal with args AuthenticationManagerBuilder, AccountAuthenticationProvider",
				"com.cybr406.post.configuration.SecurityConfiguration",
				"configureGlobal",
				AuthenticationManagerBuilder.class,
				AccountAuthenticationProvider.class);
		assertMethodAnnotationIsPresent(
				"configureGlobal must be annotated with @Autowired so that its method arguments can be supplied with values",
				"com.cybr406.post.configuration.SecurityConfiguration",
				Autowired.class,
				"configureGlobal",
				AuthenticationManagerBuilder.class,
				AccountAuthenticationProvider.class);
	}

	@Test
	public void problem_02_goodUserCredentialsCanCreatePost() throws Exception {
		mockWebServer.enqueue(new MockResponse()
				.setBody("[ \"ROLE_USER\" ]")
				.addHeader("content-type", "application/json")
				.setResponseCode(200));

		// Simulate a user with a good username and password trying ot create a new post.
		Map<String, Object> post = new HashMap<>();
		post.put("text", "Test Post");
		mockMvc.perform(post("/posts")
				.with(httpBasic("user", "user"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(post)))
				.andExpect(status().isCreated());
	}

	@Test
	public void problem_03_badUserCredentialsCannotCreatePost() throws Exception {
		mockWebServer.enqueue(new MockResponse()
				.setResponseCode(400));

		// Simulate a user with a bad username and password trying ot create a new post.
		Map<String, Object> post = new HashMap<>();
		post.put("text", "Test Post");
		mockMvc.perform(post("/posts")
				.with(httpBasic("bad_user", "bad_user"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(post)))
				.andExpect(status().isUnauthorized());
	}

}

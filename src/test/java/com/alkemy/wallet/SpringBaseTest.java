package com.alkemy.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class SpringBaseTest {

    public static final String JSON_REQUEST_FILE_SUFFIX = "-request.json";
    public static final String JSON_RESPONSE_FILE_SUFFIX = "-response.json";
    private static final String DATA_PATH = "data/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @SneakyThrows
    protected void executePost(String url, String folder, String requestFile, String responseFile, int statusCode) {
        mvc.perform(post(url)
                .with(user("foo@foo.com").password("pass123").roles("ADMIN"))
                .contentType(APPLICATION_JSON)
                .content(postBodyRequest(getInputFile(folder, requestFile)))
                .accept(APPLICATION_JSON))
        .andExpect(status().is(statusCode));
    }

    private String getInputFile(String folder, String file) {
        return DATA_PATH + folder + "/input/" + file + JSON_REQUEST_FILE_SUFFIX;
    }

    private String getOutputFile(String folder, String file) {
        return DATA_PATH + folder + "/output/" + file + JSON_RESPONSE_FILE_SUFFIX;
    }

    private String postBodyRequest(String fileName) {
        return Optional.ofNullable(fileName)
                .map(this::getResourceAsString)
                .orElse(StringUtils.EMPTY);
    }

    @SneakyThrows
    private String getResourceAsString(String fileName) {
        return resourceToString(fileName, UTF_8, Thread.currentThread().getContextClassLoader());
    }

}

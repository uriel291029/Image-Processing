package com.dojo.challenge.imageprocessingservice.controller;

import com.dojo.challenge.imageprocessingservice.dto.SearchProcessResponseDTO;
import com.dojo.challenge.imageprocessingservice.service.ProcessImageRequestService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcessImageController.class)
public class ProcessImageControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessImageRequestService processImageService;

    @Test
    public void testProcessEndpoint()
            throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.png", "image/png", "some image".getBytes());
        String guid = UUID.randomUUID().toString();

        given(processImageService.processImageRequest(Mockito.any(),
                Mockito.any())).willReturn(guid);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", "Image Title");
        map.add("description", "Image Description");
        map.add("globantEmails", "alanperez@globant.com");
        map.add("folderName", "Image Folder");
        mockMvc.perform(multipart("/image/process")
                        .file("image", firstFile.getBytes())
                        .queryParams(map))
                .andExpect(status().isCreated())
                .andExpect(content().string(guid));
    }
    
    @Test
    public void testSearchendpoint() throws Exception{
    	List<SearchProcessResponseDTO> list = new ArrayList<SearchProcessResponseDTO>();
    	SearchProcessResponseDTO element = new SearchProcessResponseDTO("status", LocalDateTime.now(), LocalDateTime.now());
    	list.add(element);
    	given(processImageService.searchProcess(Mockito.any(), Mockito.any())).willReturn(list);
    	MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    	map.add("startDate", "2023-03-21");
    	map.add("endDate", "2023-03-22");
    	mockMvc.perform(get("/image/search").params(map)).andExpect(status().isOk());
    }
    	
    @Test
    public void testStatusEndpoint() throws Exception {
        String guid = "123abc";
        String status = "anyStatus";

        given(processImageService.getImageRequestStatus(Mockito.any())).willReturn(status);

        mockMvc.perform(request(HttpMethod.GET, "/image/status/" + guid))
                .andExpect(status().isOk())
                .andExpect(content().string(status));
    }
}

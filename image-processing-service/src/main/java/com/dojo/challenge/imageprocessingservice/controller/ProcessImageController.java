package com.dojo.challenge.imageprocessingservice.controller;

import com.dojo.challenge.imageprocessingservice.dto.SearchProcessResponseDTO;
import com.dojo.challenge.imageprocessingservice.dto.request.ProcessImageRequestDTO;
import com.dojo.challenge.imageprocessingservice.service.ProcessImageRequestService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("image")
@RequiredArgsConstructor
public class ProcessImageController {

	private final ProcessImageRequestService processImageService;

    @Operation(summary = "Process Image Request - Process the image to upload the server and send it by email " +
            "and will return synchronously a RequestId (guid)")
    @PostMapping(value = "process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<String> processImageRequest(@Valid ProcessImageRequestDTO requestInformation,
                                                       MultipartFile image) {
        log.info("Starting the process image request.");
        String guid = processImageService.processImageRequest(requestInformation, image);
        return new ResponseEntity<>(guid, HttpStatus.CREATED);
    }

    @Operation(summary = "Process Image Request Status- This endpoint will receive as input parameter a RequestId (guid) and will return a status (string) that can be:\n" +
            "InProgress: a request has been received but the full process (stage 2 and 3 has not been completed)\n" +
            "Completed: the process defined in stage 2 and 3 has been completed successfully.\n" +
            "Failed: some kind of error or exception has occurred\n")
    @GetMapping("/search")
	public ResponseEntity<List<SearchProcessResponseDTO>> searchProcessImageRequest (@RequestParam String startDate, 
			@RequestParam String endDate) {
		return new ResponseEntity<>(processImageService.searchProcess(startDate, endDate), HttpStatus.OK);
	}

    @Operation(summary = "Search Process Image Requests - This endpoint will receive as input parameters:\n" +
            "Begin Date (date time)\n" +
            "End Date (date time)\n" +
            "And will return a list of all the Process Image Request received during that datetime range, including: its status, " +
            "date time of when the processing started and date time of when the processing ended (either as completed or failed).\n")
    @GetMapping("status/{guid}")
    private ResponseEntity<String> getImageRequestStatus(@PathVariable("guid") String guid) {
        String status = processImageService.getImageRequestStatus(guid);
        return ResponseEntity.ok(status);
    }
}

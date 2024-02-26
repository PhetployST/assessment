package com.kbtg.bootcamp.posttest.lotteryTest;

import com.kbtg.bootcamp.posttest.exception.InternalServiceException;
import com.kbtg.bootcamp.posttest.lottery.LotteryController;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryListResponseDto;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryRequestDto;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class LotteryControllerTest {

    @Mock
    private LotteryService lotteryService;

    @InjectMocks
    private LotteryController lotteryController;

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    @Test
    @DisplayName("Test getting lottery list successfully should return expected response")
    void testGetLotteryList_Success() {
        // Mocking the response from lotteryService
        LotteryListResponseDto mockResponse = new LotteryListResponseDto();
        when(lotteryService.getAllLotteries()).thenReturn(mockResponse);

        // Call the controller method
        ResponseEntity<LotteryListResponseDto> responseEntity = lotteryController.getLotteryList();

        // Verify that lotteryService.getAllLotteries() is called once
        verify(lotteryService, times(1)).getAllLotteries();

        // Verify the response entity status code is OK
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Verify the response entity body matches the mock response
        assertEquals(mockResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test getting lottery list when exception occurs should throw InternalServiceException")
    void testGetLotteryList_Exception() {
        // Mocking an exception from lotteryService
        when(lotteryService.getAllLotteries()).thenThrow(new RuntimeException("Test Exception"));

        // Call the controller method and expect it to throw an InternalServiceException
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                InternalServiceException.class,
                () -> lotteryController.getLotteryList()
        );

        // Verify that lotteryService.getAllLotteries() is called once
        verify(lotteryService, times(1)).getAllLotteries();

        // Verify the exception message
        assertEquals("Internal service exception with Normal service", exception.getMessage());
    }

    @Test
    @DisplayName("Test creating lottery successfully should return CREATED status and expected response")
    void testCreateLottery_Success() {
        // Mocking the requestDto
        LotteryRequestDto requestDto = new LotteryRequestDto();
        requestDto.setTicket("123456");
        requestDto.setAmount(10);
        requestDto.setPrice(100);

        // Mocking the response from lotteryService
        LotteryResponseDto mockResponse = new LotteryResponseDto("123456");
        when(lotteryService.createLottery(requestDto)).thenReturn(mockResponse);

        // Call the controller method
        ResponseEntity<LotteryResponseDto> responseEntity = lotteryController.createLottery(requestDto);

        // Verify that lotteryService.createLottery() is called once
        verify(lotteryService, times(1)).createLottery(requestDto);

        // Verify the response entity status code is CREATED
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        // Verify the response entity body matches the mock response
        assertEquals(mockResponse, responseEntity.getBody());
    }


}

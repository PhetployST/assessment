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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("getting lottery list successfully should return status 200 OK")
    void testGetLotteryList_Success() {

        LotteryListResponseDto mockResponse = new LotteryListResponseDto();
        when(lotteryService.getAllLotteries()).thenReturn(mockResponse);

        ResponseEntity<LotteryListResponseDto> responseEntity = lotteryController.getLotteryList();
        verify(lotteryService, times(1)).getAllLotteries();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test getting lottery list when exception occurs should throw Internal service exception")
    void testGetLotteryList_Exception() {

        when(lotteryService.getAllLotteries()).thenThrow(new RuntimeException("Test Exception"));

        Exception exception = assertThrows(
                InternalServiceException.class,
                () -> lotteryController.getLotteryList()
        );

        verify(lotteryService, times(1)).getAllLotteries();
        assertEquals("Error occurred while retrieving lottery list: Internal service error", exception.getMessage());
    }

    @Test
    @DisplayName("Create lottery successful should return status 200 OK and response body 123456")
    void testCreateLottery_Success() {

        LotteryRequestDto requestDto = new LotteryRequestDto();
        requestDto.setTicket("123456");
        requestDto.setPrice(90.0);
        requestDto.setAmount(1.0);

        LotteryResponseDto responseDto = new LotteryResponseDto();
        responseDto.setTicket("123456");

        when(lotteryService.createLottery(requestDto)).thenReturn(responseDto);

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<LotteryResponseDto> responseEntity = lotteryController.createLottery(requestDto, bindingResult);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(responseDto, responseEntity.getBody());
    }

    @Test
    @DisplayName("create lottery internal service exception should return An unexpected error occurred while processing your request")
    public void testCreateLottery_InternalServiceException() {

        doThrow(new RuntimeException()).when(lotteryService).createLottery(any(LotteryRequestDto.class));

        LotteryRequestDto requestDto = new LotteryRequestDto();
        requestDto.setTicket("123456");
        requestDto.setPrice(90.0);
        requestDto.setAmount(1.0);

        InternalServiceException exception = assertThrows(InternalServiceException.class, () -> lotteryController.createLottery(requestDto, mock(BindingResult.class)));
        assertEquals("An unexpected error occurred while processing your request", exception.getMessage());
    }

    @Test
    @DisplayName("")
    public void testCreateLottery_ValidationException() {
        // create test case validation exception >>> bad request
        // wait for me
    }

}

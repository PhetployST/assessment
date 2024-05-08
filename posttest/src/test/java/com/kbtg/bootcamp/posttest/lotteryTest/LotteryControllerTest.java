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

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                InternalServiceException.class,
                () -> lotteryController.getLotteryList()
        );

        verify(lotteryService, times(1)).getAllLotteries();
        assertEquals("Error occurred while retrieving lottery list: Internal service error", exception.getMessage());
    }

//    @Test
//    @DisplayName("Test creating lottery successfully should return CREATED status and expected response")
//    void testCreateLottery_Success() {
//
//        LotteryRequestDto requestDto = new LotteryRequestDto();
//        LotteryResponseDto responseDto = new LotteryResponseDto();
//
//        Mockito.when(lotteryService.createLottery(requestDto)).thenReturn(responseDto);
//        ResponseEntity<LotteryResponseDto> responseEntity = lotteryController.createLottery(requestDto);
//
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals(responseDto, responseEntity.getBody());
//    }

//    @Test
//    @DisplayName("Test creating lottery when exception occurs should throw Internal service exception")
//    public void testCreateLottery_Exception() {
//
//        LotteryRequestDto requestDto = new LotteryRequestDto();
//
//        Mockito.when(lotteryService.createLottery(requestDto)).thenThrow(new RuntimeException());
//
//        try {
//            lotteryController.createLottery(requestDto);
//            assert false;
//        } catch (InternalServiceException e) {
//            assertEquals("Internal service exception with Normal service", e.getMessage());
//        }
//    }
}

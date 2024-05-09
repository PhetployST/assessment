package com.kbtg.bootcamp.posttest.userTest;
import com.kbtg.bootcamp.posttest.exception.InternalServiceException;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryResponseDto;
import com.kbtg.bootcamp.posttest.user.UserController;
import com.kbtg.bootcamp.posttest.user.UserService;
import com.kbtg.bootcamp.posttest.user.dto.UserTicketResponseDto;
import com.kbtg.bootcamp.posttest.user.dto.UserTicketsRequestDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

    }

    @Test
    @DisplayName("Test buying lotteries controller successfully should return expected response")
    void testBuyLotteriesController_Success() {
        String userId = "1234567890";
        String lotteries = "123456";

        UserTicketsRequestDto mockResponse = new UserTicketsRequestDto();
        when(userService.buyLotteryTicket(userId, lotteries)).thenReturn(mockResponse);

        UserTicketsRequestDto response = userController.buyLotteries(userId, lotteries);

        verify(userService, times(1)).buyLotteryTicket(userId, lotteries);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Test buying lotteries controller with Internal Service Exception")
    public void testBuyLotteriesController_InternalServiceException() {

        String userId = "1234567890";
        String lotteries = "123456";

        when(userService.buyLotteryTicket(anyString(), anyString())).thenThrow(new RuntimeException("Test exception"));

        assertThrows(InternalServiceException.class, () -> {
            userController.buyLotteries(userId, lotteries);
        });

        verify(userService).buyLotteryTicket(userId, lotteries);
    }

    @Test
    @DisplayName("Test get user lotteries controller successfully should return expected response")
    public void testGetAllLotteryController_Success() {

        String userId = "1234567890";
        List<String> expectedTickets = Arrays.asList("111111", "222222", "333333");
        double expectedCount = 3.0;
        double expectedTotalPrice = 240.0;

        UserTicketResponseDto expectedResponse = new UserTicketResponseDto();
        expectedResponse.setTickets(expectedTickets);
        expectedResponse.setCount(expectedCount);
        expectedResponse.setTotalPrice(expectedTotalPrice);

        when(userService.getUserLottery(userId)).thenReturn(expectedResponse);

        UserTicketResponseDto actualResponse = userController.getAllLottery(userId);

        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).getUserLottery(userId);
    }

    @Test
    @DisplayName("Test get user lotteries controller with exception")
    public void testGetAllLotteryController_Exception() {

        String userId = "1234567890";
        when(userService.getUserLottery(userId)).thenThrow(new RuntimeException("test exception"));

        assertThrows(InternalServiceException.class, () -> userController.getAllLottery(userId));
        verify(userService, times(1)).getUserLottery(userId);
    }

    @Test
    @DisplayName("Test delete lotteries controller successfully should return expected response")
    public void testDeleteLotteries_Success() {

        String userId = "1234567890";
        String ticket = "123456";
        String expectedMessage = "Lottery ticket deleted successfully.";
        LotteryResponseDto expectedResponse = new LotteryResponseDto(expectedMessage);

        when(userService.deleteLottery(userId, ticket)).thenReturn(expectedResponse);

        LotteryResponseDto actualResponse = userController.deleteLotteries(userId, ticket);

        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).deleteLottery(userId, ticket);
    }

    @Test
    @DisplayName("Test delete lotteries controller with exception")
    public void testDeleteLotteries_Exception() {

        String userId = "1234567890";
        String ticket = "123456";
        when(userService.deleteLottery(userId, ticket)).thenThrow(new RuntimeException("Simulated exception"));

        assertThrows(InternalServiceException.class, () -> userController.deleteLotteries(userId, ticket));
        verify(userService, times(1)).deleteLottery(userId, ticket);
    }

}

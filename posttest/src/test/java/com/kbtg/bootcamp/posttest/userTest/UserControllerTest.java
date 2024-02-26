package com.kbtg.bootcamp.posttest.userTest;
import com.kbtg.bootcamp.posttest.user.UserController;
import com.kbtg.bootcamp.posttest.user.UserService;
import com.kbtg.bootcamp.posttest.user.dto.UserTicketsRequestDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private UserService userService;

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
    @DisplayName("Test buying lotteries successfully should return expected response")
    void testBuyLotteries_Success() {
        String userId = "1234567890";
        String lotteries = "123456";

        // Mocking the response from userService
        UserTicketsRequestDto mockResponse = new UserTicketsRequestDto();
        when(userService.buyLotteryTicket(userId, lotteries)).thenReturn(mockResponse);

        // Call the controller method
        UserTicketsRequestDto response = userController.buyLotteries(userId, lotteries);

        // Verify that userService.buyLotteryTicket() is called once
        verify(userService, times(1)).buyLotteryTicket(userId, lotteries);

        // Verify the response object is not null
        assertNotNull(response);
    }

}

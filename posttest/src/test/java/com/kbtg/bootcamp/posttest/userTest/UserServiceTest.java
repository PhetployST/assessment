package com.kbtg.bootcamp.posttest.userTest;

import com.kbtg.bootcamp.posttest.exception.InternalServiceException;
import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryResponseDto;
import com.kbtg.bootcamp.posttest.user.UserService;
import com.kbtg.bootcamp.posttest.user.UserTicket;
import com.kbtg.bootcamp.posttest.user.UserTicketRepository;
import com.kbtg.bootcamp.posttest.user.dto.UserTicketResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private LotteryRepository lotteryRepository;

    @Mock
    private UserTicketRepository userTicketRepository;

    @InjectMocks
    private UserService userService;

    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // buy ticket test
    @Test
    @DisplayName("Test buying lottery ticket service for a user should save lottery purchase and user ticket association")
    public void testBuyLotteries_Success() {
        // Mock dependencies
        LotteryRepository lotteryRepository = mock(LotteryRepository.class);
        UserTicketRepository userTicketRepository = mock(UserTicketRepository.class);

        // Prepare test data
        String userId = "1234567890";
        String ticket = "123456";
        double amount = 1.0;
        double price = 80.0;

        Lottery lottery = new Lottery(1,ticket, amount, price);

        // Stubbing repository methods
        when(lotteryRepository.findByTicket(ticket)).thenReturn(Optional.of(lottery));

        // Invoke the service method
        UserService userService = new UserService(lotteryRepository, userTicketRepository);
        userService.buyLotteryTicket(userId, ticket);

        // Verify interactions and state changes
        ArgumentCaptor<Lottery> lotteryCaptor = ArgumentCaptor.forClass(Lottery.class);
        verify(lotteryRepository).save(lotteryCaptor.capture());

        assertEquals(amount - 1, lotteryCaptor.getValue().getAmount());

        ArgumentCaptor<UserTicket> userTicketCaptor = ArgumentCaptor.forClass(UserTicket.class);
        verify(userTicketRepository).save(userTicketCaptor.capture());

        assertEquals(userId, userTicketCaptor.getValue().getUserId());
        assertEquals(ticket, userTicketCaptor.getValue().getTicket());
        assertEquals(price, userTicketCaptor.getValue().getPrice());
    }

    @Test
    @DisplayName("Test buying lottery ticket null should return NotFoundException")
    public void testBuyLotteries_NullLottery() {
        // Mock dependencies
        LotteryRepository lotteryRepository = mock(LotteryRepository.class);
        UserTicketRepository userTicketRepository = mock(UserTicketRepository.class);

        // Prepare test data
        String userId = "1234567890";
        String lotteryId = "123456";

        // Stubbing repository methods to return null
        when(lotteryRepository.findByTicket(lotteryId)).thenReturn(Optional.empty());

        // Invoke the service method and assert that NotFoundException is thrown
        UserService userService = new UserService(lotteryRepository, userTicketRepository);
        assertThrows(NotFoundException.class, () -> userService.buyLotteryTicket(userId, lotteryId));

        // Ensure that no interaction with repositories occurred
        verifyNoInteractions(userTicketRepository);
    }

    @Test
    @DisplayName("Test buying lottery ticket with database exception should return InternalServiceException")
    public void testBuyLotteryTicket_DatabaseAccessException() {
        // Mock dependencies
        LotteryRepository lotteryRepository = mock(LotteryRepository.class);
        UserTicketRepository userTicketRepository = mock(UserTicketRepository.class);

        String userId = "1234567890";
        String ticket = "123456";
        double amount = 1.0;
        double price = 80.0;

        Lottery lottery = new Lottery(1,ticket, amount, price);

        // Stubbing repository methods
        when(lotteryRepository.findByTicket(ticket)).thenReturn(Optional.of(lottery));
        doThrow(RuntimeException.class).when(lotteryRepository).save(any());

        // Invoke the service method and assert that InternalServiceException is thrown
        UserService userService = new UserService(lotteryRepository, userTicketRepository);
        assertThrows(InternalServiceException.class, () -> userService.buyLotteryTicket(userId, ticket));

        // Verify interactions
        verify(lotteryRepository, times(1)).findByTicket(ticket);
        verify(userTicketRepository, never()).save(any());
    }

    // test get all user lottery
    @Test
    @DisplayName("Test getting all lottery tickets for a user should return correct ticket count, total price, and ticket numbers")
    public void testGetAllUserTicketService() {

        String userId = "1111111111";
        List<UserTicket> mockUserTickets = new ArrayList<>();
        mockUserTickets.add(new UserTicket(11,"1111111111", null, "123456", 1, 80));
        mockUserTickets.add(new UserTicket(12,"1111111111", null, "123457", 1, 80));
        when(userTicketRepository.findByUserId(userId)).thenReturn(mockUserTickets);

        UserTicketResponseDto responseDto = userService.getUserLottery(userId);

        assertNotNull(responseDto);
        assertEquals(2, responseDto.getCount());
        assertEquals(160, responseDto.getTotalPrice());
        assertTrue(responseDto.getTickets().contains("123456"));
        assertTrue(responseDto.getTickets().contains("123457"));
    }

    @Test
    @DisplayName("Test getting all lottery tickets for a user no have ticket should return NotFoundException")
    public void testGetUserLottery_noTicketsFound() {
        // Mocking data
        String userId = "2222222222";
        List<UserTicket> userTickets = new ArrayList<>(); // Empty list

        // Mocking repository behavior
        when(userTicketRepository.findByUserId(userId)).thenReturn(userTickets);

        // Asserting NotFoundException
        assertThrows(NotFoundException.class, () -> {
            userService.getUserLottery(userId);
        });
    }

    // test delete lottery ticket
    @Test
    @DisplayName("Test deleting lottery ticket for a user should return deleted ticket details and update lotteries information")
    void testDeleteLotteryService() {
        String userId = "123456";
        String ticket = "1234567890";
        UserTicket userTicket = new UserTicket();
        userTicket.setUserId(userId);
        userTicket.setTicket(ticket);
        userTicket.setAmount(1);
        userTicket.setPrice(80);

        when(userTicketRepository.findByUserIdAndTicket(userId, ticket)).thenReturn(userTicket);

        LotteryResponseDto result = userService.deleteLottery(userId, ticket);

        assertNotNull(result);
        assertEquals(ticket, result.getTicket());

        verify(userTicketRepository, times(1)).delete(userTicket);

        verify(lotteryRepository, times(1)).save(argThat(lottery ->
                lottery.getTicket().equals(ticket) &&
                        lottery.getAmount() == userTicket.getAmount() &&
                        lottery.getPrice() == userTicket.getPrice()));
    }

    @Test
    @DisplayName("Test deleting lottery ticket for a user not found ticket or user should return NotFoundException")
    public void testDeleteLottery_NotFound() {
        // Mocking data
        String userId = "1234567890";
        String ticket = "123456";

        when(userTicketRepository.findByUserIdAndTicket(userId, ticket)).thenReturn(null);

        // Invoke the service method and assert that it throws NotFoundException
        assertThrows(NotFoundException.class, () -> userService.deleteLottery(userId, ticket));

        // Verify that the userTicket deletion is not invoked
        verify(userTicketRepository, never()).delete(any());
    }

}

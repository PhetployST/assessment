package com.kbtg.bootcamp.posttest.lotteryTest;

import com.kbtg.bootcamp.posttest.exception.InternalServiceException;
import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class LotteryServiceTest {

    @Mock
    private LotteryRepository lotteryRepository;

    @InjectMocks
    private LotteryService lotteryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("get all lotteries success should return list of ticket with an amount greater than or equal to 1")
    void testGetAllLotteriesService() {

        List<Lottery> mockLotteries = Arrays.asList(
                new Lottery(1, "123222", 1.0, 80),
                new Lottery(2, "456333", 1.0, 80),
                new Lottery(3, "789444", 1.0, 80)
        );

        when(lotteryRepository.findAll()).thenReturn(mockLotteries);

        LotteryListResponseDto response = lotteryService.getAllLotteries();

        List<String> expectedTickets = mockLotteries.stream()
                .filter(lottery -> lottery.getAmount() >= 1)
                .map(Lottery::getTicket)
                .collect(Collectors.toList());

        assertEquals(expectedTickets, response.getTickets());
    }

    @Test
    @DisplayName("get all lotteries exception should return There are no lottery tickets in the system")
    void testGetAllLotteriesServiceException() {

        LotteryRepository lotteryRepository = mock(LotteryRepository.class);
        LotteryService lotteryService = new LotteryService(lotteryRepository);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            lotteryService.getAllLotteries();
        });

        assertEquals("There are no lottery tickets in the system.", exception.getMessage());
    }

    @Test
    @DisplayName("Test successful creation of a lottery should return correct ticket")
    public void testCreateLotteryService_Success() {

        LotteryRequestDto requestDto = new LotteryRequestDto();
        requestDto.setTicket("123456");
        requestDto.setAmount(1.0);
        requestDto.setPrice(80.0);

        Lottery lottery = new Lottery();
        lottery.setTicket(requestDto.getTicket());
        lottery.setAmount(requestDto.getAmount());
        lottery.setPrice(requestDto.getPrice());

        when(lotteryRepository.save(any(Lottery.class))).thenReturn(lottery);

        LotteryResponseDto responseDto = lotteryService.createLottery(requestDto);

        assertEquals(requestDto.getTicket(), responseDto.getTicket());
    }

    @Test
    @DisplayName("create lotteries exception should return Failed to save lottery")
    public void testCreateLotteryService_Failure() {

        LotteryRequestDto requestDto = new LotteryRequestDto();
        requestDto.setTicket("123456");
        requestDto.setAmount(1.0);
        requestDto.setPrice(80.0);

        when(lotteryRepository.save(any(Lottery.class))).thenThrow(new RuntimeException());

        InternalServiceException exception = assertThrows(InternalServiceException.class, () -> lotteryService.createLottery(requestDto));
        assertEquals("Failed to save lottery", exception.getMessage());
    }

}

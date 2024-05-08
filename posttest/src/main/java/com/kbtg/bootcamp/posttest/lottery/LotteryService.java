package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.exception.InternalServiceException;
import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryListResponseDto;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryRequestDto;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryService {

    private final LotteryRepository lotteryRepository;

    public LotteryService(LotteryRepository lotteryRepository) {
        this.lotteryRepository = lotteryRepository;
    }

    public LotteryListResponseDto getAllLotteries() {
        List<String> tickets = lotteryRepository.findAll()
                .stream()
                .filter(lottery -> lottery.getAmount() >= 1)
                .map(Lottery::getTicket)
                .toList();

        if (tickets.isEmpty()) {
            throw new NotFoundException("There are no lottery tickets in the system.");
        }

        return new LotteryListResponseDto(tickets);
    }

    public LotteryResponseDto createLottery (LotteryRequestDto requestDto) {

        Lottery lottery = new Lottery();
        lottery.setTicket(requestDto.getTicket());
        lottery.setAmount(requestDto.getAmount());
        lottery.setPrice(requestDto.getPrice());

        try {
            lotteryRepository.save(lottery);
        } catch (Exception e) {
            throw new InternalServiceException("Failed to save lottery");
        }

        String savedTicket = lottery.getTicket();

        return new LotteryResponseDto(savedTicket);
    }
}

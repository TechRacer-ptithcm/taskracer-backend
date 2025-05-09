package ptithcm.itmc.taskracer.service.processor.internal.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.repository.JpaContributionRepository;
import ptithcm.itmc.taskracer.service.dto.dashboard.ContributionDto;
import ptithcm.itmc.taskracer.service.dto.user.UserDto;
import ptithcm.itmc.taskracer.service.mapper.dashboard.ContributionMapper;
import ptithcm.itmc.taskracer.service.processor.IContributionProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j(topic = "SERVICE-CONTRIBUTION-PROCESSOR")
@Component
@RequiredArgsConstructor
public class DefaultContributionProcessor implements IContributionProcessor {
    private final JpaContributionRepository contributionRepository;
    private final ContributionMapper mapper;
    private final Integer SECOND = 60;

    @Override
    public void handle(UUID userId, Long from, Long to) {
        Integer getMinute = Math.toIntExact(((to - from) / SECOND));
        log.info("MINUTE: {}", getMinute);
        log.info("from {} - to {}", from, to);
        LocalDateTime date = LocalDate.now().atStartOfDay();
        log.info("current time: {}", date);
        var getData = contributionRepository.findByDate(date);
        if (getData.isEmpty()) {
            var formatData = mapper.toJpa(ContributionDto.builder()
                    .date(date)
                    .minute(getMinute.intValue())
                    .user(UserDto.builder()
                            .id(userId)
                            .build())
                    .build());
            contributionRepository.save(formatData);
        } else {
            var data = getData.get();
            data.setMinute(data.getMinute() + getMinute.intValue());
            contributionRepository.save(data);
        }

    }
}

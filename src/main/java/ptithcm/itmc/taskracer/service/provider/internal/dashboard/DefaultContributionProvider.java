package ptithcm.itmc.taskracer.service.provider.internal.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.repository.JpaContributionRepository;
import ptithcm.itmc.taskracer.service.dto.dashboard.ContributionDto;
import ptithcm.itmc.taskracer.service.mapper.dashboard.ContributionMapper;
import ptithcm.itmc.taskracer.service.provider.IContributionProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-CONTRIBUTION-PROVIDER")
public class DefaultContributionProvider implements IContributionProvider {
    private final JpaContributionRepository contributionRepository;
    private final ContributionMapper contributionMapper;

    @Override
    public List<ContributionDto> getAll(UUID userId) {
        LocalDate today = LocalDate.now();
        LocalDate fromDate = today.minusYears(1);
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = today.atTime(LocalTime.MAX);
        log.info("from to: {} - {}", from, to);
        var data = contributionRepository.findAllByUserIdAndDateBetween(userId, from, to);
        var formatData = contributionMapper.toDto(data);

        Map<LocalDate, Integer> minuteMap = formatData.stream()
                .collect(Collectors.toMap(
                        c -> c.getDate().toLocalDate(),
                        ContributionDto::getMinute
                ));

        List<ContributionDto> result = new java.util.ArrayList<>();
        for (LocalDate date = fromDate; !date.isAfter(today); date = date.plusDays(1)) {
            result.add(ContributionDto.builder()
                    .date(date.atStartOfDay())
                    .minute(minuteMap.getOrDefault(date, 0))
                    .user(null)
                    .build());
        }

        return result;
    }

}

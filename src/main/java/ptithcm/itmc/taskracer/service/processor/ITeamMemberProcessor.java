package ptithcm.itmc.taskracer.service.processor;

import java.util.UUID;

public interface ITeamMemberProcessor {
    void remove(String slug, UUID userId, UUID removedUserId);

    void invite(String slug, UUID userId, UUID invitedUserId);

    void accept(String slug, UUID userId);

    void reject(String slug, UUID userId);

    void leave(String slug, UUID userId);

    void join(String slug, UUID userId);

    void requestToJoin(String slug, UUID userId);

    void acceptRequest(String slug, UUID userId, UUID requestUserId);
}

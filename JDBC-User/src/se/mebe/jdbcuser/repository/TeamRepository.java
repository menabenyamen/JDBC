package se.mebe.jdbcuser.repository;

import java.util.List;
import java.util.Map;
import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.model.Team;

public interface TeamRepository {

	boolean addTeam(Long teamId, String teamName, Long numberOfMembers, String state);

	boolean updateTeam(String teamName, Long teamId);

	List<Team> getAllTeams() throws RepositoryException;

	void disableATeam(long teamId);

	boolean addUserToTeam(Long userId, Long teamId);

	Map<Long, String> getAllStatusForTeam();

	List<Long> getAllTeamId();

	Map<Long, String> getAllTeamName();

	List<Long> getAllUserIdFromUser();

	Map<Long, Long> getAllUserIdAndTeamIdFromUser();

	Map<Long, Long> getNumberOfTeamMembers();

}

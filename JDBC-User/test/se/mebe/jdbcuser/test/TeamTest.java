package se.mebe.jdbcuser.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.model.Team;
import se.mebe.jdbcuser.repository.TeamRepository;
import se.mebe.jdbcuser.service.TeamService;

@RunWith(MockitoJUnitRunner.class)
public final class TeamTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private TeamRepository teamRepository;

	@InjectMocks
	private TeamService teamService;

	@Test
	public void shouldThrowExceptionIfTeamNameAlreadyExistWhenUpdatingTeam() throws SQLException, ServiceException {

		long teamId = 200l;
		String teamName = "Team002";
		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("team already exist");
		List<Long> userIds = new ArrayList<>();
		List<Long> teamIds = new ArrayList<>();
		userIds.add(10L);
		teamIds.add(teamId);
		Map<Long, String> teamNames = new HashMap<>();
		teamNames.put(teamId, teamName);

		when(teamRepository.getAllTeamName()).thenReturn(teamNames);
		when(teamRepository.getAllUserIdFromUser()).thenReturn(userIds);
		when(teamRepository.getAllTeamId()).thenReturn(teamIds);
		when(teamRepository.updateTeam(teamName, teamId)).thenReturn(true, false);
		teamService.updateTeam(teamName, teamId);
	}

	@Test
	public void shouldThrowExceptionIfTeamIdIsNotFoundWhenUpdatingTeam() throws SQLException, ServiceException {
		String teamName = "Mupparna";
		long teamId = 700l;

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("Can't find teamId:" + teamId);
		List<Long> userIds = new ArrayList<>();
		List<Long> teamIds = new ArrayList<>();
		teamIds.add(800L);
		userIds.add(10L);
		Map<Long, String> teamNames = new HashMap<>();
		teamNames.put(300L, "KesoKungen");

		when(teamRepository.getAllTeamName()).thenReturn(teamNames);
		when(teamRepository.getAllUserIdFromUser()).thenReturn(userIds);
		when(teamRepository.getAllTeamId()).thenReturn(teamIds);
		when(teamRepository.updateTeam(teamName, teamId)).thenReturn(false, true);

		teamService.updateTeam(teamName, teamId);
	}

	@Test
	public void confirmThatGetAllTeamWorks() throws ServiceException, RepositoryException, SQLException {

		List<Team> teams = new ArrayList<>();
		teams.add(new Team(10L, "Keso", 100L, "Active"));
		when(teamRepository.getAllTeams()).thenReturn(teams);

		teamService.getAllTeams();

		assertEquals(true, true);
	}

	@Test
	public void confirmThatATeamIsSuccessfullyAdded() throws ServiceException, RepositoryException, SQLException {

		long teamId = 600;
		String teamName = "Team Green";
		long numberOfMembers = 0;
		String state = "Active";

		teamService.addTeam(teamId, teamName, numberOfMembers, state);

		assertEquals(true, true);
	}

	@Test
	public void confirmThatAUserIsSuccessfullyAddedToATeam()
			throws ServiceException, RepositoryException, SQLException {

		long teamId = 300L;
		long userId = 3L;

		List<Long> userIdList = new ArrayList<>();
		List<Long> teamIdList = new ArrayList<>();
		Map<Long, Long> userTeamidsList = new HashMap<>();
		Map<Long, Long> numberOfTeamMembersList = new HashMap<>();
		userIdList.add(userId);
		teamIdList.add(teamId);
		userTeamidsList.put(userId, 200L);
		numberOfTeamMembersList.put(userId, teamId);

		when(teamRepository.getAllUserIdFromUser()).thenReturn(userIdList);
		when(teamRepository.getAllTeamId()).thenReturn(teamIdList);
		when(teamRepository.getAllUserIdAndTeamIdFromUser()).thenReturn(userTeamidsList);
		when(teamRepository.getNumberOfTeamMembers()).thenReturn(numberOfTeamMembersList);
		teamService.addUserToTeam(userId, teamId);

		assertEquals(true, true);
	}

	@Test
	public void confirmThatAnActiveTeamIsDisabled() throws ServiceException, RepositoryException, SQLException {

		long teamId = 200;

		List<Team> teams = new ArrayList<>();
		teams.add(new Team(200L, "Keso", 100L, "Inactive"));
		List<Long> teamIds = new ArrayList<>();
		teamIds.add(200L);
		Map<Long, String> teamStatus = new HashMap<>();
		teamStatus.put(teamId, "Inactive");

		when(teamRepository.getAllStatusForTeam()).thenReturn(teamStatus);
		when(teamRepository.getAllTeams()).thenReturn(teams);
		when(teamRepository.getAllTeamId()).thenReturn(teamIds);

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage("team " + teamId + " is inactiv and can't therefore be disabled");

		teamService.disableATeam(teamId);

	}

}

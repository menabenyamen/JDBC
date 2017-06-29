package se.mebe.jdbcuser.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import se.mebe.jdbcuser.exception.RepositoryException;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.model.Team;
import se.mebe.jdbcuser.repository.TeamRepository;



public final class TeamService {

	private final TeamRepository teamRep;
	
	public TeamService(TeamRepository teamRep) throws SQLException {
		this.teamRep = teamRep;
		
	}

	public boolean addTeam(Long teamId, String teamName, Long numberOfMembers, String state) throws ServiceException, RepositoryException {
		
		if(teamRep.getAllTeamId().contains(teamId) == false && 
		   checkIfTeamNameExist(teamName) == false &&
		   startNumberForMembers(numberOfMembers) == true && state == "active");
			
		return teamRep.addTeam(teamId, teamName, numberOfMembers, state);
	}
	
	public boolean updateTeam(String teamName, long teamId) throws ServiceException {
		
		if(teamController(teamId) == true && checkIfTeamNameExist(teamName) == false) { 
			return teamRep.updateTeam(teamName, teamId);
		}
		
		return false;
	}
	
	public boolean disableATeam(long teamId) throws ServiceException {
		
		if((teamController(teamId) == true && checkForActiveTeams(teamId) == true)){
		return true;	
		}
		return false;
	}

	public List<Team> getAllTeams() throws ServiceException, RepositoryException {
		if(teamRep.getAllTeams().isEmpty()) {
			throw new ServiceException("There are no teams");
		}
		
		else {
			return teamRep.getAllTeams();	
		}
	}
	
	public boolean addUserToTeam(long userId, long teamId) throws ServiceException {
		
		if(userController(userId) && teamController(teamId) &&
		   userDoesNotAlreadyBelongToTheTeam(userId, teamId) &&
		   checkForNumberOfUserInOneTeam(userId, teamId) == true) {
			
			return teamRep.addUserToTeam(userId, teamId);
		}
		return false;
	}
	
	private boolean checkIfTeamNameExist(String teamName) throws ServiceException {
		if(teamRep.getAllTeamName().containsValue(teamName) == true) {
			throw new ServiceException("team already exist" );
		}
		else{
			return false;
		}
	}
		
	private boolean checkForActiveTeams(long teamId) throws ServiceException {
		String temp = teamRep.getAllStatusForTeam().get(teamId);
		if(temp.equals("Active")) {
			
			teamRep.disableATeam(teamId);
		}else{
			
			throw new ServiceException("team " + teamId + " is inactiv and can't therefore be disabled" );
		}
		return true;
	}
	
	private boolean startNumberForMembers(Long numberOfMembers) throws ServiceException {
		if(numberOfMembers == 0) {
			return true;
		}
		else {
			throw new ServiceException("number of user Must be 0 when team is created");
		}
	} 
	
	private boolean userDoesNotAlreadyBelongToTheTeam(long userId,long teamId) throws ServiceException {
		
		Long team = teamRep.getAllUserIdAndTeamIdFromUser().get(userId);
		
		if(team.equals(teamId)){
				
			throw new ServiceException("User: "+ userId + " is already in that team");
		}

		return true;
	}
	
	private boolean userController(long userId) throws ServiceException {

		if(teamRep.getAllUserIdFromUser().contains(userId) == true){
			return true;	
		}
		
		else {
			throw new ServiceException("Can't find userId:"+ userId);
		}
	}
	
	private boolean teamController(long teamId) throws ServiceException {

		if(teamRep.getAllTeamId().contains(teamId) == true){
			return true;
		}
		
		else {
			throw new ServiceException("Can't find teamId:"+ teamId);
		}
	}
	
	private boolean checkForNumberOfUserInOneTeam(long userId, long teamId) throws ServiceException {
		
		Map<Long, Long> temp = teamRep.getNumberOfTeamMembers();
		
		if (temp.containsKey(teamId)) {
			long value = temp.get(teamId);

			if (value > 10) {
				throw new ServiceException("just 10 user no more !!");
			}
		
		}
		return true;	
	}
	
}


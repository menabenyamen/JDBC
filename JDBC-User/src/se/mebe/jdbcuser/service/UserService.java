package se.mebe.jdbcuser.service;

import java.sql.SQLException;
import java.util.Set;

import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.model.User;
import se.mebe.jdbcuser.repository.BackUpRepository;
import se.mebe.jdbcuser.repository.UserRepository;

public final class UserService {

	private final UserRepository userRepository;
	private final BackUpRepository backUpRepository;

	public UserService(UserRepository userRepository, BackUpRepository backUpRepository)
			throws SQLException, ServiceException {

		this.userRepository = userRepository;
		this.backUpRepository = backUpRepository;

	}

	public void createUser(User user) throws ServiceException {
		if (user.getUserName().length() < 10) {
			throw new ServiceException("This user name is less than 10 digits !");

		} else if (backUpRepository.getUsersId().contains(user.getId())) {
			throw new ServiceException("This user id is in your table !");

		} else if (backUpRepository.getUsersName(user.getUserName()).contains(user.getUserName())) {
			throw new ServiceException("This user Name exist ! Chose another one !!");

		} else {
			userRepository.addUser(user);
		}

	}

	public void updateUserInATeam(long teamId, long userId) throws ServiceException {
		if (backUpRepository.getUserTeamId(userId).contains(teamId)) {
			throw new ServiceException("This user id has a Team Id !");
		} else {
			userRepository.updateAsignTeamToUser(teamId, userId);
		}
	}

	public void inactiveAUser(long userId) throws ServiceException {
		if (backUpRepository.getUserState(userId).contains("Inactive")) {
			throw new ServiceException("This user is inactive !!");

		} else if (!backUpRepository.getUserState(userId).equals("Inactive")) {
			userRepository.disable(userId);
			backUpRepository.updateWorkItemState(userId);

		}
	}

	public Set<User> getUserDependOnId(long userId) throws ServiceException {

		if (!backUpRepository.getUsersId().contains(userId)) {
			throw new ServiceException("This user id dosent exist !");

		} else {
			return userRepository.getUserById(userId);

		}

	}

	public Set<User> getUserDependOnUserName(String userName) throws ServiceException {

		if (!backUpRepository.getUsersName(userName).contains(userName)) {
			throw new ServiceException("This user Name dosent exist !");

		} else if (backUpRepository.getUsersName(userName).contains(userName)) {
			userRepository.getUserByUserName(userName);
		}

		return userRepository.getUserByUserName(userName);

	}

	public Set<User> getUserDependOnTeam(long userId, long teamId) throws ServiceException {

		if (!backUpRepository.getUserTeamId(userId).contains(teamId)) {
			throw new ServiceException("This team id dosent exist !");

		} else {
			userRepository.getAllUsersForOneTeam(teamId);
		}

		return userRepository.getAllUsersForOneTeam(teamId);
	}

}

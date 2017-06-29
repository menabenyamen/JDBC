package se.mebe.jdbcuser.repository;

import java.util.Set;
import se.mebe.jdbcuser.exception.ServiceException;
import se.mebe.jdbcuser.model.User;

public interface UserRepository {

	User addUser(User user) throws ServiceException;

	void disable(long userId);

	Set<User> getUserById(long userId) throws ServiceException;

	Set<User> getUserByUserName(String userName) throws ServiceException;

	void updateAsignTeamToUser(long teamId, long userId);

	Set<User> getAllUsersForOneTeam(long teamId) throws ServiceException;

}

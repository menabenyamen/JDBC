package se.mebe.jdbcuser.model;

public final class Team {
	private final long teamId;
	private final String teamName;
	private final long numberOfMembers;
	private final String state;

	public Team(long teamId, String teamName, long numberOfMembers, String state) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.numberOfMembers = numberOfMembers;
		this.state = state;
	}

	public long getTeamId() {
		return teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public long getNumberofMembers() {
		return numberOfMembers;
	}

	public String getState() {
		return state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (numberOfMembers ^ (numberOfMembers >>> 32));
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + (int) (teamId ^ (teamId >>> 32));
		result = prime * result + ((teamName == null) ? 0 : teamName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (numberOfMembers != other.numberOfMembers)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (teamId != other.teamId)
			return false;
		if (teamName == null) {
			if (other.teamName != null)
				return false;
		} else if (!teamName.equals(other.teamName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Team [teamId=" + teamId + ", teamName=" + teamName + ", numberOfMembers=" + numberOfMembers + ", state="
				+ state + "]";
	}

}

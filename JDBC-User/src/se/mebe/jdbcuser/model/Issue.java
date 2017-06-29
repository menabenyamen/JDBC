package se.mebe.jdbcuser.model;

import java.io.Serializable;

public final class Issue implements Serializable {

	private static final long serialVersionUID = -2213313440701244871L;
	private final long issueId;
	private final long workItemId;
	private final String comment;

	public Issue(long issueId, String comment, long workItemId) {
		this.issueId = issueId;
		this.workItemId = workItemId;
		this.comment = comment;
	}

	public long getIssueId() {
		return issueId;
	}

	public long getWorkItemId() {
		return workItemId;
	}

	public String getComment() {
		return comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (int) (issueId ^ (issueId >>> 32));
		result = prime * result + (int) (workItemId ^ (workItemId >>> 32));
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
		Issue other = (Issue) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (issueId != other.issueId)
			return false;
		if (workItemId != other.workItemId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Issue : issueId = " + issueId + "\t" + "workItemId = " + workItemId + "\t" +"comment = " + comment +"\n";
	}

}

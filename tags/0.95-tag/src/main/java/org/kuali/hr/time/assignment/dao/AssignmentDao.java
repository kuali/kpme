package org.kuali.hr.time.assignment.dao;

import java.sql.Date;
import java.util.List;

import org.kuali.hr.time.assignment.Assignment;

public interface AssignmentDao {

	/**
	 * Returns all assignments for the provided principalId that are valid as of
	 * the specified payPeriodEndDate.
	 *
	 * @param principalId
	 * @param payPeriodEndDate
	 * @return
	 */
	public List<Assignment> findAssignments(String principalId, Date asOfDate);
	/**
	 * Save or update the given assignment
	 * @param assignment
	 */
	public void saveOrUpdate(Assignment assignment);
	/**
	 * Save of update the given list of assignments
	 * @param assignments
	 */
	public void saveOrUpdate(List<Assignment> assignments);

	/**
	 * Delete an assignment
	 * @param assignment
	 */
	public void delete(Assignment assignment);

	/**
	 * Get list of active assignments in a given work area as of a particular date
	 * @param workArea
	 * @param asOfDate
	 * @return
	 */
	public List<Assignment> getActiveAssignmentsInWorkArea(Long workArea, Date asOfDate);

	public Assignment getAssignment(String tkAssignmentId);

    public Assignment getAssignment(Long job, Long workArea, Long task, Date asOfDate);

	public List<Assignment> getActiveAssignments(Date asOfDate);
	
	public Assignment getAssignment(Long tkAssignmentId);
	
	public Assignment getAssignment(String principalId, Long jobNumber, Long workArea, Long task, Date asOfDate);
}
package org.kuali.hr.time.assignment.service;

import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.assignment.AssignmentDescriptionKey;
import org.kuali.hr.time.paycalendar.PayCalendarEntries;
import org.kuali.hr.time.timesheet.TimesheetDocument;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface AssignmentService {
	/**
	 * Fetches a list of Assignments for a given principal Id as of a particular date
	 * @param principalId
	 * @param asOfDate
	 * @return
	 */
    public List<Assignment> getAssignments(String principalId, Date asOfDate);
    /**
     * Reverse lookup of an assignment based on the assignment key and the document
     * @param timesheetDocument
     * @param assignmentKey
     * @return
     */
    public Assignment getAssignment(TimesheetDocument timesheetDocument, String assignmentKey);
    /**
     * Reverse lookup of an assignment based on the assignment id
     * @param tkAssignmentId
     * @return
     */
    public Assignment getAssignment(String tkAssignmentId);
    /**
     * Get Assignment Description key based off of description
     * @param assignmentDesc
     * @return
     */
    public AssignmentDescriptionKey getAssignmentDescriptionKey(String assignmentDesc);
    /**
     * Get all assignment descriptions for a document
     * @param td
     * @param clockOnlyAssignments
     * @return
     */
    public Map<String,String> getAssignmentDescriptions(TimesheetDocument td, boolean clockOnlyAssignments);
    /**
     * Get all assignment descriptions for an assignment
     * @param assignment
     * @return
     */
	public Map<String,String> getAssignmentDescriptions(Assignment assignment);
	/**
	 * Get all active assignments for a work area
	 * @param workArea
	 * @param asOfDate
	 * @return
	 */
	public List<Assignment> getActiveAssignmentsForWorkArea(Long workArea, Date asOfDate);

	/**
	 * Get active assignments for all users for the current date
	 * CAUTION this method will return a lot of data in a normal production env
	 * It is intended to only be used in a batch setting
	 * @param asOfDate
	 * @return
	 */
	public List<Assignment> getActiveAssignments(Date asOfDate);


    /**
     * For a given AssignmentDescriptionKey return the matching assignment.
     * @param key
     * @return
     */
    public Assignment getAssignment(AssignmentDescriptionKey key, Date asOfDate);

    
    /**
     * Fetch principal id and key as of a particular date
     * @param principalId
     * @param key
     * @param asOfDate
     * @return
     */
    public Assignment getAssignment(String principalId, AssignmentDescriptionKey key, Date asOfDate);
    
    /**
     * Get assignments by pay calendar entry
     * @param principalId
     * @param payCalendarEntry
     * @return
     */
    public List<Assignment> getAssignmentsByPayEntry(String principalId, PayCalendarEntries payCalendarEntry);
    
    /**
	 * KPME-1129 Kagata
	 * Get a list of active assignments based on principalId and jobNumber as of a particular date 
	 * @param principalId
	 * @param jobNumber
	 * @param asOfDate
	 * @return
	 */
    public List<Assignment> getActiveAssignmentsForJob(String principalId, Long jobNumber, Date asOfDate);
    
}

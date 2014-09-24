package com.advisory.ctc.integration.cat.util;

import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class PatientDemographics {

	private static int MINIMUM_FIRSTNAME_MATCH = 3;
	private static int MINIMUM_LASTNAME_MATCH = 5;
	
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String messageID;
	private Date eventDTS;
	private String sendingFacility;
	private String hospital;
	private String mrn;
	
	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	private ArrayList<String> visits = new ArrayList<String>();

	private SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public PatientDemographics(String firstName, String lastName, Date dateOfBirth) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getFirstNameForMatching() {
		if (firstName.length() < MINIMUM_FIRSTNAME_MATCH) return firstName.toLowerCase();		
		return firstName.substring(0,MINIMUM_FIRSTNAME_MATCH).toLowerCase();
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getLastNameForMatching() {
		if (lastName.length() < MINIMUM_LASTNAME_MATCH) return lastName.toLowerCase();
		return lastName.substring(0,MINIMUM_LASTNAME_MATCH).toLowerCase();
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public Date getEventDTS() {
		return eventDTS;
	}

	public void setEventDTS(Date eventDTS) {
		this.eventDTS = eventDTS;
	}

	public String getSendingFacility() {
		return sendingFacility;
	}

	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String toString() {
		return this.lastName + ", " + this.firstName + ", " + outputDateFormat.format(this.dateOfBirth); 
	}
	
	public String getHashKey() {
		return this.lastName + this.firstName + this.dateOfBirth.toString();
	}
	
	public void addVist(String visitID) {
		this.visits.add(visitID);
	}
	
	public int numVisits() {
		return this.visits.size();
	}
}

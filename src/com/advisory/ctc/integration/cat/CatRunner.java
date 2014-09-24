package com.advisory.ctc.integration.cat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.advisory.ctc.integration.cat.util.*;

public class CatRunner {
	
	//setttings
	static boolean SURPRESS_BABIES = true;
	static boolean SURPRESS_NULL_OUTPUT = true;	
	static boolean RELAXED_MATCH = false;
	static boolean IGNORE_NULL_KEYS = true;
	static boolean MESSAGE_FOCUSED = false;
		//if not message_focused, default behavior is identifer-focused
	
	//Note: ASSUME_MAIDEN_NAME only has an effect if RELAXED_MATCH is false.
	static boolean ASSUME_MAIDEN_NAME = true;	
	static boolean DETAILED_OUTPUT = true;
	static boolean DETAILED_SUMMARY = true;
	
	//do we want output on possible alternative IDs?
	static boolean RUN_PI = true;
	static boolean RUN_PN = true;
	static boolean RUN_VISIT = false;
	static int FAVORED_HASHTYPE = 0;
	
	static int NUMBER_OF_POSSIBLE_HASHKEYS = 4;
	static String[] keyTypes = {"MRN", "PN", "PI", "VISIT"};
	static int[] numberOfProblems = new int[NUMBER_OF_POSSIBLE_HASHKEYS];
	static int numberOfGoodMatches = 0;
	static int numberOfProcessedMessages = 0;
	
	static DateFormat dateOfBirthFormat = new SimpleDateFormat("yyyyMMDD");
	static DateFormat dtsFormat = new SimpleDateFormat("yyyyMMDDHHMM");	
	
	static HashMap<String, PatientDemographics> mrnHash = new HashMap<String, PatientDemographics>();
	static HashMap<String, PatientDemographics> pnHash = new HashMap<String, PatientDemographics>();
	static HashMap<String, PatientDemographics> piHash = new HashMap<String, PatientDemographics>();
	static HashMap<String, PatientDemographics> visitHash = new HashMap<String, PatientDemographics>();
	static HashMap<String, PatientDemographics> demoHash = new HashMap<String, PatientDemographics>();
	
	static HashSet<String> mrns = new HashSet<String>();
	static HashSet<String> pns = new HashSet<String>();
	static HashSet<String> pis = new HashSet<String>();
	
	static PrintWriter outputFile;
	
	public static void main(String[] args) {
		
		for (int i = 0; i < numberOfProblems.length; i++) {
			numberOfProblems[i] = 0;
		}		
		
		try {
			
			 outputFile = new PrintWriter("C:\\chrisdev\\data\\test data\\mp_dupe_issue\\test_data\\output.txt");
			 
			//open the input file
			//scan a line
			//create demographic DO
			//create MR
			//hash demographic DO by the MR
			
			//if collision
				//compare demographic information
					//if demographics match
						//add visit information
					//if demographics don't match, FLAG AS INAPPROPRIATE
			
			//if no collision
				//proceed
			
			String[] filesToProcess = {"C:\\chrisdev\\data\\test data\\mp_dupe_issue\\test_data\\full_extract.txt", "C:\\chrisdev\\data\\test data\\mp_dupe_issue\\test_data\\full_extract2.txt"};
			
			for (String fileName : filesToProcess) {
				processFile(fileName);
			}
			
			System.out.println("******************SUMMARY******************");
			outputFile.println("******************SUMMARY******************");
			
			if (MESSAGE_FOCUSED) {
				System.out.println("We have " + numberOfProblems[0] + " messages using a duplicate MRN as patient identifier.");
				outputFile.println("We have " + numberOfProblems[0] + " messages using a duplicate using MRN as patient identifier.");
				if (RUN_PN) {
					System.out.println("We have " + numberOfProblems[1] + " messages using a duplicate using PN as patient identifier.");
					outputFile.println("We have " + numberOfProblems[1] + " messages using a duplicate using PN as patient identifier.");
				}
				
				if (RUN_PI) {
					System.out.println("We have " + numberOfProblems[2] + " messages using a duplicate using PI as patient identifier.");
					outputFile.println("We have " + numberOfProblems[2] + " messages using a duplicate using PI as patient identifier.");
				}
				
				if (RUN_VISIT) {
					System.out.println("We have " + numberOfProblems[3] + " messages using a duplicate visit IDs.");
					outputFile.println("We have " + numberOfProblems[3] + " messages using a duplicate visit IDs.");
				}
				
				if (DETAILED_SUMMARY) {
					for (String affectedPatientKey : demoHash.keySet()) {
						
						System.out.println("Patient Demographics: " + demoHash.get(affectedPatientKey).toString());
						outputFile.println("Patient Demographics: " + demoHash.get(affectedPatientKey).toString());
						
						System.out.println("    Message ID: " + demoHash.get(affectedPatientKey).getMessageID());
						outputFile.println("    Message ID: " + demoHash.get(affectedPatientKey).getMessageID());
						System.out.println("    Event Timestamp: " + demoHash.get(affectedPatientKey).getEventDTS());
						outputFile.println("    Event Timestamp: " + demoHash.get(affectedPatientKey).getEventDTS());
						System.out.println("    Sending Facility: " + demoHash.get(affectedPatientKey).getSendingFacility());
						outputFile.println("    Sending Facility: " + demoHash.get(affectedPatientKey).getSendingFacility());
						System.out.println("    Hospital: " + demoHash.get(affectedPatientKey).getHospital());
						outputFile.println("    Hospital: " + demoHash.get(affectedPatientKey).getHospital());
						System.out.println("    MRN: " + demoHash.get(affectedPatientKey).getMrn());
						outputFile.println("    MRN: " + demoHash.get(affectedPatientKey).getMrn());					
						System.out.println("");
						outputFile.println("");
						
					}
				}
			}
			
			else {
								
				System.out.println("There are " + mrns.size() + " mrn numbers that are associated with more than one patient.");
				outputFile.println("There are " + mrns.size() + " mrn numbers that are associated with more than one patient.");
				
				System.out.println("There are " + pns.size() + " pn numbers that are associated with more than one patient.");
				outputFile.println("There are " + pns.size() + " pn numbers that are associated with more than one patient.");
				
				System.out.println("There are " + pis.size() + " pi numbers that are associated with more than one patient.");
				outputFile.println("There are " + pis.size() + " pi numbers that are associated with more than one patient.");
				
				System.out.println("There are " + numberOfProcessedMessages + " processed messages.");
				outputFile.println("There are " + numberOfProcessedMessages + " processed messages.");
				
				System.out.println("There are " + numberOfGoodMatches + " successful matches where the patient demographics appropriately matched where the MRN matched.");
				outputFile.println("There are " + numberOfGoodMatches + " successful matches where the patient demographics appropriately matched where the MRN matched.");
				
				if (DETAILED_SUMMARY) {
					PrintWriter problemMrns = new PrintWriter("C:\\chrisdev\\data\\test data\\mp_dupe_issue\\test_data\\overlap_mrns.txt");
					PrintWriter problemPis = new PrintWriter("C:\\chrisdev\\data\\test data\\mp_dupe_issue\\test_data\\overlap_pis.txt");
					PrintWriter problemPns = new PrintWriter("C:\\chrisdev\\data\\test data\\mp_dupe_issue\\test_data\\overlap_pns.txt");
					
					for (String key : mrns) {
						problemMrns.println(key);
					}
					
					for (String key : pis) {
						problemPis.println(key);
					}
					
					for (String key : pns) {
						problemPns.println(key);
					}

					problemMrns.close();
					problemPis.close();
					problemPns.close();
				}
			}
			
						
		}
		
		catch (Exception ex) {
			System.out.println("Aw crap");
			ex.printStackTrace();
		}
	}
	
	public static void processFile(String fullFilePath) {
		
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader("C:\\chrisdev\\data\\test data\\mp_dupe_issue\\test_data\\full_extract.txt"));
			
			//ignore first line as it contains header information
			String line = reader.readLine();
			
			while((line = reader.readLine()) != null) {								
				
				hashIt(0, line);
				
				if (RUN_PN) {
					hashIt(1, line);
				}
				
				if (RUN_PI) {
					hashIt(2, line);
				}
				
				if (RUN_VISIT) {
					hashIt(3, line);
				}
				
			}
			
			reader.close();
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public static void hashIt(int hashKeyType, String line){
		
		String dataItems[] = line.split(",");
		//msh.7.1,msh.10.1,msh.4.1,evn.2.1,pid.3.1.MR,pid.3.1.PN,pid.3.1.PI,pid.5.2,pid.5.1,pid.7.1,pid.18.1,pv1.10.1
		
		String msgDTS, msgID, sendFac, eventDTS, mrn, pn, pi, firstName, lastName, dob, visit, hosp;
		
		msgDTS = dataItems[0];
		msgID = dataItems[1];
		sendFac = dataItems[2];
		eventDTS = dataItems[3];				
		mrn = dataItems[4];
		pn = dataItems[5];
		pi = dataItems[6];
		firstName = dataItems[7];
		lastName = dataItems[8];
		dob = dataItems[9];
		
		//immediately filter out st mary's messages
		if (!sendFac.equals("K50")) return;		
		
		if (hashKeyType == FAVORED_HASHTYPE) {
			numberOfProcessedMessages++;
		}
		
		if (dataItems.length > 10) {
			visit = dataItems[10];
		
			if (dataItems.length > 11) {
				hosp = dataItems[11];
			}
			
			else {
				hosp = "";
			}
		}
		
		else {
			visit = "";
			hosp = "";
		}

		Date birthday;
		
		try {
			birthday = dateOfBirthFormat.parse(dob);
		}
		
		catch (Exception ex) {
			birthday = new Date(0);
		}
		
		PatientDemographics patDemo = new PatientDemographics(firstName, lastName, birthday);
		
		Date event;
		
		try {
			event = dtsFormat.parse(eventDTS);
		}
		
		catch (Exception ex) {
			event = new Date(0);
		}
		
		patDemo.setEventDTS(event);
		patDemo.setHospital(hosp);
		patDemo.setMessageID(msgID);
		patDemo.setSendingFacility(sendFac);
		
		String hashKey = "";
		HashMap<String, PatientDemographics> hash = null;
			
		if (hashKeyType == 0) {
			hashKey = mrn;
			hash = mrnHash;
		}
		
		else if (hashKeyType == 1) {
			hashKey = pn;
			hash = pnHash;
		}
		
		else if (hashKeyType == 2) {
			hashKey = pi;
			hash = piHash;
		}
		
		else if (hashKeyType == 3) {
			hashKey = visit;
			hash = visitHash;
		}
				
		if (hashKey == null || hashKey.equals("") || hashKey.equals(" ")) {
			
			if (!IGNORE_NULL_KEYS) {

				numberOfProblems[hashKeyType]++;
			}
			
			if (!SURPRESS_NULL_OUTPUT) {
				System.out.println("******************PROBLEM using " + keyTypes[hashKeyType] + "******************");
				outputFile.println("******************PROBLEM using " + keyTypes[hashKeyType] + "******************");
				System.out.println(patDemo.toString() + ": Has blank or null " + keyTypes[hashKeyType] + " identifier in: ");
				outputFile.println(patDemo.toString() + ": Has blank or null " + keyTypes[hashKeyType] + " identifier in: ");
				
				if (DETAILED_OUTPUT) {
					System.out.println("    Message ID: " + msgID);
					outputFile.println("    Message ID: " + msgID);
					System.out.println("    Event Timestamp: " + eventDTS);
					outputFile.println("    Event Timestamp: " + eventDTS);
					System.out.println("    Sending Facility: " + sendFac);
					outputFile.println("    Sending Facility: " + sendFac);
					System.out.println("    Hospital: " + hosp);
					outputFile.println("    Hospital: " + hosp);
					System.out.println("    MRN: " + mrn);
					outputFile.println("    MRN: " + mrn);
					System.out.println("    PN: " + pn);
					outputFile.println("    PN: " + pn);
					System.out.println("    PI: " + pi);
					outputFile.println("    PI: " + pi);
					System.out.println("");
					outputFile.println("");
				}
			}
		}
		
		else {
		
			if (hash.containsKey(hashKey)) {
				
				PatientDemographics demoMatch = hash.get(hashKey);
				
				//for demographics to indicate a match, we need to match first 5 of firstname, first 5 of last name, and date of birth
				if (demographicsMatch(patDemo, demoMatch)) {
	
					demoMatch.addVist(visit);
					
					if (hashKeyType == FAVORED_HASHTYPE) {
						numberOfGoodMatches++;
					}
					
					//System.out.println("Person " + mrn + " has " + demoMatch.numVisits() + " visits ");
				}
				
				else {
					//WE HAVE A PORBLEM
					
					numberOfProblems[hashKeyType]++;
					
					System.out.println("******************POTENTIAL INCORRECT MATCH using " + keyTypes[hashKeyType] + " as unique identifier******************");
					outputFile.println("******************POTENTIAL INCORRECT MATCH using " + keyTypes[hashKeyType] + " as unique identifier******************");
					System.out.println("Existing patient: " + demoMatch.toString() + ": " + hashKey);
					outputFile.println("Existing patient: " + demoMatch.toString() + ": " + hashKey);
					
					if (DETAILED_OUTPUT) {
						System.out.println("    Message ID: " + demoMatch.getMessageID());
						outputFile.println("    Message ID: " + demoMatch.getMessageID());
						System.out.println("    Event Timestamp: " + dtsFormat.format(demoMatch.getEventDTS()));
						outputFile.println("    Event Timestamp: " + dtsFormat.format(demoMatch.getEventDTS()));
						System.out.println("    Sending Facility: " + demoMatch.getSendingFacility());
						outputFile.println("    Sending Facility: " + demoMatch.getSendingFacility());
						System.out.println("    Hospital: " + demoMatch.getHospital());
						outputFile.println("    Hospital: " + demoMatch.getHospital());						
						System.out.println("");
						outputFile.println("");
					}
					
					System.out.println("New patient: " + patDemo.toString() + ": " + hashKey);
					outputFile.println("New patient: " + patDemo.toString() + ": " + hashKey);
					
					if (DETAILED_OUTPUT) {
						System.out.println("    Message ID: " + msgID);
						outputFile.println("    Message ID: " + msgID);
						System.out.println("    Event Timestamp: " + eventDTS);
						outputFile.println("    Event Timestamp: " + eventDTS);
						System.out.println("    Sending Facility: " + sendFac);
						outputFile.println("    Sending Facility: " + sendFac);
						System.out.println("    Hospital: " + hosp);
						outputFile.println("    Hospital: " + hosp);						
						System.out.println("");
						outputFile.println("");
					}
					
					if (hashKeyType == FAVORED_HASHTYPE) {
						//now to add both of the patients to the list of distinct patients affected
						patDemo.setMrn(mrn);
						demoHash.put(patDemo.getHashKey(), patDemo);
						
						demoMatch.setMrn(mrn);
						demoHash.put(demoMatch.getHashKey(), demoMatch);						
					}
					
					//add the identifier to the appropriate list of identifiers
					if (hashKeyType == 0) {
						mrns.add(hashKey);
					}
					
					else if (hashKeyType == 1) {
						pns.add(hashKey);
					}
					
					else if (hashKeyType == 2) {
						pis.add(hashKey);
					}
				}
			}
			
			else {
				hash.put(hashKey, patDemo);
			}
		}
	}
	
	public static boolean demographicsMatch(PatientDemographics a, PatientDemographics b) {
		
		//System.out.println("Attempting to match patient " + a.toString() + " to patient " + b.toString());
		
		boolean first = matchFirstName(a,b);
		boolean last = matchLastName(a,b);
		boolean dob = matchDateOfBirth(a,b);
		
		int numMatchedCriteria = 0;
		
		if (first) numMatchedCriteria++;		
		if (dob) numMatchedCriteria++;
		if (last) numMatchedCriteria++;
		
		if (numMatchedCriteria == 3) return true;
		
		if (RELAXED_MATCH) {
			if (numMatchedCriteria >= 2) return true;
		}
		
		if (ASSUME_MAIDEN_NAME) {
			if (first && dob) {
				return true;
			}
		}		
		
		return false;
	}
	
	public static boolean matchFirstName(PatientDemographics a, PatientDemographics b) {
	
		if (!a.getFirstNameForMatching().equals(b.getFirstNameForMatching())) {
			
			if (!SURPRESS_BABIES) {
			
				//System.out.println(a.getFirstNameForMatching() + " does not match " + b.getFirstNameForMatching());
				
				return false;
			}
			
			else {
				if (!(a.getFirstNameForMatching().startsWith("bab") || b.getFirstNameForMatching().startsWith("bab") || a.getFirstNameForMatching().startsWith("boy") || b.getFirstNameForMatching().startsWith("boy") || a.getFirstNameForMatching().startsWith("gir") || b.getFirstNameForMatching().startsWith("gir"))) {
				
					//System.out.println(a.getFirstNameForMatching() + " does not match " + b.getFirstNameForMatching());
					
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	public static boolean matchDateOfBirth(PatientDemographics a, PatientDemographics b) {
	
		if (!a.getDateOfBirth().equals(b.getDateOfBirth())) {
			
			//System.out.println(a.getDateOfBirth() + " does not match " + b.getDateOfBirth());
			
			return false;
		}
		
		return true;
	}
	
	public static boolean matchLastName(PatientDemographics a, PatientDemographics b) {
	
		if (!a.getLastNameForMatching().equals(b.getLastNameForMatching())) {
			
			//System.out.println(a.getLastNameForMatching() + " does not match " + b.getLastNameForMatching());
			
			return false;
		}
		
		return true;
	}
	
}
package rs.ac.uns.ftn.asd.Projekatsiit2024.service.reportPDF;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity.EventActivityPDF;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventStatistics.EventAttendance;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventStatistics.EventRatingsCounter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.reportPDF.PdfGenerationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventRating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;

@Service
public class ReportPDFService {
	
	@Autowired
	EventRepository eventRepository;
	
	public byte[] createEventDetailsReport(Integer eventId, UserPrincipal userPrincipal) 
			throws EventValidationException, PdfGenerationException {
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
    	if (optionalEvent.isEmpty())
    		throw new EventValidationException("No event exists with such id.");
    	
    	Event event = optionalEvent.get();
		
		isUserAllowedToGetData(userPrincipal, event);
		
		InputStream inputStream = getClass().getClassLoader().
				getResourceAsStream("reports/templates/eventdetailsreport.jrxml");
		
		//filling regular event data
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", event.getName());
		parameters.put("eventTypeName", event.getEventType().getName());
		parameters.put("description", event.getDescription());
		parameters.put("numOfAttendees", event.getNumOfAttendees().toString());
		String privacy = "public";
		if (event.getIsPrivate())
			privacy = "private";
		parameters.put("privacyType", privacy);
		parameters.put("location", event.getPlace());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a");
		parameters.put("startingTime", event.getDateOfEvent().format(formatter));
		parameters.put("endingTime", event.getEndOfEvent().format(formatter));
		String attendeesString = event.getListOfAttendees()
			    .stream()
			    .map(AuthentifiedUser::getEmail)
			    .collect(Collectors.joining(", "));
		parameters.put("attendees", attendeesString);
		
		//filling data for event activities table
		List<EventActivityPDF> eaList = event.getEventActivities().stream()
			    .map(EventActivityPDF::new) // Assuming EventActivityPDF has a constructor that takes EventActivity
			    .collect(Collectors.toList());
		JRBeanCollectionDataSource eventActivityDataSource = new JRBeanCollectionDataSource(eaList);
		parameters.put("eventActivityDataSet", eventActivityDataSource);
		
		try {
	        JasperReport report = JasperCompileManager.compileReport(inputStream);
	        JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

	        //exporting the filled report to byte array (PDF)
	        return JasperExportManager.exportReportToPdf(print);
	    } catch (JRException e) {
	        throw new PdfGenerationException("Error generating PDF.");
	    }
	}
	
	
	private boolean isUserAllowedToGetData(UserPrincipal userPrincipal, Event event) throws EventValidationException {
    	if (event.getIsPrivate()) {
    		//if event is private and user is logged out
    		if (userPrincipal == null)
    			throw new EventValidationException("You have to be logged in into account which has"
    					+ " permition to view this private event.");
    		
    		AuthentifiedUser user = userPrincipal.getUser();
    		
    		//if event is private and user is not one of the attendees of this event
    		boolean isAttendee = event.getListOfAttendees()
    		        .stream()
    		        .anyMatch(attend -> attend.getId().equals(user.getId()));
    		if (!isAttendee)
    			throw new EventValidationException("You are not on the list of attendees for this private event.");
    	}
    	
    	return true;
	}


	
	
	
	
	public byte[] createEventStatisticsReport(Integer eventId, UserPrincipal userPrincipal) 
		throws EventValidationException, PdfGenerationException {
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
    	if (optionalEvent.isEmpty())
    		throw new EventValidationException("No event exists with such id.");
    	
    	Event event = optionalEvent.get();
    	
    	isUserAllowedToSeeData(userPrincipal, event);
    	
    	InputStream inputStream = getClass().getClassLoader().
    			getResourceAsStream("reports/templates/eventstatsreport.jrxml");
    	
    	//filling attendance numbers
    	Map<String, Object> parameters = new HashMap<>();
    	EventAttendance eventAttendance1 = new EventAttendance("Attended", 
    			event.getListOfAttendees().size());
    	EventAttendance eventAttendance2 = new EventAttendance("Left space", 
    			event.getNumOfAttendees() - event.getListOfAttendees().size());
    	List<EventAttendance> eventAttendances = new ArrayList<>();
    	eventAttendances.add(eventAttendance1);
    	eventAttendances.add(eventAttendance2);
    	JRBeanCollectionDataSource eventAttendanceDataSource = new JRBeanCollectionDataSource(eventAttendances);
    	parameters.put("eventAttendanceDataSet", eventAttendanceDataSource);
    	
    	//filling event ratings
    	EventRatingsCounter erc1 = new EventRatingsCounter("One", 0);
    	EventRatingsCounter erc2 = new EventRatingsCounter("Two", 0);
    	EventRatingsCounter erc3 = new EventRatingsCounter("Three", 0);
    	EventRatingsCounter erc4 = new EventRatingsCounter("Four", 0);
    	EventRatingsCounter erc5 = new EventRatingsCounter("Five", 0);
    	List<EventRating> ratings = new ArrayList<>(event.getEventRatings());
    	for (EventRating rating: ratings) {
    		int grade = rating.getRatingValue();
    		switch (grade) {
    		case 1:
    			erc1.setNumOfAuthors(erc1.getNumOfAuthors() + 1);
    			break;
    		case 2:
    			erc2.setNumOfAuthors(erc2.getNumOfAuthors() + 1);
    			break;
    		case 3:
    			erc3.setNumOfAuthors(erc3.getNumOfAuthors() + 1);
    			break;
    		case 4:
    			erc4.setNumOfAuthors(erc4.getNumOfAuthors() + 1);
    			break;
    		case 5:
    			erc5.setNumOfAuthors(erc5.getNumOfAuthors() + 1);
    			break;
    		}		
    	}
    	List<EventRatingsCounter> eventRatings = new ArrayList<>();
    	eventRatings.add(erc1);
    	eventRatings.add(erc2);
    	eventRatings.add(erc3);
    	eventRatings.add(erc4);
    	eventRatings.add(erc5);
    	JRBeanCollectionDataSource eventRatingsDataSource = new JRBeanCollectionDataSource(eventRatings);
    	parameters.put("eventRatingsDataSet", eventRatingsDataSource);

    	
    	try {
	        JasperReport report = JasperCompileManager.compileReport(inputStream);
	        JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

	        //exporting the filled report to byte array (PDF)
	        return JasperExportManager.exportReportToPdf(print);
	    } catch (JRException e) {
	        throw new PdfGenerationException("Error generating PDF.");
	    }
	}
	
	
	private boolean isUserAllowedToSeeData(UserPrincipal userPrincipal, Event event) throws EventValidationException {
    	AuthentifiedUser user = userPrincipal.getUser();
		
    	//if organizer of the event
		if (event.getOrganizer().getId().equals(user.getId()))
			return true;
		
		//if user admin
		if (user.getRole().getName().equals("ADMIN_ROLE"))
			return true;
		
		//if any other case
		throw new EventValidationException("No one except admin and organizer of the event can "
				+ "see it's statistics.");
	}
}

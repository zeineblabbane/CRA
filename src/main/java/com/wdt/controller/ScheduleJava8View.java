package com.wdt.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.wdt.model.Administrateur;
import com.wdt.model.Cra;
import com.wdt.repository.AdminRepository;
import com.wdt.repository.CraRepository;
import com.wdt.utils.CraService;

@Named
@ViewScoped
@Scope(scopeName = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ScheduleJava8View implements Serializable {

	private static final long serialVersionUID = 1L;

	private ScheduleModel eventModel;

	@Autowired
	private Cra<?> event1 = new Cra<>();
	
	@Autowired
	private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
	
	@Autowired
	private List<Cra<?>> events = new ArrayList<Cra<?>>();

	@Autowired
	@Named("craService") 
	private CraService service;
	
	@Autowired
	private Administrateur currentAdmin;
	
    private String username;
	
	private String color;
	
	private double joursTravailles=0;
	
	private double montant=0;
	
	private Month currentMonth = LocalDate.now().getMonth();
	

/******************************************************************************************************/
	private boolean slotEventOverlap = true;
	private boolean showWeekNumbers = false;
	private boolean showHeader = true;
	private boolean draggable = true;
	private boolean resizable = true;
	private boolean showWeekends = true;
	private boolean tooltip = true;
	private boolean allDaySlot = true;
	private boolean rtl = false;

	private double aspectRatio = Double.MIN_VALUE;

	private String leftHeaderTemplate = "prev,next today";
	private String centerHeaderTemplate = "title";
	private String rightHeaderTemplate = "dayGridMonth,timeGridWeek,timeGridDay,listYear";
	private String nextDayThreshold = "09:00:00";
	private String weekNumberCalculation = "local";
	private String weekNumberCalculator = "date.getTime()";
	private String displayEventEnd;
	private String timeFormat;
	private String slotDuration = "00:30:00";
	private String slotLabelInterval;
	private String slotLabelFormat;
	private String scrollTime = "06:00:00";
	private String minTime = "04:00:00";
	private String maxTime = "20:00:00";
	private String locale = "en";
	private String timeZone = "";
	private String clientTimeZone = "local";
	private String columnHeaderFormat = "";
	private String view = "timeGridWeek";
	private String height = "auto";


/**********************************************Display all events************************************************************/
	
	
	@PostConstruct
	public void init() {

		this.username = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("username");

        //User connected
		if (this.username ==null) {
			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
			if (principal instanceof UserDetails) {
			  this.username = ((UserDetails)principal).getUsername();
			} else {
			  this.username = principal.toString();
			}
			
		}
		
		this.currentAdmin = userRepository.findByNom(username);
		
		this.fetchEvents();
		
		//Calculer jours travaillés:
		for (Cra<?> cra : this.currentAdmin.getCra()) {
			if((cra.getStartDate().getMonth()==this.currentMonth) && ( (cra.getTitle().equals("Jours Tavaillés") ) ) ) {
				if(cra.isDemiJournee()== false) {
					this.joursTravailles = this.joursTravailles + (cra.getEndDate().getDayOfMonth()-cra.getStartDate().getDayOfMonth()+1);
				}else {
					this.joursTravailles= this.joursTravailles + 0.5;
				}
			}
		}
		
		//Calculer Montant  (Supposons qu'une journee coute 20euro)
		this.montant = this.joursTravailles * 20;

	}
	
	public void fetchEvents() {
		eventModel = new DefaultScheduleModel();
		DefaultScheduleEvent<?> event;
		List<Cra<?>> events= this.getEvents();
		
		for(Cra<?> cra : events) {
			if (cra.getAdministrateur() == this.currentAdmin) {
				if((cra.getTitle().equals("Jours Tavaillés")) && (cra.isDemiJournee()== false)) {
					color="lightgreen";
				}
				else if ((cra.getTitle().equals("Jours Tavaillés")) && (cra.isDemiJournee()== true)){
					color="orange";
				}
				else if (cra.getTitle().equals("Congés")){
					color="red";
				}
				else if (cra.getTitle().equals("Astreintes")){
					color="usernameck";
				}
				else if (cra.getTitle().equals("Maladie")){
					color="white";
				}
				else if (cra.getTitle().equals("Interventions")){
					color="yellow";
				}
				else if (cra.getTitle().equals("RTT")){
					color="blue";
				}
				
				
				event = DefaultScheduleEvent.builder()
						.title(cra.getTitle())
						.startDate(cra.getStartDate())
						.endDate(cra.getEndDate())
						.borderColor(color)
						.overlapAllowed(true)
						.resizable(true)
						.draggable(true)
						.build();
				eventModel.addEvent(event);
			}
		}
	}


	/************************************Register***********************************************************************/	

    @Autowired
    private AdminRepository userRepository;
    
	public void register(){
			
		if (this.event1.getId_cra() == null) {
			
			this.event1.setAdministrateur(this.currentAdmin);
			this.event1.setMois(this.event1.getStartDate().getMonth().plus(1));
		} 

		this.service.getCraRepository().save(this.event1);
		this.event1 = new Cra<>() ;
		this.event=new DefaultScheduleEvent<>();
		this.fetchEvents();
	} 
	
/**********************************************************************************************/	
	@Autowired
	private CraRepository craRepository;
    
	public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
		event = selectEvent.getObject();
		event1=craRepository.findByStartDate(event.getStartDate());
	}

	public void onViewChange(SelectEvent<String> selectEvent) {
		view = selectEvent.getObject();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "View Changed", "View:" + view);
		addMessage(message);
	}

	public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
		event = DefaultScheduleEvent.builder().startDate(selectEvent.getObject()).endDate(selectEvent.getObject().plusHours(1)).build();
		event1.setStartDate(selectEvent.getObject());
		event1.setEndDate(selectEvent.getObject().plusHours(1));
	}

	public void onEventMove(ScheduleEntryMoveEvent moveEvent) {
	
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Modification Réussie", "");
		addMessage(message);
		
		String delta = ""+ moveEvent.getDeltaAsDuration();
		
		if (delta.substring(2,3).equals("-")) {
			String delta1 = delta.substring(3,delta.length()-1);
			int deltaInt = Integer.parseInt(delta1)/24;  
			
			event1 = craRepository.findByStartDate(moveEvent.getScheduleEvent().getStartDate().plusDays(deltaInt));			
		}
		else {
			String delta1 = delta.substring(2,delta.length()-1);
			int deltaInt = Integer.parseInt(delta1)/24;  			
			
			event1 = craRepository.findByStartDate(moveEvent.getScheduleEvent().getStartDate().minusDays(deltaInt));						
		}
		
		event1.setStartDate(moveEvent.getScheduleEvent().getStartDate());
		event1.setEndDate(moveEvent.getScheduleEvent().getEndDate());
		
		this.service.getCraRepository().save(event1);
		event1= new Cra<>();
			
	}

	public void onEventDelete() {
			this.service.getCraRepository().delete(event1);
			this.event1 = new Cra<>();
			this.fetchEvents();
	}
	
	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Start-Delta:" + event.getDeltaStartAsDuration() + ", End-Delta: " + event.getDeltaEndAsDuration());

		addMessage(message);
	}
	
	
/***************************************Getters & Setters**************************************************************/
	
	public ScheduleEvent<?> getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent<?> event) {
		this.event = event;
	}

	public Cra<?> getEvent1() {
		return event1;
	}

	public void setEvent1(Cra<?> event1) {
		this.event1 = event1;
	}

	public List<Cra<?>> getEvents() {
		this.events = new ArrayList<>();
		this.service.getCraRepository().findAll().forEach(events::add);
		return events;
	}

	public void setEvents(List<Cra<?>> events) {
		this.events = events;
	}

	public CraService getService() {
		return service;
	}

	public void setService(CraService service) {
		this.service = service;
	}
	
	public ScheduleModel getEventModel() {
		return eventModel;
	}
	
	public Administrateur getCurrentAdmin() {
		return currentAdmin;
	}

	public void setCurrentAdmin(Administrateur currentAdmin) {
		this.currentAdmin = currentAdmin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public AdminRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(AdminRepository userRepository) {
		this.userRepository = userRepository;
	}

	public CraRepository getCraRepository() {
		return craRepository;
	}

	public void setCraRepository(CraRepository craRepository) {
		this.craRepository = craRepository;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	
	}
	
	public Month getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(Month currentMonth) {
		this.currentMonth = currentMonth;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public double getJoursTravailles() {
		return joursTravailles;
	}

	public void setJoursTravailles(double joursTravailles) {
		this.joursTravailles = joursTravailles;
	}
	
/****************************************** Getters & Setters ***************************************************************************/
	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public boolean isShowWeekends() {
		return showWeekends;
	}

	public void setShowWeekends(boolean showWeekends) {
		this.showWeekends = showWeekends;
	}

	public boolean isSlotEventOverlap() {
		return slotEventOverlap;
	}

	public void setSlotEventOverlap(boolean slotEventOverlap) {
		this.slotEventOverlap = slotEventOverlap;
	}

	public boolean isShowWeekNumbers() {
		return showWeekNumbers;
	}

	public void setShowWeekNumbers(boolean showWeekNumbers) {
		this.showWeekNumbers = showWeekNumbers;
	}

	public boolean isShowHeader() {
		return showHeader;
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public boolean isTooltip() {
		return tooltip;
	}

	public void setTooltip(boolean tooltip) {
		this.tooltip = tooltip;
	}

	public boolean isRtl() {
		return rtl;
	}

	public void setRtl(boolean rtl) {
		this.rtl = rtl;
	}

	public boolean isAllDaySlot() {
		return allDaySlot;
	}

	public void setAllDaySlot(boolean allDaySlot) {
		this.allDaySlot = allDaySlot;
	}

	public double getAspectRatio() {
		return aspectRatio == 0 ? Double.MIN_VALUE : aspectRatio;
	}

	public void setAspectRatio(double aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public String getLeftHeaderTemplate() {
		return leftHeaderTemplate;
	}

	public void setLeftHeaderTemplate(String leftHeaderTemplate) {
		this.leftHeaderTemplate = leftHeaderTemplate;
	}

	public String getCenterHeaderTemplate() {
		return centerHeaderTemplate;
	}

	public void setCenterHeaderTemplate(String centerHeaderTemplate) {
		this.centerHeaderTemplate= centerHeaderTemplate;
	}

	public String getRightHeaderTemplate() {
		return rightHeaderTemplate;
	}

	public void setRightHeaderTemplate(String rightHeaderTemplate) {
		this.rightHeaderTemplate= rightHeaderTemplate;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getNextDayThreshold() {
		return nextDayThreshold;
	}

	public void setNextDayThreshold(String nextDayThreshold) {
		this.nextDayThreshold = nextDayThreshold;
	}

	public String getWeekNumberCalculation() {
		return weekNumberCalculation;
	}

	public void setWeekNumberCalculation(String weekNumberCalculation) {
		this.weekNumberCalculation = weekNumberCalculation;
	}

	public String getWeekNumberCalculator() {
		return weekNumberCalculator;
	}

	public void setWeekNumberCalculator(String weekNumberCalculator) {
		this.weekNumberCalculator = weekNumberCalculator;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getSlotDuration() {
		return slotDuration;
	}

	public void setSlotDuration(String slotDuration) {
		this.slotDuration = slotDuration;
	}

	public String getSlotLabelInterval() {
		return slotLabelInterval;
	}

	public void setSlotLabelInterval(String slotLabelInterval) {
		this.slotLabelInterval = slotLabelInterval;
	}

	public String getSlotLabelFormat() {
		return slotLabelFormat;
	}

	public void setSlotLabelFormat(String slotLabelFormat) {
		this.slotLabelFormat = slotLabelFormat;
	}

	public String getDisplayEventEnd() {
		return displayEventEnd;
	}

	public void setDisplayEventEnd(String displayEventEnd) {
		this.displayEventEnd = displayEventEnd;
	}

	public String getScrollTime() {
		return scrollTime;
	}

	public void setScrollTime(String scrollTime) {
		this.scrollTime = scrollTime;
	}

	public String getMinTime() {
		return minTime;
	}

	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getClientTimeZone() {
		return clientTimeZone;
	}

	public void setClientTimeZone(String clientTimeZone) {
		this.clientTimeZone = clientTimeZone;
	}

	public String getColumnHeaderFormat() {
		return columnHeaderFormat;
	}

	public void setColumnHeaderFormat(String columnHeaderFormat) {
		this.columnHeaderFormat = columnHeaderFormat;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	
/******************************************************************************************/	
	@Bean(name="entityManagerFactory")
	public LocalSessionFactoryBean sessionFactory() {
	    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

	    return sessionFactory;
	} 
	

}
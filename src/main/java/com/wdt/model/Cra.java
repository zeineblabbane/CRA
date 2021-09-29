package com.wdt.model;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
import org.springframework.stereotype.Component;


@Entity
@Table(name = "cra", catalog = "wdt")
@Component
public class Cra<T> extends DefaultScheduleEvent<T> implements ScheduleEvent<T>{
	private static final long serialVersionUID = 1L;

    private Integer id_cra;
    private Month mois;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean demiJournee;
    private Administrateur administrateur;
    
    @Transient
    private T object;
    
    public Cra() {
    }
    
    @Transient
    public T getObject() {
        return object;
    }
    
    public void setObject(T object) {
		this.object = object;
	}
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_cra", unique = true, nullable = false)
    public Integer getId_cra() {
		return id_cra;
	}

	public void setId_cra(Integer id_cra) {
		this.id_cra = id_cra;
	}

	

	@Column(name = "mois", nullable = false, length = 45)
    public Month getMois() {
		return mois;
	}

	public void setMois(Month month) {
		this.mois = month;
	}

	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional=false)
	@JoinColumn(name = "idAdmin", nullable = false)
	public Administrateur getAdministrateur() {
		return administrateur;
	}

	public void setAdministrateur(Administrateur administrateur) {
		this.administrateur = administrateur;
	}

   
    @Column(name = "title", nullable = false, length = 45)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
    @Column(name = "startDate", nullable = false)
    public LocalDateTime getStartDate() {
        return startDate;
    }

     
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    
    @Column(name = "endDate", nullable = false)
    public LocalDateTime getEndDate() {
        return endDate;
    }

     
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    
    @Column(name = "demi_journee", nullable = false)
    public boolean isDemiJournee() {
        return demiJournee;
    }

     
    public void setDemiJournee(boolean demiJournee) {
        this.demiJournee = demiJournee;
    }

    public int hashCode() {
        return Objects.hash(title, startDate, endDate);
    }
  
    public String toString() {
        return "Cra{title=" + title + ",startDate=" + startDate + ",endDate=" + endDate + "}";
    }

	

}

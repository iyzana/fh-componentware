package de.fh_dortmund.inf.cw.chat.server.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries(value = {
		@NamedQuery(name="CommonStatistic.findAll", query="select s from CommonStatistic s"),
		@NamedQuery(name="CommonStatistic.findLast", query="select s from CommonStatistic s order by s.startingDate desc")
})
public class CommonStatistic extends Statistic {
	@Id
	@GeneratedValue
	private long id;
	
	private Date startingDate;
	private Date endDate;

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}

package de.fh_dortmund.inf.cw.chat.server.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQuery(name="UserStatistic.findAll", query="select s from UserStatistic s")
public class UserStatistic extends Statistic {
	@Id
	@GeneratedValue
	private long id;
	
	private Date lastLogin;

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
}

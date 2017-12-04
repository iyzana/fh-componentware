package de.fh_dortmund.inf.cw.chat.server.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries(value = {
		@NamedQuery(name="User.findAll", query="select u from User u"),
		@NamedQuery(name="User.findByName", query="select u from User u where u.name = :name")
})
public class User extends TrackedEntity {
	@Id
	@GeneratedValue
	private long id;
	
	@Column(unique = true, nullable = false, length=50)
	private String name;
	@Column(nullable = false, length=128)
	private String pw;

	@OneToOne(cascade = CascadeType.ALL)
	private UserStatistic stat;

	public UserStatistic getStat() {
		return stat;
	}

	public void setStat(UserStatistic stat) {
		this.stat = stat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
}

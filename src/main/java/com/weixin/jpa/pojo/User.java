package com.weixin.jpa.pojo;/*
package com.weixin.jpa.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class User {

	@Id
	@GeneratedValue
	@Column(name = "t_id")
	private Integer id;
	
	@Column(name = "t_name")
	@NotNull(message = "名称不能为空")
	private String name;
	
	@Column(name = "t_date")
	private Date date;

	public User(){}
	
	public User(Integer id,String name,Date date) {
		this.id = id;
		this.name = name;
		this.date = date;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
*/

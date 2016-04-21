package my.vaadin.app;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


import com.vaadin.data.fieldgroup.PropertyId;

/**
 * A entity object, like in any other Java application. In a typical real world
 * application this could for example be a JPA entity.
 */
@SuppressWarnings("serial")
public class Customer implements Serializable, Cloneable {

	private Long id;

	private String name = "";

	private String model = "";

    @Min(1000)
    @Max(10000)
    @NotNull(message="Please enter a valid email address.")
    @PropertyId("year")
	private Integer year;

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	private Date birthDate;

	private CustomerStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
	}

	public String getModel() {
		return model;
	}

	/**
	 * Set the value of lastName
	 *
	 * @param lastName
	 *            new value of lastName
	 */
	public void setModel(String lastName) {
		this.model = lastName;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		if (year < 1000) {
			MyUI.label.setValue("WRONG DATA!!!");
			
		}else{
			this.year = year;
		}
	}

	/**
	 * Get the value of firstName
	 *
	 * @return the value of firstName
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the value of firstName
	 *
	 * @param firstName
	 *            new value of firstName
	 */
	public void setName(String name) {
		this.name = name;
	}

	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (this.id == null) {
			return false;
		}

		if (obj instanceof Customer && obj.getClass().equals(getClass())) {
			return this.id.equals(((Customer) obj).id);
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 43 * hash + (id == null ? 0 : id.hashCode());
		return hash;
	}

	@Override
	public Customer clone() throws CloneNotSupportedException {
		return (Customer) super.clone();
	}

	@Override
	public String toString() {
		return name + " " + model;
	}
}
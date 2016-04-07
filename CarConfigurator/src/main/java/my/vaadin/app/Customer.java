package my.vaadin.app;

import java.io.Serializable;
import java.util.Date;

/**
 * A entity object, like in any other Java application. In a typical real world
 * application this could for example be a JPA entity.
 */
@SuppressWarnings("serial")
public class Customer implements Serializable, Cloneable {

	private Long id;

	private String name = "";

	private String model = "";

	private Integer year;

	private String type;

	private CustomerStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the value of email
	 *
	 * @return the value of email
	 */

	/**
	 * Set the value of email
	 *
	 * @param email
	 *            new value of email
	 */

	/**
	 * Get the value of status
	 *
	 * @return the value of status
	 */
	public CustomerStatus getStatus() {
		return status;
	}

	/**
	 * Set the value of status
	 *
	 * @param status
	 *            new value of status
	 */
	public void setStatus(CustomerStatus status) {
		this.status = status;
	}

	/**
	 * Get the value of lastName
	 *
	 * @return the value of lastName
	 */
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
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
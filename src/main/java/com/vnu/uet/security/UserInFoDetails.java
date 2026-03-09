package com.vnu.uet.security;

public class UserInFoDetails {

	/**
	 *
	 */
	private Long id;
	private String login;

	public UserInFoDetails() {
		super();
	}

	public UserInFoDetails(Long id, String login, String email, Long custId, String orgIn, String folderPath,
                           String folderId, String dbSuffix) {
		super();
		this.id = id;
		this.login = login;
		this.email = email;
		this.custId = custId;
		this.orgIn = orgIn;
		this.folderPath = folderPath;
		this.folderId = folderId;
		this.dbSuffix = dbSuffix;
	}

	private String email;
	private Long custId;
	private String orgIn;
	private String folderPath;
	private String folderId;
	private String dbSuffix;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getOrgIn() {
		return orgIn;
	}

	public void setOrgIn(String orgIn) {
		this.orgIn = orgIn;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getDbSuffix() {
		return dbSuffix;
	}

	public void setDbSuffix(String dbSuffix) {
		this.dbSuffix = dbSuffix;
	}

}

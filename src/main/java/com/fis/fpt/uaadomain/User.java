package com.fis.fpt.uaadomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class User extends AbstractAuditingEntity implements Serializable {

    public static final String Column_Base = " id , login ,null as password_hash, first_name , last_name ,address , gender , email , activated , lang_key ,image_url, activation_key , reset_key , reset_date , phone , dob , null avatar ,null signature , cust_id , org_in , db_suffix , blocked , folder_path , folder_id , atm_activated , smd_activated , null digital_signature , created_by , created_date , last_modified_by , last_modified_date , last_modified_password_date ";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
//	@Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 2)
    @Column(name = "gender", nullable = false)
    private String gender;

    @Size(max = 255)
    @Column(name = "address", length = 50)
    private String address;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    // @Pattern(regexp = Constants.PHONE_REGEX)
    @Size(min = 1, max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "dob")
    private Instant dob;

    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "signature")
    private byte[] signature;
    @NotNull
    @Column(name = "cust_id")
    private Long custId;
    @NotNull
    @Size(max = 256)
    @Column(name = "org_in")
    private String orgIn;
    @NotNull
    @Size(max = 5)
    @Column(name = "db_suffix")
    private String dbSuffix;

    @Column(name = "blocked")
    private Boolean blocked;

    @Size(max = 256)
    @Column(name = "folder_path")
    private String folderPath;

    @Size(max = 256)
    @Column(name = "folder_id")
    private String folderId;

    @Column(name = "atm_activated")
    private Boolean atmActivated;

    @Column(name = "smd_activated")
    private Instant smdActivated;

    @Type(type = "json")
    @Column(name = "digital_signature", columnDefinition = "json")
    private FieldDesign digitalSignature;

    //	@LastModifiedDate
    @Column(name = "last_modified_password_date")
    @JsonIgnore
    private Instant lastModifiedPasswordDate;

    public Instant getLastModifiedPasswordDate() {
        return lastModifiedPasswordDate;
    }

    public void setLastModifiedPasswordDate(Instant lastModifiedPasswordDate) {
        this.lastModifiedPasswordDate = lastModifiedPasswordDate;
    }

    public FieldDesign getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(FieldDesign digitalSignature) {
        this.digitalSignature = digitalSignature;
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

    public String getDbSuffix() {
        return dbSuffix;
    }

    public void setDbSuffix(String dbSuffix) {
        this.dbSuffix = dbSuffix;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "jhi_user_authority", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id"), @JoinColumn(name = "org_in", referencedColumnName = "org_in") }, inverseJoinColumns = {
        @JoinColumn(name = "authority_name", referencedColumnName = "name") })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    public UserPasswordRepositoryDTO toUserPasswordRepo(String mode) {

        UserPasswordRepositoryDTO userPasswordRepositoryDTO = new UserPasswordRepositoryDTO();
        ZonedDateTime now = ZonedDateTime.now();
        userPasswordRepositoryDTO.setType(mode);
        userPasswordRepositoryDTO.setUserId(this.getId());
        userPasswordRepositoryDTO.setPasswordHash(this.getPassword());
        userPasswordRepositoryDTO.setStatus(this.getBlocked());
        userPasswordRepositoryDTO.setCustId(this.getCustId());
        userPasswordRepositoryDTO.setOrgIn(this.getOrgIn());
        userPasswordRepositoryDTO.setCreatedDate(now);
        userPasswordRepositoryDTO.setCreatedBy(this.getLogin());
        userPasswordRepositoryDTO.setLastModifiedBy(this.getLogin());
        userPasswordRepositoryDTO.setLastModifiedDate(now);

        return userPasswordRepositoryDTO;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getDob() {
        return dob;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public Boolean getAtmActivated() {
        return atmActivated;
    }

    public void setAtmActivated(Boolean atmActivated) {
        this.atmActivated = atmActivated;
    }

    public User atmActivated(Boolean atmActivated) {
        this.atmActivated = atmActivated;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Instant getSmdActivated() {
        return smdActivated;
    }

    public User smdActivated(Instant smdActivated) {
        this.smdActivated = smdActivated;
        return this;
    }

    public void setSmdActivated(Instant smdActivated) {
        this.smdActivated = smdActivated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", login=" + login + ", password=" + password + ", firstName=" + firstName
            + ", lastName=" + lastName + ", email=" + email + ", activated=" + activated + ", langKey=" + langKey
            + ", imageUrl=" + imageUrl + ", activationKey=" + activationKey + ", resetKey=" + resetKey
            + ", resetDate=" + resetDate + ", phone=" + phone + ", dob=" + dob + ", avatar="
            + Arrays.toString(avatar) + ", signature=" + Arrays.toString(signature) + ", custId=" + custId
            + ", orgIn=" + orgIn + ", dbSuffix=" + dbSuffix + ", blocked=" + blocked + ", folderPath=" + folderPath
            + ", folderId=" + folderId + ", authorities=" + authorities + "]";
    }

}

package com.fis.fpt.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A VariableSystem.
 */
@Entity
@Table(name = "variable_system")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariableSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "variable_system")
    private String variableSystem;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "org_in")
    private String orgIn;

    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

}

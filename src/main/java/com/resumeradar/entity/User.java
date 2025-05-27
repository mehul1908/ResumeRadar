package com.resumeradar.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

	private static final long serialVersionUID = -7068646083309194098L;

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String name;

	@Column(unique = true, nullable = false)
    private String email;

	@JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // Enum: JOB_SEEKER, RECRUITER, ADMIN

    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user")
    private Resume resumes;

    @JsonIgnore
    @OneToMany(mappedBy = "recruiter")
    private List<Job> postedJobs;
    
    public User(String name, String email, String password, String role) {
		super();
		this.name = name;
		this.email = email;
		this.password = password; 
		this.role = Role.valueOf(role);
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return UserDetails.super.isAccountNonExpired();
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return UserDetails.super.isAccountNonLocked();
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return UserDetails.super.isCredentialsNonExpired();
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return UserDetails.super.isEnabled();
	}
}


package com.wellsfargo.counselor.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// --------------------- FinancialAdvisor Entity ---------------------

@Entity
@Table(name = "financial_advisor")
@Data
@NoArgsConstructor
class FinancialAdvisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advisorId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "advisor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Client> clients = new ArrayList<>();

    public void addClient(Client client) {
        clients.add(client);
        client.setAdvisor(this);
    }

    public void removeClient(Client client) {
        clients.remove(client);
        client.setAdvisor(null);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

// --------------------- Client Entity ---------------------
@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advisor_id", nullable = false)
    private FinancialAdvisor advisor;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Portfolio portfolio;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        if (portfolio != null && portfolio.getClient() != this) {
            portfolio.setClient(this);
        }
    }
}

// --------------------- Portfolio Entity ---------------------
@Entity
@Table(name = "portfolio")
@Data
@NoArgsConstructor
class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolioId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private Client client;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Security> securities = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addSecurity(Security security) {
        securities.add(security);
        security.setPortfolio(this);
    }

    public void removeSecurity(Security security) {
        securities.remove(security);
        security.setPortfolio(null);
    }
}

// --------------------- Security Entity ---------------------
@Entity
@Table(name = "security")
@Data
@NoArgsConstructor
class Security {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long securityId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String category;

    private LocalDate purchaseDate;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal purchasePrice;

    @Column(precision = 15, scale = 4, nullable = false)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
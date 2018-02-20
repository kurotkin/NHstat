package com.kurotkin.model.mysql;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "profitability_algo")
public class ProfitabilityAlgorithms {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "scrypt")
    private BigDecimal scrypt;

    @Column(name = "sha256")
    private BigDecimal sha256;

    @Column(name = "scryptnf")
    private BigDecimal scryptnf;

    @Column(name = "X11")
    private BigDecimal X11;

    @Column(name = "x13")
    private BigDecimal paying;

    @Column(name = "keccak")
    private BigDecimal keccak;

    @Column(name = "x15")
    private BigDecimal x15;

    @Column(name = "nist5")
    private BigDecimal nist5;

    @Column(name = "neoscrypt")
    private BigDecimal neoscrypt;

    @Column(name = "lyra2re")
    private BigDecimal lyra2re;

    @Column(name = "whirlpoolx")
    private BigDecimal whirlpoolx;

    @Column(name = "qubit")
    private BigDecimal qubit;

    @Column(name = "quark")
    private BigDecimal quark;

    @Column(name = "axiom")
    private BigDecimal axiom;

    @Column(name = "lyra2rev2")
    private BigDecimal lyra2rev2;

    public ProfitabilityAlgorithms() {
    }

    public ProfitabilityAlgorithms(Long id,
                                   BigDecimal scrypt,
                                   BigDecimal sha256,
                                   BigDecimal scryptnf,
                                   BigDecimal x11,
                                   BigDecimal paying,
                                   BigDecimal keccak,
                                   BigDecimal x15,
                                   BigDecimal nist5,
                                   BigDecimal neoscrypt,
                                   BigDecimal lyra2re,
                                   BigDecimal whirlpoolx,
                                   BigDecimal qubit,
                                   BigDecimal quark,
                                   BigDecimal axiom,
                                   BigDecimal lyra2rev2) {
        this.id = id;
        this.scrypt = scrypt;
        this.sha256 = sha256;
        this.scryptnf = scryptnf;
        X11 = x11;
        this.paying = paying;
        this.keccak = keccak;
        this.x15 = x15;
        this.nist5 = nist5;
        this.neoscrypt = neoscrypt;
        this.lyra2re = lyra2re;
        this.whirlpoolx = whirlpoolx;
        this.qubit = qubit;
        this.quark = quark;
        this.axiom = axiom;
        this.lyra2rev2 = lyra2rev2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getScrypt() {
        return scrypt;
    }

    public void setScrypt(BigDecimal scrypt) {
        this.scrypt = scrypt;
    }

    public BigDecimal getSha256() {
        return sha256;
    }

    public void setSha256(BigDecimal sha256) {
        this.sha256 = sha256;
    }

    public BigDecimal getScryptnf() {
        return scryptnf;
    }

    public void setScryptnf(BigDecimal scryptnf) {
        this.scryptnf = scryptnf;
    }

    public BigDecimal getX11() {
        return X11;
    }

    public void setX11(BigDecimal x11) {
        X11 = x11;
    }

    public BigDecimal getPaying() {
        return paying;
    }

    public void setPaying(BigDecimal paying) {
        this.paying = paying;
    }

    public BigDecimal getKeccak() {
        return keccak;
    }

    public void setKeccak(BigDecimal keccak) {
        this.keccak = keccak;
    }

    public BigDecimal getX15() {
        return x15;
    }

    public void setX15(BigDecimal x15) {
        this.x15 = x15;
    }

    public BigDecimal getNist5() {
        return nist5;
    }

    public void setNist5(BigDecimal nist5) {
        this.nist5 = nist5;
    }

    public BigDecimal getNeoscrypt() {
        return neoscrypt;
    }

    public void setNeoscrypt(BigDecimal neoscrypt) {
        this.neoscrypt = neoscrypt;
    }

    public BigDecimal getLyra2re() {
        return lyra2re;
    }

    public void setLyra2re(BigDecimal lyra2re) {
        this.lyra2re = lyra2re;
    }

    public BigDecimal getWhirlpoolx() {
        return whirlpoolx;
    }

    public void setWhirlpoolx(BigDecimal whirlpoolx) {
        this.whirlpoolx = whirlpoolx;
    }

    public BigDecimal getQubit() {
        return qubit;
    }

    public void setQubit(BigDecimal qubit) {
        this.qubit = qubit;
    }

    public BigDecimal getQuark() {
        return quark;
    }

    public void setQuark(BigDecimal quark) {
        this.quark = quark;
    }

    public BigDecimal getAxiom() {
        return axiom;
    }

    public void setAxiom(BigDecimal axiom) {
        this.axiom = axiom;
    }

    public BigDecimal getLyra2rev2() {
        return lyra2rev2;
    }

    public void setLyra2rev2(BigDecimal lyra2rev2) {
        this.lyra2rev2 = lyra2rev2;
    }
}

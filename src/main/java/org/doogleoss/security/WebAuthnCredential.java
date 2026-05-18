package org.doogleoss.security;

import java.util.List;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.webauthn.WebAuthnCredentialRecord;
import io.quarkus.security.webauthn.WebAuthnCredentialRecord.RequiredPersistedData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "webauthn_credential")
public class WebAuthnCredential extends PanacheEntityBase {

    @Id
    @Column(name = "credential_id", nullable = false, updatable = false)
    public String credentialId;
    @Column(name = "public_key")
    public byte[] publicKey;
    @Column(name = "public_key_algorithm", nullable = false)
    public long publicKeyAlgorithm;
    @Column(name = "counter", nullable = false)
    public long counter;
    @Column(name = "aaguid")
    public UUID aaguid;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    public User user;

    public WebAuthnCredential() {}

    public WebAuthnCredential(WebAuthnCredentialRecord webAuthnCredentialRecord, User user) {
        RequiredPersistedData data = webAuthnCredentialRecord.getRequiredPersistedData();
        this.credentialId = data.credentialId();
        this.publicKey = data.publicKey();
        this.publicKeyAlgorithm = data.publicKeyAlgorithm();
        this.counter = data.counter();
        this.aaguid = data.aaguid();
        this.user = user;
        user.webAuthnCredential = this;
    }

    public WebAuthnCredentialRecord toRecord() {
        return WebAuthnCredentialRecord.fromRequiredPersistedData(new RequiredPersistedData(
                user.username, credentialId, aaguid, publicKey, publicKeyAlgorithm, counter));
    }

    public static List<WebAuthnCredential> findByUsername(String username) {
        return list("user.username", username);
    }

    public static WebAuthnCredential findByCredentialId(String credentialId) {
        return findById(credentialId);
    }
}

package org.doogleoss.security;

import java.util.List;
import java.util.Set;

import io.quarkus.logging.Log;
import io.quarkus.security.webauthn.WebAuthnCredentialRecord;
import io.quarkus.security.webauthn.WebAuthnUserProvider;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@Blocking
@ApplicationScoped
public class WebAuthnSetup implements WebAuthnUserProvider {

    @Override
    @Transactional
    public Uni<List<WebAuthnCredentialRecord>> findByUsername(String username) {
        return Uni.createFrom().item(WebAuthnCredential.findByUsername(username).stream()
                .map(WebAuthnCredential::toRecord).toList());
    }

    @Override
    @Transactional
    public Uni<WebAuthnCredentialRecord> findByCredentialId(String credentialId) {
        WebAuthnCredential credential = WebAuthnCredential.findByCredentialId(credentialId);
        Log.infof("findByCredentialId: credentialId=%s, found=%s", credentialId, credential != null);

        if (credential == null) {
            return Uni.createFrom().failure(new RuntimeException("Credential not found"));
        }
        return Uni.createFrom().item(credential.toRecord());
    }

    @Override
    @Transactional
    public Uni<Void> store(WebAuthnCredentialRecord record) {

        var existingUser = User.findByUsername(record.getUsername());
        User user = existingUser != null ? existingUser : new User();


        user.username = record.getUsername();

        WebAuthnCredential credential = new WebAuthnCredential(record, user);
        if (existingUser == null) {
            user.persist();
        }
        credential.persist();

        return Uni.createFrom().voidItem();
    }

    @Override
    @Transactional
    public Uni<Void> update(String credentialId, long counter) {
        WebAuthnCredential credential = WebAuthnCredential.findByCredentialId(credentialId);
        if (credential != null) {
            credential.counter = counter;
        }
        return Uni.createFrom().voidItem();
    }

    @Override
    public Set<String> getRoles(String username) {
        if ("admin".equals(username)) {
            return Set.of("user", "admin");
        }
        return Set.of("user");
    }
}

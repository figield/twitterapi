package com.twitter.domain.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode(of = "reference")
public class AccountReference {

    @NonNull
    String reference;

    public AccountReference clone() {
        return AccountReference.of(this.getReference());
    }
}


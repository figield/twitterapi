package com.twitter.domain.model

import spock.lang.Specification

class AccountReferenceSpec extends Specification {


    def "Should clone accountReference"() {

        when:
            AccountReference accountReferenceClone = accountReference.clone()
        then:
            accountReferenceClone.equals(accountReference)
            !accountReferenceClone.is(accountReference)
        where:
            accountReference = AccountReference.of("username")

    }

}

package org.bracit.bcb_player_onboarding_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDetails {
    private Address presentAddressObj;
    private boolean permanentSameAsPresent;
    private Address permanentAddressObj;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String divisionId;
        private String districtId;
        private String upazilaId;
        private String unionId;
        private String house;
        private String village;
        private String po;
        private String postcode;
    }
}

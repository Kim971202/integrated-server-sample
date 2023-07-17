package com.daesung.domain.dggw.web.data;

import com.daesung.domain.dggw.web.RequestCommonValue;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class EnergyUseTarget extends RequestCommonValue {
    private String energyType;
    private String targetUsage;
}

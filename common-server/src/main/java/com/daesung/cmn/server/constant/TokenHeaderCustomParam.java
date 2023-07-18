package com.daesung.cmn.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenHeaderCustomParam {
    SID("_sid"), DID("_did"), TID("_tid");

    private String key;
}

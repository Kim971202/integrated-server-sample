package com.daesung.cmn.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenPayload {

        HID("_hid"), IOT("_iot"), FID("_fid");

        private String key;

}

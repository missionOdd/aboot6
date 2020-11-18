/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.annotation.type;

/**
 * @author mission
 * @since 2019/12/31 9:39
 */
public enum DictType {
    //总是显示
    ALWAYS,
    //传参数显示 &showDict =1
    PARAMETER,
    //传参数显示,能够获取所有字典 showDict=user_status,dept_status or &showDict=1
    CUSTOMER
}

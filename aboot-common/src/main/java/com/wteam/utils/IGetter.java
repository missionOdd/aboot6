/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.utils;

import java.io.Serializable;


/**
 * getter方法接口定义
 * @author mission
 * @since 2020/07/29 17:29
 */
@FunctionalInterface
public interface IGetter<T> extends Serializable {
    Object apply(T source);
}

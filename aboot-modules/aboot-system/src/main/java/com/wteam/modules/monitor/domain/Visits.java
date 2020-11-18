/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.monitor.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 访问记录 持久类
 * @author mission
 * @since 2019/07/11 20:22
 */
@Entity
@Data
@Table(name = "sys_visits")
public class Visits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    @Column(name = "pv_counts")
    private Long pvCounts;

    @Column(name = "ip_counts")
    private Long ipCounts;

    @Column(name = "created_by")
    private Timestamp createdBy;

    @Column(name = "week_day")
    private String weekDay;
}

/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.domain.dto;


import com.wteam.domain.ColumnInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mission
 * @since 2019/09/27 17:21
 */
@Data
public class GenForm {

  @NotNull(message = "columnInfos不能为空")
  private List<ColumnInfo> columnInfos;

  @NotNull(message = "tableName不能为空")
  private String tableName;

  @NotNull(message = "remark 不能为空")
  private String remark ;
}

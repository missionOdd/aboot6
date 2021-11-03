/*
 * copyleft © 2019-2021
 */

package com.wteam.utils.enums;

/**
 * @author mission
 * @since 2020/08/29 18:17
 */
public enum OptionType {
	FUNCTION,   //执行函数
	AND,        //添加and
	OR,         //添加or
	BETWEEN,    //between x1 and x2
	STARTS_LIKE,//like %s
	END_LIKE,   //like s%
	LIKE,       //like %s%
	EQ,         //=
	GT,         //>
	LT,         //<
	GE,         //>=
	LE,         //<=
	IN,         //in
	TRUE,       //is true
	FALSE,      //is false
	//REGEXP,     //regexp(propName,regex)
	IS_NULL,    // is null
	IS_NOT_NULL,//is not null
	NOT_EQ,     //!=
	NOT_LIKE,   //not like %s%
	NOT_STARTS_LIKE,//not like %s
	NOT_END_LIKE,   //not like s%
}

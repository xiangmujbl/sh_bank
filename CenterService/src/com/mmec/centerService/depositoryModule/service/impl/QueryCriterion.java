package com.mmec.centerService.depositoryModule.service.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import com.mmec.centerService.depositoryModule.service.Criterion;


public class QueryCriterion implements Criterion {

	private String fieldName;       //属性名  
    private Object value;           //对应值  
    private Operator operator;      //计算符  
    private String[] fieldNames;       //属性名  
    private Operator[] operators;      //计算符  
    protected QueryCriterion(String fieldName, Object value, Operator operator) {  
        this.fieldName = fieldName;  
        this.value = value;  
        this.operator = operator;  
    }  
    protected QueryCriterion(String[] fieldNames, Object value, Operator[] operators) {  
        this.value = value;  
        this.operators = operators;   
        this.fieldNames = fieldNames;   
    }  
    protected QueryCriterion(String fieldName, Operator operator) {  
        this.fieldName = fieldName;  
        this.operator = operator;   
    }  
    public String getFieldName() {  
        return fieldName;  
    }  
    public Object getValue() {  
        return value;  
    }  
    public Operator getOperator() {  
        return operator;  
    }  
    @SuppressWarnings({ "rawtypes", "unchecked" })  
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,  
            CriteriaBuilder builder) {  
        Path[] expression = new Path[10];  
        if(null != fieldNames && fieldNames.length > 0){  
        	for(int i = 0;i<fieldNames.length;i++)
        	{
	        	if(fieldNames[i].contains(".")){  
	                String[] names = StringUtils.split(fieldNames[i], ".");  
	                expression[i] = root.get(names[0]).get(names[1]);  
	            }else{  
	                expression[i] = root.get(fieldNames[i]);  
	            }  
        	}
        	Predicate[] predicates = new Predicate[fieldNames.length];
        	for(int i= 0 ;i<fieldNames.length; i++)
            {
        		operator = operators[i];
        		switch (operator ) {  
                case EQ:  
                	predicates[i] = builder.equal(expression[i], value);  
                	break;
                case NE:  
                	predicates[i] = builder.notEqual(expression[i], value);  
                	break;
                case LIKE:  
                	predicates[i] = builder.like((Expression<String>) expression[i], "%" + value + "%");  
                	break;
                case LT:  
                	predicates[i] = builder.lessThan(expression[i], (Comparable) value);  
                	break;
                case GT:  
                	predicates[i] = builder.greaterThan(expression[i], (Comparable) value);  
                	break;
                case LTE:  
                	predicates[i] = builder.lessThanOrEqualTo(expression[i], (Comparable) value);  
                	break;
                case GTE:  
                	predicates[i] = builder.greaterThanOrEqualTo(expression[i], (Comparable) value);  
                	break;
                case NEQ:  
                	predicates[i] = builder.notEqual(expression[i], value);  
                	break;
                case NULL:  
                	predicates[i] = builder.isNull(expression[i]);  
                	break;
                case NNULL:  
                	predicates[i] = builder.isNotNull(expression[i]);  
                	break;
                default:  
                    return null;  
                }  
            }
        	switch (predicates.length) {  
            case 2:  
            	return builder.or(predicates[0],predicates[1]);
            case 3:  
            	return builder.or(predicates[0],predicates[1],predicates[2]);
            case 4:  
            	return builder.or(predicates[0],predicates[1],predicates[2],predicates[3]);
            default:  
                return null;  
            }  
        }
        else
        {
        	 if(fieldName.contains(".")){  
	            String[] names = StringUtils.split(fieldName, ".");  
	            expression[0] = root.get(names[0]);  
	            for (int i = 1; i < names.length; i++) {  
	                expression[0] = expression[0].get(names[i]);  
	            }  
	        }else{  
	            expression[0] = root.get(fieldName);  
	        }  
        	 
        	 switch (operator) {  
             case EQ:  
                 return builder.equal(expression[0], value);  
             case NE:  
                 return builder.notEqual(expression[0], value);  
             case LIKE:  
                 return builder.like((Expression<String>) expression[0], "%" + value + "%");  
             case LT:  
                 return builder.lessThan(expression[0], (Comparable) value);  
             case GT:  
                 return builder.greaterThan(expression[0], (Comparable) value);  
             case LTE:  
                 return builder.lessThanOrEqualTo(expression[0], (Comparable) value);  
             case GTE:  
                 return builder.greaterThanOrEqualTo(expression[0], (Comparable) value);  
             case NEQ:  
                 return builder.notEqual(expression[0], value);  
             case NULL:  
                 return builder.isNull(expression[0]);  
             case NNULL:  
                 return builder.isNotNull(expression[0]);  
             case OR:  
                 return builder.or(builder.equal(expression[0], value),builder.equal(expression[1], value));  
             default:  
                 return null;  
             }  
        }
    }  
}

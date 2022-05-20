package com.smd21.smdinsole.common;


import com.smd21.smdinsole.common.model.TokenUserModel;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author jf0004
 * @since 2020. 12. 18.
 */
public class CommonUtil {

    /**
     * Object type 변수가 비어있는지 체크
     *
     * @param obj
     * @return Boolean : true / false
     */
    public static Boolean empty(Object obj) {
        if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
        else if (obj instanceof List) return obj == null || ((List<?>) obj).isEmpty();
        else if (obj instanceof Map) return obj == null || ((Map<?, ?>) obj).isEmpty();
        else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
        else if (obj instanceof Long) return obj == null || parseLong(obj) == 0;
        else if (obj instanceof Integer) return obj == null || parseInt(obj) == 0;
        else return obj == null;
    }

	/**
	 * Object type 변수가 비어있지 않은지 체크
	 *
	 * @param obj
	 * @return Boolean : true / false
	 */
	public static Boolean notEmpty(Object obj) {
		return !empty(obj);
	}

	public static long parseLong(Object value) {
		long ret = 0;
		if ( value != null && value != "") {
			if ( value instanceof BigInteger ) {
				ret = ((BigInteger) value).longValue();
			} else if ( value instanceof String ) {
				ret = Long.valueOf( (String) value );
			} else if ( value instanceof BigDecimal ) {
				ret = ((BigDecimal) value).longValue();
			} else if ( value instanceof Number ) {
				ret = Long.valueOf( ((Number) value).longValue() );
			} else {
				throw new ClassCastException( "Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigInteger." );
			}
		}
		return ret;

	}

	public static int parseInt(Object value) {
		int ret = 0;
		if ( value != null && value != "") {
			if ( value instanceof BigInteger ) {
				ret = ((BigInteger) value).intValue();
			} else if ( value instanceof String ) {
				ret = Integer.valueOf( (String) value );
			} else if ( value instanceof BigDecimal ) {
				ret = ((BigDecimal) value).intValue();
			} else if ( value instanceof Number ) {
				ret = Integer.valueOf( ((Number) value).intValue() );
			} else {
				throw new ClassCastException( "Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigInteger." );
			}
		}
		return ret;
	}

	public static BigDecimal parseBigDecimal(Object value) {
		BigDecimal ret = BigDecimal.ZERO;
		if( value != null && value != "") {
			if( value instanceof BigDecimal ) {
				ret = (BigDecimal) value;
			} else if( value instanceof String ) {
				ret = new BigDecimal( (String) value );
			} else if( value instanceof BigInteger ) {
				ret = new BigDecimal( (BigInteger) value );
			} else if( value instanceof Number ) {
				ret = new BigDecimal( ((Number)value).doubleValue() );
			} else {
				throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
			}
		}
		return ret;
	}

	/**
	 * 두 지점간의 거리 계산
	 *
	 * @param lat1 지점 1 위도
	 * @param lon1 지점 1 경도
	 * @param lat2 지점 2 위도
	 * @param lon2 지점 2 경도
	 * @param unit 거리 표출단위
	 * @return
	 */
	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		if (unit == "kilometer") {
			dist = dist * 1.609344;
		} else if(unit == "meter"){
			dist = dist * 1609.344;
		}

		return (dist);
	}


	// This function converts decimal degrees to radians
	public static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// This function converts radians to decimal degrees
	public static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

}
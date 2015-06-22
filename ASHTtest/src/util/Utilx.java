/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author mahmud
 */
public class Utilx {
    public static java.util.Date convertToUtilDate(java.sql.Date date) {
        return new java.util.Date(date.getYear(), date.getMonth(), date.getDay());
    }
    public static java.sql.Date convertToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getYear(), date.getMonth(), date.getDay());
    }
    public static int comapreDates(java.util.Date first, java.util.Date second) {
        if (first.getYear() != second.getYear())
            return first.getYear() - second.getYear();
        else if (first.getMonth() != second.getMonth())
            return first.getMonth() - second.getMonth();
        else 
            return first.getDay() - second.getDay();
    }
    public static int comapreDates(java.sql.Date first, java.sql.Date second) {
        return comapreDates(convertToUtilDate(first), convertToUtilDate(second));
    }
    public static int comapreDates(java.sql.Date first, java.util.Date second) {
        return comapreDates(convertToUtilDate(first), second);
    }
    public static int comapreDates(java.util.Date first, java.sql.Date second) {
        return comapreDates(first, convertToUtilDate(second));
    }
}

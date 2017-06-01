package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

/**
 * Created by Chaly on 2017/3/6.
 */
public enum Relation {


    FATHER("F", "父亲"),
    MOTHER("M", "母亲"),
    SPOUSE("S", "配偶"),
    PARENT("S", "父母"),
    SIBLING("B", "兄弟/姐妹"),
    GIRLFRIEND_OR_BOYFRIEND("F", "未婚妻/未婚夫"),
    CHILDREN("C", "子女"),
    FRIEND("C", "朋友"),
    COLLEAGUE("C", "同事"),
    FAMILY("R", "亲戚"),
    BUSINESS("B", "商业关系"),
    EMPLOYER("P", "雇主"),
    SCHOOL("H", "学校"),
    OTHER("O", "其他");


//	<option value="R">RELATIVE</option>
//	<option value="S">SPOUSE</option>
//	<option value="C">FRIEND</option>
//	<option value="B">BUSINESS ASSOCIATE</option>
//	<option value="P">EMPLOYER</option>
//	<option value="H">SCHOOL OFFICIAL</option>
//	<option value="O">OTHER</option>
//  <option value="F">FIANCÉ/FIANCÉE</option>
//  <option value="C">CHILD</option>
//  <option value="B">SIBLING</option>

//  <option value="C">CHILD</option>
//  <option value="P">PARENT</option>
//  <option value="S">SPOUSE</option>
//  <option value="R">OTHER RELATIVE</option>
//  <option value="F">FRIEND</option>
//  <option selected="selected" value="O">OTHER</option>



    private String letter;

    public String getValue() {
        return letter;
    }

    private Relation(String letter, String value) {
        this.letter = letter;
        try {
            Field fieldName = getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, value);
            fieldName.setAccessible(false);
        } catch (Exception e) {
        }
    }
}

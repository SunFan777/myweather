package com.myweather.app.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Cn2Spell {  
	   
    /** 
    * ºº×Ö×ª»»Î»ººÓïÆ´ÒôÊ××ÖÄ¸£¬Ó¢ÎÄ×Ö·û²»±ä 
    * @param chines ºº×Ö 
    * @return Æ´Òô 
    */  
    public static String converterToFirstSpell(String chinese){         
        String pinyinName = "";  
        char[] nameChar = chinese.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        for (int i = 0; i < nameChar.length; i++) {  
            if (nameChar[i] > 128) {  
                try {  
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);  
                } catch (BadHanyuPinyinOutputFormatCombination e) {  
                    e.printStackTrace();  
                }  
            }else{  
                pinyinName += nameChar[i];  
            }  
        }  
        return pinyinName;  
    }  
   
    /** 
    * ºº×Ö×ª»»Î»ººÓïÆ´Òô£¬Ó¢ÎÄ×Ö·û²»±ä 
    * @param chines ºº×Ö 
    * @return Æ´Òô 
    */  
    public static String converterToSpell(String chinese){          
        String pinyinName = "";  
        char[] nameChar = chinese.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        for (int i = 0; i < nameChar.length; i++) {  
            if (nameChar[i] > 128) {  
                try {  
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];  
                } catch (BadHanyuPinyinOutputFormatCombination e) {  
                    e.printStackTrace();  
                }  
            }else{  
                pinyinName += nameChar[i];  
            }  
        }  
        return pinyinName;  
    }  
       
}

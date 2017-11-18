package com.example.user.homework.Detail;

//커스텀리스트 뷰에 담을 아이템 정보 클래스 선언
public class DetailCustomItem {
    int mIcon; // image resource
    String nName; // text
    String nAge;  // text
    String nValue;

    DetailCustomItem(int aIcon, String aName, String aAge, String aValue) {
        mIcon = aIcon;
        nName = aName;
        nAge = aAge;
        nValue = aValue;
    }
}
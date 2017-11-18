package com.example.user.homework.Detail;

//커스텀리스트 뷰에 담을 아이템 정보 클래스 선언
public class DetailCustomItem {
    String imagePath; // image resource
    String nName; // text
    String nPrice;  // text
    String nValue;

    DetailCustomItem(String aImagePath, String aName, String aPrice, String aValue) {
        imagePath = aImagePath;
        nName = aName;
        nPrice = aPrice;
        nValue = aValue;
    }
}
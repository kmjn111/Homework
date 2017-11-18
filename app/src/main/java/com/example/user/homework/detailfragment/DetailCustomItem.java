package com.example.user.homework.detailfragment;

//커스텀리스트 뷰에 담을 아이템 정보 클래스 선언
public class DetailCustomItem {
    public String _id;
    public String imagePath; // image resource
    public String nName; // text
    public String nPrice;  // text
    public String nValue;

    public DetailCustomItem(String aId, String aImagePath, String aName, String aPrice, String aValue) {
        _id = aId;
        imagePath = aImagePath;
        nName = aName;
        nPrice = aPrice;
        nValue = aValue;
    }
}
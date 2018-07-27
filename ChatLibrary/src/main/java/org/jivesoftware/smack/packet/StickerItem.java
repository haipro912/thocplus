package org.jivesoftware.smack.packet;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ThanhNT72 on 5/20/2015.
 */
public class StickerItem {
    String collectionId;
    String itemId;
    String typeSticker;
    String urlImage;
    String urlVoice;

    public StickerItem() {
    }

    public StickerItem(String collectionId, String itemId, String typeSticker, String urlImage, String urlVoice) {
        this.collectionId = collectionId;
        this.itemId = itemId;
        this.typeSticker = typeSticker;
        this.urlImage = urlImage;
        this.urlVoice = urlVoice;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTypeSticker() {
        return typeSticker;
    }

    public void setTypeSticker(String typeSticker) {
        this.typeSticker = typeSticker;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlVoice() {
        return urlVoice;
    }

    public void setUrlVoice(String urlVoice) {
        this.urlVoice = urlVoice;
    }


    public JSONObject convertDataToJsonObject() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("collectionid", collectionId);
        object.put("itemid", itemId);
        object.put("type", typeSticker);
        object.put("urlvoice", urlVoice);
        object.put("urlimg", urlImage);
        return object;
    }

    @Override
    public String toString() {
        return "StickerItem{" +
                "collectionId='" + collectionId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", typeSticker='" + typeSticker + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", urlVoice='" + urlVoice + '\'' +
                '}';
    }
}

/**
 * $RCSfile$
 * $Revision: 11644 $
 * $Date: 2010-02-18 16:37:16 +0100 (jeu. 18 févr. 2010) $
 * <p>
 * Copyright 2003-2007 Jive Software.
 * <p>
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.smack.util;

import com.viettel.util.ConvertHelper;
import com.viettel.util.Log;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.AdvertiseItem;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.EventReceivedMessage;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQCall;
import org.jivesoftware.smack.packet.IQGetInfo;
import org.jivesoftware.smack.packet.IQGroup;
import org.jivesoftware.smack.packet.IQInfo;
import org.jivesoftware.smack.packet.KeyValueConfig;
import org.jivesoftware.smack.packet.MUCAdmin;
import org.jivesoftware.smack.packet.Member;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Ping;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.ReengCallOutPacket;
import org.jivesoftware.smack.packet.ReengCallPacket;
import org.jivesoftware.smack.packet.ReengEventPacket;
import org.jivesoftware.smack.packet.ReengMessagePacket;
import org.jivesoftware.smack.packet.ReengMusicPacket;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.ShareMusicMessagePacket;
import org.jivesoftware.smack.packet.StickerItem;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.sasl.NonSASLAuthInfo;
import org.jivesoftware.smack.sasl.SASLMechanism.Failure;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class that helps to parse packets. Any parsing packets method that
 * must be shared between many clients must be placed in this utility class.
 *
 * @author Gaston Dombiak
 */
public class PacketParserUtils {
    /**
     * Namespace used to store packet properties.
     */
    private static final String PROPERTIES_NAMESPACE = "http://www.jivesoftware.com/xmlns/xmpp/properties";
    private static final String TAG = PacketParserUtils.class.getSimpleName();

    /**
     * parser reeng message
     *
     * @param parser
     * @return
     * @throws Exception
     */
    public static Packet parseReengMessage(XmlPullParser parser, ReengMessagePacket.SubType subType,
                                           String external, String subTypeStr) throws Exception {
        ReengMessagePacket reengMessagePacket = new ReengMessagePacket();
        String id = parser.getAttributeValue("", "id");
        reengMessagePacket.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
        reengMessagePacket.setTo(parser.getAttributeValue("", "to"));
        reengMessagePacket.setFrom(parser.getAttributeValue("", "from"));
        // set typeString, type, external
        reengMessagePacket.setTypeString(parser.getAttributeValue("", "type"));
        reengMessagePacket.setExternal(external);
        // set member
        reengMessagePacket.setSender(parser.getAttributeValue("", "member"));
        reengMessagePacket.setSenderName(parser.getAttributeValue("", "name"));
        reengMessagePacket.setLastAvatar(parser.getAttributeValue("", "lastavatar"));
        reengMessagePacket.setStickyState(parser.getAttributeValue("", "is_sticky"));
        reengMessagePacket.setSubType(subType);
        reengMessagePacket.setSubTypeString(subTypeStr);
        reengMessagePacket.setTimeSend(parser.getAttributeValue("", "timesend"));
        reengMessagePacket.setExpired(ConvertHelper.parserLongFromString(parser.getAttributeValue("", "expired"), -1));
        boolean done = false;
        try {
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    // message text
                    if (elementName.equals("body")) {
                        reengMessagePacket.setLargeEmo(parser.getAttributeValue("", "emoticon"));
                        reengMessagePacket.setBody(parser.nextText());
                    } else if (elementName.equals("avatar")) { // message offical
                        reengMessagePacket.setAvatarUrl(parser.nextText());
                    } else if (elementName.equals("name")) {
                        reengMessagePacket.setName(parser.nextText());
                    } else if (elementName.equals("officalname")) {
                        reengMessagePacket.setOfficalName(parser.nextText());
                    } else if (elementName.equals("tel")) {
                        reengMessagePacket.setTel(parser.nextText());
                    } else if (elementName.equals("sharevideov2")) {
                        reengMessagePacket.setVideoUrl(parser.nextText());
                    } else if (elementName.equals("type")) {
                        // message file, voice, image
                        String fileType = parser.nextText();
                        reengMessagePacket.setFileType(ReengMessagePacket.FileType.fromString(fileType));
                    } else if (elementName.equals("id")) {
                        String fileId = parser.nextText();
                        reengMessagePacket.setFileId(fileId);
                    } else if (elementName.equals("duration")) {
                        String duration = parser.nextText();
                        reengMessagePacket.setDuration(ConvertHelper.parserIntFromString(duration, -1));
                    } else if (elementName.equals("packageid")) {
                        reengMessagePacket.setStickerPacket(parser.nextText());
                    } else if (elementName.equals("itemid")) {
                        reengMessagePacket.setStickerId(ConvertHelper.parserIntFromString(parser.nextText(), 1));
                    } else if (elementName.equals("item_image_url")) {
                        reengMessagePacket.setImageUrl(parser.nextText());
                    } else if (elementName.equals("item_voice_url")) {
                        reengMessagePacket.setVoiceUrl(parser.nextText());
                    } else if (elementName.equals("sticker_list")) {
                        boolean isDone = false;
                        List<StickerItem> listSticker = new ArrayList<StickerItem>();
                        String collectionId = "";
                        String itemId = "";
                        String typeSticker = "";
                        String urlImage = "";
                        String urlVoice = "";
                        while (!isDone) {
                            int eventTypeSticker = parser.next();
                            if (eventTypeSticker == XmlPullParser.START_TAG) {
                                String elementNameSticker = parser.getName();
                                if (elementNameSticker.equals("itemid")) {
                                    itemId = parser.nextText();
                                } else if (elementNameSticker.equals("packageid")) {
                                    collectionId = parser.nextText();
                                } else if (elementNameSticker.equals("item_type")) {
                                    typeSticker = parser.nextText();
                                } else if (elementNameSticker.equals("item_image_url")) {
                                    urlImage = parser.nextText();
                                } else if (elementNameSticker.equals("item_voice_url")) {
                                    urlVoice = parser.nextText();
                                }
                            } else if (eventTypeSticker == XmlPullParser.END_TAG) {
                                if (parser.getName().equals("sticker_item")) {
                                    StickerItem item = new StickerItem(collectionId, itemId, typeSticker, urlImage,
                                            urlVoice);
                                    listSticker.add(item);
                                } else if (parser.getName().equals("sticker_list")) {
                                    isDone = true;
                                }
                            }
                        }
                        //to json
                        JSONArray array = new JSONArray();
                        for (StickerItem item : listSticker) {
                            JSONObject obj = item.convertDataToJsonObject();
                            array.put(obj);
                        }
                        reengMessagePacket.setJsonListSticker(array.toString());
                    } else if (elementName.equals("size")) {
                        String size = parser.nextText();
                        reengMessagePacket.setSize(ConvertHelper.parserIntFromString(size, -1));
                    } else if (elementName.equals("thumb")) {
                        reengMessagePacket.setVideoThumb(parser.nextText());
                    } else if (elementName.equals("thread")) {
                        reengMessagePacket.setThread(parser.nextText());
                    } else if (elementName.equals("media_link")) {
                        reengMessagePacket.setMediaLink(parser.nextText());
                    }
                    // parser group message notifile
                    else if (elementName.equals("room")) {
                        reengMessagePacket.setGroupName(parser.getAttributeValue("", "name"));
                        reengMessagePacket.setGroupId(parser.getAttributeValue("", "jid"));
                        reengMessagePacket.setFromJid(parser.getAttributeValue("", "fromJid"));
                    } else if (elementName.equals("member")) {
                        String jid = parser.getAttributeValue("", "jid");
                        String invitedFrom = parser.getAttributeValue("", "invitedFrom");
                        String role = parser.getAttributeValue("", "role");
                        String code = parser.getAttributeValue("", "code");
                        String nickName = parser.getAttributeValue("", "name");
                        String avatar = parser.getAttributeValue("", "avatar");
                        int codeInt = 0;
                        if (code != null && code.length() > 0) {
                            codeInt = ConvertHelper.parserIntFromString(code, -1);
                        }
                        Member member = new Member(jid, role, codeInt, nickName, avatar);
                        member.setInvitedFrom(invitedFrom);
                        reengMessagePacket.addMember(member);
                    } else if (elementName.equals("keep_private")) {
                        int groupPrivate = ConvertHelper.parserIntFromString(parser.getAttributeValue("", "value"), -1);
                        reengMessagePacket.setGroupPrivate(groupPrivate);
                    } else if (elementName.equals("avatar_created")) {
                        reengMessagePacket.setGroupAvatar(parser.nextText());
                    } else if (elementName.equals("gtype")) {
                        reengMessagePacket.setGroupClass(ConvertHelper.parserIntFromString(parser.nextText(), 0));
                    } else if (elementName.equals("from_name")) {// lam quen
                        reengMessagePacket.setFromName(parser.nextText());
                    } else if (elementName.equals("to_name")) {
                        reengMessagePacket.setToName(parser.nextText());
                    } else if (elementName.equals("nick")) {
                        reengMessagePacket.setNick(parser.nextText());
                    } else if (elementName.equals("app_id")) {
                        reengMessagePacket.setAppId(parser.nextText());
                    } else if (elementName.equals("from_avatar")) { // avatar nguoi moi lam quen
                        reengMessagePacket.setFromAvatar(parser.nextText());
                    } else if (elementName.equals("to_avatar")) {   // a vatar nguoi  nhan lam quen
                        reengMessagePacket.setToAvatar(parser.nextText());
                    } else if (elementName.equals("lat")) {// chia se vi tri
                        reengMessagePacket.setLat(parser.nextText());
                    } else if (elementName.equals("lng")) {
                        reengMessagePacket.setLng(parser.nextText());
                    } else if (elementName.equals("amount_money")) {
                        reengMessagePacket.setAmountMoney(parser.nextText());
                    } else if (elementName.equals("unit_money")) {
                        reengMessagePacket.setUnitMoney(parser.nextText());
                    } else if (elementName.equals("time_transfer")) {
                        reengMessagePacket.setTimeTransferMoney(parser.nextText());
                    } else if (elementName.equals("subject")) {
                        reengMessagePacket.setSubject(parser.nextText());
                    } else if (elementName.equals("thread")) {
                        reengMessagePacket.setThread(parser.nextText());
                    } else if (elementName.equals("no_store")) {
                        reengMessagePacket.setNoStore(true);
                    } else if (elementName.equals("error")) {
                        reengMessagePacket.setError(parseError(parser));
                    } else if (elementName.equals("key")) {
                        reengMessagePacket.addKeyConfig(parser.nextText());
                    } else if (elementName.equals("link")) {
                        reengMessagePacket.setLink(parser.nextText());
                    } else if (elementName.equals("star_id")) {// event room
                        reengMessagePacket.setEventRoomId(parser.nextText());
                    } else if (elementName.equals("star_name")) {
                        reengMessagePacket.setEventRoomName(parser.nextText());
                    } else if (elementName.equals("star_avatar")) {
                        reengMessagePacket.setEventRoomAvatar(parser.nextText());
                    } else if (elementName.equals("leftaction")) {
                        reengMessagePacket.setDlLeftLabel(parser.getAttributeValue("", "label"));
                        reengMessagePacket.setDlLeftAction(parser.nextText());
                    } else if (elementName.equals("rightaction")) {
                        reengMessagePacket.setDlRightLabel(parser.getAttributeValue("", "label"));
                        reengMessagePacket.setDlRightAction(parser.nextText());
                    } else if (elementName.equals("img")) {
                        reengMessagePacket.setGifImg(parser.nextText());
                    } else if (elementName.equals("servicetype")) {
                        reengMessagePacket.setServiceType(parser.nextText());
                    } else if (elementName.equals("confirmString")) {
                        reengMessagePacket.setConfirm(parser.nextText());
                    } else if (elementName.equals("titleButton")) {
                        reengMessagePacket.setLabel(parser.nextText());
                    } else if (elementName.equals("image_url")) {
                        reengMessagePacket.setImageLinkUrl(parser.nextText());
                    } else if (elementName.equals("linkto")) {
                        reengMessagePacket.setImageLinkTo(parser.nextText());
                    } else if (elementName.equals("reply")) {
                        reengMessagePacket.setReply(parser.nextText());
                    } else if (elementName.equals("action")) {
                        if (subType == ReengMessagePacket.SubType.vote) {
                            String pollType = parser.getAttributeValue("", "type");
                            reengMessagePacket.setPollType(pollType);
                            reengMessagePacket.setPollDetail(parser.nextText());
                        } else {
                            String title = parser.getAttributeValue("", "title");
                            String desc = parser.getAttributeValue("", "desc");
                            String icon = parser.getAttributeValue("", "icon");
                            String action = parser.nextText();
                            AdvertiseItem advertiseItem = new AdvertiseItem(title, desc, icon, action);
                            reengMessagePacket.addAdvertiseItem(advertiseItem);
                        }
                    } else if (elementName.equals("ratio")) {
                        reengMessagePacket.setRatio(parser.nextText());
                    } else if (elementName.equals("bplus_amount")) {
                        reengMessagePacket.setBPlusAmount(parser.nextText());
                    } else if (elementName.equals("bplus_desc")) {
                        reengMessagePacket.setBPlusDesc(parser.nextText());
                    } else if (elementName.equals("bplus_id")) {
                        reengMessagePacket.setBPlusId(parser.nextText());
                    } else if (elementName.equals("bplus_type")) {
                        reengMessagePacket.setBPlusType(parser.nextText());
                    } else if (elementName.equals("sender")) {
                        reengMessagePacket.setLixiSender(parser.nextText());
                    } else if (elementName.equals("receiver")) {
                        reengMessagePacket.setLixiReceiver(parser.nextText());
                    } else if (elementName.equals("amount")) {
                        reengMessagePacket.setAmountMoney(parser.nextText());
                    } else if (elementName.equals("requestid")) {
                        reengMessagePacket.setRequestIdLixi(parser.nextText());
                    } else if (elementName.equals("members")) {
                        reengMessagePacket.setListMemberLixiStr(parser.nextText());
                    } else if (elementName.equals("tag")) {
                        reengMessagePacket.setTextTag(parser.nextText());
                        Log.i(TAG, "reengMessagePacket tag: " + reengMessagePacket.getTextTag());
                    } else if (elementName.equals("pin")) {
                        reengMessagePacket.setPinMsgTitle(parser.getAttributeValue("", "title"));
                        reengMessagePacket.setPinMsgImg(parser.getAttributeValue("", "img"));
                        String pinType = parser.getAttributeValue("", "type");
                        reengMessagePacket.setPinType(ConvertHelper.parserLongFromString(pinType, -1));
                        reengMessagePacket.setPinMsgTarget(parser.getAttributeValue("", "target"));
                        String pinAction = parser.getAttributeValue("", "action");
                        reengMessagePacket.setPinMsgAction(ConvertHelper.parserIntFromString(pinAction, -1));

                        String pinThreadType = parser.getAttributeValue("", "threadtype");
                        reengMessagePacket.setPinThreadType(ConvertHelper.parserIntFromString(pinThreadType, -1));
                        String pinExpired = parser.getAttributeValue("", "expired");
                        reengMessagePacket.setPinExpired(ConvertHelper.parserLongFromString(pinExpired, -1));
                    } else if (elementName.equals("conf")) {
                        String key = parser.getAttributeValue("", "key");
                        String value = parser.getAttributeValue("", "value");
                        KeyValueConfig keyValueConfig = new KeyValueConfig(key, value);
                        reengMessagePacket.setKeyValueConfig(keyValueConfig);
                    } else if (elementName.equals("sticker_data")) {
                        reengMessagePacket.setStickerData(parser.nextText());
                    } else if (elementName.equals("cover_msg")) {
                        reengMessagePacket.setImgCover(parser.nextText());
                    } else if (elementName.equals("avatar_msg")) {
                        reengMessagePacket.setImgAvatar(parser.nextText());
                    }
                    // Otherwise, it must be a packet extension.
                    else {
                        Log.i(TAG, "parsePacketExtension");
                        reengMessagePacket.addExtension(PacketParserUtils
                                .parsePacketExtension(elementName, namespace,
                                        parser));
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("message")) {
                        done = true;
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Exception", e);
        }
        return reengMessagePacket;
    }


    public static ReengMessagePacket parseReengMessageUpdate(XmlPullParser parser) {
        ReengMessagePacket reengMessagePacket = new ReengMessagePacket();
        ReengMessagePacket.SubType subType = ReengMessagePacket.SubType.empty;
        boolean done = false;
        try {
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    if (elementName.equals("message")) {
                        String subTypeString = parser.getAttributeValue("", "subtype");
                        subType = ReengMessagePacket.SubType.fromString(subTypeString);
                        String external = parser.getAttributeValue("", "external");

                        String id = parser.getAttributeValue("", "id");
                        reengMessagePacket.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
                        reengMessagePacket.setTo(parser.getAttributeValue("", "to"));
                        reengMessagePacket.setFrom(parser.getAttributeValue("", "from"));
                        // set typeString, type, external
                        reengMessagePacket.setTypeString(parser.getAttributeValue("", "type"));
                        reengMessagePacket.setExternal(external);
                        // set member
                        reengMessagePacket.setSender(parser.getAttributeValue("", "member"));
                        reengMessagePacket.setSenderName(parser.getAttributeValue("", "name"));
                        reengMessagePacket.setLastAvatar(parser.getAttributeValue("", "lastavatar"));
                        reengMessagePacket.setStickyState(parser.getAttributeValue("", "is_sticky"));
                        reengMessagePacket.setSubType(subType);
                        reengMessagePacket.setSubTypeString(subTypeString);
                        reengMessagePacket.setTimeSend(parser.getAttributeValue("", "timesend"));
                        reengMessagePacket.setExpired(ConvertHelper.parserLongFromString(parser.getAttributeValue("", "expired"), -1));
                    } else if (elementName.equals("body")) {
                        reengMessagePacket.setLargeEmo(parser.getAttributeValue("", "emoticon"));
                        reengMessagePacket.setBody(parser.nextText());
                    } else if (elementName.equals("avatar")) { // message offical
                        reengMessagePacket.setAvatarUrl(parser.nextText());
                    } else if (elementName.equals("name")) {
                        reengMessagePacket.setName(parser.nextText());
                    } else if (elementName.equals("officalname")) {
                        reengMessagePacket.setOfficalName(parser.nextText());
                    } else if (elementName.equals("tel")) {
                        reengMessagePacket.setTel(parser.nextText());
                    } else if (elementName.equals("sharevideov2")) {
                        reengMessagePacket.setVideoUrl(parser.nextText());
                    } else if (elementName.equals("type")) {
                        // message file, voice, image
                        String fileType = parser.nextText();
                        reengMessagePacket.setFileType(ReengMessagePacket.FileType.fromString(fileType));
                    } else if (elementName.equals("id")) {
                        String fileId = parser.nextText();
                        reengMessagePacket.setFileId(fileId);
                    } else if (elementName.equals("duration")) {
                        String duration = parser.nextText();
                        reengMessagePacket.setDuration(ConvertHelper.parserIntFromString(duration, -1));
                    } else if (elementName.equals("packageid")) {
                        reengMessagePacket.setStickerPacket(parser.nextText());
                    } else if (elementName.equals("itemid")) {
                        reengMessagePacket.setStickerId(ConvertHelper.parserIntFromString(parser.nextText(), 1));
                    } else if (elementName.equals("item_image_url")) {
                        reengMessagePacket.setImageUrl(parser.nextText());
                    } else if (elementName.equals("item_voice_url")) {
                        reengMessagePacket.setVoiceUrl(parser.nextText());
                    } else if (elementName.equals("sticker_list")) {
                        boolean isDone = false;
                        List<StickerItem> listSticker = new ArrayList<StickerItem>();
                        String collectionId = "";
                        String itemId = "";
                        String typeSticker = "";
                        String urlImage = "";
                        String urlVoice = "";
                        while (!isDone) {
                            int eventTypeSticker = parser.next();
                            if (eventTypeSticker == XmlPullParser.START_TAG) {
                                String elementNameSticker = parser.getName();
                                if (elementNameSticker.equals("itemid")) {
                                    itemId = parser.nextText();
                                } else if (elementNameSticker.equals("packageid")) {
                                    collectionId = parser.nextText();
                                } else if (elementNameSticker.equals("item_type")) {
                                    typeSticker = parser.nextText();
                                } else if (elementNameSticker.equals("item_image_url")) {
                                    urlImage = parser.nextText();
                                } else if (elementNameSticker.equals("item_voice_url")) {
                                    urlVoice = parser.nextText();
                                }
                            } else if (eventTypeSticker == XmlPullParser.END_TAG) {
                                if (parser.getName().equals("sticker_item")) {
                                    StickerItem item = new StickerItem(collectionId, itemId, typeSticker, urlImage,
                                            urlVoice);
                                    listSticker.add(item);
                                } else if (parser.getName().equals("sticker_list")) {
                                    isDone = true;
                                }
                            }
                        }
                        //to json
                        JSONArray array = new JSONArray();
                        for (StickerItem item : listSticker) {
                            JSONObject obj = item.convertDataToJsonObject();
                            array.put(obj);
                        }
                        reengMessagePacket.setJsonListSticker(array.toString());
                    } else if (elementName.equals("size")) {
                        String size = parser.nextText();
                        reengMessagePacket.setSize(ConvertHelper.parserIntFromString(size, -1));
                    } else if (elementName.equals("thumb")) {
                        reengMessagePacket.setVideoThumb(parser.nextText());
                    } else if (elementName.equals("thread")) {
                        reengMessagePacket.setThread(parser.nextText());
                    } else if (elementName.equals("media_link")) {
                        reengMessagePacket.setMediaLink(parser.nextText());
                    }
                    // parser group message notifile
                    else if (elementName.equals("room")) {
                        reengMessagePacket.setGroupName(parser.getAttributeValue("", "name"));
                        reengMessagePacket.setGroupId(parser.getAttributeValue("", "jid"));
                        reengMessagePacket.setFromJid(parser.getAttributeValue("", "fromJid"));
                    } else if (elementName.equals("member")) {
                        String jid = parser.getAttributeValue("", "jid");
                        String invitedFrom = parser.getAttributeValue("", "invitedFrom");
                        String role = parser.getAttributeValue("", "role");
                        String code = parser.getAttributeValue("", "code");
                        String nickName = parser.getAttributeValue("", "name");
                        String avatar = parser.getAttributeValue("", "avatar");
                        int codeInt = 0;
                        if (code != null && code.length() > 0) {
                            codeInt = ConvertHelper.parserIntFromString(code, -1);
                        }
                        Member member = new Member(jid, role, codeInt, nickName, avatar);
                        member.setInvitedFrom(invitedFrom);
                        reengMessagePacket.addMember(member);
                    } else if (elementName.equals("keep_private")) {
                        int groupPrivate = ConvertHelper.parserIntFromString(parser.getAttributeValue("", "value"), -1);
                        reengMessagePacket.setGroupPrivate(groupPrivate);
                    } else if (elementName.equals("avatar_created")) {
                        reengMessagePacket.setGroupAvatar(parser.nextText());
                    } else if (elementName.equals("gtype")) {
                        reengMessagePacket.setGroupClass(ConvertHelper.parserIntFromString(parser.nextText(), 0));
                    } else if (elementName.equals("from_name")) {// lam quen
                        reengMessagePacket.setFromName(parser.nextText());
                    } else if (elementName.equals("to_name")) {
                        reengMessagePacket.setToName(parser.nextText());
                    } else if (elementName.equals("nick")) {
                        reengMessagePacket.setNick(parser.nextText());
                    } else if (elementName.equals("app_id")) {
                        reengMessagePacket.setAppId(parser.nextText());
                    } else if (elementName.equals("from_avatar")) { // avatar nguoi moi lam quen
                        reengMessagePacket.setFromAvatar(parser.nextText());
                    } else if (elementName.equals("to_avatar")) {   // a vatar nguoi  nhan lam quen
                        reengMessagePacket.setToAvatar(parser.nextText());
                    } else if (elementName.equals("lat")) {// chia se vi tri
                        reengMessagePacket.setLat(parser.nextText());
                    } else if (elementName.equals("lng")) {
                        reengMessagePacket.setLng(parser.nextText());
                    } else if (elementName.equals("amount_money")) {
                        reengMessagePacket.setAmountMoney(parser.nextText());
                    } else if (elementName.equals("unit_money")) {
                        reengMessagePacket.setUnitMoney(parser.nextText());
                    } else if (elementName.equals("time_transfer")) {
                        reengMessagePacket.setTimeTransferMoney(parser.nextText());
                    } else if (elementName.equals("subject")) {
                        reengMessagePacket.setSubject(parser.nextText());
                    } else if (elementName.equals("thread")) {
                        reengMessagePacket.setThread(parser.nextText());
                    } else if (elementName.equals("no_store")) {
                        reengMessagePacket.setNoStore(true);
                    } else if (elementName.equals("error")) {
                        reengMessagePacket.setError(parseError(parser));
                    } else if (elementName.equals("key")) {
                        reengMessagePacket.addKeyConfig(parser.nextText());
                    } else if (elementName.equals("link")) {
                        reengMessagePacket.setLink(parser.nextText());
                    } else if (elementName.equals("star_id")) {// event room
                        reengMessagePacket.setEventRoomId(parser.nextText());
                    } else if (elementName.equals("star_name")) {
                        reengMessagePacket.setEventRoomName(parser.nextText());
                    } else if (elementName.equals("star_avatar")) {
                        reengMessagePacket.setEventRoomAvatar(parser.nextText());
                    } else if (elementName.equals("leftaction")) {
                        reengMessagePacket.setDlLeftLabel(parser.getAttributeValue("", "label"));
                        reengMessagePacket.setDlLeftAction(parser.nextText());
                    } else if (elementName.equals("rightaction")) {
                        reengMessagePacket.setDlRightLabel(parser.getAttributeValue("", "label"));
                        reengMessagePacket.setDlRightAction(parser.nextText());
                    } else if (elementName.equals("img")) {
                        reengMessagePacket.setGifImg(parser.nextText());
                    } else if (elementName.equals("servicetype")) {
                        reengMessagePacket.setServiceType(parser.nextText());
                    } else if (elementName.equals("confirmString")) {
                        reengMessagePacket.setConfirm(parser.nextText());
                    } else if (elementName.equals("titleButton")) {
                        reengMessagePacket.setLabel(parser.nextText());
                    } else if (elementName.equals("image_url")) {
                        reengMessagePacket.setImageLinkUrl(parser.nextText());
                    } else if (elementName.equals("linkto")) {
                        reengMessagePacket.setImageLinkTo(parser.nextText());
                    } else if (elementName.equals("reply")) {
                        reengMessagePacket.setReply(parser.nextText());
                    } else if (elementName.equals("action")) {
                        if (subType == ReengMessagePacket.SubType.vote) {
                            String pollType = parser.getAttributeValue("", "type");
                            reengMessagePacket.setPollType(pollType);
                            reengMessagePacket.setPollDetail(parser.nextText());
                        } else {
                            String title = parser.getAttributeValue("", "title");
                            String desc = parser.getAttributeValue("", "desc");
                            String icon = parser.getAttributeValue("", "icon");
                            String action = parser.nextText();
                            AdvertiseItem advertiseItem = new AdvertiseItem(title, desc, icon, action);
                            reengMessagePacket.addAdvertiseItem(advertiseItem);
                        }
                    } else if (elementName.equals("ratio")) {
                        reengMessagePacket.setRatio(parser.nextText());
                    } else if (elementName.equals("bplus_amount")) {
                        reengMessagePacket.setBPlusAmount(parser.nextText());
                    } else if (elementName.equals("bplus_desc")) {
                        reengMessagePacket.setBPlusDesc(parser.nextText());
                    } else if (elementName.equals("bplus_id")) {
                        reengMessagePacket.setBPlusId(parser.nextText());
                    } else if (elementName.equals("bplus_type")) {
                        reengMessagePacket.setBPlusType(parser.nextText());
                    } else if (elementName.equals("sender")) {
                        reengMessagePacket.setLixiSender(parser.nextText());
                    } else if (elementName.equals("receiver")) {
                        reengMessagePacket.setLixiReceiver(parser.nextText());
                    } else if (elementName.equals("amount")) {
                        reengMessagePacket.setAmountMoney(parser.nextText());
                    } else if (elementName.equals("requestid")) {
                        reengMessagePacket.setRequestIdLixi(parser.nextText());
                    } else if (elementName.equals("members")) {
                        reengMessagePacket.setListMemberLixiStr(parser.nextText());
                    } else if (elementName.equals("tag")) {
                        reengMessagePacket.setTextTag(parser.nextText());
                        Log.i(TAG, "reengMessagePacket tag: " + reengMessagePacket.getTextTag());
                    } else if (elementName.equals("pin")) {
                        reengMessagePacket.setPinMsgTitle(parser.getAttributeValue("", "title"));
                        reengMessagePacket.setPinMsgImg(parser.getAttributeValue("", "img"));
                        String pinType = parser.getAttributeValue("", "type");
                        reengMessagePacket.setPinType(ConvertHelper.parserLongFromString(pinType, -1));
                        reengMessagePacket.setPinMsgTarget(parser.getAttributeValue("", "target"));
                        String pinAction = parser.getAttributeValue("", "action");
                        reengMessagePacket.setPinMsgAction(ConvertHelper.parserIntFromString(pinAction, -1));
                    } else if (elementName.equals("conf")) {
                        String key = parser.getAttributeValue("", "key");
                        String value = parser.getAttributeValue("", "value");
                        KeyValueConfig keyValueConfig = new KeyValueConfig(key, value);
                        reengMessagePacket.setKeyValueConfig(keyValueConfig);
                    } else if (elementName.equals("sticker_data")) {
                        reengMessagePacket.setStickerData(parser.nextText());
                    } else if (elementName.equals("cover_msg")) {
                        reengMessagePacket.setImgCover(parser.nextText());
                    } else if (elementName.equals("avatar_msg")) {
                        reengMessagePacket.setImgAvatar(parser.nextText());
                    }
                    // Otherwise, it must be a packet extension.
                    else {
                        Log.i(TAG, "parsePacketExtension");
                        reengMessagePacket.addExtension(PacketParserUtils
                                .parsePacketExtension(elementName, namespace,
                                        parser));
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("message")) {
                        done = true;
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Exception", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reengMessagePacket;
    }

    /**
     * parser reeng event
     *
     * @param parser
     * @return
     * @throws Exception
     */

    public static Packet parseReengEvent(XmlPullParser parser, ReengMessagePacket.SubType subType, String
            subTypeString) throws Exception {
        ReengEventPacket reengEventPacket = new ReengEventPacket();
        String id = parser.getAttributeValue("", "id");
        reengEventPacket.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
        reengEventPacket.setTo(parser.getAttributeValue("", "to"));
        reengEventPacket.setFrom(parser.getAttributeValue("", "from"));
        // set typeString, type
        reengEventPacket.setTypeString(parser.getAttributeValue("", "type"));
        // set member mesage group
        reengEventPacket.setSender(parser.getAttributeValue("", "member"));
        reengEventPacket.setSenderName(parser.getAttributeValue("", "name"));
        reengEventPacket.setStickyState(parser.getAttributeValue("", "is_sticky"));
        reengEventPacket.setSubType(subType);
        reengEventPacket.setSubTypeString(subTypeString);
        // parser truong time
        reengEventPacket.setTimeSend(parser.getAttributeValue("", "timesend"));
        reengEventPacket.setExpired(ConvertHelper.parserLongFromString(parser.getAttributeValue("", "expired"), -1));
        reengEventPacket.setBannerAction(parser.getAttributeValue("", "action"));
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                String elementName = parser.getName();
                //String namespace = parser.getNamespace();
                // packet event id
                if (elementName.equals("id")) {
                    reengEventPacket.addToListIdOfEvent(parser.nextText());
                } else if (elementName.equals("force")) {
                    reengEventPacket.setForce(parser.nextText());
                } else if (elementName.equals("body")) {
                    reengEventPacket.setLargeEmo(parser.getAttributeValue("", "emoticon"));
                    reengEventPacket.setBody(parser.nextText());
                } else if (elementName.equals("link")) {
                    reengEventPacket.setLink(parser.nextText());
                } else if (elementName.equals("smsout")) {
                    reengEventPacket.setSmsRemain(parser.getAttributeValue("", "remain"));
                    reengEventPacket.setSmsState(parser.getAttributeValue("", "type"));
                    reengEventPacket.setSmsDesc(parser.getAttributeValue("", "desc"));
                } else if (elementName.equals("no_store")) {
                    reengEventPacket.setNoStore(true);
                } else if (elementName.equals("delivered")) {
                    reengEventPacket.setEventType(ReengEventPacket.EventType.delivered);
                    String seen = parser.getAttributeValue("", "seen"); //neu ko co attribute seen thi se la null
                    reengEventPacket.setSeenState(ConvertHelper.parserIntFromString(seen, -1));
                } else if (elementName.equals("media_link")) {
                    reengEventPacket.setMediaLink(parser.nextText());
                } else if (elementName.equals("banner")) {
                    JSONObject banner = new JSONObject();
                    banner.put("id", parser.getAttributeValue("", "bannerid"));
                    banner.put("type", parser.getAttributeValue("", "type"));
                    banner.put("title", parser.getAttributeValue("", "title"));
                    banner.put("desc", parser.getAttributeValue("", "desc"));
                    banner.put("icon", parser.getAttributeValue("", "icon"));
                    banner.put("confirm", parser.getAttributeValue("", "confirm"));
                    banner.put("timesend", parser.getAttributeValue("", "timesend"));
                    banner.put("expire", parser.getAttributeValue("", "expire"));
                    banner.put("image", parser.getAttributeValue("", "image"));
                    String isImage = parser.getAttributeValue("", "isImage");
                    banner.put("isImage", ConvertHelper.parserBoolenFromString(isImage, false));
                    banner.put("target", parser.getAttributeValue("", "target"));
                    banner.put("link", parser.nextText());
                    reengEventPacket.addBannerJson(banner);
                } else if (elementName.equals("subject")) {
                    reengEventPacket.setSubject(parser.nextText());
                } else if (elementName.equals("offline")) {
                    reengEventPacket.setOffline(parser.nextText());
                } else {
                    ReengEventPacket.EventType event = ReengEventPacket.EventType.fromString(elementName);
                    reengEventPacket.setEventType(event);
                    parser.next();
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("message")) {
                    done = true;
                }
            }
        }
        return reengEventPacket;
    }

    /**
     * @param parser
     * @return
     */
    public static Packet parseReceiverChatMessage(XmlPullParser parser) throws Exception {
        EventReceivedMessage event = new EventReceivedMessage();
        event.setTo(parser.getAttributeValue("", "to"));
        event.setFrom(parser.getAttributeValue("", "from"));
        event.setFrom(parser.getAttributeValue("", "ns"));
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                parser.getName();
                if (parser.getName().equals("id")) {
                    String fileId = parser.nextText();
                    event.setMessageID(fileId);
                } else {
                    parser.nextText();
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("message")) {
                    done = true;
                }
            }
        }
        return event;
    }

    /**
     * parser reeng music
     *
     * @param parser
     * @return
     * @throws Exception
     */
    public static Packet parseReengMusic(XmlPullParser parser,
                                         ReengMessagePacket.SubType subType,
                                         String external,
                                         String subTypeStr) throws Exception {
        ReengMusicPacket reengMusicPacket = new ReengMusicPacket();
        String id = parser.getAttributeValue("", "id");
        reengMusicPacket.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
        reengMusicPacket.setTo(parser.getAttributeValue("", "to"));
        reengMusicPacket.setFrom(parser.getAttributeValue("", "from"));
        // set typeString, type, external
        reengMusicPacket.setTypeString(parser.getAttributeValue("", "type"));
        reengMusicPacket.setExternal(external);
        // set member
        reengMusicPacket.setSender(parser.getAttributeValue("", "member"));
        reengMusicPacket.setSenderName(parser.getAttributeValue("", "name"));
        reengMusicPacket.setStickyState(parser.getAttributeValue("", "is_sticky"));
        reengMusicPacket.setSubType(subType);
        reengMusicPacket.setSubTypeString(subTypeStr);
        reengMusicPacket.setTimeSend(parser.getAttributeValue("", "timesend"));
        reengMusicPacket.setTimeReceive(parser.getAttributeValue("", "timereceive"));
        reengMusicPacket.setExpired(ConvertHelper.parserLongFromString(parser.getAttributeValue("", "expired"), -1));
        boolean done = false;
        try {
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    // message text
                    if (elementName.equals("id")) {
                        reengMusicPacket.setResponsePacketId(parser.nextText());
                    } else if (elementName.equals("avatar")) { // message offical
                        reengMusicPacket.setAvatarUrl(parser.nextText());
                    } else if (elementName.equals("name")) {
                        reengMusicPacket.setName(parser.nextText());
                    } else if (elementName.equals("officalname")) {
                        reengMusicPacket.setOfficalName(parser.nextText());
                    } else if (elementName.equals("body")) {
                        reengMusicPacket.setLargeEmo(parser.getAttributeValue("", "emoticon"));
                        reengMusicPacket.setBody(parser.nextText());
                    } else if (elementName.equals("status")) {
                        reengMusicPacket.setMusicStatus(ReengMusicPacket.MusicStatus.fromString(parser.nextText()));
                    } else if (elementName.equals("action")) {
                        reengMusicPacket.setMusicAction(ReengMusicPacket.MusicAction.fromString(parser.nextText()));
                    } else if (elementName.equals("no_store")) { // the no_store
                        reengMusicPacket.setNoStore(true);
                    } else if (elementName.equals("sessionid")) {
                        reengMusicPacket.setSessionId(parser.nextText());
                    } else if (elementName.equals("songid")) {
                        reengMusicPacket.setSongId(parser.nextText());
                    } else if (elementName.equals("songname")) {
                        reengMusicPacket.setSongName(parser.nextText());
                    } else if (elementName.equals("singername")) {
                        reengMusicPacket.setSinger(parser.nextText());
                    } else if (elementName.equals("songurl")) {
                        reengMusicPacket.setSongUrl(parser.nextText());
                    } else if (elementName.equals("mediaurl")) {
                        reengMusicPacket.setMediaUrl(parser.nextText());
                    } else if (elementName.equals("songthumb")) {
                        reengMusicPacket.setSongThumb(parser.nextText());
                    } else if (elementName.equals("songtype")) {
                        reengMusicPacket.setSongType(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                    } else if (elementName.equals("nick")) {
                        reengMusicPacket.setNick(parser.nextText());
                    } else if (elementName.equals("lastchangeavatar")) {
                        reengMusicPacket.setStrangerAvatar(parser.nextText());
                    } else if (elementName.equals("postername")) {
                        reengMusicPacket.setStrangerPosterName(parser.nextText());
                    } else if (elementName.equals("thread")) {
                        reengMusicPacket.setThread(parser.nextText());
                    } // parser group message notifile
                    else if (elementName.equals("subject")) {
                        reengMusicPacket.setSubject(parser.nextText());
                    } else if (elementName.equals("thread")) {
                        reengMusicPacket.setThread(parser.nextText());
                    } else if (elementName.equals("no_store")) {
                        reengMusicPacket.setNoStore(true);
                    } else if (elementName.equals("room_online")) {
                        reengMusicPacket.setRoomStateOnline(parser.nextText());
                    } else if (elementName.equals("room_music")) {
                        reengMusicPacket.setRoomStateMusic(parser.nextText());
                    } else if (elementName.equals("crbt_code")) {
                        reengMusicPacket.setCrbtCode(parser.nextText());
                    } else if (elementName.equals("crbt_price")) {
                        reengMusicPacket.setCrbtPrice(parser.nextText());
                    } else if (elementName.equals("session")) {
                        reengMusicPacket.setSession(parser.nextText());
                    } else if (elementName.equals("error")) {
                        reengMusicPacket.setError(parseError(parser));
                    } else if (elementName.equals("leftaction")) {
                        reengMusicPacket.setDlLeftLabel(parser.getAttributeValue("", "label"));
                        reengMusicPacket.setDlLeftAction(parser.nextText());
                    } else if (elementName.equals("rightaction")) {
                        reengMusicPacket.setDlRightLabel(parser.getAttributeValue("", "label"));
                        reengMusicPacket.setDlRightAction(parser.nextText());
                    } else if (elementName.equals("servicetype")) {
                        reengMusicPacket.setMediaUrl(parser.nextText());
                    } else if (elementName.equals("reply")) {
                        reengMusicPacket.setReply(parser.nextText());
                    } else if (elementName.equals("autoplay")) {
                        reengMusicPacket.setAutoPlayVideo(ConvertHelper.parserIntFromString(parser.nextText(), 0));
                    }
                    // Otherwise, it must be a packet extension.
                    else {
                        Log.i(TAG, "parsePacketExtension");
                        reengMusicPacket.addExtension(PacketParserUtils
                                .parsePacketExtension(elementName, namespace,
                                        parser));
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("message")) {
                        done = true;
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Exception", e);
        }
        return reengMusicPacket;
    }

    /**
     * parser reeng call
     *
     * @param parser
     * @return
     * @throws Exception
     */
    public static Packet parseReengCall(XmlPullParser parser,
                                        ReengCallPacket.SubType subType,
                                        String external,
                                        String subTypeStr) throws Exception {
        ReengCallPacket reengCallPacket = new ReengCallPacket();
        String id = parser.getAttributeValue("", "id");
        reengCallPacket.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
        reengCallPacket.setTo(parser.getAttributeValue("", "to"));
        reengCallPacket.setFrom(parser.getAttributeValue("", "from"));
        // set typeString, type, external
        reengCallPacket.setTypeString(parser.getAttributeValue("", "type"));
        reengCallPacket.setExternal(external);
        // set member
        reengCallPacket.setSender(parser.getAttributeValue("", "member"));
        reengCallPacket.setSenderName(parser.getAttributeValue("", "name"));
        reengCallPacket.setStickyState(parser.getAttributeValue("", "is_sticky"));
        reengCallPacket.setSubType(subType);
        reengCallPacket.setSubTypeString(subTypeStr);
        reengCallPacket.setTimeSend(parser.getAttributeValue("", "timesend"));
        reengCallPacket.setTimeReceive(parser.getAttributeValue("", "timereceive"));
        reengCallPacket.setAttrStatus(parser.getAttributeValue("", "status"));
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                // message call
                if (elementName.equals("name")) {
                    reengCallPacket.setName(parser.nextText());
                } else if (elementName.equals("officalname")) {
                    reengCallPacket.setOfficalName(parser.nextText());
                } else if (elementName.equals("body")) {
                    reengCallPacket.setBody(parser.nextText());
                } else if (elementName.equals("no_store")) { // the no_store
                    reengCallPacket.setNoStore(true);
                } else if (elementName.equals("nick")) {
                    reengCallPacket.setNick(parser.nextText());
                } else if (elementName.equals("lastchangeavatar")) {
                    reengCallPacket.setStrangerAvatar(parser.nextText());
                } else if (elementName.equals("postername")) {
                    reengCallPacket.setStrangerPosterName(parser.nextText());
                } else if (elementName.equals("thread")) {
                    reengCallPacket.setThread(parser.nextText());
                } // parser group message notifile
                else if (elementName.equals("subject")) {
                    reengCallPacket.setSubject(parser.nextText());
                } else if (elementName.equals("thread")) {
                    reengCallPacket.setThread(parser.nextText());
                } else if (elementName.equals("error")) {
                    reengCallPacket.setError(parseError(parser));
                } else if (elementName.equals("servicetype")) {
                    reengCallPacket.setServiceType(parser.nextText());
                } else if (elementName.equals("talk_stranger")) {
                    reengCallPacket.setCallConfide(true);
                } else if (elementName.equals("video_call")) {
                    reengCallPacket.setVideoCall(true);
                } else if (elementName.equals("only_audio")) {
                    reengCallPacket.setOnlyAudio(true);
                } else if (elementName.equals("callinfo")) {
                    boolean isDone = false;
                    while (!isDone) {
                        int eventTypeCall = parser.next();
                        if (eventTypeCall == XmlPullParser.START_TAG) {
                            String callInfo = parser.getName();
                            if (callInfo.equals("caller")) {
                                reengCallPacket.setCaller(parser.nextText());
                            } else if (callInfo.equals("callee")) {
                                reengCallPacket.setCallee(parser.nextText());
                            } else if (callInfo.equals("error")) {
                                reengCallPacket.setCallError(ReengCallPacket.CallError.fromString(parser.nextText()));
                            } else if (callInfo.equals("session")) {
                                reengCallPacket.setCallSession(parser.nextText());
                            } else if (callInfo.equals("calldata")) {
                                String callDataType = "";
                                String callDataValue = "";
                                boolean isDoneData = false;
                                while (!isDoneData) {
                                    int eventTypeCallData = parser.next();
                                    if (eventTypeCallData == XmlPullParser.START_TAG) {
                                        String elementData = parser.getName();
                                        if (elementData.equals("type")) {
                                            callDataType = parser.nextText();
                                        } else if (elementData.equals("data")) {
                                            callDataValue = parser.nextText();
                                        }
                                    } else if (eventTypeCallData == XmlPullParser.END_TAG) {
                                        if (parser.getName().equals("calldata")) {
                                            isDoneData = true;
                                        }
                                    }
                                }
                                reengCallPacket.setCallData(callDataType, callDataValue);
                            } else if (callInfo.equals("iceservers")) {
                                boolean isDoneIce = false;
                                while (!isDoneIce) {
                                    int eventTypeCallData = parser.next();
                                    if (eventTypeCallData == XmlPullParser.START_TAG) {
                                        String elementData = parser.getName();
                                        if (elementData.equals("server")) {
                                            reengCallPacket.addIceServer(parser.getAttributeValue("", "user"), parser
                                                    .getAttributeValue("", "credential"), parser.nextText());
                                        }
                                    } else if (eventTypeCallData == XmlPullParser.END_TAG) {
                                        if (parser.getName().equals("iceservers")) {
                                            isDoneIce = true;
                                        }
                                    }
                                }
                            }
                        } else if (eventTypeCall == XmlPullParser.END_TAG) {
                            if (parser.getName().equals("callinfo")) {
                                isDone = true;
                            }
                        }
                    }
                } else {// Otherwise, it must be a packet extension.
                    Log.i(TAG, "parsePacketExtension");
                    reengCallPacket.addExtension(PacketParserUtils
                            .parsePacketExtension(elementName, namespace,
                                    parser));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("message")) {
                    done = true;
                }
            }
        }
        return reengCallPacket;
    }

    public static Packet parseReengCallOut(XmlPullParser parser,
                                           ReengCallOutPacket.SubType subType,
                                           String external,
                                           String subTypeStr) throws Exception {
        ReengCallOutPacket reengCallOutPacket = new ReengCallOutPacket();
        String id = parser.getAttributeValue("", "id");
        reengCallOutPacket.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
        reengCallOutPacket.setTo(parser.getAttributeValue("", "to"));
        reengCallOutPacket.setFrom(parser.getAttributeValue("", "from"));
        // set typeString, type, external
        reengCallOutPacket.setTypeString(parser.getAttributeValue("", "type"));
        reengCallOutPacket.setExternal(external);
        // set member
        reengCallOutPacket.setSender(parser.getAttributeValue("", "member"));
        reengCallOutPacket.setSenderName(parser.getAttributeValue("", "name"));
        reengCallOutPacket.setStickyState(parser.getAttributeValue("", "is_sticky"));
        reengCallOutPacket.setSubType(subType);
        reengCallOutPacket.setSubTypeString(subTypeStr);
        reengCallOutPacket.setTimeSend(parser.getAttributeValue("", "timesend"));
        reengCallOutPacket.setTimeReceive(parser.getAttributeValue("", "timereceive"));
        reengCallOutPacket.setAttrStatus(parser.getAttributeValue("", "status"));
        boolean done = false;
        try {
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    // message call
                    if (elementName.equals("name")) {
                        reengCallOutPacket.setName(parser.nextText());
                    } else if (elementName.equals("officalname")) {
                        reengCallOutPacket.setOfficalName(parser.nextText());
                    } else if (elementName.equals("body")) {
                        reengCallOutPacket.setBody(parser.nextText());
                    } else if (elementName.equals("no_store")) { // the no_store
                        reengCallOutPacket.setNoStore(true);
                    } else if (elementName.equals("nick")) {
                        reengCallOutPacket.setNick(parser.nextText());
                    } else if (elementName.equals("thread")) {
                        reengCallOutPacket.setThread(parser.nextText());
                    } // parser group message notifile
                    else if (elementName.equals("subject")) {
                        reengCallOutPacket.setSubject(parser.nextText());
                    } else if (elementName.equals("thread")) {
                        reengCallOutPacket.setThread(parser.nextText());
                    } else if (elementName.equals("error")) {
                        reengCallOutPacket.setError(parseError(parser));
                    } else if (elementName.equals("servicetype")) {
                        reengCallOutPacket.setServiceType(parser.nextText());
                    } else if (elementName.equals("type")) {
                        reengCallOutPacket.setCallOutType(ReengCallOutPacket.CallOutType.fromString(parser.nextText()));
                    } else if (elementName.equals("callinfo")) {
                        boolean isDone = false;
                        while (!isDone) {
                            int eventTypeCall = parser.next();
                            if (eventTypeCall == XmlPullParser.START_TAG) {
                                String callInfo = parser.getName();
                                if (callInfo.equals("caller")) {
                                    reengCallOutPacket.setCaller(parser.nextText());
                                } else if (callInfo.equals("callee")) {
                                    reengCallOutPacket.setCallee(parser.nextText());
                                } else if (callInfo.equals("error")) {
                                    String errorStr = parser.nextText();
                                    reengCallOutPacket.setCallStatusStr(errorStr);
                                    reengCallOutPacket.setCallStatus(ReengCallOutPacket.CallStatus.fromString
                                            (errorStr));
                                } else if (callInfo.equals("session")) {
                                    reengCallOutPacket.setCallSession(parser.nextText());
                                } else if (callInfo.equals("data")) {
                                    reengCallOutPacket.setCallOutData(parser.nextText());
                                } else if (callInfo.equals("iceservers")) {
                                    boolean isDoneIce = false;
                                    while (!isDoneIce) {
                                        int eventTypeCallData = parser.next();
                                        if (eventTypeCallData == XmlPullParser.START_TAG) {
                                            String elementData = parser.getName();
                                            if (elementData.equals("server")) {
                                                reengCallOutPacket.addIceServer(parser.getAttributeValue("", "user"),
                                                        parser.getAttributeValue("", "credential"), parser.nextText());
                                            }
                                        } else if (eventTypeCallData == XmlPullParser.END_TAG) {
                                            if (parser.getName().equals("iceservers")) {
                                                isDoneIce = true;
                                            }
                                        }
                                    }
                                }
                            } else if (eventTypeCall == XmlPullParser.END_TAG) {
                                if (parser.getName().equals("callinfo")) {
                                    isDone = true;
                                }
                            }
                        }
                    } else {// Otherwise, it must be a packet extension.
                        Log.i(TAG, "parsePacketExtension");
                        reengCallOutPacket.addExtension(PacketParserUtils
                                .parsePacketExtension(elementName, namespace,
                                        parser));
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("message")) {
                        done = true;
                    }
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return reengCallOutPacket;
    }

    /**
     * Parses a message packet.
     *
     * @param parser the XML parser, positioned at the start of a message packet.
     * @return a Message packet.
     * @throws Exception if an exception occurs while parsing the packet.
     */
    public static Packet parseMessage(XmlPullParser parser) throws Exception {
        // toanvk2 parser reeng meesage
        String subTypeString = parser.getAttributeValue("", "subtype");
        ReengMessagePacket.SubType subType = ReengMessagePacket.SubType.fromString(subTypeString);
        String ns = parser.getAttributeValue("", "ns");
        String external = parser.getAttributeValue("", "external");
        // subtype ==  event, view, no_route, typing, update, upgrade;
        if (ReengMessagePacket.SubType.containsEvent(subType)) {
            return parseReengEvent(parser, subType, subTypeString);
        } else if (ShareMusicMessagePacket.SubType.contains(subTypeString)) {
            return parseShareMusicMessage(parser, subTypeString);
        } else if (ns != null && ns.equals("vt:message:event")) {
            return parseReceiverChatMessage(parser);
        } else if (ReengMessagePacket.SubType.containsCall(subType)) {
            return parseReengCall(parser, subType, external, subTypeString);
        } else if (ReengMessagePacket.SubType.containsCallOut(subType) || ReengMessagePacket.SubType.containsCallIn
                (subType)) {
            return parseReengCallOut(parser, subType, external, subTypeString);
        } else if (ReengMessagePacket.SubType.containsMusic(subType) ||
                subType.equals(ReengMessagePacket.SubType.watch_video)) {
            return parseReengMusic(parser, subType, external, subTypeString);
        } else {// message thuong (subType != ReengMessagePacket.SubType.normal)
            //else if (subTypeString != null && subTypeString.length() > 0)
            // ca truong hop message ko co subtype thi cung coi la subtype chua ho tro
            return parseReengMessage(parser, subType, external, subTypeString);
        }
    }

    private static Packet parseShareMusicMessage(XmlPullParser parser, String subTypeString) throws Exception {
        ShareMusicMessagePacket shareMusicMessagePacket = new ShareMusicMessagePacket();
        //String id = parser.getAttributeValue("", "id");
//        shareMusicMessagePacket.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
        shareMusicMessagePacket.setTo(parser.getAttributeValue("", "to"));
        shareMusicMessagePacket.setFrom(parser.getAttributeValue("", "from"));
        // set typeString, type
        shareMusicMessagePacket.setTypeString(parser.getAttributeValue("", "type"));
        //set subtype
        shareMusicMessagePacket.setSubType(ShareMusicMessagePacket.SubType.fromString(subTypeString));
        // set external
        //shareMusicMessagePacket.setExternal(external);
        boolean done = false;
        try {
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    // message text
                    if (elementName.equals("member")) {
                        shareMusicMessagePacket.setSender(parser.nextText());
                    } else if (elementName.equals("songid")) {
                        shareMusicMessagePacket.setSongId(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                    } else if (elementName.equals("songname")) {
                        shareMusicMessagePacket.setSongName(parser.nextText());
                    } else if (elementName.equals("state")) {
                        shareMusicMessagePacket.setState(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                    } else if (elementName.equals("id")) {
                        shareMusicMessagePacket.setPacketID(parser.nextText());
                    } else if (elementName.equals("invite")) {
                        shareMusicMessagePacket.setSender(parser.getAttributeValue(0));
                    } else if (elementName.equals("error")) {
                        shareMusicMessagePacket.setErrorCode(parser.getAttributeValue(0));
                    } else if (elementName.equals("songurl")) {
                        shareMusicMessagePacket.setSongUrl(parser.nextText());
                    } else if (elementName.equals("singername")) {
                        shareMusicMessagePacket.setSingerName(parser.nextText());
                    } else if (elementName.equals("songthumb")) {
                        shareMusicMessagePacket.setSongThumb(parser.nextText());
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("message")) {
                        done = true;
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Exception", e);
        }
        return shareMusicMessagePacket;
    }

    /**
     * Parses a presence packet.
     *
     * @param parser the XML parser, positioned at the start of a presence packet.
     * @return a Presence packet.
     * @throws Exception if an exception occurs while parsing the packet.
     */
    public static Presence parsePresence(XmlPullParser parser) throws Exception {
        Presence.Type type = Presence.Type.available;
        String typeString = parser.getAttributeValue("", "type");
        if (typeString != null && !typeString.equals("")) {
            try {
                type = Presence.Type.valueOf(typeString);
            } catch (IllegalArgumentException iae) {
                System.err.println("Found invalid presence type " + typeString);
            }
        }
        // subtype
        Presence.SubType subType = Presence.SubType.normal;
        String subTypeString = parser.getAttributeValue("", "subtype");
        if (subTypeString != null && !subTypeString.equals("")) {
            try {
                subType = Presence.SubType.valueOf(subTypeString);
            } catch (IllegalArgumentException iae) {
                Log.e(TAG, "IllegalArgumentException", iae);
            }
        }
        Presence presence = new Presence(type);
        presence.setTo(parser.getAttributeValue("", "to"));
        presence.setFrom(parser.getAttributeValue("", "from"));
        String id = parser.getAttributeValue("", "id");
        presence.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
        String language = getLanguageAttribute(parser);
        if (language != null && !"".equals(language.trim())) {
            presence.setLanguage(language);
        }
        presence.setSubType(subType);
        presence.setDomainFile(parser.getAttributeValue("", "domain_file"));
        presence.setDomainMsg(parser.getAttributeValue("", "domain_msg"));
        presence.setDomainOnMedia(parser.getAttributeValue("", "domain_on_media"));
        presence.setDomainImage(parser.getAttributeValue("", "domain_img"));
        // domain keeng
        presence.setDomainServiceKeeng(parser.getAttributeValue("", "kservice"));
        presence.setDomainMedia2Keeng(parser.getAttributeValue("", "kmedia"));
        presence.setDomainImageKeeng(parser.getAttributeValue("", "kimage"));
        // config state
        presence.setVip(ConvertHelper.parserIntFromString(parser.getAttributeValue("", "vip"), -1));
        presence.setCBNV(ConvertHelper.parserIntFromString(parser.getAttributeValue("", "cbnv"), -1));
        presence.setCall(ConvertHelper.parserIntFromString(parser.getAttributeValue("", "call"), -1));
        presence.setSSL(ConvertHelper.parserIntFromString(parser.getAttributeValue("", "ssl"), -1));
        presence.setSmsIn(ConvertHelper.parserIntFromString(parser.getAttributeValue("", "smsin"), -1));
        presence.setCallOut(ConvertHelper.parserIntFromString(parser.getAttributeValue("", "callout"), -1));
        presence.setAvnoNumber(parser.getAttributeValue("", "virtual"));
        presence.setJoinStateStr(parser.getAttributeValue("", "joinstate"));
        presence.setMochaApi(parser.getAttributeValue("", "apisec"));
        presence.setAvnoEnable(ConvertHelper.parserIntFromString(parser.getAttributeValue("", "enable_avno"), -1));
        presence.setTabCallEnable(ConvertHelper.parserIntFromString(parser.getAttributeValue("", "tab_call"), -1));
        // Parse sub-elements
        boolean done = false;
        try {
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    if (elementName == null) {
                    } else if (elementName.equals("status")) {
                        String stt = parser.nextText();
                        if (stt != null)
                            presence.setStatus(stt);
                        else
                            presence.setStatus("");
                    } else if (elementName.equals("avatar")) {
                        if (subType == Presence.SubType.change_avatar) {
                            presence.setLastAvatar(parser.nextText());
                        } else if (subType == Presence.SubType.remove_avatar) {
                            parser.next();
                        }
                    } else if (elementName.equals("timestamp")) {
                        presence.setTimeStamp(ConvertHelper.parserLongFromString(parser.nextText(), -1));
                    } else if (elementName.equals("contactInfo")) {
                        presence.setContactInfo(parser.nextText());
                    } else if (elementName.equals("room")) {
                        presence.setMusicInfo(parser.nextText());
                    } else if (elementName.equals("now")) {
                        presence.setNowSv(ConvertHelper.parserLongFromString(parser.nextText(), -1));
                    } else if (elementName.equals("priority")) {
                        presence.setPriority(ConvertHelper.parserIntFromString(parser.nextText(), 0));
                    } else if (elementName.equals("show")) {
                        String modeText = parser.nextText();
                        try {
                            presence.setState(modeText);
                        } catch (IllegalArgumentException iae) {
                            Log.e(TAG, "IllegalArgumentException", iae);
                        }
                    } else if (elementName.equals("total")) {
                        presence.setFollow(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                    } else if (elementName.equals("image_url")) {
                        presence.setBackgroundRoom(parser.nextText());
                    } else if (elementName.equals("error")) {
                        presence.setError(parseError(parser));
                    } else if (elementName.equals("feeds")) {
                        presence.setFeed(parser.nextText());
                    } else if (elementName.equals("permission")) {
                        presence.setPermission(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                    } else if (elementName.equals("name")) {
                        presence.setUserName(parser.nextText());
                    } else if (elementName.equals("birthdayStr")) {
                        presence.setUserBirthdayStr(parser.nextText());
                    } else if (elementName.equals("vip")) {
                        presence.setVipInfo(ConvertHelper.parserIntFromString(parser.nextText(), 0));
                    } else if (elementName.equals("locationId")) {
                        presence.setLocationId(parser.nextText());
                    } else if (elementName.equals("properties")
                            && namespace.equals(PROPERTIES_NAMESPACE)) {
                        Map<String, Object> properties = parseProperties(parser);
                        // Set packet properties.
                        for (String name : properties.keySet()) {
                            presence.setProperty(name, properties.get(name));
                        }
                    }
                    // Otherwise, it must be a packet extension.
                    else {
                        presence.addExtension(PacketParserUtils
                                .parsePacketExtension(elementName, namespace,
                                        parser));
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("presence")) {
                        done = true;
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Exception", e);
        }
        return presence;
    }

    /**
     * Parses an IQ packet.
     *
     * @param parser the XML parser, positioned at the start of an IQ packet.
     * @return an IQ object.
     * @throws Exception if an exception occurs while parsing the packet.
     */
    public static Packet parseIQ(XmlPullParser parser, Connection connection)
            throws Exception {
        //XmlPullParser originalParser = parser;
        IQ iqPacket = null;
        String id = parser.getAttributeValue("", "id");
        String to = parser.getAttributeValue("", "to");
        String from = parser.getAttributeValue("", "from");
        IQ.Type type = IQ.Type.fromString(parser.getAttributeValue("", "type"));
        XMPPError error = null;
        boolean done = false;
        try {
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    if (elementName.equals("error")) {
                        error = PacketParserUtils.parseError(parser);
                    } else if (elementName.equals("query")
                            && namespace.equals("jabber:iq:auth")) {
                        iqPacket = parseAuthentication(parser);
                    } else if (elementName.equals("query")
                            && namespace.equals("jabber:iq:roster")) {
                        iqPacket = parseRoster(parser);
                    } else if (elementName.equals("query")
                            && namespace.equals("jabber:iq:register")) {
                        iqPacket = parseRegistration(parser);
                    } else if (elementName.equals("query") && namespace.equals("reeng:iq:ping")) {
                        // parser ping
                        iqPacket = parsePing(parser);
                    } else if (elementName.equals("query") && IQInfo.containsIQInfo(namespace)) {
                        iqPacket = parseIQInfo(parser);
                    } else if (elementName.equals("ping") && namespace.equals("urn:xmpp:ping")) {
                        // iq ping tu sv, parser di cho do loi(gagagagagaag)
                        iqPacket = parseUrnPing(parser);
                    } else if (elementName.equals("query") && namespace.equals("urn:xmpp:ping")) {
                        iqPacket = parseUrnPing(parser);
                    } else if (elementName.equals("query") && namespace.equals("reeng:iq:contacts")) {
                        // parse get info
                        iqPacket = parseGetInfo(parser);
                    } else if (elementName.equals("query") && IQCall.containsIQCall(namespace)) {
                        iqPacket = parseIQCall(parser, elementName);
                    } else if (elementName.equals("bind")
                            && namespace.equals("urn:ietf:params:xml:ns:xmpp-bind")) {
                        //  iqPacket = parseAuthentication(parser);
                        iqPacket = parseResourceBinding(parser);
                    } else if (elementName.equals("auth_info")) {
                        iqPacket = parseNonSASLSuccess(parser);
                    }
                    //  parser group
                    else if (namespace.equals("http://jabber.org/protocol/muc#admin")) {
                        iqPacket = parseMembersOfGroupChat(parser);
                    } else if (elementName.equals("query") && (namespace.equals("create") ||
                            namespace.equals("invite") || namespace.equals("rename") ||
                            namespace.equals("leave") || namespace.equals("config") ||
                            namespace.equals("kick") || namespace.equals("makeAdmin") ||
                            namespace.equals("groupPrivate"))) {
                        IQGroup.GroupType groupType = IQGroup.GroupType.fromString(namespace);
                        iqPacket = parseIQGroup(parser, groupType);
                    }
                    // Otherwise, see if there is a registered provider for
                    // this element name and namespace.
                    else {
                        Object provider = ProviderManager.getInstance()
                                .getIQProvider(elementName, namespace);
                        if (provider != null) {
                            if (provider instanceof IQProvider) {
                                iqPacket = ((IQProvider) provider).parseIQ(parser);
                            } else if (provider instanceof Class) {
                                iqPacket = (IQ) parseWithIntrospection(elementName,
                                        (Class) provider, parser);
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("iq")) {
                        done = true;
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Exception", e);
        }
        // Decide what to do when an IQ packet was not understood
        if (iqPacket == null) {
            if (IQ.Type.GET == type || IQ.Type.SET == type) {
                // If the IQ stanza is of type "get" or "set" containing a child
                // element
                // qualified by a namespace it does not understand, then answer
                // an IQ of
                // type "error" with code 501 ("feature-not-implemented")
                iqPacket = new IQ() {
                    public String getChildElementXML() {
                        return null;
                    }
                };
                iqPacket.setPacketID(id);
                iqPacket.setTo(from);
                iqPacket.setFrom(to);
                iqPacket.setType(IQ.Type.ERROR);
                iqPacket.setError(new XMPPError(XMPPError.Condition.feature_not_implemented));
                connection.sendPacket(iqPacket);
                return null;
            } else {
                // If an IQ packet wasn't created above, create an empty IQ
                // packet.
                iqPacket = new IQ() {
                    public String getChildElementXML() {
                        return null;
                    }
                };
            }
        }
        // Set basic values on the iq packet.
        iqPacket.setPacketID(id);
        iqPacket.setTo(to);
        iqPacket.setFrom(from);
        iqPacket.setType(type);
        iqPacket.setError(error);
        return iqPacket;
    }

    /**
     * parse packet for getting member of group
     *
     * @param parser
     * @return
     * @author DungNH8
     */
    private static MUCAdmin parseMembersOfGroupChat(XmlPullParser parser) throws Exception {
        MUCAdmin mucAdmin = new MUCAdmin();
        boolean done = false;
        MUCAdmin.Item item;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("item")) {
                    // get params
                    String jid = parser.getAttributeValue("", "jid");
                    String nick = parser.getAttributeValue("", "nick");
                    String affiliation = parser.getAttributeValue("",
                            "affiliation");
                    String role = parser.getAttributeValue("", "role");
                    // Create packet.
                    item = new MUCAdmin.Item(affiliation, role);
                    // set jid, nick
                    item.setJid(jid);
                    item.setNick(nick);
                    // set item to mucadmin packet
                    mucAdmin.addItem(item);
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return mucAdmin;
    }

    private static IQ parseNonSASLSuccess(XmlPullParser parser) throws Exception {
        NonSASLAuthInfo authentication = new NonSASLAuthInfo();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("token")) {
                    authentication.setToken(parser.nextText());
                } else if (parser.getName().equals("domain_file")) {
                    authentication.setDomainFile(parser.nextText());
                } else if (parser.getName().equals("domain_msg")) {
                    authentication.setDomainMessage(parser.nextText());
                } else if (parser.getName().equals("domain_on_media")) {
                    authentication.setDomainOnMedia(parser.nextText());
                } else if (parser.getName().equals("public_key")) {
                    authentication.setPublicRSAKey(parser.nextText());
                } else if (parser.getName().equals("kservice")) {
                    authentication.setDomainServiceKeeng(parser.nextText());
                } else if (parser.getName().equals("kmedia")) {
                    authentication.setDomainMedia2Keeng(parser.nextText());
                } else if (parser.getName().equals("kimage")) {
                    authentication.setDomainImageKeeng(parser.nextText());
                } else if (parser.getName().equals("vip")) {
                    authentication.setVipInfo(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                } else if (parser.getName().equals("cbnv")) {
                    authentication.setCBNV(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                } else if (parser.getName().equals("call")) {
                    authentication.setCall(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                } else if (parser.getName().equals("ssl")) {
                    authentication.setSSL(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                } else if (parser.getName().equals("smsin")) {
                    authentication.setSmsIn(ConvertHelper.parserIntFromString(parser.nextText(), -1));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("auth_info")) {
                    done = true;
                }
            }
        }
        return authentication;
    }

    private static Authentication parseAuthentication(XmlPullParser parser)
            throws Exception {
        Authentication authentication = new Authentication();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("username")) {
                    authentication.setUsername(parser.nextText());
                } else if (parser.getName().equals("password")) {
                    authentication.setPassword(parser.nextText());
                } else if (parser.getName().equals("digest")) {
                    authentication.setDigest(parser.nextText());
                } else if (parser.getName().equals("resource")) {
                    authentication.setResource(parser.nextText());
                } else if (parser.getName().equals("revision")) {
                    authentication.setRevision(parser.nextText());
                } else if (parser.getName().equals("status")) { // add status
                    authentication.setStatus(parser.nextText());
                } else if (parser.getName().equals("lastChangeAvatar")) {
                    // add last change avatar
                    authentication.setMyAvatarNewChangeTime(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return authentication;
    }

    private static IQGroup parseIQGroup(XmlPullParser parser, IQGroup.GroupType groupType) throws Exception {
        IQGroup iqGroup = new IQGroup();
        iqGroup.setGroupType(groupType);
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("member")) {
                    String jid = parser.getAttributeValue("", "jid");
                    String role = parser.getAttributeValue("", "role");
                    String code = parser.getAttributeValue("", "code");
                    String nickName = parser.getAttributeValue("", "name");
                    String avatar = parser.getAttributeValue("", "lavatar");
                    if (code != null)
                        iqGroup.addMemberObject(jid, role, ConvertHelper.parserIntFromString(code, 200), nickName, avatar);
                    else
                        iqGroup.addMemberObject(jid, role, 200, nickName, avatar);// truong hop query thong tin ko tra ve
                    // truong nay
                    // set =201
                } else if (parser.getName().equals("room")) {
                    String roomJid = parser.getAttributeValue("", "jid");
                    String roomName = parser.getAttributeValue("", "name")
                            .trim();
                    iqGroup.setGroupName(roomName);
                    iqGroup.setGroupJid(roomJid);
                } else if (parser.getName().equals("keep_private")) {
                    int groupPrivate = ConvertHelper.parserIntFromString(parser.getAttributeValue("", "value"), -1);
                    iqGroup.setGroupPrivate(groupPrivate);
                } else if (parser.getName().equals("avatar_created")) {
                    iqGroup.setGroupAvatar(parser.nextText());
                } else if (parser.getName().equals("gtype")) {
                    iqGroup.setGroupClass(ConvertHelper.parserIntFromString(parser.nextText(), 0));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return iqGroup;
    }

    // roster packet
    private static RosterPacket parseRoster(XmlPullParser parser)
            throws Exception {
        RosterPacket roster = new RosterPacket();
        boolean done = false;
        RosterPacket.Item item = null;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("item")) {
                    String jid = parser.getAttributeValue("", "jid");
                    // String name = parser.getAttributeValue("", "name");
                    // Create packet.
                    item = new RosterPacket.Item(jid);
                    // Set ask.
                    String ask = parser.getAttributeValue("", "ask");
                    RosterPacket.ItemStatus pkStatus = RosterPacket.ItemStatus
                            .fromString(ask);
                    item.setItemStatus(pkStatus);
                    // Set status.
                    String status = parser.getAttributeValue("", "status");
                    if (status != null && status.length() > 0) {
                        item.setStatus(status.trim());
                    } else {
                        item.setStatus("");
                    }
                    // Set state.
                    String state = parser.getAttributeValue("", "state");
                    if (state != null && state.length() > 0) {
                        int stateInt = ConvertHelper.parserIntFromString(state, -1);
                        if (stateInt >= 0 && stateInt <= 3) {
                            item.setState(stateInt);
                        }
                    } else {
                        item.setState(0);
                    }
                    // Set last change avatar
                    String lastChangeAvatar = parser.getAttributeValue("",
                            "lastChangeAvatar");
                    if (lastChangeAvatar != null
                            && lastChangeAvatar.length() > 0) {
                        item.setLastChangeAvatar(lastChangeAvatar);
                    }
                    // Set type.
                    String subscription = parser.getAttributeValue("",
                            "subscription");
                    RosterPacket.ItemType type = RosterPacket.ItemType
                            .valueOf(subscription != null ? subscription
                                    : "none");
                    item.setItemType(type);
                }
                if (parser.getName().equals("group") && item != null) {
                    final String groupName = parser.nextText();
                    if (groupName != null && groupName.trim().length() > 0) {
                        item.addGroupName(groupName);
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("item")) {
                    roster.addRosterItem(item);
                }
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return roster;
    }

    private static Registration parseRegistration(XmlPullParser parser)
            throws Exception {
        Registration registration = new Registration();
        Map<String, String> fields = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                // Any element that's in the jabber:iq:register namespace,
                // attempt to parse it if it's in the form <name>value</name>.
                if (parser.getNamespace().equals("jabber:iq:register")) {
                    String name = parser.getName();
                    String value = "";
                    if (fields == null) {
                        fields = new HashMap<String, String>();
                    }
                    if (parser.next() == XmlPullParser.TEXT) {
                        value = parser.getText();
                    }
                    // Ignore instructions, but anything else should be added to
                    // the map.
                    if (!name.equals("instructions")) {
                        fields.put(name, value);
                    } else {
                        registration.setInstructions(value);
                    }
                }
                // Otherwise, it must be a packet extension.
                else {
                    registration.addExtension(PacketParserUtils
                            .parsePacketExtension(parser.getName(),
                                    parser.getNamespace(), parser));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        registration.setAttributes(fields);
        return registration;
    }

    private static Ping parseUrnPing(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        Ping ping = new Ping();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("ping"))
                    ping.setNameSpace(parser.getNamespace());
                else if (parser.getName().equals("query"))
                    ping.setNameSpace(parser.getNamespace());
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("ping") || parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return ping;
    }

    private static Ping parsePing(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        Ping ping = new Ping();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("query"))
                    ping.setNameSpace(parser.getNamespace());
//                parser.nextText();
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return ping;
    }

    private static IQInfo parseIQInfo(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        IQInfo iqInfo = new IQInfo();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                String elementName = parser.getName();
                if (elementName.equals("query")) {
                    iqInfo.setNameSpace(parser.getNamespace());
                } else if (!elementName.equals("iq")) {
                    iqInfo.addElements(elementName, parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return iqInfo;
    }

    private static IQGetInfo parseGetInfo(XmlPullParser parser) throws IOException, XmlPullParserException {
        IQGetInfo iqInfo = new IQGetInfo();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("query"))
                    iqInfo.setNameSpace(parser.getNamespace());
                else if (parser.getName().equals("item")) {
                    String state = parser.getAttributeValue("", "state");
                    // String name = parser.getAttributeValue("", "name");
                    String lastOn = parser.getAttributeValue("", "lastOnline");
                    String lastSeen = parser.getAttributeValue("", "lastSeen");
                    iqInfo.setState(ConvertHelper.parserIntFromString(state, -1));
                    iqInfo.setLastOn(ConvertHelper.parserLongFromString(lastOn, -1));
                    iqInfo.setLastSeen(ConvertHelper.parserLongFromString(lastSeen, -1));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return iqInfo;
    }

    private static IQCall parseIQCall(XmlPullParser parser, String elementName) throws IOException,
            XmlPullParserException {
        IQCall iqCall = new IQCall(elementName);
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("query")) {
                    iqCall.setNameSpace(parser.getNamespace());
                } else if (parser.getName().equals("session")) {
                    iqCall.setCallSession(parser.nextText());
                } else if (parser.getName().equals("caller")) {
                    iqCall.setCaller(parser.nextText());
                } else if (parser.getName().equals("callee")) {
                    iqCall.setCallee(parser.nextText());
                } else if (parser.getName().equals("error")) {
                    iqCall.setErrorCode(parser.nextText());
                } else if (parser.getName().equals("codecPrefs")) {
                    iqCall.setCodecPrefs(parser.nextText());
                } else if (parser.getName().equals("iceservers")) {
                    boolean isDoneIce = false;
                    while (!isDoneIce) {
                        int eventTypeCallData = parser.next();
                        if (eventTypeCallData == XmlPullParser.START_TAG) {
                            String elementData = parser.getName();
                            if (elementData.equals("server")) {
                                iqCall.addIceServer(parser.getAttributeValue("", "user"), parser.getAttributeValue
                                        ("", "credential"), parser.nextText());
                            }
                        } else if (eventTypeCallData == XmlPullParser.END_TAG) {
                            if (parser.getName().equals("iceservers")) {
                                isDoneIce = true;
                            }
                        }
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return iqCall;
    }

    private static Bind parseResourceBinding(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        Bind bind = new Bind();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("jid")) {
                    bind.setJid(parser.nextText());
                } else if (parser.getName().equals("nickname")) {
                    bind.setUserName(parser.nextText());
                } else if (parser.getName().equals("resource")) {
                    bind.setResource(parser.nextText());
                } else if (parser.getName().equals("status")) { // add status
                    bind.setStatus(parser.nextText());
                } else if (parser.getName().equals("lastChangeAvatar")) {
                    // add last change avatar
                    bind.setLastChangeAvatar(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("bind")) {
                    done = true;
                }
            }
        }
        return bind;
    }

    /**
     * Parse the available SASL mechanisms reported from the server.
     *
     * @param parser the XML parser, positioned at the start of the mechanisms
     *               stanza.
     * @return a collection of Stings with the mechanisms included in the
     * mechanisms stanza.
     * @throws Exception if an exception occurs while parsing the stanza.
     */
    public static Collection<String> parseMechanisms(XmlPullParser parser)
            throws Exception {
        List<String> mechanisms = new ArrayList<String>();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG) {
                String elementName = parser.getName();
                if (elementName.equals("mechanism")) {
                    mechanisms.add(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("mechanisms")) {
                    done = true;
                }
            }
        }
        return mechanisms;
    }

    /**
     * Parse the available compression methods reported from the server.
     *
     * @param parser the XML parser, positioned at the start of the compression
     *               stanza.
     * @return a collection of Stings with the methods included in the
     * compression stanza.
     * @throws Exception if an exception occurs while parsing the stanza.
     */
    public static Collection<String> parseCompressionMethods(
            XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> methods = new ArrayList<String>();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG) {
                String elementName = parser.getName();
                if (elementName.equals("method")) {
                    methods.add(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("compression")) {
                    done = true;
                }
            }
        }
        return methods;
    }

    /**
     * Parse a properties sub-packet. If any errors occur while de-serializing
     * Java object properties, an exception will be printed and not thrown since
     * a thrown exception will shut down the entire connection.
     * ClassCastExceptions will occur when both the sender and receiver of the
     * packet don't have identical versions of the same class.
     *
     * @param parser the XML parser, positioned at the start of a properties
     *               sub-packet.
     * @return a map of the properties.
     * @throws Exception if an error occurs while parsing the properties.
     */
    public static Map<String, Object> parseProperties(XmlPullParser parser)
            throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        while (true) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG
                    && parser.getName().equals("property")) {
                // Parse a property
                boolean done = false;
                String name = null;
                String type = null;
                String valueText = null;
                Object value = null;
                while (!done) {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) {
                        String elementName = parser.getName();
                        if (elementName.equals("name")) {
                            name = parser.nextText();
                        } else if (elementName.equals("value")) {
                            type = parser.getAttributeValue("", "type");
                            valueText = parser.nextText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (parser.getName().equals("property")) {
                            if ("integer".equals(type)) {
                                value = Integer.valueOf(valueText);
                            } else if ("long".equals(type)) {
                                value = Long.valueOf(valueText);
                            } else if ("float".equals(type)) {
                                value = Float.valueOf(valueText);
                            } else if ("double".equals(type)) {
                                value = Double.valueOf(valueText);
                            } else if ("boolean".equals(type)) {
                                value = Boolean.valueOf(valueText);
                            } else if ("string".equals(type)) {
                                value = valueText;
                            } else if ("java-object".equals(type)) {
                                try {
                                    byte[] bytes = StringUtils
                                            .decodeBase64(valueText);
                                    ObjectInputStream in = new ObjectInputStream(
                                            new ByteArrayInputStream(bytes));
                                    value = in.readObject();
                                } catch (Exception e) {
                                    Log.e(TAG, "Exception", e);
                                }
                            }
                            if (name != null && value != null) {
                                properties.put(name, value);
                            }
                            done = true;
                        }
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("properties")) {
                    break;
                }
            }
        }
        return properties;
    }

    /**
     * Parses SASL authentication error packets.
     *
     * @param parser the XML parser.
     * @return a SASL Failure packet.
     * @throws Exception if an exception occurs while parsing the packet.
     */
    public static Failure parseSASLFailure(XmlPullParser parser)
            throws Exception {
        String condition = null;
        boolean isLocked = false;
        int lockedTime = 0;
        int retries = -1;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("locked")) {
                    isLocked = true;
                    lockedTime = ConvertHelper.parserIntFromString(parser.getAttributeValue(0), 0);
                }
                if (parser.getName().equals("retries")) {
//                    isLocked = true;
                    retries = ConvertHelper.parserIntFromString(parser.getAttributeValue(0), -1);
                }
                if (!parser.getName().equals("failure")) {
                    condition = parser.getName();
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("failure")) {
                    done = true;
                }
            } else if (eventType == XmlPullParser.TEXT) {
                retries = ConvertHelper.parserIntFromString(parser.getText(), -1);
            }
        }
        return new Failure(condition, isLocked, lockedTime, retries);
    }

    /**
     * Parses stream error packets.
     *
     * @param parser the XML parser.
     * @return an stream error packet.
     * @throws Exception if an exception occurs while parsing the packet.
     */
    public static StreamError parseStreamError(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        StreamError streamError = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG) {
                streamError = new StreamError(parser.getName());
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("error")) {
                    done = true;
                }
            }
        }
        return streamError;
    }

    /**
     * Parses error sub-packets.
     *
     * @param parser the XML parser.
     * @return an error sub-packet.
     * @throws Exception if an exception occurs while parsing the packet.
     */
    public static XMPPError parseError(XmlPullParser parser) throws Exception {
        final String errorNamespace = "urn:ietf:params:xml:ns:xmpp-stanzas";
        String errorCode = "-1";
        String type = null;
        String message = null;
        String condition = null;
        List<PacketExtension> extensions = new ArrayList<PacketExtension>();

        // Parse the error header
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            if (parser.getAttributeName(i).equals("code")) {
                errorCode = parser.getAttributeValue("", "code");
            }
            if (parser.getAttributeName(i).equals("type")) {
                type = parser.getAttributeValue("", "type");
            }
        }
        boolean done = false;
        // Parse the text and condition tags
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("text")) {
                    message = parser.nextText();
                } else {
                    // Condition tag, it can be xmpp error or an application
                    // defined error.
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    if (errorNamespace.equals(namespace)) {
                        condition = elementName;
                    } else {
                        extensions.add(parsePacketExtension(elementName,
                                namespace, parser));
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("error")) {
                    done = true;
                }
            }
        }
        // Parse the error type.
        XMPPError.Type errorType = XMPPError.Type.CANCEL;
        try {
            if (type != null) {
                errorType = XMPPError.Type.valueOf(type.toUpperCase());
            }
        } catch (IllegalArgumentException iae) {
            // Print stack trace. We shouldn't be getting an illegal error type.
            Log.e(TAG, "Exception", iae);
        }
        return new XMPPError(ConvertHelper.parserIntFromString(errorCode, 602), errorType, condition,
                message, extensions);
    }

    /**
     * Parses a packet extension sub-packet.
     *
     * @param elementName the XML element name of the packet extension.
     * @param namespace   the XML namespace of the packet extension.
     * @param parser      the XML parser, positioned at the starting element of the
     *                    extension.
     * @return a PacketExtension.
     * @throws Exception if a parsing error occurs.
     */
    public static PacketExtension parsePacketExtension(String elementName,
                                                       String namespace, XmlPullParser parser) throws Exception {
        // See if a provider is registered to handle the extension.
        Object provider = ProviderManager.getInstance().getExtensionProvider(
                elementName, namespace);
        if (provider != null) {
            if (provider instanceof PacketExtensionProvider) {
                return ((PacketExtensionProvider) provider)
                        .parseExtension(parser);
            } else if (provider instanceof Class) {
                return (PacketExtension) parseWithIntrospection(elementName,
                        (Class) provider, parser);
            }
        }
        // No providers registered, so use a default extension.
        DefaultPacketExtension extension = new DefaultPacketExtension(
                elementName, namespace);
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                // If an empty element, set the value with the empty string.
                if (parser.isEmptyElementTag()) {
                    extension.setValue(name, "");
                }
                // Otherwise, get the the element text.
                else {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.TEXT) {
                        String value = parser.getText();
                        extension.setValue(name, value);
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(elementName)) {
                    done = true;
                }
            }
        }
        return extension;
    }

    private static String getLanguageAttribute(XmlPullParser parser) {
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            String attributeName = parser.getAttributeName(i);
            if ("xml:lang".equals(attributeName)
                    || ("lang".equals(attributeName) && "xml".equals(parser
                    .getAttributePrefix(i)))) {
                return parser.getAttributeValue(i);
            }
        }
        return null;
    }

    public static Object parseWithIntrospection(String elementName,
                                                Class objectClass, XmlPullParser parser) throws Exception {
        boolean done = false;
        Object object = objectClass.newInstance();
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                String stringValue = parser.nextText();
                Class propertyType = object
                        .getClass()
                        .getMethod(
                                "get" + Character.toUpperCase(name.charAt(0))
                                        + name.substring(1)
                        ).getReturnType();
                // Get the value of the property by converting it from a
                // String to the correct object type.
                Object value = decode(propertyType, stringValue);
                // Set the value of the bean.
                object.getClass()
                        .getMethod(
                                "set" + Character.toUpperCase(name.charAt(0))
                                        + name.substring(1), propertyType
                        )
                        .invoke(object, value);
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(elementName)) {
                    done = true;
                }
            }
        }
        return object;
    }

    /**
     * Decodes a String into an object of the specified type. If the object type
     * is not supported, null will be returned.
     *
     * @param type  the type of the property.
     * @param value the encode String value to decode.
     * @return the String value decoded into the specified type.
     * @throws Exception If decoding failed due to an error.
     */
    private static Object decode(Class type, String value) throws Exception {
        if (type.getName().equals("java.lang.String")) {
            return value;
        }
        if (type.getName().equals("boolean")) {
            return Boolean.valueOf(value);
        }
        if (type.getName().equals("int")) {
            return Integer.valueOf(value);
        }
        if (type.getName().equals("long")) {
            return Long.valueOf(value);
        }
        if (type.getName().equals("float")) {
            return Float.valueOf(value);
        }
        if (type.getName().equals("double")) {
            return Double.valueOf(value);
        }
        if (type.getName().equals("java.lang.Class")) {
            return Class.forName(value);
        }
        return null;
    }
}
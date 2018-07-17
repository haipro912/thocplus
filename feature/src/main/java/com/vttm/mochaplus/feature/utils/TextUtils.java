package com.vttm.mochaplus.feature.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.Normalizer;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HaiKE on 9/15/17.
 */

public class TextUtils {
    static final String TAG = "TextUtils";
    private static Pattern patternSpecial;
    private static TextUtils mInstant;
    private final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private Pattern patternNormalizer;
    private Pattern patternA;
    private Pattern patternE;
    private Pattern patternI;
    private Pattern patternO;
    private Pattern patternU;
    private Pattern patternY;
    private Pattern patternD;
    private Pattern patternXmlCr;

    {
        suffixes.put(1_000L, "K");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "B");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    private TextUtils() {
        initPattern();
        initPatternXml();
    }

    public static TextUtils getInstant() {
        if (mInstant == null)
            mInstant = new TextUtils();
        return mInstant;
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public static void copyToClipboard(Context context, String content) {
//        try
//        {
//            if (!hasHoneycomb()) {
//                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                clipboard.setText(content);
//            } else {
//                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                android.content.ClipData clip = android.content.ClipData.newPlainText(context.getResources().getString(R.string.copied_to_clipboard), content);
//                clipboard.setPrimaryClip(clip);
//            }
//        }
//        catch (Exception ex)
//        {
//
//        }
//    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static int countSpace(String content) {
        if (content == null || content.length() <= 0)
            return -1;
        int size = content.length();
        int spaceCount = 0;
        for (int i = 0; i < size; i++) {
            if (content.charAt(i) == ' ') {
                spaceCount++;
            }
        }
        return spaceCount;
    }

    public static boolean hasSpecialCharacter(String input) {
        Matcher matcher = patternSpecial.matcher(input);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public static int parseIntValue(String content, int def) {
        try {
            return Integer.parseInt(content);
        } catch (Exception ex) {
            return def;
        }
    }

    public static String textBoldWithHTML(String text) {
        return "<b>" + text + "</b>";
    }

    // https://www.omniref.com/ruby/gems/babosa/0.3.3/files/lib/babosa/utf8/mappings.rb
    public static <T extends Appendable> T escapeNonLatin(CharSequence sequence, T out) throws java.io.IOException {
        for (int i = 0; i < sequence.length(); i++) {
            char ch = sequence.charAt(i);
            if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.BASIC_LATIN) {
                out.append(ch);
            } else {
                int codepoint = Character.codePointAt(sequence, i);
                // handle supplementary range chars
                i += Character.charCount(codepoint) - 1;
                // emit entity
                out.append("&#x");
                out.append(Integer.toHexString(codepoint));
                out.append(";");
            }
        }
        return out;
    }

    public static String convertUtf8ToUnicode(String input) {
        if (input != null) {
            try {
                String temp = new String(input.getBytes("ISO-8859-1"), "UTF-8");
                return Html.fromHtml(temp).toString();
            } catch (UnsupportedEncodingException e) {
//                Log.e(TAG, e);
            } catch (Exception e) {
//                Log.e(TAG, e);
            }
        }
        return input;
    }

    /*
    * Convert string
    */
    public static String convert(String org) {
        // convert to VNese no sign. @haidh 2008
        char arrChar[] = org.toCharArray();
        char result[] = new char[arrChar.length];
        for (int i = 0; i < arrChar.length; i++) {
            switch (arrChar[i]) {
                case '\u00E1':
                case '\u00E0':
                case '\u1EA3':
                case '\u00E3':
                case '\u1EA1':
                case '\u0103':
                case '\u1EAF':
                case '\u1EB1':
                case '\u1EB3':
                case '\u1EB5':
                case '\u1EB7':
                case '\u00E2':
                case '\u1EA5':
                case '\u1EA7':
                case '\u1EA9':
                case '\u1EAB':
                case '\u1EAD':
                case '\u0203':
                case '\u01CE': {
                    result[i] = 'a';
                    break;
                }
                case '\u00E9':
                case '\u00E8':
                case '\u1EBB':
                case '\u1EBD':
                case '\u1EB9':
                case '\u00EA':
                case '\u1EBF':
                case '\u1EC1':
                case '\u1EC3':
                case '\u1EC5':
                case '\u1EC7':
                case '\u0207': {
                    result[i] = 'e';
                    break;
                }
                case '\u00ED':
                case '\u00EC':
                case '\u1EC9':
                case '\u0129':
                case '\u1ECB': {
                    result[i] = 'i';
                    break;
                }
                case '\u00F3':
                case '\u00F2':
                case '\u1ECF':
                case '\u00F5':
                case '\u1ECD':
                case '\u00F4':
                case '\u1ED1':
                case '\u1ED3':
                case '\u1ED5':
                case '\u1ED7':
                case '\u1ED9':
                case '\u01A1':
                case '\u1EDB':
                case '\u1EDD':
                case '\u1EDF':
                case '\u1EE1':
                case '\u1EE3':
                case '\u020F': {
                    result[i] = 'o';
                    break;
                }
                case '\u00FA':
                case '\u00F9':
                case '\u1EE7':
                case '\u0169':
                case '\u1EE5':
                case '\u01B0':
                case '\u1EE9':
                case '\u1EEB':
                case '\u1EED':
                case '\u1EEF':
                case '\u1EF1': {
                    result[i] = 'u';
                    break;
                }
                case '\u00FD':
                case '\u1EF3':
                case '\u1EF7':
                case '\u1EF9':
                case '\u1EF5': {
                    result[i] = 'y';
                    break;
                }
                case '\u0111': {
                    result[i] = 'd';
                    break;
                }
                case '\u00C1':
                case '\u00C0':
                case '\u1EA2':
                case '\u00C3':
                case '\u1EA0':
                case '\u0102':
                case '\u1EAE':
                case '\u1EB0':
                case '\u1EB2':
                case '\u1EB4':
                case '\u1EB6':
                case '\u00C2':
                case '\u1EA4':
                case '\u1EA6':
                case '\u1EA8':
                case '\u1EAA':
                case '\u1EAC':
                case '\u0202':
                case '\u01CD': {
                    result[i] = 'A';
                    break;
                }
                case '\u00C9':
                case '\u00C8':
                case '\u1EBA':
                case '\u1EBC':
                case '\u1EB8':
                case '\u00CA':
                case '\u1EBE':
                case '\u1EC0':
                case '\u1EC2':
                case '\u1EC4':
                case '\u1EC6':
                case '\u0206': {
                    result[i] = 'E';
                    break;
                }
                case '\u00CD':
                case '\u00CC':
                case '\u1EC8':
                case '\u0128':
                case '\u1ECA': {
                    result[i] = 'I';
                    break;
                }
                case '\u00D3':
                case '\u00D2':
                case '\u1ECE':
                case '\u00D5':
                case '\u1ECC':
                case '\u00D4':
                case '\u1ED0':
                case '\u1ED2':
                case '\u1ED4':
                case '\u1ED6':
                case '\u1ED8':
                case '\u01A0':
                case '\u1EDA':
                case '\u1EDC':
                case '\u1EDE':
                case '\u1EE0':
                case '\u1EE2':
                case '\u020E': {
                    result[i] = 'O';
                    break;
                }
                case '\u00DA':
                case '\u00D9':
                case '\u1EE6':
                case '\u0168':
                case '\u1EE4':
                case '\u01AF':
                case '\u1EE8':
                case '\u1EEA':
                case '\u1EEC':
                case '\u1EEE':
                case '\u1EF0': {
                    result[i] = 'U';
                    break;
                }

                case '\u00DD':
                case '\u1EF2':
                case '\u1EF6':
                case '\u1EF8':
                case '\u1EF4': {
                    result[i] = 'Y';
                    break;
                }
                case '\u0110':
                case '\u00D0':
                case '\u0089': {
                    result[i] = 'D';
                    break;
                }
                default:
                    result[i] = arrChar[i];
            }
        }
        return new String(result);
    }

    public static String convertTag(String str) {
        if (str == null)
            str = "";
        return str.replace("<", "&lt;").replace("[keke]", "<img src=\"em_001\"/>").replace("[lol]", "<img src=\"em_002\"/>").replace("[nice]", "<img src=\"em_003\"/>").replace("[dizzy]", "<img src=\"em_004\"/>")
                .replace("[cry]", "<img src=\"em_005\"/>").replace("[greedy]", "<img src=\"em_006\"/>").replace("[crazy]", "<img src=\"em_007\"/>").replace("[hum]", "<img src=\"em_008\"/>").replace("[cute]", "<img src=\"em_009\"/>")
                .replace("[angry]", "<img src=\"em_010\"/>").replace("[sweat]", "<img src=\"em_011\"/>").replace("[smile]", "<img src=\"em_012\"/>").replace("[sleepy]", "<img src=\"em_013\"/>").replace("[money]", "<img src=\"em_014\"/>")
                .replace("[cool]", "<img src=\"em_016\"/>").replace("[surprise]", "<img src=\"em_018\"/>").replace("[lust]", "<img src=\"em_022\"/>").replace("[clap]", "<img src=\"em_023\"/>").replace("[sad]", "<img src=\"em_024\"/>")
                .replace("[think]", "<img src=\"em_025\"/>").replace("[sick]", "<img src=\"em_026\"/>").replace("[kiss]", "<img src=\"em_027\"/>").replace("[supercilious]", "<img src=\"em_029\"/>")
                .replace("[quiet]", "<img src=\"em_032\"/>").replace("[yawn]", "<img src=\"em_034\"/>").replace("[question]", "<img src=\"em_036\"/>").replace("[winking]", "<img src=\"em_037\"/>").replace("[shy]", "<img src=\"em_038\"/>")
                .replace("[gonna cry]", "<img src=\"em_039\"/>").replace("[silent]", "<img src=\"em_041\"/>").replace("[strong]", "<img src=\"em_042\"/>").replace("[weak]", "<img src=\"em_043\"/>")
                .replace("[camera]", "<img src=\"em_048\"/>").replace("[car]", "<img src=\"em_049\"/>").replace("[plane]", "<img src=\"em_050\"/>").replace("[love]", "<img src=\"em_051\"/>").replace("[rabbit]", "<img src=\"em_053\"/>")
                .replace("[panda]", "<img src=\"em_052\"/>").replace("[ok]", "<img src=\"em_056\"/>").replace("[like]", "<img src=\"em_057\"/>").replace("[yeah]", "<img src=\"em_059\"/>").replace("[fist]", "<img src=\"em_061\"/>")
                .replace("[rose]", "<img src=\"em_064\"/>").replace("[heart]", "<img src=\"em_065\"/>").replace("[broken heart]", "<img src=\"em_066\"/>").replace("[pig]", "<img src=\"em_067\"/>")
                .replace("[coffee]", "<img src=\"em_068\"/>").replace("[mic]", "<img src=\"em_069\"/>").replace("[moon]", "<img src=\"em_070\"/>").replace("[sun]", "<img src=\"em_071\"/>").replace("[beer]", "<img src=\"em_072\"/>")
                .replace("[gift]", "<img src=\"em_074\"/>").replace("[clock]", "<img src=\"em_076\"/>").replace("[bike]", "<img src=\"em_077\"/>").replace("[cake]", "<img src=\"em_078\"/>").replace("[snow]", "<img src=\"em_081\"/>")
                .replace("[snowman]", "<img src=\"em_082\"/>").replace("[hat]", "<img src=\"em_083\"/>").replace("[leaf]", "<img src=\"em_084\"/>").replace("[football]", "<img src=\"em_085\"/>").replace(":del", "<img src=\"key_back\"/>")
                .replace("[lt]", "<").replace("[gt]", ">").replace("\n", "<br/>");
    }

    public String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    private void initPattern() {
        patternNormalizer = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        patternA = Pattern.compile("[ă,â,à,ằ,ầ,á,ắ,ấ,ả,ẳ,ẩ,ã,ẵ,ẫ,ạ,ặ,ậ]");
        patternE = Pattern.compile("[è,é,ẻ,ẽ,ẹ,ê,ề,ế,ể,ễ,ệ]");
        patternI = Pattern.compile("[ì,í,ỉ,ĩ,ị]");
        patternO = Pattern.compile("[ô,ơ,ò,ồ,ờ,ó,ố,ớ,ỏ,ổ,ở,õ,ỗ,ỡ,ọ,ộ,ợ]");
        patternU = Pattern.compile("[ư,ù,ừ,ú,ứ,ủ,ử,ũ,ữ,ụ,ự]");
        patternY = Pattern.compile("[ỳ,ý,ỷ,ỹ,ỵ]");
        patternD = Pattern.compile("[đ]");
        patternSpecial = Pattern.compile("[\"{}]");
    }

//    public static String convert(String input) {
//        input = convertUtf8ToUnicode(input);
//        if (TextUtils.isEmpty(input))
//            return input;
//        else
//            return input.toLowerCase();
//    }

    private void initPatternXml() {
        patternXmlCr = Pattern.compile("\n");
    }

    public String convertUnicodeToAscci(String input) {
        input = input.toLowerCase();
        // replace dd
        input = patternD.matcher(input).replaceAll("d");
        if (Build.VERSION.SDK_INT >= 9) {
            String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
            return patternNormalizer.matcher(nfdNormalizedString).replaceAll("");
        } else {
            input = patternA.matcher(input).replaceAll("a");
            input = patternE.matcher(input).replaceAll("e");
            input = patternI.matcher(input).replaceAll("i");
            input = patternO.matcher(input).replaceAll("o");
            input = patternU.matcher(input).replaceAll("u");
            input = patternY.matcher(input).replaceAll("y");
            return input;
        }
    }

    public String escapeURL(String input) {
        input = patternXmlCr.matcher(input).replaceAll("<br/>");
        return input;
    }

    /**
     * remove underline suggest text
     *
     * @param spannable
     */
    public void removeUnderlines(Spannable spannable) {
        UnderlineSpan underlineSpans[] = spannable.getSpans(0, spannable.length(), UnderlineSpan.class);
        for (UnderlineSpan underlineSpan : underlineSpans) {
            spannable.removeSpan(underlineSpan);
        }
        /*
         * URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class); for
		 * (URLSpan span: spans) { int start = s.getSpanStart(span); int end =
		 * s.getSpanEnd(span); NoUnderlineSpan noUnderline = new
		 * NoUnderlineSpan(); s.setSpan(noUnderline, start, end, 0); }
		 */
    }

    /**
     * remove style text
     *
     * @param spannable
     */
    public void removeStyles(Spannable spannable) {
        StyleSpan styleSpans[] = spannable.getSpans(0, spannable.length(), StyleSpan.class);
        for (StyleSpan styleSpan : styleSpans) {
            if (styleSpan.getStyle() == Typeface.BOLD) {
                spannable.removeSpan(styleSpan);
            }
            if (styleSpan.getStyle() == Typeface.ITALIC) {
                spannable.removeSpan(styleSpan);
            }
        }
    }

    public static String getDomainName(String url) {
        try
        {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }
        catch (Exception e)
        {
            return "";
        }
    }
}

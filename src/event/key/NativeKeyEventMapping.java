package event.key;

import static org.jnativehook.keyboard.NativeKeyEvent.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import util.map.WrappedMap;

@NonNullByDefault
public class NativeKeyEventMapping extends WrappedMap<Integer, String> {

    /**
     * Trims the remapped text and treats it as null (i.e. ignore keypresses from
     * this button) if its an empty string
     */
    public static @Nullable String processRawMappedValue(@Nullable String remappedText) {
        if (remappedText == null)
            return null;
        remappedText = remappedText.trim();
        if (remappedText.isEmpty())
            return null;
        return remappedText;
    }

    private static void addKeyCodes(int... keyCodes) {
        // Handle potential duplicates in text representations of the different virtual key codes
        for (int keyCode : keyCodes) {
            String baseText = getKeyText(keyCode).replace(",", "").trim();
            String text = baseText;
            int count = 1;
            while (DEFAULT.containsValue(text)) {
                text = String.format("%s_%d", baseText, ++count);
            }
            DEFAULT.put(keyCode, text);
        }
    }

    public static final NativeKeyEventMapping DEFAULT = new NativeKeyEventMapping();

    static {
        addKeyCodes(VC_A, VC_B, VC_C, VC_D, VC_E, VC_F, VC_G, VC_H, VC_I, VC_J, VC_K, VC_L, VC_M, VC_N, VC_O, VC_P, VC_Q, VC_R, VC_S, VC_T, VC_U, VC_V, VC_W, VC_X, VC_Y, VC_Z);
        addKeyCodes(VC_1, VC_2, VC_3, VC_4, VC_5, VC_6, VC_7, VC_8, VC_9, VC_0);
        addKeyCodes(VC_F1, VC_F2, VC_F3, VC_F4, VC_F5, VC_F6, VC_F7, VC_F8, VC_F9, VC_F10, VC_F11, VC_F12);
        addKeyCodes(VC_F13, VC_F14, VC_F15, VC_F16, VC_F17, VC_F18, VC_F19, VC_F20, VC_F21, VC_F22, VC_F23, VC_F24);
        addKeyCodes(VC_BACKQUOTE);
        addKeyCodes(VC_SPACE);
        addKeyCodes(VC_MINUS, VC_EQUALS, VC_BACKSPACE);
        addKeyCodes(VC_OPEN_BRACKET, VC_CLOSE_BRACKET, VC_BACK_SLASH);
        addKeyCodes(VC_COMMA, VC_PERIOD, VC_SLASH);
        addKeyCodes(VC_SEMICOLON, VC_QUOTE, VC_ENTER);
        addKeyCodes(VC_NUM_LOCK, VC_SEPARATOR);
        addKeyCodes(VC_TAB, VC_CAPS_LOCK);
        addKeyCodes(VC_SHIFT, VC_CONTROL, VC_ALT, VC_META, VC_CONTEXT_MENU);
        addKeyCodes(VC_ESCAPE);
        addKeyCodes(VC_PRINTSCREEN, VC_SCROLL_LOCK, VC_PAUSE);
        addKeyCodes(VC_INSERT, VC_DELETE, VC_HOME, VC_END, VC_PAGE_UP, VC_PAGE_DOWN);
        addKeyCodes(VC_UP, VC_LEFT, VC_CLEAR, VC_RIGHT, VC_DOWN);
        addKeyCodes(VC_POWER, VC_SLEEP, VC_WAKE);
        addKeyCodes(VC_MEDIA_PLAY, VC_MEDIA_STOP, VC_MEDIA_PREVIOUS, VC_MEDIA_NEXT, VC_MEDIA_SELECT, VC_MEDIA_EJECT);
        addKeyCodes(VC_VOLUME_MUTE, VC_VOLUME_UP, VC_VOLUME_DOWN);
        addKeyCodes(VC_APP_MAIL, VC_APP_CALCULATOR, VC_APP_MUSIC, VC_APP_PICTURES);
        addKeyCodes(VC_BROWSER_SEARCH, VC_BROWSER_HOME, VC_BROWSER_BACK, VC_BROWSER_FORWARD, VC_BROWSER_STOP, VC_BROWSER_REFRESH, VC_BROWSER_FAVORITES);
        addKeyCodes(VC_KATAKANA, VC_UNDERSCORE, VC_FURIGANA, VC_KANJI, VC_HIRAGANA, VC_YEN);
        addKeyCodes(VC_SUN_STOP, VC_SUN_PROPS, VC_SUN_FRONT, VC_SUN_OPEN, VC_SUN_FIND, VC_SUN_AGAIN, VC_SUN_UNDO, VC_SUN_COPY, VC_SUN_INSERT, VC_SUN_CUT);
        addKeyCodes(VC_SUN_HELP);
        addKeyCodes(VC_UNDEFINED);
    }

    public static final Map<String, Integer> DEFAULT_INVERSE = DEFAULT.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    public static NativeKeyEventMapping createDefault() {
        return new NativeKeyEventMapping(DEFAULT);
    }

    public static String getDefaultText(int key) {
        return Objects.requireNonNull(DEFAULT.get(key));
    }

    private NativeKeyEventMapping() {
        this(new LinkedHashMap<>());
    }

    public NativeKeyEventMapping(NativeKeyEventMapping other) {
        // Use a copy so that we don't modify the backing map of the other remapping
        super(new LinkedHashMap<>(other.map));
    }

    private NativeKeyEventMapping(LinkedHashMap<Integer, String> map) {
        // Make sure that all access to the backing map is synchronised
        super(Collections.synchronizedMap(map));
    }

    private String buildString(Function<Entry<Integer, String>, String> entryWriter) {
        StringBuilder sb = new StringBuilder();
        entrySet().forEach(entry -> sb.append(entryWriter.apply(entry) + "\n"));

        // Remove the last newline if present
        String str = sb.toString();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    public String getDefaultToMappedText() {
        return buildString(entry -> {
            Integer key = entry.getKey();
            String value = entry.getValue();
            return String.format("%s, %s", DEFAULT.get(key), value == null ? "" : value);
        });
    }

    @Override public String toString() {
        return buildString(entry -> {
            String value = entry.getValue();
            return String.format("%d, %s", entry.getKey(), value == null ? "" : value);
        });
    }

}

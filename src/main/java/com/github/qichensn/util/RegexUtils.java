package com.github.qichensn.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 正则表达式工具类
 */
public class RegexUtils {

    // 使用线程安全的Map缓存已编译的Pattern对象，避免重复编译，提升性能
    private static final ConcurrentHashMap<String, Pattern> patternCache = new ConcurrentHashMap<>();

    /**
     * 判断给定的字符串是否是一个语法正确的正则表达式。
     *
     * @param regex 待检验的字符串
     * @return 如果是有效的正则表达式则返回true，否则返回false
     */
    public static boolean isValidRegex(String regex) {
        if (regex == null) {
            return false; // null不是有效正则表达式
        }
        try {
            // 尝试编译正则表达式，如果失败会抛出PatternSyntaxException
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            // 捕获到语法异常，说明不是有效的正则表达式
            return false;
        }
    }

    /**
     * 判断目标字符串是否完全匹配给定的正则表达式。
     * <p>
     * 注意：此方法使用的是完全匹配（matches），即整个输入字符串都必须符合正则表达式。
     * 如果需要查找部分匹配，请使用 {@link #find(String, String)} 方法。
     * </p>
     *
     * @param input 目标字符串
     * @param regex 正则表达式字符串
     * @return 如果input完全匹配regex则返回true，否则返回false。
     *         如果regex不是一个有效的正则表达式，则返回false。
     *         如果input或regex为null，则返回false。
     */
    public static boolean matches(String input, String regex) {
        if (input == null || regex == null) {
            return false;
        }
        if (!isValidRegex(regex)) {
            return false; // 如果正则表达式无效，直接返回false
        }
        try {
            // 从缓存获取Pattern，如果不存在则编译并放入缓存
            Pattern pattern = patternCache.computeIfAbsent(regex, Pattern::compile);
            return pattern.matcher(input).matches();
        } catch (Exception e) {
            // 虽然已经检查了有效性，但理论上仍可能有其他运行时异常（尽管罕见）
            // 为了健壮性，捕获所有异常并返回false
            return false;
        }
    }


    /**
     * 判断目标字符串中是否存在与给定正则表达式匹配的部分。
     * <p>
     * 注意：此方法使用的是查找匹配（find），即只要输入字符串中有子串符合正则表达式即可。
     * 如果需要完全匹配，请使用 {@link #matches(String, String)} 方法。
     * </p>
     *
     * @param input 目标字符串
     * @param regex 正则表达式字符串
     * @return 如果input中存在匹配regex的子串则返回true，否则返回false。
     *         如果regex不是一个有效的正则表达式，则返回false。
     *         如果input或regex为null，则返回false。
     */
    public static boolean find(String input, String regex) {
        if (input == null || regex == null) {
            return false;
        }
        if (!isValidRegex(regex)) {
            return false; // 如果正则表达式无效，直接返回false
        }
        try {
            // 从缓存获取Pattern，如果不存在则编译并放入缓存
            Pattern pattern = patternCache.computeIfAbsent(regex, Pattern::compile);
            return pattern.matcher(input).find();
        } catch (Exception e) {
            // 同上，为了健壮性捕获异常
            return false;
        }
    }

    /**
     * （可选）清除内部缓存的Pattern对象。
     * 在长时间运行且使用了大量不同正则表达式的应用中，
     * 可能需要调用此方法来释放内存。
     */
    public static void clearCache() {
        patternCache.clear();
    }

    // --- 示例用法 ---
    public static void main(String[] args) {
        System.out.println("--- 测试 isValidRegex ---");
        System.out.println("isValidRegex(\"[a-z]+\") -> " + isValidRegex("[a-z]+")); // true
        System.out.println("isValidRegex(\"[a-z++\") -> " + isValidRegex("[a-z++"));   // false
        System.out.println("isValidRegex(null) -> " + isValidRegex(null));             // false
        System.out.println("isValidRegex(\"\") -> " + isValidRegex(""));               // true (空字符串是有效的正则表达式)


        System.out.println("\n--- 测试 matches ---");
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        String validEmail = "user@example.com";
        String invalidEmail = "user@.com";

        System.out.println("matches(\"" + validEmail + "\", \"" + emailRegex + "\") -> " +
                matches(validEmail, emailRegex)); // true
        System.out.println("matches(\"" + invalidEmail + "\", \"" + emailRegex + "\") -> " +
                matches(invalidEmail, emailRegex)); // false

        String digitRegex = "\\d+"; // 匹配一个或多个数字
        String allDigits = "12345";
        String notAllDigits = "123abc";

        System.out.println("matches(\"" + allDigits + "\", \"" + digitRegex + "\") -> " +
                matches(allDigits, digitRegex)); // true
        System.out.println("matches(\"" + notAllDigits + "\", \"" + digitRegex + "\") -> " +
                matches(notAllDigits, digitRegex)); // false (因为不是完全匹配)


        System.out.println("\n--- 测试 find ---");
        System.out.println("find(\"" + notAllDigits + "\", \"" + digitRegex + "\") -> " +
                find(notAllDigits, digitRegex)); // true (因为包含数字子串)


        System.out.println("\n--- 测试无效正则表达式 ---");
        System.out.println("matches(\"test\", \"[invalid++\") -> " + matches("test", "[invalid++")); // false
        System.out.println("find(\"test\", \"[invalid++\") -> " + find("test", "[invalid++"));       // false

    }
}
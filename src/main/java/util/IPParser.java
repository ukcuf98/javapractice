package util;

///
// IP地址查询器
//
// @author iPan
// @version 2014-2-16
//
public final class IPParser {
    private static final String DATA_PATH = "qqwry.dat";

    // 获取指定IP的城市
    public static String getCountry(String ip) {
        return new SubIPParser(DATA_PATH).seek(ip).getCountry();
    }

    // 获取指定IP的位置
    public static String getLocal(String ip) {
        return new SubIPParser(DATA_PATH).seek(ip).getLocation();
    }

    // 获取指定IP的城市+位置
    public static String getForSeparator(String ip, String sep) {
        SubIPParser parser = new SubIPParser(DATA_PATH).seek(ip);
        return parser.getCountry() + sep + parser.getLocation();
    }

    private IPParser() {
    }

    // 对原版代码做了重构，逻辑保持不变；
    public static void main(String[] args) throws Exception {
        long initUsedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        long start = System.currentTimeMillis();
        // 查询IP地址
        System.out.println(IPParser.getForSeparator("101.251.207.114", ", "));
        System.out.println(IPParser.getForSeparator("60.247.59.68", ", "));
        long end = System.currentTimeMillis();
        long endUsedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        // 性能测试
        System.out.println("time spent:" + (end - start) + " ms");
        System.out.println("memory consumes:" + (endUsedMemory - initUsedMemory) / 1024 + " kb");
    }

}
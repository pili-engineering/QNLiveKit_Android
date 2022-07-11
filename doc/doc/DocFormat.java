//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import jdk.nashorn.internal.parser.JSONParser;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class DocFormat {
//
//    public String toJson() {
//        return  JSON.toJSONString(this);
//    }
//
//    public HashMap<String, String> reflect;
//    public String name;
//    public boolean home;
//    public List<NavItem> nav;
//    public List<BlockItem> blocks = new ArrayList<>();
//    public Describe describe;
//
//    /**
//     * 类
//     */
//    public static class BlockItem {
//        public String name;
//        public List<String> desc = new ArrayList<>();
//        public String example;
//        public List<TabItem> tab = new ArrayList<>();
//        public List<ElementItem> elements = new ArrayList<>();
//    }
//
//    //方法
//    public static class ElementItem {
//        public String name;
//        public String sign;
//        public List<String> desc = new ArrayList<>();
//        public List<String> note;
//        public List<String> warning;
//
//        public List<ParameterItem> parameters = new ArrayList<>();
//        public String returns;
//        public String declaration;
//        public List<CodeItem> code;
//        public List<TabItem> tab;
//    }
//
//    public static class TabItem {
//        public String name;
//        public List<String> list;
//    }
//
//    public static class ParameterItem {
//        public String name;
//        public String optional;
//        public String type;
//        public String desc;
//    }
//
//    public static class CodeItem {
//        public String code;
//        public String describe;
//        public String processing;
//    }
//
//    public static class NavItem {
//        public boolean active;
//        public String link;
//    }
//
//    public static class Describe {
//        public List<String> content = new ArrayList<>();
//        public List<String> note;
//        public String warning;
//    }
//}

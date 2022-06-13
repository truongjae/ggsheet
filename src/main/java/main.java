import util.ApiUtil;
import util.ContentType;

public class main {
    public static void main(String[] args) {
        String username = "username1";
        String password = "123";
        String body = "entry.1574514420="+username+"&"+"entry.1352636205="+password;
        ApiUtil.postMethod("https://docs.google.com/forms/u/0/d/e/1FAIpQLScDw4216M5BqZkmCh2kLDg4azzRPQHf0NsVLTD9WESSxdAwuQ/formResponse",
                body,null,null, ContentType.FORM_DATA);
    }
}

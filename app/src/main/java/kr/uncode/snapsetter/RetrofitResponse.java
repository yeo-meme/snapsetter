package kr.uncode.snapsetter;

import java.util.Date;
import java.util.List;

public class RetrofitResponse {

    public List<Documents> documents;

    class Documents {
        String collection;
        Date datetime;
        String display_sitename;
        String doc_url;
        String image_url;
        String thumbnail_url;
        int height;
        int width;

    }
}

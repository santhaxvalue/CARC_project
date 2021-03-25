package utils;

import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.MediaType;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.OkHttpClient;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Request;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.RequestBody;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Response;

import org.json.JSONObject;

/**
 * Created by ramsrini on 04/06/18.
 */

public class KairosActivity {

        private final static String KAIROS_URL = "https://api.kairos.com/enroll";

        private String serverResponse;

        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
        private JSONObject postdata = new JSONObject();



        public String HttpConnection(String image, String subjectID, String galleryName) {
            OkHttpClient client = new OkHttpClient();

            try {
                postdata.put("image", image);
                postdata.put("subject_id", subjectID);
                postdata.put("gallery_name", galleryName);

                RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                String KEY = "e51194acf682991706d76d6ea1508032";
                String ID = "e6e84ead";
                final Request request = new Request.Builder()
                        .url(KAIROS_URL)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("app_id", ID)
                        .addHeader("app_key", KEY)
                        .build();

                Response response = client.newCall(request).execute();
                serverResponse = response.body().string();

            } catch (Exception ex) {
                ex.getMessage();
            }

            return serverResponse;
        }

}

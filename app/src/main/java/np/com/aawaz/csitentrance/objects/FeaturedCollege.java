package np.com.aawaz.csitentrance.objects;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FeaturedCollege {
    public String banner_image;
    public String name;
    public String address;
    public String detail;
    public long phone;
    public String website;

    public FeaturedCollege() {

    }
}

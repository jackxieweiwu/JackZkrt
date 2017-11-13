package zkrtdrone.zkrt.com.maplib.info.until;

/**
 * Created by fhuya on 11/11/14.
 */
public interface UnitProvider {

    public final static String SQUARE_SYMBOL = "\u00B2";

    String areaToString(double areaInSqMeters);

    String distanceToString(double distanceInMeters);

}

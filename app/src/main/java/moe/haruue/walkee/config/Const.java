package moe.haruue.walkee.config;

/**
 * Global constant
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class Const {

    public static final String SPKEY_USERNAME = "username";
    public static final String SPKEY_USER_AVATAR = "user_avatar";
    public static final String SPKEY_MODE = "mode";
    public static final String SPKEY_CUSTOMIZE_TIME_START = "customize_time_start";
    public static final String SPKEY_CUSTOMIZE_TIME_END = "customize_time_end";

    public static final String FILE_NAME_AVATAR = "avatar";

    public static final String DB_NAME_PERMISSION = "permission.db";
    public static final int DB_VER_PERMISSION = 1;
    public static final String DB_NAME_LOG = "log.db";
    public static final int DB_VER_LOG = 1;

    public static final int MODE_EASY = 0;
    public static final int MODE_HARD = 1;

    public static final long INTERVAL = 250;
    public static final long TIMEOUT_BACK_STAND = 700;
    public static final long TIMEOUT_BACK_STAND_LONG = 2000;
    public static final long TIMEOUT_UNLOCK_HARD = 1_200;
    public static final long TIMEOUT_RELOCK_HARD = 120_000;

}

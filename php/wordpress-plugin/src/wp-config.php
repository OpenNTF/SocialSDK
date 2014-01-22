***REMOVED***
/**
 * The base configurations of the WordPress.
 *
 * This file has the following configurations: MySQL settings, Table Prefix,
 * Secret Keys, WordPress Language, and ABSPATH. You can find more information
 * by visiting {@link http://codex.wordpress.org/Editing_wp-config.php Editing
 * wp-config.php} Codex page. You can get the MySQL settings from your web host.
 *
 * This file is used by the wp-config.php creation script during the
 * installation. You don't have to use the web site, you can just copy this file
 * to "wp-config.php" and fill in the values.
 *
 * @package WordPress
 */

// ** MySQL settings - You can get this info from your web host ** //
/** The name of the database for WordPress */
define('DB_NAME', 'wordpress');

/** MySQL database username */
define('DB_USER', 'root');

/** MySQL database password */
define('DB_PASSWORD', 'ymi123');

/** MySQL hostname */
define('DB_HOST', 'localhost');

/** Database Charset to use in creating database tables. */
define('DB_CHARSET', 'utf8');

/** The Database Collate type. Don't change this if in doubt. */
define('DB_COLLATE', '');

/**#@+
 * Authentication Unique Keys and Salts.
 *
 * Change these to different unique phrases!
 * You can generate these using the {@link https://api.wordpress.org/secret-key/1.1/salt/ WordPress.org secret-key service}
 * You can change these at any point in time to invalidate all existing cookies. This will force all users to have to log in again.
 *
 * @since 2.6.0
 */
define('AUTH_KEY',         'X]}oV_Ao$p)ng{Td|0n+[`GK]C@w}VO^rt(. l<@Fctf:qdhcn$L3f,)wQ:R9@<.');
define('SECURE_AUTH_KEY',  'm}@O-d!)27M CC|^%,a xF67H2)sv}JN7I>{Ncis$|K9Ufdu)g?bRp~w2J$n0.{S');
define('LOGGED_IN_KEY',    '--|Q+`EEw]<YBta|do%&zN-5p4%69VF-9:H9FeM2%8B#2SdZYfhB/Ma=~DYEo^_W');
define('NONCE_KEY',        '-Q+|mEAi-+`T|&P YWr-#L:sF-***1ryK%J<!Qh`Z(9.^-QcsL+9~&-+3,8A6z//');
define('AUTH_SALT',        '5+(VlQxxa9Qu&(U,OhG{j#S:IjGSJ5RjDxUIU2Oe{ktM/,+!<4?{O|o[BUZtVcjK');
define('SECURE_AUTH_SALT', 'q$y2.mGVGeNdSE3|-3/DPm:WURMh-FP4y9*>=81`.n?j8D>j5O6#o,D%^XDfJhe{');
define('LOGGED_IN_SALT',   '6C)t{UltM1e6!*!tM2 ITTJ>g(41&xgPa! cM<HM.^=i*.V`TLf%,6pctvCZ2ONy');
define('NONCE_SALT',       'B<S9U{tle1|u#&pr-Ec2]appDwF&D-1|1l8K-xZDv!@T8]K16:-8Q_|QYJE6a%WL');

/**#@-*/

/**
 * WordPress Database Table prefix.
 *
 * You can have multiple installations in one database if you give each a unique
 * prefix. Only numbers, letters, and underscores please!
 */
$table_prefix  = 'wp_';

/**
 * WordPress Localized Language, defaults to English.
 *
 * Change this to localize WordPress. A corresponding MO file for the chosen
 * language must be installed to wp-content/languages. For example, install
 * de_DE.mo to wp-content/languages and set WPLANG to 'de_DE' to enable German
 * language support.
 */
define('WPLANG', '');

/**
 * For developers: WordPress debugging mode.
 *
 * Change this to true to enable the display of notices during development.
 * It is strongly recommended that plugin and theme developers use WP_DEBUG
 * in their development environments.
 */
define('WP_DEBUG', false);

/* That's all, stop editing! Happy blogging. */

/** Absolute path to the WordPress directory. */
if ( !defined('ABSPATH') )
	define('ABSPATH', dirname(__FILE__) . '/');

/** Sets up WordPress vars and included files. */
require_once(ABSPATH . 'wp-settings.php');

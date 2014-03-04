// Profile for mobile builds on WebKit.
//
// Use when:
//		- target to webkit platforms (iOS and Android)
//		- document is in standards mode (i.e., with <!DOCTYPE html>)
// Usage:
//		./build.sh releaseDir=... action=release optimize=closure profile=webkitMobile

var profile = {
	// relative to this file
	basePath: ".",

	// relative to base path
	releaseDir: "./release/dojo-nonamd",

	stripConsole: "none",

	// Use closure to optimize, to remove code branches for has("ie") etc.
	optimize: "closure",
	layerOptimize: "closure",

	action: "release",

	selectorEngine: "lite",

	cssOptimize: "comments",

	internStrings: true,

	localeList:[],

	packages: [
		{
			name: "sbt",
			location: "./sbt"
		}
	],

	resourceTags : {
	    amd : function(filename, mid) {
	        return /\.js$/.test(filename);
		}
	},

	// since this build it intended to be utilized with properly-expressed AMD modules;
	// don't insert absolute module ids into the modules
	insertAbsMids:0,

	// this is a "new-version" profile since is sets the variable "profile" rather than "dependencies"; therefore
	// the layers property is a map from AMD module id to layer properties...
	layers: {
		"sbt-core-dojo-nonamd": {
			// the module dojo/dojo is the default loader (you can make multiple bootstraps with the new builder)
			include: [
				"sbt/main"
			],
			exclude: [
				"sbt/_config"
			],
			customBase: true,
			boot: false 
		},
		"sbt-extra-controls-dojo-nonamd": {
			include: [
				"sbt/control-main"
			],
			exclude: [
				"sbt/_config"
			],
			customBase: true,
			boot: false 
		}
	}
};

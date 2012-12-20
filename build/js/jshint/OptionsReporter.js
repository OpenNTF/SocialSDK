/**
 * OptionsReporter.js
 *
 * A reporter that discards many warnings that can't be disabled with options.
 * This file was based on our default reporter so output is the same.
 *
 * @author Carlos Manias
 *
 * Usage:
 *   jshint myfile.js --reporter=OptionsReporter.js
 */

module.exports = {
    reporter: function (results, data) {

        var len = results.length;
        var str = "";
        var sbtConfig;

        var filteredResults = [];
        var fs = require("fs");
        if (fs) {
            //We read the config file
            var fileLocation = 'jshint/jsHint.json';
            var contents = fs.readFileSync(fileLocation, 'utf8');
            
            //We strip out single and multi-line comments
            contents = contents.replace(/\/\/.*?(?=\n|\r|$)|\/\*[\s\S]+?\*\//g, '');

            //We parse the json
            var data = JSON.parse(contents);
            sbtConfig = data.predef;
        }
        results.forEach(function (result) {
            var file = result.file;
            var error = result.error;
            var ignoredError = false;
            var matches;

            if (!sbtConfig.mixspaces&&error.reason.match(/Mixed spaces and tabs\./)) {
                ignoredError = true;
	        }
            
            if (!sbtConfig.unsafechar&&error.reason.match(/Unsafe character\./)) {
               ignoredError = true;
            }
            
            if ((matches = error.reason.match(/Missing space after '(.+)'\./))) {
                if (matches[1]==='function'&&error.evidence.match(/.*function([\w,?]*)\s*{?\s*/)) {
                    if (sbtConfig.anonfunc) {
                        error.reason = 'Anonymous function, please name it to increase servicieability';
                        ignoredError = false;
                    } else {
                        ignoredError = true;
                    }
                } else if (!sbtConfig.missingspace) {
                    ignoredError = true;
                }
            }
            
            if (!sbtConfig.indentation&&error.reason.match(/Expected '.+' to have an indentation at \d{0,4} instead at \d{0,4}\./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.unexpectedspace&&error.reason.match(/Unexpected space after '.*'\./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.useXtocompare&&error.reason.match(/Use '\S+' to compare with '.*'\./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.confusinguseof&&error.reason.match(/Confusing use of '.*'\./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.unsecaped&&error.reason.match(/Unescaped '.{1}'\./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.missingradix&&error.reason.match(/Missing radix parameter./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.unncessarysemicolon&&error.reason.match(/Unnecessary semicolon./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.varnotdeclaredcorrectly&&error.reason.match(/Variable \S+ was not declared correctly./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.varoverwrinIE&&error.reason.match(/Value of '\S+' may be overwritten in IE./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.outofscpe&&error.reason.match(/'\S+' used out of scope./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.badvarforin&&error.reason.match(/Bad for in variable '\S+'./g)) {
               ignoredError = true;
            }

            if (!sbtConfig.dupemember&&error.reason.match(/Duplicate member '\S+'./g)) {
               ignoredError = true;
            }
            
            if (ignoredError) {
                return;
            }

            str += file  + ': line ' + error.line + ', col ' +
                error.character + ', ' + error.reason + '\n';

            if (sbtConfig.showevidence) {
                str += "EVIDENCE ["+error.evidence+"]\n\n";
            }

            filteredResults.push(result);
        });

        len = filteredResults.length;

        if (len>0) {
            process.stdout.write(str + "\n" + len + ' error' + ((len === 1) ? '' : 's') + "\n");
            process.exit(1);
        } else {
            process.exit(0);
        }
    }
};
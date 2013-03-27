/**
 * OptionsReporter_checkstyle.js
 *
 * A reporter that discards many warnings that can't be disabled with options.
 * This file was based the checkstyle reporter so the output will be checkstyle xml.
 *
 * @author Carlos Manias
 *
 * Usage:
 *   jshint myfile.js --reporter=OptionsReporter_checkstyle.js
 */

module.exports = {
    reporter: function (results, data) {

        var sbtConfig;

        var files = {},
            out = [],
            pairs = {
                "&": "&amp;",
                '"': "&quot;",
                "'": "&apos;",
                "<": "&lt;",
                ">": "&gt;"
            },
            file, fileName, i, issue, globals, unuseds;
        
        var fs = require("fs")
        if (fs) {
            //We read the config file
            var fileLocation = 'jshint/jsHint.json';
            var contents = fs.readFileSync(fileLocation, 'utf8');

            //We strip out single and multi-line comments
            contents = contents.replace(/\/\/.*?(?=\n|\r|$)|\/\*[\s\S]+?\*\//g, '');

            //We parse the json
            var jsonData = JSON.parse(contents);
            sbtConfig = jsonData.predef;
        }

        function encode(s) {
            for (var r in pairs) {
                if (typeof(s) !== "undefined") {
                    s = s.replace(new RegExp(r, "g"), pairs[r]);
                }
            }
            return s || "";
        }

        function checkOptions(error) {
            var ignoredError = false;
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
            return ignoredError;
        }

        var numErrors = 0;
        var numWarnings = 0;

        results.forEach(function (result) {
            result.file = result.file.replace(/^\.\//, '');
            var file = result.file;
            var error = result.error;
            var matches;

            ignoredError = checkOptions(error);
            
            if (ignoredError) {
                return;
            }

            if (!files[result.file]) {
                files[result.file] = [];
            }

            numErrors++;

            // Add the error
            files[result.file].push({
                severity: 'error',
                line: result.error.line,
                column: result.error.character,
                message: result.error.reason,
                source: result.error.raw
            });
        });

        var includeWarnings = sbtConfig.includeWarnings;

        if (includeWarnings) {
            data.forEach(function (result) {
                file = data.file;
                globals = result.implieds;
                unuseds = result.unused;

                // Register the file
                result.file = result.file.replace(/^\.\//, '');
                if (!files[result.file]) {
                    files[result.file] = [];
                }

                if (globals) {
                    globals.forEach(function (global) {
                        numWarnings++;
                        files[result.file].push({
                            severity: 'warning',
                            line: global.line,
                            column: 0,
                            message: "Implied global '" + global.name + "'",
                            source: 'jshint.implied-globals'
                        });
                    });
                }
                if (unuseds) {
                    unuseds.forEach(function (unused) {
                        numWarnings++;
                        files[result.file].push({
                            severity: 'warning',
                            line: unused.line,
                            column: 0,
                            message: "Unused variable: '" + unused.name + "'",
                            source: 'jshint.implied-unuseds'
                        });
                    });
                }
            });
        }

        if (Object.keys(files).length > 0) {
            out.push("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            out.push("<checkstyle version=\"4.3\">");
            for (fileName in files) {
                if (files.hasOwnProperty(fileName)) {
                    out.push("\t<file name=\"" + fileName + "\">");
                    for (i = 0; i < files[fileName].length; i++) {
                        issue = files[fileName][i];
                        out.push(
                            "\t\t<error " +
                                "line=\"" + issue.line + "\" " +
                                "column=\"" + issue.column + "\" " +
                                "severity=\"" + issue.severity + "\" " +
                                "message=\"" + encode(issue.message) + "\" " +
                                "source=\"" + encode(issue.source) + "\" " +
                                "/>"
                        );
                    }
                    out.push("\t</file>");
                }
            }
            out.push("</checkstyle>");
            var result = out.join("\n") + "\n";

            process.stdout.write(result);
            process.stdout.write("There were "+numErrors+" errors and "+numWarnings+" warnings");
            fs.writeFileSync(sbtConfig.outputreport+"/jshint.xml", result, "utf8");

            process.exit((numErrors==0)?1:2);
        } else {
            process.stdout.write("There were no issues");
            process.exit(0);
        }
    }
};
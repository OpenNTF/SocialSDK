(function($) {

    var snippetPage = "includes/js_snippet.jsp";
    var outlinePage = "includes/outline.jsp";
    var previewPage = "javascriptPreview.jsp";

    function getUrlParameter(url, name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(url)||[,""])[1].replace(/\+/g, '%20'))||null;
    }

    function createQuery(queryMap, delimiter){
        if(!queryMap){
            return null;
        }
        var delim = delimiter;
        if(!delim){
            delim = ",";
        }
        var pairs = [];
        for(var name in queryMap){
            var value = queryMap[name];
            pairs.push(encodeURIComponent(name) + "=" + encodeURIComponent(value));
        }
        return pairs.join(delim);
    }
    
    function getSubstitutionParams(){
        var result = {};
        
        var snippetParamList = $("#propertyContents input[type=text]");
        if(snippetParamList.length > 0){
            var i;
            for(i = 0; i < snippetParamList.length; i++){
                var input = snippetParamList[i];
                if(input.value){
                    result[input.name] = input.value;
                }
                else{
                    if(document.getElementById("requiredMarker")){ // check if it is required
                        if(result.missingParams){
                            result.missingParams.push(input.name);
                        }
                        else{
                            result.missingParams = [];
                            result.missingParams.push(input.name);
                        }
                    }
                    else{
                        result[input.name] = input.value; // put in the empty value so it overwrites the cached version 
                    }
                }
            }
        }
        
        return result;
    }
    
    function displayMissingParamsMessage(missingParamsArray){
        $("#paramsMissingError").text("The following parameters must not be empty: " + missingParamsArray.join(",")).removeClass("hide");
        var propContent = $("#propertyContents");
        propContent.css("display", ""); 
        propContent.siblings().css("display", "none");
        
        var propTab = $("#propertyTab");
        propTab.addClass("active"); 
        propTab.siblings().removeClass("active");
    }
    
    function hideMissingParamsMessage(){
        $("#paramsMissingError").addClass("hide");
    }
    
    function ajaxRefresh(snippet, jsLibId, env, themeId, callback){
        // refresh snippet with js_snippet.jsp
        var queryObj = {
            snippet: snippet,
            jsLibId: jsLibId
        };
        if(env != null && env != ""){
            queryObj.env = env;
        }
        if(themeId != null && themeId != ""){
            queryObj.themeId = themeId;
        }
        var queryString = "?";
        queryString += createQuery(queryObj, "&");
        
        if(callback){
            $("#snippetContainer").empty();
            $("#previewFrame").attr('src', "about:blank");
            $("#previewLink").empty();
            callback(queryString);
        }
        else if(snippet){
            var snippetQuery = snippetPage + queryString;
            $("#snippetContainer").load(snippetQuery, function(){
                var subParams = getSubstitutionParams();
                if(subParams.missingParams){
                    displayMissingParamsMessage(subParams.missingParams);
                    return;
                }
                else{
                    hideMissingParamsMessage();
                }
            });
            
            
            // refresh iframe with javascriptPreview.jsp.
            var previewQuery = previewPage + queryString;
            $("#previewFrame").attr('src', previewQuery);
            
            // update previewLink
            var newLink = previewQuery;
            $("#previewLink").attr("href", newLink).text(newLink);
        }
    }
    // Debug flags whether we are going to use firebug.
    function postCode(frame, debug){
        var htmlDiv = document.getElementById("htmlContents");
        var jsDiv = document.getElementById("jsContents"); 
        var cssDiv = document.getElementById("cssContents");

        var html = htmlDiv.firstChild && htmlDiv.firstChild.CodeMirror ? htmlDiv.firstChild.CodeMirror.getValue() : htmlDiv.textContent;
        var js = jsDiv.firstChild && jsDiv.firstChild.CodeMirror ? jsDiv.firstChild.CodeMirror.getValue() : jsDiv.textContent;
        var css = cssDiv.firstChild && cssDiv.firstChild.CodeMirror ? cssDiv.firstChild.CodeMirror.getValue() : cssDiv.textContent;

        var postData = {
            snippet: getSnippet(),
            debug: debug,
            jsLibId: getJsLibId()
        }; // base of a get request
        if(getEnv()){
            postData.env = getEnv();
        }
        if(getThemeId()){
            postData.themeId = getThemeId();
        }
        $.extend(postData, getSubstitutionParams()); // add sub params
        if(postData.missingParams){
            displayMissingParamsMessage(postData.missingParams);
            return;
        }
        else{
            hideMissingParamsMessage();
        }
        
        var newLink = previewPage + "?" + createQuery(postData, "&");
        
        $.extend(postData, {
            htmlData: html,
            jsData: js,
            cssData: css
         }); // add html etc
        
        $.post(previewPage, postData, function(data) {
                var wrapper = $(".iframeWrapper");
                wrapper.find(frame).remove();
                var $frame = $('<iframe id="previewFrame" src=""  width="100%" height="100%" style="border-style:none;"></iframe>');
                wrapper.append($frame);

                var preview=($frame[0].contentWindow || $frame[0].contentDocument);
                if (preview.document)
                    preview = preview.document;

                preview.open();
                preview.write(data);
                preview.close();
        }, 'html');
        // update previewLink
        
        $("#previewLink").attr("href", newLink).text(newLink);
    }

    function showDialog(){
        var $popoutDiv = $("#showHtmlPopout");
        $popoutDiv.dialog({
            height: "auto",
            width: "500",
            modal: true,
            open: function(){
                $popoutDiv.text($("#previewFrame").contents().find("html").html()).addCodeMirror("text/html");
            },
            close: function(){
                $popoutDiv.empty();
            }
        });
    }

    function setSnippet(snippet){
        $("body").data("snippet", snippet);
    }

    function setSnippetFromUrl(url){
        setSnippet(getUrlParameter(url, "snippet"));
    }

    function setJsLibId(jsLibId){
        if(jsLibId)
            $("body").data("jsLibId", jsLibId);
        else
            $("body").data("jsLibId", "dojo180");
    }

    function setJsLibIdFromUrl(url){
        setJsLibId(getUrlParameter(url, "jsLibId"));
    }
    
    function setEnv(env){
        if(env)
            $("body").data("env", env);
    }

    function setEnvFromUrl(url){
        setEnv(getUrlParameter(url, "env"));
    }

    function setThemeId(themeId){
        $("body").data("themeId", themeId);
    }

    function setThemeIdFromUrl(url){
        setThemeId(getUrlParameter(url, "themeId"));
    }

    function getThemeId(){
        return $("body").data("themeId");
    }

    function getSnippet(){
        return $("body").data("snippet");
    }

    function getJsLibId(){
        return $("body").data("jsLibId");
    }
    
    function getEnv(){
        return $("body").data("env");
    }

    $.fn.makeActive = function(){
        $(this).siblings().removeClass('active');
        $(this).addClass('active');
    };

    $(document).ready(function(){
        setSnippetFromUrl(window.location.href);
        setJsLibIdFromUrl(window.location.href);
        setEnvFromUrl(window.location.href);
        setThemeIdFromUrl(window.location.href);

        $("#runButton").click(function(e){
            postCode(document.getElementById('previewFrame'), false);
        });

        $("#debugButton").click(function(e){
            postCode(document.getElementById('previewFrame'), true);
        });

        $("#showHtmlButton").click(function(e){
            showDialog();
        });
        
        var onLeafNodeClicked = function(e){
            var snippet = this.id;

            setSnippet(snippet);
            var theme = getThemeId();
            var jsLibId = getJsLibId() != null ? getJsLibId() : getUrlParameter(window.location.href, "jsLibId");
            ajaxRefresh(snippet, jsLibId, getEnv(), theme);
        };
        
        var setLeafBehaviour = function(){
            $(".leafNode").each(function(index) {
                var data = $.hasData( this ) && $._data( this );
                if(!data)
                    $(this).click(onLeafNodeClicked);
            });
            $("div[class*='leafNode'] > div > span").css('cursor', 'pointer');
        };

        var setNewLeafNodeListener = function(){
            $('#tree').on("newNodeEvent", function(e){
                setLeafBehaviour();
            });
        };
        $("#treeOutline").ajaxComplete(function(){
            setNewLeafNodeListener();
        });
        setLeafBehaviour();
        setNewLeafNodeListener();
        $("#envChange").change(function(e){
            e.preventDefault();
            var env = getUrlParameter($("#envChange option:selected").attr("value"), "env");
            setEnv(env);
            ajaxRefresh(getSnippet(), getJsLibId(), getEnv(), getThemeId(), function(parameters){
                var outlineQuery = outlinePage + parameters;
                $("#treeOutline").load(outlineQuery);
            });
        });
        $("#libChange").change(function(e){
            e.preventDefault();
            var jsLibId = getUrlParameter($("#libChange option:selected").attr("value"), "jsLibId");
            setJsLibId(jsLibId);
            ajaxRefresh(getSnippet(), getJsLibId(), getEnv(), getThemeId(), function(parameters){
                var outlineQuery = outlinePage + parameters;
                $("#treeOutline").load(outlineQuery);
            });
        });
        var cssObj = {
                'max-height' : document.body.scrollHeight,
                'height' : '100%',
                'overflow' : 'auto'
        };
        $('.span3').css(cssObj);
    });

})(jQuery);
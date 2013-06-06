(function($) {

    var snippetPage = "includes/js_snippet.jsp";
    var previewPage = "javascriptPreview.jsp";

    function getUrlParameter(url, name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(url)||[,""])[1].replace(/\+/g, '%20'))||null;
    }

    function buildUrl(){

    }

    function ajaxRefresh(snippet, jsLibId, env, themeId){
        // refresh snippet with js_snippet.jsp
        var parameters = "?snippet=" + snippet + "&jsLibId=" + jsLibId;
        if(env != null && env != "")
            parameters = parameters + "&env=" + env;
        if(themeId != null && themeId != "")
            parameters = parameters + "&themeId=" + themeId;
        var snippetQuery = snippetPage + parameters;
        $("#snippetContainer").load(snippetQuery);
        // refresh iframe with javascriptPreview.jsp.
        var previewQuery = previewPage + parameters;
        $("#previewFrame").attr('src', previewQuery);

        // update previewLink
        $("#previewLink").attr("href", previewQuery).text(previewQuery);
    }
    // Debug flags whether we are going to use firebug.
    function postCode(frame, debug){
        var htmlDiv = document.getElementById("htmlContents");
        var jsDiv = document.getElementById("jsContents"); 
        var cssDiv = document.getElementById("cssContents");

        var html = htmlDiv.firstChild && htmlDiv.firstChild.CodeMirror ? htmlDiv.firstChild.CodeMirror.getValue() : htmlDiv.textContent;
        var js = jsDiv.firstChild && jsDiv.firstChild.CodeMirror ? jsDiv.firstChild.CodeMirror.getValue() : jsDiv.textContent;
        var css = cssDiv.firstChild && cssDiv.firstChild.CodeMirror ? cssDiv.firstChild.CodeMirror.getValue() : cssDiv.textContent;

        $.post(previewPage, { snippet: getSnippet(), htmlData: html, jsData: js, cssData: css, debug: debug, jsLibId:getJsLibId(), env: getEnv(),  themeId: getThemeId()}, function(data) {
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

        $('#tree').on("newNodeEvent", function(e){
            setLeafBehaviour();
        });
        
        setLeafBehaviour();
        $("#envChange").change(function(e){
            e.preventDefault();
            var env = getUrlParameter($("#envChange option:selected").attr("value"), "env");
            setEnv(env);
            ajaxRefresh(getSnippet(), getJsLibId(), getEnv(), getThemeId());
        });
        $("#libChange").change(function(e){
            e.preventDefault();
            var jsLibId = getUrlParameter($("#libChange option:selected").attr("value"), "jsLibId");
            setJsLibId(jsLibId);
            ajaxRefresh(getSnippet(), getJsLibId(), getEnv(), getThemeId());
        });
        var cssObj = {
                'max-height' : document.body.scrollHeight,
                'height' : '100%',
                'overflow' : 'auto'
        };
        $('.span3').css(cssObj);
    });

})(jQuery);
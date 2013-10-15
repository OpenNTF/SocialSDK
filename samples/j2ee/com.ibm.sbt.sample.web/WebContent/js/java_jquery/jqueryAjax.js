(function($) {

    var snippetPage = "includes/java_snippet.jsp";
    var outlinePage = "includes/outline.jsp";
    var previewPage = "javaPreview.jsp";

    function getUrlParameter(url, name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(url)||[,""])[1].replace(/\+/g, '%20'))||null;
    }

    function ajaxRefresh(snippet, themeId, env, callback){
        // refresh snippet with java_snippet.jsp
        var parameters = "?snippet=" + snippet;
        if(themeId!=null && themeId!=""){
            parameters = parameters + "&themeId=" + themeId + "&env=" + env;
        }
        if(!env){
            setEnv(env="defaultEnvironment");
        }
        parameters = parameters + "&env=" + env + "&lang=java";
        var snippetQuery = snippetPage + parameters;
        if(callback){
            $("#snippetContainer").empty();
            $("#previewFrame").attr('src', "about:blank");
            $("#previewLink").empty();
            callback(parameters);
        }
        else if(snippet){
            $("#snippetContainer").load(snippetQuery);
            // refresh iframe with javaPreview.jsp.
            var previewQuery = previewPage + parameters;
            $("#previewFrame").attr('src', previewQuery);
    
            // update previewLink
            $("#previewLink").attr("href", previewQuery).text(previewQuery);
        }
        
    }

    function setSnippet(snippet){
        $("body").data("snippet", snippet);
    }

    function setSnippetFromUrl(url){
        setSnippet(getUrlParameter(url, "snippet"));
    }

    function setThemeId(themeId){
        $("body").data("themeId", themeId);
    }

    function setThemeIdFromUrl(url){
        setThemeId(getUrlParameter(url, "themeId"));
    }
    
    function setEnv(env){
        if(env){
            $("body").data("env", env);
        }
    }

    function setEnvFromUrl(url){
        setEnv(getUrlParameter(url, "env"));
    }

    function getThemeId(){
        return $("body").data("themeId");
    }

    function getSnippet(){
        return $("body").data("snippet");
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
        setEnvFromUrl(window.location.href);
        setThemeIdFromUrl(window.location.href);
        
        var onLeafNodeClicked = function(e){
            var snippet = this.id;

            setSnippet(snippet);
            var theme = getThemeId();
            ajaxRefresh(snippet, theme, getEnv());
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
        $('#tree').on("newNodeEvent", function(e){
            setLeafBehaviour();
        });
        $("#treeOutline").ajaxComplete(function(){
            setNewLeafNodeListener();
        });
        setLeafBehaviour();
        setNewLeafNodeListener();
        $("#envChange").change(function(e){
            e.preventDefault();
            var env = getUrlParameter($("#envChange option:selected").attr("value"), "env");
            setEnv(env);
            ajaxRefresh(getSnippet(), getThemeId(), getEnv(), function(parameters){
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
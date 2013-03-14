(function($) {

    var snippetPage = "includes/java_snippet.jsp";
    var previewPage = "javaPreview.jsp";

    function getUrlParameter(url, name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(url)||[,""])[1].replace(/\+/g, '%20'))||null;
    }

    function ajaxRefresh(snippet, themeId){
        // refresh snippet with java_snippet.jsp
        var parameters = "?snippet=" + snippet;
        if(themeId!=null && themeId!="")
            parameters = parameters + "&themeId=" + themeId;
        var snippetQuery = snippetPage + parameters;
        $("#snippetContainer").load(snippetQuery);
        // refresh iframe with javaPreview.jsp.
        var previewQuery = previewPage + parameters;
        $("#previewFrame").attr('src', previewQuery);

        // update previewLink
        $("#previewLink").attr("href", previewQuery).text(previewQuery);
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

    function getThemeId(){
        return $("body").data("themeId");
    }

    function getSnippet(){
        return $("body").data("snippet");
    }

    $.fn.makeActive = function(){
        $(this).siblings().removeClass('active');
        $(this).addClass('active');
    };

    $(document).ready(function(){
        setSnippetFromUrl(window.location.href);
        setThemeIdFromUrl(window.location.href);
        
        var onLeafNodeClicked = function(e){
            var snippet = this.id;

            setSnippet(snippet);
            var theme = getThemeId();
            ajaxRefresh(snippet, theme);
        };
        
        var setLeafBehaviour = function(){
            $(".leafNode").click(onLeafNodeClicked);
            $("div[class*='leafNode'] > div > span").css('cursor', 'pointer');
        };

        $('#tree').on("newNodeEvent", function(e){
            setLeafBehaviour();
        });
        
        setLeafBehaviour();
        
    });

})(jQuery);